package com.guards.attendance.database;

import com.guards.attendance.models.Packet;

import java.util.List;

/**
 * Created by Bilal Rashid on 2/21/2018.
 */

public class DatabaseUtils {
    private static DatabaseUtils instance;
    private static AppDataBase dataBase;
    public static DatabaseUtils with(AppDataBase appDataBase) {

        if (dataBase == null)
            dataBase = appDataBase;

        if (instance == null)
            instance = new DatabaseUtils();

        return instance;
    }
    public void addPackets(List<Packet> packets) {
        if (dataBase == null)
            return;

        Packet[] packet = new Packet[4];
        packet[0] = packets.get(0);
        packet[1] = packets.get(1);
        packet[2] = packets.get(2);
        packet[3] = packets.get(3);

        dataBase.packetDao().insert(packets);
    }
}
