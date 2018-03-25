package com.guards.attendance.database;

import com.guards.attendance.models.Guard;
import com.guards.attendance.models.Packet;
import com.guards.attendance.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            Packet[] temp = dataBase.packetDao().getPackets(current.getU_id(),
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
    public List<Packet> getPacketsOfEmployee(String emp_id){
        return dataBase.packetDao().getEmployeePackets(emp_id);
    }
    public List<Packet> getAllPackets(){
        return dataBase.packetDao().loadAll();
    }
    public List<Packet> getLastWeekPackets(){
        List<Packet> allPackets = dataBase.packetDao().loadAll();
        List<Packet> result = new ArrayList<>();
        for (int i = 0; i < allPackets.size(); i++){
            Packet temp_packet = allPackets.get(i);
            Date lastweek = new Date(Calendar.getInstance().getTime().getTime() - (7L * 24L * 60L * 60L * 1000L));
            Date packetDate = null;
            try {
                packetDate = new SimpleDateFormat(Constants.DATE_FORMAT).parse(temp_packet.date_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                if(lastweek.compareTo(packetDate)<1){
                    result.add(temp_packet);
                }
            }catch (Exception e){e.printStackTrace();}
        }
        return result;
    }
    public List<Guard> getGuards(){
        List<Guard> guardList = new ArrayList<>();
        List<Packet> packetList = dataBase.packetDao().loadAllEmployees(Constants.UNIQUE_ID_GUARD);
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
    public List<Guard> getSupervisors(){
        List<Guard> guardList = new ArrayList<>();
        List<Packet> packetList = dataBase.packetDao().loadAllEmployees(Constants.UNIQUE_ID_SUPERVISOR);
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
