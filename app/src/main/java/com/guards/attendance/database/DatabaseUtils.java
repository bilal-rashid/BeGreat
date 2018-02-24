package com.guards.attendance.database;

import com.guards.attendance.models.Guard;
import com.guards.attendance.models.Packet;
import java.util.ArrayList;
import java.util.HashMap;
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
        dataBase.packetDao().insert(packets);
    }
    public void addPacketsToDB(List<Packet> packetList){
        for (int i =0; i < packetList.size();i++) {
            Packet current = packetList.get(i);
            Packet[] temp = dataBase.packetDao().getPackets(current.getIdentifier(),
                    current.getEmp_id(),current.getStatus(),current.getDate_time());
            if(temp == null || temp.length ==0 ){
                dataBase.packetDao().insert(current);
            }
        }

    }
    public List<Guard> getEmployees(){
        List<Guard> guardList = new ArrayList<>();
        List<Packet> packetList = dataBase.packetDao().loadAll();
        HashMap<String, String> hashMap = new HashMap<>();
        for(int i=0;i<packetList.size();i++){
            hashMap.put(packetList.get(i).emp_id,packetList.get(i).number);
        }
        if (hashMap.size() > 0) {
            for (int i = 0; i < hashMap.size(); i++) {
                guardList.add(new Guard(hashMap.values().toArray()[i] + "", hashMap.keySet().toArray()[i] + ""));
            }
        }
        return guardList;
    }
}
