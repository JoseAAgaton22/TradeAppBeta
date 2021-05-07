package com.app.tradeapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.tradeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DeudasFragment extends Fragment {

    FloatingActionButton boton_crear, individual, grupal;
    Button boton_crear_aviso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deudas, container, false);

        boton_crear = view.findViewById(R.id.boton_crear);
        individual = view.findViewById(R.id.individual);
        grupal = view.findViewById(R.id.grupal);
        boton_crear_aviso = view.findViewById(R.id.boton_crear_aviso);

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

        return view;
    }
}