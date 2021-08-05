package com.app.tradeapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tradeapp.Fragments.PerfilFragment;
import com.app.tradeapp.Model.User;
import com.app.tradeapp.R;
import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CodeudorAdapter extends RecyclerView.Adapter<CodeudorAdapter.ViewHolder> {

    private Context mContext;
    private List<User> codeudorList;
    private FirebaseUser firebaseUser;
    public static List<String> str_id;

    public CodeudorAdapter(Context mContext, List<User> codeudorList) {
        this.mContext = mContext;
        this.codeudorList = codeudorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.codeudor_item, parent, false);
        return new CodeudorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        str_id = new ArrayList<>();

        final User user = codeudorList.get(position);
        holder.boton_agregar.setVisibility(View.VISIBLE);
        holder.nombres.setText(user.getNombres());
        holder.apellidos.setText(user.getApellidos());
        //Glide.with(mContext).load(user.getImgURL()).into(holder.foto);

        holder.boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.boton_agregar.getText().equals("AGREGAR")){
                    holder.boton_agregar.setBackgroundResource(R.drawable.button_active_background);
                    holder.boton_agregar.setText("AGREGADO");
                    str_id.add(codeudorList.get(position).getId());
                }
                else{
                    holder.boton_agregar.setBackgroundResource(R.drawable.button_background);
                    holder.boton_agregar.setText("AGREGAR");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return codeudorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public TextView nombres;
        public TextView apellidos;
        public CircleImageView foto;
        public Button boton_agregar;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            nombres = itemView.findViewById(R.id.nombres_codeudor);
            apellidos = itemView.findViewById(R.id.apellidos_codeudor);
            foto = itemView.findViewById(R.id.foto_codeudor);
            boton_agregar = itemView.findViewById(R.id.boton_agregar);
        }
    }

}
