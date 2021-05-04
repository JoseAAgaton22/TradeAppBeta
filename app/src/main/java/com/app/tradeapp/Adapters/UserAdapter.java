package com.app.tradeapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);

        holder.boton_solicitud.setVisibility(View.VISIBLE);

        holder.nombre.setText(user.getNombres());
        holder.apellido.setText(user.getApellidos());
        Glide.with(mContext).load(user.getImgURL()).into(holder.imagen);
        siguiendo(user.getId(), holder.boton_solicitud);

        if(user.getId().equals(firebaseUser.getUid())){
            holder.boton_solicitud.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", mContext.MODE_PRIVATE).edit();
                editor.putString("perfilID", user.getId());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.frame, new PerfilFragment()).commit();

            }
        });

        holder.boton_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.boton_solicitud.getText().toString().equals("ENVIAR SOLICITUD")){
                    FirebaseDatabase.getInstance().getReference().child("personas").child(firebaseUser.getUid()).child("agregados").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("personas").child(user.getId()).child("agregado").child(firebaseUser.getUid()).setValue(true);
                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("personas").child(firebaseUser.getUid()).child("agregados").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("personas").child(user.getId()).child("agregado").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nombre;
        public TextView apellido;
        public CircleImageView imagen;
        public Button boton_solicitud;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombre);
            apellido = itemView.findViewById(R.id.apellido);
            imagen = itemView.findViewById(R.id.imagen_perfil);
            boton_solicitud = itemView.findViewById(R.id.boton_solicitud);

        }
    }

    private void siguiendo(final String userID, final Button boton){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("personas")
                .child(firebaseUser.getUid()).child("agregados");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userID).exists()){
                    boton.setText("AGREGADO");
                }
                else {
                    boton.setText("ENVIAR SOLICITUD");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
