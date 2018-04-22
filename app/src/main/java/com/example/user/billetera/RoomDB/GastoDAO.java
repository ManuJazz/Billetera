package com.example.user.billetera.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.user.billetera.Gasto;

import java.util.List;

/**
 * Created by user on 5/04/18.
 */
@Dao
public interface GastoDAO {
    @Insert
    public long insertGasto(Gasto gasto);

    @Query("SELECT * FROM Gasto")
    public List<Gasto> getAll();

    @Query("SELECT * FROM Gasto WHERE fecha = :date")
    public List<Gasto> getByDate(int date);

    @Query("SELECT * FROM Gasto WHERE fecha BETWEEN :date-10 AND :date")
    public List<Gasto> getSurround(int date);

    @Query("SELECT * FROM Gasto WHERE importe = (SELECT MAX(importe) FROM Gasto)")
    public Gasto getMaxImporte();
}
