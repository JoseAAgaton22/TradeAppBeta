package com.app.tradeapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
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
import android.widget.TextView;
import android.widget.Toast;

import com.app.tradeapp.Adapters.CategoriasAdapater;
import com.app.tradeapp.Adapters.IngresosAdapter;
import com.app.tradeapp.R;

import java.util.Calendar;


public class TransactionsFragment extends Fragment implements IngresosAdapter.sendData {

    ImageView boton_cancel;
    EditText valor_transaccion, descripcion_transaccion;
    RadioGroup rg_transaccion;
    RadioButton rb_ingreso, rb_gasto;
    TextView categoria, fecha, descripcion;
    ImageButton boton_categoria;
    RecyclerView recyclerCategoria;
    Button boton_añadir;
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

        IngresosAdapter ingresosAdapter = new IngresosAdapter(this);
        boton_cancel = view.findViewById(R.id.cancel);
        valor_transaccion = view.findViewById(R.id.valor_transaccion);
        descripcion_transaccion = view.findViewById(R.id.descripcion_transaccion);
        rg_transaccion = view.findViewById(R.id.radioGroup_transaccion);
        rb_ingreso = view.findViewById(R.id.button_ingreso);
        rb_gasto = view.findViewById(R.id.button_gasto);
        categoria = view.findViewById(R.id.txt_categoria);
        fecha = view.findViewById(R.id.fecha_transaccion);
        descripcion = view.findViewById(R.id.descripcion);
        boton_categoria = view.findViewById(R.id.button_categorias);
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
                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment(), "PERFIL").commit();
            }
        });

        rg_transaccion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button_ingreso:
                        if (recyclerCategoria.getVisibility() == View.VISIBLE) {
                            recyclerCategoria.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.button_gasto:
                        if (recyclerCategoria.getVisibility() == View.VISIBLE) {
                            recyclerCategoria.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });

        boton_categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boton_categoria.setPressed(true);
                if (rb_gasto.isChecked() == false && rb_ingreso.isChecked() == false){
                    Toast.makeText (getActivity(), "Seleccione el tipo de transacción", Toast.LENGTH_LONG).show();
                } else if (rb_ingreso.isChecked() == true){
                    final IngresosAdapter adapater = new IngresosAdapter(IngresosAdapter.listaCategorias_ingreso);
                    recyclerCategoria.setAdapter(adapater);
                    if (recyclerCategoria.getVisibility() == View.VISIBLE) {
                        recyclerCategoria.setVisibility(View.GONE);
                    } else {
                        recyclerCategoria.setVisibility(View.VISIBLE);
                    }
                } else if (rb_gasto.isChecked() == true) {
                    final CategoriasAdapater adapaterCat = new CategoriasAdapater(CategoriasAdapater.listaCategorias_gasto);
                    recyclerCategoria.setAdapter(adapaterCat);
                    if (recyclerCategoria.getVisibility() == View.VISIBLE) {
                        recyclerCategoria.setVisibility(View.GONE);
                    } else {
                        recyclerCategoria.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

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

        return view;
    }

    @Override
    public void sendCategoria(String nombre_categoria) { categoria.setText(nombre_categoria);
    }

}