package com.guards.attendance.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.guards.attendance.models.Packet;

import java.util.List;

/**
 * Created by Bilal Rashid on 2/21/2018.
 */
@Dao
public interface PacketDao {
    @Insert
    void insert(List<Packet> packets);

    @Insert
    void insert(Packet packet);

    @Update
    void update(Packet... packets);

    @Delete
    void delete(Packet... packets);

    @Query("Select * FROM packet")
    List<Packet> loadAll();

    @Query("Select * FROM packet WHERE u_id == :ident AND emp_id == :empid AND status == :status AND date_time == :datetime")
    Packet[] getPackets(String ident, String empid, String status, String datetime);

    @Query("Select * FROM packet WHERE emp_id == :empid")
    List<Packet> getEmployeePackets(String empid);

    @Query("Select * FROM packet WHERE u_id == :identifier")
    List<Packet> loadAllEmployees(String identifier);

    @Query("Select * FROM packet WHERE packetId >:id AND status NOT IN(:value,:value1)")
    List<Packet> getPacketsToSync(int id, String value, String value1);
}
