package com.example.user.billetera;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Gasto")
public class Gasto {
    @PrimaryKey(autoGenerate = true)
    private long id_gasto;
    private String concepto;
    private Float importe;
    private int fecha;

    public Gasto(String concepto, Float importe, int fecha){
        this.concepto=concepto;
        this.importe=importe;
        this.fecha=fecha;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public long getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(long id_gasto) {
        this.id_gasto = id_gasto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }
}
