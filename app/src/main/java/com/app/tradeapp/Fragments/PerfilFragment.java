package com.app.tradeapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tradeapp.EditProfileActivity;
import com.app.tradeapp.Model.GestionTransaccion;
import com.app.tradeapp.Model.Perfil;
import com.app.tradeapp.R;
import com.app.tradeapp.StartActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    TextView name_user, cuenta, correo, amigos, seguidores, biografia;
    ImageButton editar_perfil;
    CircleImageView foto_perfil;
    Button cerrar;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        foto_perfil = view.findViewById(R.id.foto_perfil);
        name_user = view.findViewById(R.id.id_usuario);
        cuenta = view.findViewById(R.id.cuenta);
        correo = view.findViewById(R.id.correo_user);
        amigos = view.findViewById(R.id.amigos);
        seguidores = view.findViewById(R.id.seguidores);
        editar_perfil = view.findViewById(R.id.editar_perfil);
        biografia = view.findViewById(R.id.biografia_user);
        cerrar = view.findViewById(R.id.boton_cerrar);

        datos_usuario();
        perfil_usuario();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    private void datos_usuario() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Perfil perfil = snapshot.getValue(Perfil.class);
                String nombre_completo = (perfil.getNombres() + " " + perfil.getApellidos());
                String nombreMayus = "";

                for (String i : nombre_completo.split(" ")) {
                    nombreMayus += i.substring(0, 1).toUpperCase() + i.substring(1, i.length()).toLowerCase() + " ";
                }

                String url = perfil.getImagen();
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round);
                Glide.with(getActivity()).load(url).apply(requestOptions).into(foto_perfil);
                name_user.setText(nombreMayus);
                cuenta.setText(perfil.getCuenta());
                correo.setText(perfil.getCorreo());
                biografia.setText(perfil.getBio());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void perfil_usuario() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference_amigos = FirebaseDatabase.getInstance().getReference("personas").child(firebaseUser.getUid()).child("agregados");
        DatabaseReference reference_seguidores = FirebaseDatabase.getInstance().getReference("personas").child(firebaseUser.getUid()).child("agregado");

        reference_amigos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                amigos.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        reference_seguidores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                seguidores.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}