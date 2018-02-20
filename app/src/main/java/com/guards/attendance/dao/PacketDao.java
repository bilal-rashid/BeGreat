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

    @Update
    void update(Packet... packets);

    @Delete
    void delete(Packet... packets);

    @Query("Select * FROM packet")
    List<Packet> loadAll();

//    @Query("Select * FROM packet WHERE hooman_id == :owner")
//    Packet[] loadAllCatOwners(int owner);
}
