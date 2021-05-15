package com.app.tradeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.tradeapp.Fragments.AmortizacionFragment;
import com.app.tradeapp.Fragments.DeudasFragment;
import com.app.tradeapp.Fragments.EstadisticasFragment;
import com.app.tradeapp.Fragments.HomeFragment;
import com.app.tradeapp.Fragments.PerfilFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ChipNavigationBar chipNavigationBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottom_menu);

        if(savedInstanceState == null){
            chipNavigationBar.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.frame, homeFragment, "HOME").commit();
        }

        Log.e("Comprobacion", "true");

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.deudas:
                        fragment = new DeudasFragment();
                        break;
                    case R.id.amortizacion:
                        fragment = new AmortizacionFragment();
                        break;
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.estadisticas:
                        fragment = new EstadisticasFragment();
                        break;
                    case R.id.perfil:
                        fragment = new PerfilFragment();
                        break;
                }

                if(fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                }
                else {
                    Log.e(TAG, "Error creando el fragmento");
                }
            }
        });

    }

}