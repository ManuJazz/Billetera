package com.example.user.billetera;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.user.billetera.RoomDB.AppDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class ImportFragment extends Fragment {
    private GridView gridView;
    private List<String> datos;
    private ArrayAdapter<String> adapter;
    private CalendarView calendario;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.rootView = inflater.inflate(R.layout.fragment_import, container, false);

        //Se obtiene la fecha actual
        Fechactual fechactual = new Fechactual();
        final String[] fechaSeleccionada = {fechactual.getFechaActual()};
        Log.w("main", "-> "+ fechaSeleccionada[0]);

        //Selector de fecha
        calendario = rootView.findViewById(R.id.calendario);
        calendario.setMaxDate(System.currentTimeMillis());
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                fechaSeleccionada[0] = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                Log.w("main", "-> "+ fechaSeleccionada[0]);
                mostrarGastos(Integer.parseInt(fechaSeleccionada[0]));
            }
        });

        //Se muestra la información de los gastos
        mostrarGastos(Integer.parseInt(fechaSeleccionada[0]));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.w("fragment", "Got it! on"+position);
            }
        });

        return rootView;
    }

    private void mostrarGastos(int fecha){
        gridView = (GridView) this.rootView.findViewById(R.id.gastos);
        datos = new ArrayList<String>();
        adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, datos);
        gridView.setAdapter(adapter);
        float cantidadTotal = 0, aux;
        try {
            Log.w("fragment", "Asyncall");
            List<Gasto> gastoList = new consultarGastosDia().execute(fecha).get();
            for(int index=0;index<gastoList.size();index++){
                Log.w("fragment", "Elemento "+index+" - "+gastoList.get(index).getFecha());
                datos.add(gastoList.get(index).getConcepto());
                aux = gastoList.get(index).getImporte();
                datos.add(String.valueOf(aux));
                cantidadTotal = cantidadTotal + aux;
            }
            DecimalFormat formatoDecimal = new DecimalFormat("#.##");
            TextView total = rootView.findViewById(R.id.total);
            total.setText(formatoDecimal.format(cantidadTotal));
            adapter.notifyDataSetChanged();
            if(!gastoList.isEmpty()){
                setDynamicHeight(gridView);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Método para extender el alto del GridView
    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > 2 ){
            x = items/2;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    //Llamadas asíncronas
    //Consulta todos los gastos
    class consultarTodosGasto extends AsyncTask<Void, Void, List<Gasto>> {
        @Override
        protected List<Gasto> doInBackground(Void... voids) {
            AppDatabase gastoDatabase = AppDatabase.getDatabase(getActivity());
            return gastoDatabase.gastoDao().getAll();
        }
    }

    //Consulta gastos de una fecha
    class consultarGastosDia extends AsyncTask<Integer, Void, List<Gasto>> {
        @Override
        protected List<Gasto> doInBackground(Integer... integers) {
            AppDatabase gastoDatabase = AppDatabase.getDatabase(getActivity());
            return gastoDatabase.gastoDao().getByDate(integers[0]);
        }
    }

}
