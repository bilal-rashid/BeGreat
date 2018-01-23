package com.guards.attendance.models;

import com.guards.attendance.utils.Constants;

/**
 * Created by Bilal Rashid on 1/23/2018.
 */

public class Packet {
    public String identifier;
    public String emp_id;
    public boolean status;
    public String date_time;

    public Packet(String emp_id, boolean status, String date_time) {
        this.emp_id = emp_id;
        this.status = status;
        this.date_time = date_time;
        this.identifier = Constants.UNIQUE_ID;
    }
}
