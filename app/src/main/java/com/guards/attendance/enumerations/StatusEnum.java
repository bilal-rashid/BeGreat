package com.guards.attendance.enumerations;

/**
 * Created by Bilal Rashid on 1/24/2018.
 */

public enum StatusEnum {
    EMERGENCY("Emergency"),
    CHECKIN("Checkin"),
    CHECKOUT("Checkout"),
    RESPONSE("Responded"),
    NO_RESPONSE("Not Responded");

    private String name;

    StatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
