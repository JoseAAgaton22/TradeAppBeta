package com.app.tradeapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.tradeapp.Model.GestionTransaccion;
import com.app.tradeapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EstadisticasFragment extends Fragment {

    TextView ingresosTotales, egresosTotales, balanceGeneral;
    Spinner filtros;
    LinearLayout empty_items;
    double totalIngresos = 0;
    double totalGastos = 0;
    private PieChart pieChart;
    List<GestionTransaccion> lista_ingresos;
    List<GestionTransaccion> lista_gastos;
    Button boton_crear;
    Context context;

    public EstadisticasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);

        ingresosTotales = view.findViewById(R.id.ingresosTotales);
        egresosTotales = view.findViewById(R.id.gastosTotales);
        balanceGeneral = view.findViewById(R.id.balanceGeneral);
        pieChart = view.findViewById(R.id.pieChart);
        empty_items = view.findViewById(R.id.empty_items);
        boton_crear = view.findViewById(R.id.boton_crear_aviso);
        context = getContext();

        filtros = view.findViewById(R.id.filtros);

        lista_gastos = new ArrayList<>();
        lista_ingresos = new ArrayList<>();

        String[] filtro = {"General", "Ingresos", "Egresos"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.spinner_item_bancos, filtro);
        filtros.setAdapter(adapter);
        pieChart.setVisibility(View.GONE);

        filtros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    administrar_Ingresos("general");
                }
                else if(position == 1){
                    administrar_Ingresos("ingresos");
                }
                else if(position == 2){
                    administrar_Ingresos("egresos");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        boton_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TransactionsFragment(), "PERFIL").commit();
            }
        });

        return view;
    }

    private void administrar_Ingresos(String filtro) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("transacciones").child("ingresos").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                lista_ingresos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    lista_ingresos.add(gestionTransaccion);
                }

                //String ingresoNeto = numberFormat.format(totalIngresos);
                //ingresosTotales.setText(ingresoNeto);

                administrar_Gastos(filtro);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void administrar_Gastos(String filtro) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("transacciones").child("gastos").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                lista_gastos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    lista_gastos.add(gestionTransaccion);
                }

                //String gastoNeto = numberFormat.format(totalGastos);
                //egresosTotales.setText(gastoNeto);

                balance(filtro);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void balance(String filtro) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        double ingresos = 0;
        double gastos = 0;

        for(GestionTransaccion ingreso : lista_ingresos){
            ingresos += Double.parseDouble(ingreso.getValor());
        }
        for(GestionTransaccion gasto : lista_gastos){
            gastos += Double.parseDouble(gasto.getValor());
        }

        double balance_general = ingresos - gastos;
        ingresosTotales.setText(numberFormat.format(ingresos));
        egresosTotales.setText(numberFormat.format(gastos));
        balanceGeneral.setText(numberFormat.format(balance_general));


        if(lista_gastos.size() != 0 || lista_ingresos.size() != 0) {

            empty_items.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
            //Gráfica del balance general
            pieChart.setUsePercentValues(true);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setDragDecelerationFrictionCoef(0.99f);
            pieChart.getDescription().setEnabled(false);
            pieChart.animateY(1000);

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            final int[] CUSTOM_COLORS = {
                    Color.rgb(0, 27, 72),
                    Color.rgb(0, 68, 129)
            };

            if (filtro.equals("general")) {
                pieEntries.add(new PieEntry((float) ingresos, "Ingresos"));
                pieEntries.add(new PieEntry((float) gastos, "Gastos"));
            } else if (filtro.equals("ingresos")) {

                if(lista_ingresos.size() != 0) {
                    Map<String, String> datos = new HashMap<String, String>();

                    for (GestionTransaccion gestionTransaccion : lista_ingresos) {

                        String categoria = gestionTransaccion.getCategoria();
                        String valor = gestionTransaccion.getValor();

                        if (datos.containsKey(categoria)) {
                            datos.put(categoria, String.valueOf(Double.parseDouble(datos.get(categoria)) + Double.parseDouble(gestionTransaccion.getValor())));
                        } else {
                            datos.put(categoria, valor);
                        }
                    }

                    Iterator iterator = datos.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry dato = (Map.Entry) iterator.next();
                        pieEntries.add(new PieEntry(Float.parseFloat(String.valueOf(dato.getValue())), String.valueOf(dato.getKey())));
                    }
                }
                else {
                    empty_items.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                }

            } else if (filtro.equals("egresos")) {
                if(lista_gastos.size() != 0) {
                    Map<String, String> datos = new HashMap<String, String>();

                    for (GestionTransaccion gestionTransaccion : lista_gastos) {

                        String categoria = gestionTransaccion.getCategoria();
                        String valor = gestionTransaccion.getValor();

                        if (datos.containsKey(categoria)) {
                            datos.put(categoria, String.valueOf(Double.parseDouble(datos.get(categoria)) + Double.parseDouble(gestionTransaccion.getValor())));
                        } else {
                            datos.put(categoria, valor);
                        }
                    }

                    Iterator iterator = datos.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry dato = (Map.Entry) iterator.next();
                        pieEntries.add(new PieEntry(Float.parseFloat(String.valueOf(dato.getValue())), String.valueOf(dato.getKey())));
                    }
                }
                else {
                    empty_items.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                }
            }

            PieDataSet dataSet = new PieDataSet(pieEntries, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setValueFormatter(new PercentFormatter());
            dataSet.setColors(CUSTOM_COLORS);
            dataSet.setValueTextSize(14f);
            if(getContext() != null) {
                dataSet.setValueTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_light));
            }
            dataSet.setValueTextColor(Color.WHITE);

            PieData pieData = new PieData();
            pieData.addDataSet(dataSet);
            pieChart.setData(pieData);
            if(getContext() != null) {
                pieChart.setCenterTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_light));
                pieChart.setEntryLabelTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_light));
                pieChart.setNoDataTextTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_light));
            }
            pieChart.setHoleRadius(2);
            pieChart.setTransparentCircleAlpha(0);

            Legend legend = pieChart.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        }
    }
}
