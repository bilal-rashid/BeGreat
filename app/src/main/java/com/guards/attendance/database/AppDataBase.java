package com.guards.attendance.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.guards.attendance.dao.PacketDao;
import com.guards.attendance.models.Packet;

/**
 * Created by Bilal Rashid on 2/20/2018.
 */
@Database(entities = {Packet.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;


    public abstract PacketDao packetDao();


    public static AppDataBase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    "begreat-packets-db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
