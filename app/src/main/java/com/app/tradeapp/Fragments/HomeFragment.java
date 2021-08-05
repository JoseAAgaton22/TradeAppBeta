package com.app.tradeapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tradeapp.Adapters.UserAdapter;
import com.app.tradeapp.Model.GestionTransaccion;
import com.app.tradeapp.Model.User;
import com.app.tradeapp.R;
import com.app.tradeapp.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    double ingresosTotales = 0;
    double gastosTotales = 0;
    private TextView pagos, endeudamiento, porcentaje_ibre;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private ImageView cancelar;
    private FrameLayout crear_billetera;

    EditText barraB;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        pagos = view.findViewById(R.id.pagos);
        endeudamiento = view.findViewById(R.id.endeudamiento);
        porcentaje_ibre = view.findViewById(R.id.porcentaje_libre);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setVisibility(View.GONE);

        barraB = view.findViewById(R.id.buscar);
        cancelar = view.findViewById(R.id.cancelar);

        crear_billetera = view.findViewById(R.id.crear_billetera);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers);
        recyclerView.setAdapter(userAdapter);

        resumen_usuario();
        porcentajes(ingresosTotales, gastosTotales);

        crear_billetera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), TransactionsActivity.class));
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TransactionsFragment(), "PERFIL").commit();
            }
        });

        barraB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(recyclerView.getVisibility() == View.VISIBLE){
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity(), v);
                barraB.clearFocus();
            }
        });

        leerUsuarios();
        barraB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarUsuarios(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void buscarUsuarios(String busqueda){
        Query query = FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("nombres").startAt(busqueda).endAt(busqueda+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    mUsers.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void leerUsuarios(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(barraB.getText().toString().equals("")){
                    mUsers.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        mUsers.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void resumen_usuario(){

        ArrayList<Double> lista_pagos = new ArrayList<>();
        ArrayList<Double> lista_ingresos = new ArrayList<>();
        ArrayList<Double> lista_gastos = new ArrayList<>();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference_pago = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid());
        DatabaseReference reference_ingreso = FirebaseDatabase.getInstance().getReference("transacciones").child("ingresos").child(firebaseUser.getUid());
        DatabaseReference reference_gasto = FirebaseDatabase.getInstance().getReference("transacciones").child("gastos").child(firebaseUser.getUid());

        reference_pago.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    String str_pago = gestionTransaccion.getValor();
                    double pago = Double.parseDouble(str_pago);
                    lista_pagos.add(pago);
                    }

                String str_pago = String.valueOf(lista_pagos.size());
                pagos.setText(str_pago);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        reference_ingreso.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    String str_ingreso = gestionTransaccion.getValor();
                    double ingreso = Double.parseDouble(str_ingreso);
                    lista_ingresos.add(ingreso);
                }

                for (double i : lista_ingresos) {
                    HomeFragment.this.porcentajes(ingresosTotales += i, HomeFragment.this.gastosTotales);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        reference_gasto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GestionTransaccion gestionTransaccion = dataSnapshot.getValue(GestionTransaccion.class);
                    String str_gasto = gestionTransaccion.getValor();
                    double gasto = Double.parseDouble(str_gasto);
                    lista_gastos.add(gasto);
                }

                for (double i : lista_gastos) {
                    HomeFragment.this.porcentajes(HomeFragment.this.ingresosTotales, gastosTotales += i);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void porcentajes (double ingresos, double gastos) {

        if (ingresos!=0 || gastos!=0) {
            double porcentaje = ((ingresos - gastos) / ingresos) * 100;
            String str_porcentaje = String.valueOf(String.format("%.0f", porcentaje));
            porcentaje_ibre.setText(str_porcentaje + "%");

            double cap_endeudamiento = (gastos / ingresos) * 100;
            String str_endeudamiento = String.valueOf(String.format("%.0f", cap_endeudamiento));
            endeudamiento.setText(str_endeudamiento + "%");

        } else {
            porcentaje_ibre.setText("0%");
            endeudamiento.setText("0%");
        }
    }
}