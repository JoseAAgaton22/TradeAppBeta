package com.app.tradeapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tradeapp.Model.Pago;
import com.app.tradeapp.R;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.ViewHolder>{

    public Context mContext;
    public List<Pago> mPagos;
    public Date fechaActual;

    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    public PagoAdapter(Context mContext, List<Pago> mPagos) {
        this.mContext = mContext;
        this.mPagos = mPagos;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pago_item, parent, false);
        return new PagoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Pago pago = mPagos.get(position);

        holder.titulo_pago.setText(pago.getNombre());
        holder.pago.setText("$ "+pago.getValor());

        String fecha = pago.getFecha_de_vencimiento();
        holder.fecha.setText(fecha);

        fechaActual = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] fechaA = format.format(fechaActual).split("-"); //mm/dd/yy
        String[] fechaP = pago.getFecha_de_vencimiento().split("-");

        int añoA = Integer.parseInt(fechaA[0]);
        int mesA = Integer.parseInt(fechaA[1]);
        int diaA = Integer.parseInt(fechaA[2]);

        int añoP = Integer.parseInt(fechaP[0]);
        int mesP = Integer.parseInt(fechaP[1]);
        int diaP = Integer.parseInt(fechaP[2]);

        if(añoP > añoA){
            if((añoP - añoA) == 1) {
                if ((12 - mesA) + (mesP) < 12 && (12 - mesA) + (mesP) > 1) {
                    holder.numero.setText(String.valueOf((12 - mesA) + (mesP)));
                    holder.tiempo.setText("Meses");
                }
                else if((12 - mesA) + (mesP) == 1){
                    if((30 - diaA) + (diaP) < 30 && (30 - diaA) + (diaP) > 1){
                        holder.numero.setText(String.valueOf((30 - diaA) + (diaP)));
                        holder.tiempo.setText("Días");
                    }
                    else if((30 - diaA) + (diaP) == 1){
                        holder.numero.setText(String.valueOf((30 - diaA) + (diaP)));
                        holder.tiempo.setText("Día");
                    }
                    else {
                        holder.numero.setText(String.valueOf((12 - mesA) + (mesP)));
                        holder.tiempo.setText("Mes");
                    }
                }
                else {
                    holder.numero.setText(String.valueOf(añoP-añoA));
                    holder.tiempo.setText("Año");
                }
            }
            else {
                holder.numero.setText(String.valueOf(añoP-añoA));
                holder.tiempo.setText("Años");
            }
        }
        else if(añoP == añoA){
            if((mesP-mesA) > 1){
                holder.numero.setText(String.valueOf(mesP-mesA));
                holder.tiempo.setText("Meses");
            }
            else if((mesP-mesA) == 1){
                if((30 - diaA) + (diaP) < 30 && (30 - diaA) + (diaP) > 1){
                    holder.numero.setText(String.valueOf((30 - diaA) + (diaP)));
                    holder.tiempo.setText("Días");
                }
                else if((30 - diaA) + (diaP) == 1){
                    holder.numero.setText(String.valueOf((30 - diaA) + (diaP)));
                    holder.tiempo.setText("Día");
                }
                else {
                    holder.numero.setText(String.valueOf(mesP - mesA));
                    holder.tiempo.setText("Mes");
                }
            }
            else if(mesP == mesA){
                if((diaP - diaA) >= 0){
                    holder.numero.setText(String.valueOf(diaP - diaA));
                    holder.tiempo.setText("Días");
                }
                else{
                    holder.numero.setText("0");
                    holder.tiempo.setText("Vencido");
                }
            }
        }
        else {
            holder.numero.setText("0");
            holder.tiempo.setText("Vencido");
        }

        if(pago.getEstado().equals("pago")){
            holder.botonPP.setBackgroundResource(R.drawable.estado_background_pago);
            holder.botonPP.setText("PAGO");
        }
        else{
            holder.botonPP.setBackgroundResource(R.drawable.estado_background);
            holder.botonPP.setText("PENDIENTE");
        }

        holder.botonPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.botonPP.getText().equals("PAGO")){
                    reference = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid()).child(pago.getId()).child("estado");
                    reference.setValue("pendiente");
                    holder.botonPP.setBackgroundResource(R.drawable.estado_background);
                    holder.botonPP.setText("PENDIENTE");
                }
                else {
                    reference = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid()).child(pago.getId()).child("estado");
                    reference.setValue("pago");
                    holder.botonPP.setBackgroundResource(R.drawable.estado_background_pago);
                    holder.botonPP.setText("PAGO");
                }
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog(pago.getId());
            }
        });

        pagoInfo(holder.titulo_pago, holder.numero, holder.tiempo);

    }

    @Override
    public int getItemCount() {
        return mPagos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView titulo_pago, numero, tiempo, fecha, pago;
        public Button botonPP;
        public ImageView eliminar;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            titulo_pago = itemView.findViewById(R.id.titulo_pago);
            numero = itemView.findViewById(R.id.numero);
            tiempo = itemView.findViewById(R.id.tiempo);
            botonPP = itemView.findViewById(R.id.boton_pp);
            fecha = itemView.findViewById(R.id.fecha_de_vencimiento);
            pago = itemView.findViewById(R.id.valor_pago);
            eliminar = itemView.findViewById(R.id.eliminar);
        }
    }

    private void pagoInfo(TextView titulo, TextView numero, TextView tiempo){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Pago pago = snapshot.getValue(Pago.class);
                    pago.setNombre(pago.getNombre());
                    pago.setDescripcion(pago.getDescripcion());
                    pago.setFecha_de_vencimiento(pago.getFecha_de_vencimiento());
                    pago.setEstado(pago.getEstado());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void eliminarPago(String idPago){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pagos").child(firebaseUser.getUid()).child(idPago);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    void confirmDialog(String idPago){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("¿Quieres eliminar este pago?");
        builder.setMessage("Este pago se eliminará permanentemente");

        builder.setPositiveButton("eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarPago(idPago);

            }
        });


        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
