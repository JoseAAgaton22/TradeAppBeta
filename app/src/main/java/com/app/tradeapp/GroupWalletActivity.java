package com.app.tradeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.tradeapp.Adapters.CodeudorAdapter;
import com.app.tradeapp.Adapters.SubvalorAdapter;
import com.app.tradeapp.Model.RandomString;
import com.app.tradeapp.Model.Subvalor;
import com.app.tradeapp.Model.User;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GroupWalletActivity extends AppCompatActivity {

    EditText nombre_pago, descripcion_pago, valor_pago, subvalor_nombre, subvalor_valor;
    Button boton_subvalores, boton_pago, boton_pendiente, boton_completado, boton_calcular;
    ImageView boton_cancel, boton_done, boton_cancelar_subvalor;
    TextView mesYaño;
    CompactCalendarView calendario;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());
    RecyclerView recyclerSubvalores;
    RecyclerView recyclerView;
    SubvalorAdapter subvalorAdapter;
    Calendar calendar;
    List<Subvalor> mSubvalores;
    List<User> codeudorList;
    List<String> agregados;
    CodeudorAdapter codeudorAdapter;
    String fechaVencimiento = "";
    String horaVencimiento = "";
    String estado = "ninguno";
    ProgressDialog dialog;
    Dialog hora;
    double total = 0;

    FirebaseUser firebaseUser;
    String id;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_wallet);

        nombre_pago = findViewById(R.id.nombre_pago);
        descripcion_pago = findViewById(R.id.descripcion_pago);
        valor_pago = findViewById(R.id.valor_pago);
        boton_subvalores = findViewById(R.id.boton_subvalores);
        boton_cancelar_subvalor = findViewById(R.id.borrar_subvalor);
        boton_pago = findViewById(R.id.boton_pago);
        boton_calcular = findViewById(R.id.boton_calcular);
        boton_pendiente = findViewById(R.id.boton_pendiente);
        boton_completado = findViewById(R.id.boton_done);
        calendario = findViewById(R.id.calendario);
        calendario.setUseThreeLetterAbbreviation(true);
        boton_cancel = findViewById(R.id.cancel);
        boton_done = findViewById(R.id.done);
        subvalor_nombre = findViewById(R.id.subvalor_nombre);
        subvalor_valor = findViewById(R.id.subvalor_valor);
        hora = new Dialog(this);
        mesYaño = findViewById(R.id.indicador_fecha);
        calendar = Calendar.getInstance();

        getAgregados();
        recyclerView = findViewById(R.id.recycler_codeudores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GroupWalletActivity.this, LinearLayoutManager.HORIZONTAL, false));
        codeudorList = new ArrayList<>();
        codeudorAdapter = new CodeudorAdapter(GroupWalletActivity.this, codeudorList);
        recyclerView.setAdapter(codeudorAdapter);

        recyclerSubvalores = findViewById(R.id.recycler_subvalores);
        recyclerSubvalores.setHasFixedSize(true);
        recyclerSubvalores.setLayoutManager(new LinearLayoutManager(GroupWalletActivity.this));
        mSubvalores = new ArrayList<>();
        subvalorAdapter = new SubvalorAdapter(GroupWalletActivity.this, mSubvalores);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        agregados = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Mi notificacion", "Mi notificacion", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        boton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        boton_subvalores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_subvalor_nombre = subvalor_nombre.getText().toString();
                String str_subvalor_valor = subvalor_valor.getText().toString();

                if (TextUtils.isEmpty(str_subvalor_nombre) || TextUtils.isEmpty(str_subvalor_valor)) {
                    Toast.makeText(GroupWalletActivity.this, "Debes llenar los valores", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double valor = Double.parseDouble(str_subvalor_valor);
                        Subvalor subvalor = new Subvalor(str_subvalor_nombre, valor);
                        mSubvalores.add(subvalor);
                        recyclerSubvalores.setAdapter(subvalorAdapter);

                        subvalor_nombre.setText("");
                        subvalor_valor.setText("");
                    } catch (Exception ex) {
                        Toast.makeText(GroupWalletActivity.this, "Debes ingresar un valor numerico", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        boton_calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total = 0;
                for(Subvalor s : mSubvalores){
                    total += s.getSubvalor_valor();
                }
                valor_pago.setText(String.valueOf(total));
            }
        });

        boton_cancelar_subvalor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(GroupWalletActivity.this, v);
                subvalor_nombre.setText("");
                subvalor_valor.setText("");
                subvalor_nombre.clearFocus();
                subvalor_valor.clearFocus();
            }
        });

        boton_pago.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(boton_pendiente.isPressed()){
                    boton_pendiente.setPressed(false);
                }
                boton_pago.setPressed(true);
                estado = "pago";

                return true;
            }
        });
        boton_pendiente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(boton_pago.isPressed()){
                    boton_pago.setPressed(false);
                }
                boton_pendiente.setPressed(true);
                estado = "pendiente";

                return true;
            }
        });

        calendario.setFirstDayOfWeek(Calendar.SUNDAY);
        mesYaño.setText(fechaEspecifica(calendar.getTime()));

        calendario.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                fechaVencimiento = format.format(dateClicked);

                ImageView done;
                TimePicker timePicker;

                hora.setContentView(R.layout.popup_hora);

                timePicker = hora.findViewById(R.id.time_picker);
                done = hora.findViewById(R.id.done);

                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay > 12) {
                            int horaTarde = hourOfDay-12;
                            horaVencimiento = String.valueOf(horaTarde) + ":" + String.valueOf(minute) + " PM";
                        }
                        else {
                            horaVencimiento = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + " AM";
                        }
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(GroupWalletActivity.this, "Establecido a las "+horaVencimiento, Toast.LENGTH_SHORT).show();
                        hora.dismiss();
                    }
                });

                hora.show();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mesYaño.setText(fechaEspecifica(firstDayOfNewMonth));
            }
        });

        boton_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_valor_pago = valor_pago.getText().toString();
                double pagoTotal = Double.parseDouble(str_valor_pago);
                int codeudores = CodeudorAdapter.str_id.size() + 1;
                double deudaDividida = pagoTotal/codeudores;

                String str_deudaDividida = String.format("%.0f", deudaDividida);
                String str_nombre_pago = nombre_pago.getText().toString();
                String str_descripcion_pago = descripcion_pago.getText().toString();

                if(TextUtils.isEmpty(str_nombre_pago) || TextUtils.isEmpty(str_valor_pago)){
                    Toast.makeText(GroupWalletActivity.this, "Debes poner un nombre y valor", Toast.LENGTH_SHORT).show();
                }
                else if((!boton_pago.isPressed()) && (!boton_pago.isPressed())){
                    Toast.makeText(GroupWalletActivity.this, "Debes seleccionar el estado del pago", Toast.LENGTH_SHORT).show();
                }
                else if(fechaVencimiento.equals("") || horaVencimiento.equals("")){
                    Toast.makeText(GroupWalletActivity.this, "Debes seleccionar la fecha y hora", Toast.LENGTH_SHORT).show();
                }
                else if (CodeudorAdapter.str_id.size()==0) {
                    Toast.makeText(GroupWalletActivity.this, "Debes seleccionar al menos un codeudor", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(GroupWalletActivity.this, "Mi notificacion");
                        builder.setContentTitle("Notificación de prueba");
                        builder.setSmallIcon(R.drawable.ic_trade_app);
                        builder.setAutoCancel(true);
                        builder.setContentText("Tienes un nuevo pago compartido en el que te corresponde pagar $"+str_deudaDividida);

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(GroupWalletActivity.this);
                        managerCompat.notify(1, builder.build());

                        dialog = new ProgressDialog(GroupWalletActivity.this);
                        dialog.setMessage("Estamos subiendo tu pago");
                        dialog.show();
                        subirPago(str_nombre_pago, str_descripcion_pago, str_deudaDividida, estado, fechaVencimiento, horaVencimiento);
                        subirPagoCodeudor(str_nombre_pago, str_descripcion_pago, str_deudaDividida, estado, fechaVencimiento, horaVencimiento);
                        subirGasto(str_deudaDividida,fechaVencimiento, str_descripcion_pago);
                        subirGastoCodeudor(str_deudaDividida,fechaVencimiento, str_descripcion_pago);

                    }catch (Exception ex){
                        Toast.makeText(GroupWalletActivity.this, "Debes introducir un valor numérico", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        boton_completado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_valor_pago = valor_pago.getText().toString();
                double pagoTotal = Double.parseDouble(str_valor_pago);
                int codeudores = CodeudorAdapter.str_id.size() + 1;
                double deudaDividida = pagoTotal/codeudores;

                String str_deudaDividida = String.format("%.0f", deudaDividida);
                String str_nombre_pago = nombre_pago.getText().toString();
                String str_descripcion_pago = descripcion_pago.getText().toString();


                if(TextUtils.isEmpty(str_nombre_pago) || TextUtils.isEmpty(str_valor_pago)){
                    Toast.makeText(GroupWalletActivity.this, "Debes poner un nombre y valor", Toast.LENGTH_SHORT).show();
                }
                else if(!boton_pago.isPressed() && !boton_pendiente.isPressed()){
                    Toast.makeText(GroupWalletActivity.this, "Debes seleccionar el estado del pago", Toast.LENGTH_SHORT).show();
                }
                else if(fechaVencimiento.equals("") || horaVencimiento.equals("")){
                    Toast.makeText(GroupWalletActivity.this, "Debes seleccionar la fecha y hora", Toast.LENGTH_SHORT).show();
                }
                else if (CodeudorAdapter.str_id.size()==0) {
                    Toast.makeText(GroupWalletActivity.this, "Debes seleccionar al menos un codeudor", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(GroupWalletActivity.this, "Mi notificacion");
                        builder.setContentTitle("Notificación de prueba");
                        builder.setSmallIcon(R.drawable.ic_trade_app);
                        builder.setAutoCancel(true);
                        builder.setContentText("Tienes un nuevo pago compartido en el que te corresponde pagar $"+str_deudaDividida);

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(GroupWalletActivity.this);
                        managerCompat.notify(1, builder.build());

                        dialog = new ProgressDialog(GroupWalletActivity.this);
                        dialog.setMessage("Estamos subiendo tu pago");
                        dialog.show();
                        subirPago(str_nombre_pago, str_descripcion_pago, str_deudaDividida, estado, fechaVencimiento, horaVencimiento);
                        subirPagoCodeudor(str_nombre_pago, str_descripcion_pago, str_deudaDividida, estado, fechaVencimiento, horaVencimiento);
                        subirGasto(str_deudaDividida,fechaVencimiento, str_descripcion_pago);
                        subirGastoCodeudor(str_deudaDividida,fechaVencimiento, str_descripcion_pago);

                    }catch (Exception ex){
                        Toast.makeText(GroupWalletActivity.this, "Debes introducir un valor numérico", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPago(String nombre_pago, String descripcion_pago, String valor_pago, String estado, String fechaVencimiento, String horaVencimiento){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RandomString randomString = new RandomString();
        String id = randomString.nextString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid()).child(id);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("nombre",nombre_pago);
        hashMap.put("descripcion", descripcion_pago);
        hashMap.put("valor", valor_pago);
        hashMap.put("estado", estado);
        hashMap.put("fecha_de_vencimiento", fechaVencimiento);
        hashMap.put("hora_de_vencimiento", horaVencimiento);

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                dialog.dismiss();
                onBackPressed();
            }
        });

    }

    private void subirPagoCodeudor(String nombre_pago, String descripcion_pago, String valor_pago, String estado, String fechaVencimiento, String horaVencimiento){

        RandomString randomString = new RandomString();
        String id = randomString.nextString();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        for (int i = 0; i<CodeudorAdapter.str_id.size(); i++) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("pagos").child(CodeudorAdapter.str_id.get(i)).child(id);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", id);
            hashMap.put("nombre",nombre_pago);
            hashMap.put("descripcion", descripcion_pago);
            hashMap.put("valor", valor_pago);
            hashMap.put("estado", estado);
            hashMap.put("fecha_de_vencimiento", fechaVencimiento);
            hashMap.put("hora_de_vencimiento", horaVencimiento);

            reference.setValue(hashMap);

        }
    }

    private void subirGasto(String str_valor_transaccion, String fechaTransaccion, String str_descripcion_transaccion) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RandomString randomString = new RandomString();
        String id = randomString.nextString();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("categoria", "Deudas");
        hashMap.put("valor", str_valor_transaccion);
        hashMap.put("fecha_de_transaccion", fechaTransaccion);
        hashMap.put("descripcion", str_descripcion_transaccion);

        Task<Void> reference = FirebaseDatabase.getInstance().getReference("transacciones").child("gastos").child(firebaseUser.getUid()).child(id).setValue(hashMap);
    }

    private void subirGastoCodeudor(String str_valor_transaccion, String fechaTransaccion, String str_descripcion_transaccion) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RandomString randomString = new RandomString();
        String id = randomString.nextString();

        for (int i = 0; i < CodeudorAdapter.str_id.size(); i++) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("transacciones").child("gastos").child(CodeudorAdapter.str_id.get(i)).child(id);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", id);
            hashMap.put("categoria", "Deudas");
            hashMap.put("valor", str_valor_transaccion);
            hashMap.put("fecha_de_transaccion", fechaTransaccion);
            hashMap.put("descripcion", str_descripcion_transaccion);

            reference.setValue(hashMap);
        }
    }

    private String fechaEspecifica(Date firstDayOfNewMonth){

        String año = firstDayOfNewMonth.toString().substring(30,34);
        String mes = firstDayOfNewMonth.toString().substring(4,7);
        String letrero = "";

        switch (mes){
            case ("Jan"):
                letrero = letrero+"Enero "+año;
                break;
            case ("Feb"):
                letrero = letrero+"Febrero "+año;
                break;
            case ("Mar"):
                letrero = letrero+"Marzo "+año;
                break;
            case ("Apr"):
                letrero = letrero+"Abril "+año;
                break;
            case ("May"):
                letrero = letrero+"Mayo "+año;
                break;
            case ("Jun"):
                letrero = letrero+"Junio "+año;
                break;
            case ("Jul"):
                letrero = letrero+"Julio "+año;
                break;
            case ("Aug"):
                letrero = letrero+"Agosto "+año;
                break;
            case ("Sep"):
                letrero = letrero+"Septiembre "+año;
                break;
            case ("Oct"):
                letrero = letrero+"Octubre "+año;
                break;
            case ("Nov"):
                letrero = letrero+"Noviembre "+año;
                break;
            case ("Dec"):
                letrero = letrero+"Diciembre "+año;
                break;
        }

        return letrero;
    }

    private static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void getAgregados() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("personas")
                .child(firebaseUser.getUid()).child("agregados");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                agregados.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    agregados.add(dataSnapshot.getKey());
                }
                showAgregados();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void showAgregados() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                codeudorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    for (String id : agregados) {
                        if (user.getId().equals(id)) {
                            codeudorList.add(user);
                        }
                    }
                }
                codeudorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}