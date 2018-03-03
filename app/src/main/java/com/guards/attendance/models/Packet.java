package com.guards.attendance.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.guards.attendance.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bilal Rashid on 1/23/2018.
 */
@Entity
public class Packet {
    @PrimaryKey(autoGenerate = true)
    public int packetId;
    @ColumnInfo(name = "u_id")
    public String u_id;
    @ColumnInfo(name = "emp_id")
    public String emp_id;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "date_time")
    public String date_time;
    @ColumnInfo(name = "point")
    public String point;
    @ColumnInfo(name = "number")
    public String number;

    public String getNumber() {
        return number;
    }

    public Packet setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public Packet(String emp_id, String status, String date_time) {
        this.emp_id = emp_id;
        this.status = status;
        this.date_time = date_time;
        this.u_id = Constants.UNIQUE_ID_GUARD;
    }

    public Packet(String emp_id, String status, String date_time, String point, boolean is_supervisor) {
        this.emp_id = emp_id;
        this.status = status;
        this.date_time = date_time;
        this.point = point;
        if (is_supervisor)
            this.u_id = Constants.UNIQUE_ID_SUPERVISOR;
        else
            this.u_id = Constants.UNIQUE_ID_GUARD;
    }
    public int compare(Packet packet) {
        Date packetDate = null;
        Date thisDate= null;
        try {
            packetDate = new SimpleDateFormat("dd/MM/yy HH:mm").parse(packet.date_time);
            thisDate = new SimpleDateFormat("dd/MM/yy HH:mm").parse(this.date_time);
            return thisDate.compareTo(packetDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
