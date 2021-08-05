package com.app.tradeapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EstadisticasFragment extends Fragment {

    TextView ingresosTotales, egresosTotales, balanceGeneral, prueba;
    double totalIngresos = 0;
    double totalGastos = 0;
    private LineChart lineChart;
    private LineDataSet lineDataSet;
    private PieChart pieChart;

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

        prueba = view.findViewById(R.id.prueba);
        ingresosTotales = view.findViewById(R.id.ingresosTotales);
        egresosTotales = view.findViewById(R.id.gastosTotales);
        balanceGeneral = view.findViewById(R.id.balanceGeneral);
        pieChart = view.findViewById(R.id.pieChart);

        administrar_Ingresos();
        administrar_Gastos();
        balance(totalIngresos, totalGastos);

        return view;
    }

    private void administrar_Ingresos() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("transacciones").child("ingresos").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                ArrayList<Double> lista_ingresos = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    String str_ingreso = gestionTransaccion.getValor();
                    double ingreso = Double.parseDouble(str_ingreso);
                    lista_ingresos.add(ingreso);

                }

                //double totalIngresos = 0;
                for (double i : lista_ingresos) {
                    EstadisticasFragment.this.balance(totalIngresos += i, EstadisticasFragment.this.totalGastos);
                    String ingresoNeto = String.valueOf(numberFormat.format(totalIngresos));
                    ingresosTotales.setText(ingresoNeto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void administrar_Gastos() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("transacciones").child("gastos").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                ArrayList<Double> lista_gastos = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    String str_gasto = gestionTransaccion.getValor();
                    double gasto = Double.parseDouble(str_gasto);
                    lista_gastos.add(gasto);
                    if (snapshot.child("categoria").equals("Hogar")) {
                        prueba.setText("Hola mundo");
                    }
                    else {
                        prueba.setText("Aló policia");
                    }

                }

                //double totalGastos = 0;
                for (double i : lista_gastos) {
                    EstadisticasFragment.this.balance(EstadisticasFragment.this.totalIngresos, totalGastos += i);
                    String gastoNeto = String.valueOf(numberFormat.format(totalGastos));
                    egresosTotales.setText(gastoNeto);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void balance(double ingresos, double gastos) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        double balance_general = ingresos - gastos;
        balanceGeneral.setText(String.valueOf(numberFormat.format(balance_general)));

        //Gráfica del balance general
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        pieEntries.add(new PieEntry((float) ingresos, "Ingresos"));
        pieEntries.add(new PieEntry((float) gastos, "Gastos"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData();
        pieData.addDataSet(dataSet);
        pieChart.setData(pieData);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }
}
