package com.app.tradeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText correo, contraseña;
    Button boton_iniciar;
    TextView label_registrar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializacion de las variables en activity_login
        correo = findViewById(R.id.txt_correo);
        contraseña = findViewById(R.id.txt_contraseña);
        boton_iniciar = findViewById(R.id.boton_iniciar);
        label_registrar = findViewById(R.id.label_registrar);

        auth = FirebaseAuth.getInstance();

        //se lleva al usuario a la pantalla de registro al presionar la etiqueta "Registrate"
        label_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //Comprobacion de datos para iniciar sesion
        boton_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(LoginActivity.this); //popup de carga mientras se comprueban los datos
                pd.setMessage("Espera un momento");
                pd.show();

                String str_correo = correo.getText().toString();  //se reciben los datos puestos en los campos de texto
                String str_contraseña = contraseña.getText().toString();

                if(TextUtils.isEmpty(str_contraseña) || TextUtils.isEmpty(str_correo)){ //aviso en caso de no haber llenado los campos
                    Toast.makeText(LoginActivity.this, "Debes llenar los campos", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else {
                    //funcion firebase para iniciar sesion con correo y contraseña
                    auth.signInWithEmailAndPassword(str_correo, str_contraseña).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //En la base de datos se buscará el usuario con el id correspondiente al correo ingresado
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(auth.getCurrentUser().getUid());

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pd.dismiss(); //Una vez iniciada la sesion del usuario, se cierra el popup de carga
                                        //Se lleva al usuario a la pantalla principal
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        pd.dismiss();
                                    }
                                });
                            }else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "Usuario o contraseña invalidos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}