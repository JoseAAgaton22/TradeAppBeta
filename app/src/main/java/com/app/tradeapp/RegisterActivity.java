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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText correo, nombre, apellido, contraseña, contraseñaC;
    Button boton_registrar;
    TextView label_registrar;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inicializacion de variables en activity_register
        correo = findViewById(R.id.txt_correo);
        nombre = findViewById(R.id.txt_nombres);
        apellido = findViewById(R.id.txt_apellidos);
        contraseña = findViewById(R.id.txt_contraseña);
        contraseñaC = findViewById(R.id.txt_contraseña_c);
        boton_registrar = findViewById(R.id.boton_registrar);
        label_registrar = findViewById(R.id.label_iniciar);

        auth = FirebaseAuth.getInstance();

        //Se lleva al usuario a la pantalla de inicio de sesion al presionar en la etiqueta "Iniciar sesión"
        label_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        //Comprobación de datos para el registro
        boton_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this); //Se inicia popup de carga mientras se realiza el registro
                pd.setMessage("Por favor espera");
                pd.show();

                //Obtencion de la informacion en los campos del formulario de registro
                String str_correo = correo.getText().toString();
                String str_nombre = nombre.getText().toString();
                String str_apellido = apellido.getText().toString();
                String str_contraseña = contraseña.getText().toString();
                String str_contraseñaC = contraseñaC.getText().toString();

                if(TextUtils.isEmpty(str_correo) || TextUtils.isEmpty(str_nombre) || TextUtils.isEmpty(str_apellido)
                        || TextUtils.isEmpty(str_contraseña) || TextUtils.isEmpty(str_contraseñaC)){
                    //Aviso en caso de no haber llenado algún camp
                    Toast.makeText(RegisterActivity.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else if(str_contraseña.length() < 8){
                    //La contraseña debe tener minimo 8 caracteres
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else if(!str_contraseña.equals(str_contraseñaC)){
                    //Aviso en caso de que no coincidan las contraseñas
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else{
                    //Si los datos son correctos, inicia el registro
                    registrar(str_correo, str_nombre, str_apellido, str_contraseña);
                }
            }
        });
    }

    private void registrar(final String correo, final String nombre, String apellido, String contraseña){
        //funcion firebase para registrar un usuario con correo y contraseña
        auth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Una vez creado el usuario, seobtiene su ID para ser registrada con los demás datos en la base de datos
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    //En la base de datos se creará un arbol en formado JSON cuyas ramas(child) serán "usarios"-> <ID del usuario que se crea>
                    reference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userid);

                    //En la rama específicada en el anterior comentario, se ingresan los siguientes datos
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("correo", correo);
                    hashMap.put("nombres",nombre.toLowerCase());
                    hashMap.put("apellidos",apellido.toLowerCase());
                    hashMap.put("imagen", "https://firebasestorage.googleapis.com/v0/b/tradeapp-e06b1.appspot.com/o/user1.png?alt=media&token=22db1cab-5e07-46d5-bdcf-105a220e482f");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Una vez completado el registro en la base de datos, se quita el popup de carga
                                pd.dismiss();
                                //Se lleva al usuario a la pantalla principal de la app
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Email o contraseña invalidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}