package com.example.eti2menf.callstop;

import com.example.eti2menf.callstop.application.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ETI2MENF on 16/10/2017.
 */

public class TimerObj extends RealmObject {
    @PrimaryKey
    private int id;
    private Integer time;
    private boolean status;
    private boolean remark;

    public TimerObj() {
    }

    public TimerObj(Integer time, boolean status, boolean remark) {
        this.id = MyApplication.tObjectId.incrementAndGet();
        this.time = time;
        this.status = status;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isRemark() {
        return remark;
    }

    public void setRemark(boolean remark) {
        this.remark = remark;
    }
}
