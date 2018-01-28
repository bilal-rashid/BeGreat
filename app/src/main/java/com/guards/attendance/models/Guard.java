package com.guards.attendance.models;

/**
 * Created by Bilal Rashid on 1/28/2018.
 */

public class Guard {
    public String number;
    public String emp_id;

    public Guard(String number, String emp_id) {
        this.emp_id = emp_id;
        this.number = number;
    }
}
