package com.raad.primerproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        Button btnIngresar = findViewById(R.id.btnIngresar);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnRegistrar.setOnClickListener(Registrar());
        btnIngresar.setOnClickListener(Ingresar());

    }

    //Metodo para registrar usuario
    private View.OnClickListener Registrar() {
        return v -> {
            Intent i = new Intent(Login.this, FormularioRegistro.class);
            startActivity(i);
        };
    }

    //Metodo para ingresar a la sesion
    private View.OnClickListener Ingresar() {
        return v -> {
            //Encontramos nuestros campos
            EditText etUsuario = findViewById(R.id.etUsuario);
            EditText etContra = findViewById(R.id.etContra);

            //Transformamos los datos a tipo texto
            String correo = etUsuario.getText().toString();
            String contra = etContra.getText().toString();

            //Si algun campo esta vacio, le informamos al usuario
            if (correo.isEmpty() || contra.isEmpty()) {
                Toast.makeText(Login.this, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
                return; //Regresamos y evitamos que se ejecute el codigo de validacion
            }

            //Creamos un objeto de la base de datos para poder llamar al metodo de validacion de login
            DbHelper db = new DbHelper(Login.this);
            boolean resultadoLogin = db.validarLogin(correo,contra);

            if(resultadoLogin){
                //Abrimos la pantalla principal ya que se validaron las credenciales
                Intent i = new Intent(Login.this,PantallaPrincipal.class);
                startActivity(i);
                //Se cierra la actividad de Login
                finish();
            }else{
                Toast.makeText(Login.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        };
    }
}