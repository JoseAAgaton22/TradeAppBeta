package com.app.tradeapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tradeapp.Adapters.CategoriasAdapater;
import com.app.tradeapp.Adapters.IngresosAdapter;
import com.app.tradeapp.R;
import com.app.tradeapp.RandomString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;


public class TransactionsFragment extends Fragment {

    ImageView boton_cancel;
    EditText valor_transaccion, descripcion_transaccion;
    RadioGroup rg_transaccion;
    RadioButton rb_ingreso, rb_gasto;
    TextView fecha, descripcion;
    RecyclerView recyclerCategoria;
    Button boton_añadir;
    ProgressDialog dialog;
    Activity activity;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CategoriasAdapater.obtenerLista();
        IngresosAdapter.obtenerListaIngresos();
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        boton_cancel = view.findViewById(R.id.cancel);
        valor_transaccion = view.findViewById(R.id.valor_transaccion);
        descripcion_transaccion = view.findViewById(R.id.descripcion_transaccion);
        rg_transaccion = view.findViewById(R.id.radioGroup_transaccion);
        rb_ingreso = view.findViewById(R.id.button_ingreso);
        rb_gasto = view.findViewById(R.id.button_gasto);
        fecha = view.findViewById(R.id.fecha_transaccion);
        descripcion = view.findViewById(R.id.descripcion);
        boton_añadir = view.findViewById(R.id.button_añadir);

        recyclerCategoria = view.findViewById(R.id.recyclerCategoria);
        recyclerCategoria.setLayoutManager(new GridLayoutManager(this.activity, 4));
        recyclerCategoria.setHasFixedSize(true);
        recyclerCategoria.setVisibility(View.GONE);

        //final CategoriasAdapater adapaterCat = new CategoriasAdapater(CategoriasAdapater.listaCategorias_gasto);
        //recyclerCategoria.setAdapter(adapaterCat);

        boton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new EstadisticasFragment()).commit();
            }
        });

        rg_transaccion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button_ingreso:
                        recyclerCategoria.setVisibility(View.GONE);
                        final IngresosAdapter adapater = new IngresosAdapter(IngresosAdapter.listaCategorias_ingreso);
                        recyclerCategoria.setAdapter(adapater);
                        if (recyclerCategoria.getVisibility() == View.VISIBLE) {
                            recyclerCategoria.setVisibility(View.GONE);
                        } else {
                            recyclerCategoria.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.button_gasto:
                        recyclerCategoria.setVisibility(View.GONE);
                        final CategoriasAdapater adapaterCat = new CategoriasAdapater(CategoriasAdapater.listaCategorias_gasto);
                        recyclerCategoria.setAdapter(adapaterCat);
                        if (recyclerCategoria.getVisibility() == View.VISIBLE) {
                            recyclerCategoria.setVisibility(View.GONE);
                        } else {
                            recyclerCategoria.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });

        //categoria


        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH);
                int dia = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.style_calendar,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int mes_int = month;
                        String mes_txt = null;
                        if (mes_int==0){
                            mes_txt = "Ene";
                        }
                        else if (mes_int==1){
                            mes_txt = "Feb";
                        }
                        else if (mes_int==2){
                            mes_txt = "Mar";
                        }
                        else if (mes_int==3){
                            mes_txt = "Abr";
                        }
                        else if (mes_int==4){
                            mes_txt = "May";
                        }
                        else if (mes_int==5){
                            mes_txt = "Jun";
                        }
                        else if (mes_int==6){
                            mes_txt = "Jul";
                        }
                        else if (mes_int==7){
                            mes_txt = "Ago";
                        }
                        else if (mes_int==8){
                            mes_txt = "Sept";
                        }
                        else if (mes_int==9){
                            mes_txt = "Oct";
                        }
                        else if (mes_int==10){
                            mes_txt = "Nov";
                        }
                        else if (mes_int==11){
                            mes_txt = "Dic";
                        }
                        String fecha_transaccion = dayOfMonth + "-" + mes_txt + "-" + year;
                        fecha.setText(fecha_transaccion);
                    }
                }, year, mes, dia);
                datePickerDialog.show();
            }
        });

        descripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descripcion_transaccion.getVisibility() == View.VISIBLE) {
                    descripcion_transaccion.setVisibility(View.GONE);
                } else {
                    descripcion_transaccion.setVisibility(View.VISIBLE);
                }
            }
        });


        boton_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_valor_transaccion = valor_transaccion.getText().toString();
                String fechaTransaccion = fecha.getText().toString();
                String str_descripcion_transaccion = descripcion_transaccion.getText().toString();
                String str_categoria_ingreso = IngresosAdapter.categoriaSeleccionada;
                String str_categoria_gasto = CategoriasAdapater.categoriaSeleccionada;

                if (TextUtils.isEmpty(str_valor_transaccion) || (TextUtils.isEmpty(str_categoria_gasto) && TextUtils.isEmpty(str_categoria_ingreso))) {
                    Toast.makeText(getContext(), "Debe ingresar el valor y categoria de su transaccion", Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(fechaTransaccion)) {
                    Toast.makeText(getActivity(), "Ingrese la fecha de la transaccion", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (rb_ingreso.isChecked() == true) {
                        dialog = new ProgressDialog(getActivity());
                        dialog.setMessage("Estamos gestionando tu transacción");
                        dialog.show();
                        subirIngreso(str_categoria_ingreso, str_valor_transaccion, fechaTransaccion, str_descripcion_transaccion);
                    } else if (rb_gasto.isChecked() == true) {
                        dialog = new ProgressDialog(getActivity());
                        dialog.setMessage("Estamos gestionando tu transacción");
                        dialog.show();
                        subirGasto(str_categoria_gasto, str_valor_transaccion, fechaTransaccion, str_descripcion_transaccion);
                    }
                }
            }
        });

        return view;
    }

    private void subirIngreso(String str_categoria, String str_valor_transaccion, String fechaTransaccion, String str_descripcion_transaccion) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RandomString randomString = new RandomString();
        String id = randomString.nextString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("categoria", str_categoria);
        hashMap.put("valor", str_valor_transaccion);
        hashMap.put("fecha_de_transaccion", fechaTransaccion);
        hashMap.put("descripcion", str_descripcion_transaccion);

        Task<Void> reference = FirebaseDatabase.getInstance().getReference("transacciones").child("ingresos").child(firebaseUser.getUid()).child(id).setValue(hashMap);
        reference.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                dialog.dismiss();
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            }
        });
    }

    private void subirGasto(String str_categoria, String str_valor_transaccion, String fechaTransaccion, String str_descripcion_transaccion) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RandomString randomString = new RandomString();
        String id = randomString.nextString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("categoria", str_categoria);
        hashMap.put("valor", str_valor_transaccion);
        hashMap.put("fecha_de_transaccion", fechaTransaccion);
        hashMap.put("descripcion", str_descripcion_transaccion);

        Task<Void> reference = FirebaseDatabase.getInstance().getReference("transacciones").child("gastos").child(firebaseUser.getUid()).child(id).setValue(hashMap);
        reference.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                dialog.dismiss();
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
            }
        });
    }
}