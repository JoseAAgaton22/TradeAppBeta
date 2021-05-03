package com.app.tradeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.tradeapp.Fragments.AmortizacionFragment;
import com.app.tradeapp.Fragments.DeudasFragment;
import com.app.tradeapp.Fragments.EstadisticasFragment;
import com.app.tradeapp.Fragments.HomeFragment;
import com.app.tradeapp.Fragments.PerfilFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}