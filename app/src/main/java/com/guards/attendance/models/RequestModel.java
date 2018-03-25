package com.guards.attendance.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bilal Rashid on 3/25/2018.
 */

public class RequestModel {
    @SerializedName("DbId")
    public long DbId;

    @SerializedName("Packets")
    public List<Packet> Packets;

    public RequestModel(long dbId, List<Packet> packets) {
        DbId = dbId;
        Packets = packets;
    }
}
