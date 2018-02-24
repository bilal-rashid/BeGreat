package com.guards.attendance.models;

/**
 * Created by Bilal Rashid on 1/18/2018.
 */

public class User {
    public String username;
    public String password;
    public String employee_code;
    public String image_path;
    public boolean is_supervisor;

    public User(String username, String password, String employee_code, String image_path) {
        this.username = username;
        this.password = password;
        this.employee_code = employee_code;
        this.image_path = image_path;
    }
    public User(String username, String password, String employee_code, String image_path, boolean supervisor) {
        this.username = username;
        this.password = password;
        this.employee_code = employee_code;
        this.image_path = image_path;
        this.is_supervisor = supervisor;
    }
}
