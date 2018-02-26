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
    @ColumnInfo(name = "identifier")
    public String identifier;
    @ColumnInfo(name = "emp_id")
    public String emp_id;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "date_time")
    public String date_time;
    @ColumnInfo(name = "location")
    public String location;
    @ColumnInfo(name = "number")
    public String number;

    public String getNumber() {
        return number;
    }

    public Packet setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Packet(String emp_id, String status, String date_time) {
        this.emp_id = emp_id;
        this.status = status;
        this.date_time = date_time;
        this.identifier = Constants.UNIQUE_ID_GUARD;
    }

    public Packet(String emp_id, String status, String date_time, String location, boolean is_supervisor) {
        this.emp_id = emp_id;
        this.status = status;
        this.date_time = date_time;
        this.location = location;
        if (is_supervisor)
            this.identifier = Constants.UNIQUE_ID_SUPERVISOR;
        else
            this.identifier = Constants.UNIQUE_ID_GUARD;
    }
    public int compare(Packet packet) {
        Date packetDate = null;
        Date thisDate= null;
        try {
            packetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(packet.date_time);
            thisDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.date_time);
            return thisDate.compareTo(packetDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
