package com.example.user.billetera.RoomDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.user.billetera.Gasto;

/**
 * Created by user on 5/04/18.
 */

@Database(entities = {Gasto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    private static Context myContext;

    public static AppDatabase getDatabase(Context context){
        if(instance== null){
            myContext=context;
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app.db").build();
        }
        return instance;
    }

    public abstract GastoDAO gastoDao();

}

