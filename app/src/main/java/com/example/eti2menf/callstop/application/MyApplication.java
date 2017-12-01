package com.example.eti2menf.callstop.application;

import android.app.Application;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by ETI2MENF on 16/10/2017.
 */

public class MyApplication extends Application {
    public static AtomicInteger tObjectId = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealConfig();
        Realm realm = Realm.getDefaultInstance();
//        tObjectId = getIdByTable(realm, TimerObj.class);
        realm.close();
    }


    private void setUpRealConfig() {
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass) {
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
