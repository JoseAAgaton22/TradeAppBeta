package com.app.tradeapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tradeapp.Model.GestionTransaccion;
import com.app.tradeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

public class EstadisticasFragment extends Fragment {

    TextView ingresosTotales, egresosTotales, balanceGeneral;

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

        administrar_Ingresos();
        administrar_Gastos();

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

                double totalIngresos = 0;
                for (double i : lista_ingresos) {
                    totalIngresos += i;
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

                }

                double totalGastos = 0;
                for (double i : lista_gastos) {
                    totalGastos += i;
                    String gastoNeto = String.valueOf(numberFormat.format(totalGastos));
                    egresosTotales.setText(gastoNeto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

