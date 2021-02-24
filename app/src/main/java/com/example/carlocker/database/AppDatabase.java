package com.example.carlocker.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.carlocker.models.Vehicle;

@Database(entities = {Vehicle.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract VehicleDao vehicleDao();
}
