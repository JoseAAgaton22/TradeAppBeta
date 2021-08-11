package com.app.tradeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tradeapp.Model.Perfil;
import com.app.tradeapp.Model.RandomString;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class EditProfileActivity extends AppCompatActivity {

    TextView cambio_foto;
    RadioButton personal, familiar;
    EditText nombre, correo, bio;
    ImageView foto_perfil,guardar_cambios, cancelar;
    ProgressDialog dialog;

    DatabaseReference reference;
    private Uri imagen_perfil;
    private StorageTask actualizar_img;
    StorageReference storageRef;
    Bitmap bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        cambio_foto = findViewById(R.id.cambio_foto);
        personal = findViewById(R.id.personal);
        familiar = findViewById(R.id.familiar);
        nombre = findViewById(R.id.nombre_act);
        correo = findViewById(R.id.correo_act);
        bio = findViewById(R.id.bio);
        foto_perfil = findViewById(R.id.foto);
        guardar_cambios = findViewById(R.id.listo);
        cancelar = findViewById(R.id.cancel);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Perfil perfil = snapshot.getValue(Perfil.class);
                Glide.with(getApplicationContext()).load(perfil.getImagen()).into(foto_perfil);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        cambio_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfileActivity.this);
            }
        });

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfileActivity.this);
            }
        });

    }

    public void actualizarPerfil (String nombre_act, String correo_act, String bio, String cuenta){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("nombres", nombre_act.toLowerCase());
        hashMap.put("correo", correo_act.toLowerCase());
        hashMap.put("bio", bio);
        hashMap.put("cuenta", cuenta);

        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                dialog.dismiss();
                onBackPressed();
            }
        });
    }

    public void actualizarImagen () {

        //Comprimir imagen
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imagen_perfil = CropImage.getPickImageResultUri(this, data);

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri uri = result.getUri();
            File url = new File(uri.getPath());
            Picasso.with(this).load(url).into(foto_perfil);


            try {
                bitmap = new Compressor(this).compressToBitmap(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream);
            final byte [] bytes = byteArrayOutputStream.toByteArray();

            guardar_cambios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(nombre.getText().toString()) || TextUtils.isEmpty(correo.getText().toString())) {
                        Toast.makeText(EditProfileActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dialog = new ProgressDialog(EditProfileActivity.this);
                        dialog.setMessage("Actualizando datos");
                        dialog.show();
                        if (personal.isChecked() == true) {
                            storageRef = FirebaseStorage.getInstance().getReference();
                            RandomString randomString = new RandomString();
                            String id = randomString.nextString();
                            final StorageReference storageReference = storageRef.child(id);
                            UploadTask uploadTask = storageReference.putBytes(bytes);

                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw Objects.requireNonNull(task.getException()) ;
                                    }
                                    return storageReference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Uri> task) {

                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        String miUrl = downloadUri.toString();

                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        reference = FirebaseDatabase.getInstance().getReference("usuarios").child(firebaseUser.getUid());

                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("imagen", miUrl);

                                        reference.updateChildren(hashMap);
                                    }
                                }
                            });
                            actualizarPerfil(nombre.getText().toString(),
                                    correo.getText().toString(), bio.getText().toString(), "Cuenta personal");
                        }
                        else if (familiar.isChecked() == true) {
                            actualizarImagen();
                            actualizarPerfil(nombre.getText().toString(),
                                    correo.getText().toString(), bio.getText().toString(), "Cuenta familiar");
                        }

                    }
                }
            });
        }
    }
}