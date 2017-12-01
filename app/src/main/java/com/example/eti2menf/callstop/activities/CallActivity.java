package com.example.eti2menf.callstop.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.eti2menf.callstop.R;
import com.example.eti2menf.callstop.TimerObj;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;


public class CallActivity extends AppCompatActivity  {
    private static final String TAG = "CallActivity";

    @BindView(R.id.btnGuardar) Button btnGuardar;
    @BindView(R.id.npMinutos) NumberPicker npMinutos;
    @BindView(R.id.npSegundos) NumberPicker npSegundos;
    @BindView(R.id.chckStatus) CheckBox chckStatus;
    @BindView(R.id.chckRemark) CheckBox chckRemark;

    SharedPreferences sp;

    private Realm realm;
    private RealmResults<TimerObj> objs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences("CallTimer", Activity.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        npMinutos = (NumberPicker) findViewById(R.id.npMinutos);
        npSegundos = (NumberPicker) findViewById(R.id.npSegundos);
        chckStatus = (CheckBox) findViewById(R.id.chckStatus);
        chckRemark = (CheckBox) findViewById(R.id.chckRemark);

        loadPicker();
        setValues();

    }

    private void setValues() {
        objs = realm.where(TimerObj.class).findAll();
        if (objs.size() > 0) {
            npMinutos.setValue(objs.get(0).getTime() / 60);
            npSegundos.setValue(objs.get(0).getTime() % 60);
            chckStatus.setChecked(objs.get(0).isStatus());
            chckRemark.setChecked(objs.get(0).isRemark());
        }
    }

    private void loadPicker() {
        String[] values = new String[60];
        for (int i = 0; i < values.length; i++) {
            values[i] = i < 10 ? "0" + i : String.valueOf(i);
        }

        npMinutos.setMinValue(0);
        npMinutos.setMaxValue(59);
        npMinutos.setDisplayedValues(values);
        npSegundos.setMinValue(0);
        npSegundos.setMaxValue(59);
        npSegundos.setDisplayedValues(values);
    }

    @OnClick(R.id.btnGuardar)
    public void fnGuardar(View v) {
        saveTime();
    }

    private void saveTime() {

        Integer tiempo = (npMinutos.getValue() * 60) + npSegundos.getValue();
        if (tiempo > 0) {
            realm.beginTransaction();
            realm.deleteAll();
            TimerObj obj = new TimerObj(tiempo, chckStatus.isChecked(), chckRemark.isChecked());
            realm.copyToRealm(obj);
            realm.commitTransaction();
            String status = chckStatus.isChecked() ? "Activo" : "Inactivo";
            Toast.makeText(this, "Configuración guardad exitosamente estatus \"" + status + "\"", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "commit");
        } else {
            Toast.makeText(this, "El tiempo de bloqueo no debe ser 0", Toast.LENGTH_SHORT).show();
        }
    }

    public void call(Bundle b) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String phoneNo = "tel:" + b.getString("incoming_number");
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(phoneNo)));
        Log.d("Entrada de texto", b.getString("incoming_number"));
    }
}
