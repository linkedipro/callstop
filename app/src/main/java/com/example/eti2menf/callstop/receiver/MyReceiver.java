package com.example.eti2menf.callstop.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.eti2menf.callstop.TimerObj;
import com.example.eti2menf.callstop.activities.CallActivity;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyReceiver extends BroadcastReceiver {
    int count = 0;
    private String TAG = "MyReceiver";
    private RealmResults<TimerObj> objs;
    private Realm realm = null;

    boolean flag = false;

    @Override
    public void onReceive(Context c, Intent i) {
        flag = false;
        Bundle bundle = i.getExtras();
        if (bundle == null) {
            return;
        }
        String s = bundle.getString(TelephonyManager.EXTRA_STATE);

        if (i.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.d(TAG, i.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
        } else if (s.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            Log.d(TAG, bundle.getString("incoming_number"));
        } else if (s.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            flag = true;
            countTimer(c, bundle);
        } else if (s.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            /*evento Boton colgar */
            flag = false;
            Toast.makeText(c, "Llamada terminada", Toast.LENGTH_SHORT).show();

        }

    }


    public void countTimer(final Context c, final Bundle b) {
        if(realm == null){
            realm = Realm.getDefaultInstance();
        }
        final Timer timer = new Timer();
        objs = realm.where(TimerObj.class).findAll();
        if (objs.size() > 0) {
            final boolean remark = objs.get(0).isRemark();
            if(objs.get(0).isStatus()){
                final int t = objs.get(0).getTime();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        Log.d("Time My Receiver:---> ", String.valueOf(count++));
                        if (count == t) {
                            timer.cancel();
                            count = 0;
                            if(flag){
                                stopCall(c, remark, b);
                            }
                        }
                    }
                }, 1000, 1000);
            }
        }
    }

    public boolean stopCall(Context c, boolean r, Bundle b) {
        try {
            TelephonyManager manager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            Class classT = Class.forName(manager.getClass().getName());
            Method mGetTel = classT.getDeclaredMethod("getITelephony");
            // Ignore that the method is supposed to be private
            mGetTel.setAccessible(true);
            Object inter = mGetTel.invoke(manager);
            Class telephonyInterfaceClass = Class.forName(inter.getClass().getName());
            Method method = telephonyInterfaceClass.getDeclaredMethod("endCall");
            // Invoke endCall()
            method.invoke(inter);
            if(flag && r){
                call(c,b);
            }
        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("Receiver", "PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }
    private void call(final Context c, final Bundle b) {
        if (ActivityCompat.checkSelfPermission(c.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("Time My Receiver:---> ", String.valueOf(count++));
                if (count == 3) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + b.getString("incoming_number")));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(intent);
                    count = 0;
                    timer.cancel();
                }
            }
        }, 1000, 1000);

    }

}