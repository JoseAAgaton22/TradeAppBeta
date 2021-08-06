package com.app.tradeapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tradeapp.Adapters.PagoAdapter;
import com.app.tradeapp.GroupWalletActivity;
import com.app.tradeapp.Model.Pago;
import com.app.tradeapp.R;
import com.app.tradeapp.WalletActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeudasFragment extends Fragment {

    private PagoAdapter pagoAdapter;
    private List<Pago> mPagos;
    private RecyclerView recyclerView;
    private FloatingActionButton boton_crear, individual, grupal;
    private Button boton_crear_aviso;
    private LinearLayout empty_items;
    private TextView deudasTitulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deudas, container, false);

        boton_crear = view.findViewById(R.id.boton_crear);
        individual = view.findViewById(R.id.individual);
        grupal = view.findViewById(R.id.grupal);
        boton_crear_aviso = view.findViewById(R.id.boton_crear_aviso);
        empty_items = view.findViewById(R.id.empty_items);
        deudasTitulo = view.findViewById(R.id.deudas_titulo);

        recyclerView = view.findViewById(R.id.recycler_pagos);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mPagos = new ArrayList<>();
        pagoAdapter = new PagoAdapter(getContext(), mPagos);
        recyclerView.setAdapter(pagoAdapter);

        leerPagos();

        individual.setVisibility(View.GONE);
        grupal.setVisibility(View.GONE);

        boton_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(individual.getVisibility() == View.GONE && grupal.getVisibility() == View.GONE){
                    individual.setVisibility(View.VISIBLE);
                    grupal.setVisibility(View.VISIBLE);
                }
                else {
                    individual.setVisibility(View.GONE);
                    grupal.setVisibility(View.GONE);
                }
            }
        });

        boton_crear_aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(individual.getVisibility() == View.GONE && grupal.getVisibility() == View.GONE){
                    individual.setVisibility(View.VISIBLE);
                    grupal.setVisibility(View.VISIBLE);
                }
                else {
                    individual.setVisibility(View.GONE);
                    grupal.setVisibility(View.GONE);
                }
            }
        });

        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WalletActivity.class));
            }
        });

        grupal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GroupWalletActivity.class));
            }
        });

        return view;
    }

    private void leerPagos(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mPagos.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Pago pago = dataSnapshot.getValue(Pago.class);
                    mPagos.add(pago);
                }
                if(mPagos.size() != 0){
                    empty_items.setVisibility(View.GONE);
                }
                else {
                    empty_items.setVisibility(View.VISIBLE);
                }
                pagoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}