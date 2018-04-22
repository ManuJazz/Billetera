package com.example.user.billetera;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.billetera.RoomDB.AppDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GalleryFragment extends Fragment {

    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        try {
            // Obtención datos
            List <Gasto> ultimosGastos = new consultarUltimosGastos().execute(Integer.parseInt(new Fechactual().getFechaActual())).get();

            // Inflate the layout for this fragment
            GraphView graph = (GraphView) this.rootView.findViewById(R.id.graph);
            DataPoint[] dataPoint = new DataPoint[ultimosGastos.size()];
            for(int index=0;index<ultimosGastos.size();index++){
                String aux = ultimosGastos.get(index).getFecha()+", "+ultimosGastos.get(index).getImporte();
                Log.w("bucle", aux);
                dataPoint[index] = new DataPoint(ultimosGastos.get(index).getFecha(), ultimosGastos.get(index).getImporte());
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoint);
            graph.addSeries(series);

            //Muestra el pago más caro
            Gasto gastoMasCaro = new consultarGastoCaro().execute().get();
            String conceptoMasCaro = gastoMasCaro.getConcepto() + " ("+gastoMasCaro.getImporte()+" €)";
            TextView importeMasCaro = (TextView) this.rootView.findViewById(R.id.conceptoMasCaro);
            importeMasCaro.setText(conceptoMasCaro);

            //Muestra el pago más repetido
            Gasto gastoMasRepetido = new consultarGastoRepetido().execute().get();
            String conceptoMasRepetido = gastoMasCaro.getConcepto() + " ("+gastoMasCaro.getImporte()+" €)";
            TextView importeMasRepetido = (TextView) this.rootView.findViewById(R.id.conceptoMasRepetido);
            importeMasRepetido.setText(conceptoMasRepetido);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return this.rootView;
    }

    //Llamadas asíncronas
    //Consulta los gastos de los últimos quince días
    class consultarUltimosGastos extends AsyncTask<Integer, Void, List<Gasto>> {
        @Override
        protected List<Gasto> doInBackground(Integer... integers) {
            AppDatabase gastoDatabase = AppDatabase.getDatabase(getActivity());
            return gastoDatabase.gastoDao().getSurround(integers[0]);
        }
    }

    //Consulta el gasto más caro
    class consultarGastoCaro extends AsyncTask<Void, Void, Gasto> {
        @Override
        protected Gasto doInBackground(Void... voids) {
            AppDatabase gastoDatabase = AppDatabase.getDatabase(getActivity());
            return gastoDatabase.gastoDao().getMaxImporte();
        }
    }

    //Consultar el gasto más repetido
    class consultarGastoRepetido extends AsyncTask<Void, Void, Gasto> {
        @Override
        protected Gasto doInBackground(Void... voids) {
            AppDatabase gastoDatabase = AppDatabase.getDatabase(getActivity());
            /*TO DO*/
            return gastoDatabase.gastoDao().getMaxImporte();
        }
    }
}
