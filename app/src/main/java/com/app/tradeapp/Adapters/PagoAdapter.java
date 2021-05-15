package com.app.tradeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        String fecha = fechaTraducida(pago.getFecha_de_vencimiento());
        holder.fecha.setText(fecha);

        fechaActual = Calendar.getInstance().getTime();
        String fechaA = fechaActual.toString().substring(0,11)+fechaActual.toString().substring(30,34);

        if(pago.getFecha_de_vencimiento().substring(11, 15).equals(fechaA.substring(11,15))){
            if(pago.getFecha_de_vencimiento().substring(4, 7).equals(fechaA.substring(4,7))){
                if(Integer.parseInt(pago.getFecha_de_vencimiento().substring(8, 10))-Integer.parseInt(fechaA.substring(8,10)) > 0){
                    holder.numero.setText(String.valueOf(Integer.parseInt(pago.getFecha_de_vencimiento().substring(8, 10))-Integer.parseInt(fechaA.substring(8,10))));
                    holder.tiempo.setText("Días");
                }
                else if(Integer.parseInt(pago.getFecha_de_vencimiento().substring(8, 10))-Integer.parseInt(fechaA.substring(8,10)) == 0){
                    holder.numero.setText("0");
                    holder.tiempo.setText("Días");
                }
                else {
                    holder.numero.setText("0");
                    holder.tiempo.setText("Vencido");
                }
            }
            else {
                int mesesCount = meses(pago.getFecha_de_vencimiento().substring(4, 7), fechaA.substring(4, 7), 0);

                if(mesesCount == 0){
                    holder.numero.setText(String.valueOf(mesesCount));
                    holder.tiempo.setText("Vencido");
                }
                else if(mesesCount < 0){
                    int mesesD = mesesCount*-1;
                    int diasM = 30;
                    int sumM = (30*mesesD);
                    if(fechaA.substring(4, 7).equals("Jan") || fechaA.substring(4, 7).equals("Mar") || fechaA.substring(4, 7).equals("May")
                            || fechaA.substring(4, 7).equals("Jul") || fechaA.substring(4, 7).equals("Aug") || fechaA.substring(4, 7).equals("Oct")
                            || fechaA.substring(4, 7).equals("Dec")){diasM = 31;}
                    if(mesesCount == -1){sumM=0;}
                    int diasDeMas = (diasM - Integer.parseInt(fechaA.substring(8, 10)))+sumM+Integer.parseInt(pago.getFecha_de_vencimiento().substring(8, 10));
                    if(diasDeMas > 30){
                        holder.numero.setText(String.valueOf(diasDeMas/30));
                        holder.tiempo.setText("Meses");
                    }
                    else {
                        holder.numero.setText(String.valueOf(diasDeMas));
                        holder.tiempo.setText("Días");
                    }
                }
                else {
                    holder.numero.setText(String.valueOf(mesesCount));
                    holder.tiempo.setText("Meses");
                }
            }
        }
        else {
            if(Integer.parseInt(pago.getFecha_de_vencimiento().substring(11, 15)) > Integer.parseInt(fechaA.substring(11,15))){
                if(Integer.parseInt(pago.getFecha_de_vencimiento().substring(11, 15)) - Integer.parseInt(fechaA.substring(11,15)) > 1){
                    holder.tiempo.setText("Años");
                    holder.numero.setText(String.valueOf(Integer.parseInt(pago.getFecha_de_vencimiento().substring(11, 15)) - Integer.parseInt(fechaA.substring(11,15))));
                }
                else {
                    int mesesCount = meses(pago.getFecha_de_vencimiento().substring(4, 7), fechaA.substring(4, 7), 1);
                    holder.numero.setText(String.valueOf(mesesCount));
                    holder.tiempo.setText("Meses");
                }
            }
            else {
                holder.tiempo.setText("Vencido");
                holder.numero.setText("0");
            }
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

        pagoInfo(holder.titulo_pago, holder.numero, holder.tiempo);

    }

    @Override
    public int getItemCount() {
        return mPagos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView titulo_pago, numero, tiempo, fecha;
        public Button botonPP;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            titulo_pago = itemView.findViewById(R.id.titulo_pago);
            numero = itemView.findViewById(R.id.numero);
            tiempo = itemView.findViewById(R.id.tiempo);
            botonPP = itemView.findViewById(R.id.boton_pp);
            fecha = itemView.findViewById(R.id.fecha_de_vencimiento);
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

    private String fechaTraducida(String fecha){
        String fechaT = "";
        switch (fecha.substring(0,3)){
            case "Sun":
                fechaT = fechaT+"Domingo "+fecha.substring(8,10)+" de";
                break;
            case "Mon":
                fechaT = fechaT+"Lunes "+fecha.substring(8,10)+" de";
                break;
            case "Tue":
                fechaT = fechaT+"Martes "+fecha.substring(8,10)+" de";
                break;
            case "Wed":
                fechaT = fechaT+"Miercoles "+fecha.substring(8,10)+" de";
                break;
            case "Thu":
                fechaT = fechaT+"Jueves "+fecha.substring(8,10)+" de";
                break;
            case "Fri":
                fechaT = fechaT+"Viernes "+fecha.substring(8,10)+" de";
                break;
            case "Sat":
                fechaT = fechaT+"Sabado "+fecha.substring(8,10)+" de";
                break;
        }

        switch (fecha.substring(4,7)){
            case ("Jan"):
                fechaT = fechaT+" Enero ";
                break;
            case ("Feb"):
                fechaT = fechaT+" Febrero ";
                break;
            case ("Mar"):
                fechaT = fechaT+" Marzo ";
                break;
            case ("Apr"):
                fechaT = fechaT+" Abril ";
                break;
            case ("May"):
                fechaT = fechaT+" Mayo ";
                break;
            case ("Jun"):
                fechaT = fechaT+" Junio ";
                break;
            case ("Jul"):
                fechaT = fechaT+" Julio ";
                break;
            case ("Aug"):
                fechaT = fechaT+" Agosto ";
                break;
            case ("Sep"):
                fechaT = fechaT+" Septiembre ";
                break;
            case ("Oct"):
                fechaT = fechaT+" Octubre ";
                break;
            case ("Nov"):
                fechaT = fechaT+" Noviembre ";
                break;
            case ("Dec"):
                fechaT = fechaT+" Diciembre ";
                break;
        }

        return fechaT;
    }

    private int meses(String primerMes, String segundoMes, int años){

        String[] ordenMeses = new String[12];
        ordenMeses[0] = "Jan";
        ordenMeses[1] = "Feb";
        ordenMeses[2] = "Mar";
        ordenMeses[3] = "Apr";
        ordenMeses[4] = "May";
        ordenMeses[5] = "Jun";
        ordenMeses[6] = "Jul";
        ordenMeses[7] = "Aug";
        ordenMeses[8] = "Sep";
        ordenMeses[9] = "Oct";
        ordenMeses[10] = "Nov";
        ordenMeses[11] = "Dec";

        int count = 0;
        boolean contar = false;
        boolean encontrado = false;

        for(int k = 0; k <= años; k++) {
            for (int i= 0; i < 12; i++) {
                if(ordenMeses[i].equals(primerMes) && contar){
                    encontrado = true;
                    count += 1;
                    break;
                }
                if (ordenMeses[i].equals(segundoMes)) {
                    contar = true;
                } else if (contar) {
                    count += 1;
                }
            }
        }

        if(!encontrado){
            count = 0;
        }
        else if(encontrado){
            count = -1*count;
        }

        return count;

    }

}
