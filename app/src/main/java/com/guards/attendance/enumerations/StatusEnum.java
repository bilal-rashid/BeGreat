package com.guards.attendance.enumerations;

/**
 * Created by Bilal Rashid on 1/24/2018.
 */

public enum StatusEnum {
    EMERGENCY("Emergency","1"),
    CHECKIN("Checkin","2"),
    CHECKOUT("Checkout","3"),
    RESPONSE("Responded","4"),
    NO_RESPONSE("Not Responded","5"),
    LOCATION("Location","6"),
    NO_LOCATION("Gps Off","7");

    private String name;
    private String value;

    StatusEnum(String name,String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
}
