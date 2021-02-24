package com.example.carlocker.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.carlocker.models.Vehicle;

import java.util.List;

@Dao
public interface VehicleDao
{
    @Query("SELECT * FROM vehicle WHERE user_email = :userEmail AND type = :type")
    List<Vehicle> getVehicleType(String userEmail, String type);

    @Query("SELECT * FROM vehicle WHERE user_email = :userEmail")
    List<Vehicle> getAllVehicles(String userEmail);

    @Insert
    void createVehicle(Vehicle... vehicle);

    @Delete
    void delete(Vehicle vehicle);
}
