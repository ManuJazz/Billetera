package com.example.user.billetera;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.billetera.RoomDB.AppDatabase;
import com.example.user.billetera.RoomDB.GastoDAO;

import java.util.concurrent.ExecutionException;

public class Nuevo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(this.getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_nuevo);

        ImageButton guardar = (ImageButton) findViewById(R.id.anadir);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText concepto = (EditText) findViewById(R.id.concepto);
                final String concept = concepto.getText().toString();
                EditText importe = (EditText) findViewById(R.id.importe);
                final String cost = importe.getText().toString();

                Log.w("nuevo", "Se pulsa");
                if(!concept.equals("") && !cost.equals("")) {
                    Log.w("nuevo", "Los campos están rellenos");
                    final Float coste = Float.parseFloat(cost);
                    nuevoGasto(concept, coste);
                }else{
                    CharSequence mensajeToast = "Los campos están vacíos";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(v.getContext(), mensajeToast, duration);
                    toast.show();
                }
            }
        });
    }

    public void nuevoGasto(String concepto, Float importe){

        //Añadir a BD
        try {
            Gasto gasto = new Gasto(concepto, importe, Integer.parseInt(new Fechactual().getFechaActual()));
            Log.w("inserción", "Nuevo dato "+gasto.getFecha());
            new insertarGasto().execute(gasto).get();
            Intent volver = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(volver);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Llamadas asíncronas
    //Inserta gasto
    class insertarGasto extends AsyncTask<Gasto, Void, Long> {
        @Override
        protected Long doInBackground(Gasto... gasto) {
            AppDatabase gastoDatabase = AppDatabase.getDatabase(Nuevo.this);
            return gastoDatabase.gastoDao().insertGasto(gasto[0]);
        }
    }
}
