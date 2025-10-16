package com.raad.primerproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        //btnIngresar.setOnClickListener(Ingresar());

        EditText etUsuario = findViewById(R.id.etUsuario);
        EditText etContra = findViewById(R.id.etContra);
        Button btnIngresar = findViewById(R.id.btnIngresar);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnRegistrar.setOnClickListener(Registrar());



        /*Con el findViewId se tiene acceso al widget desde la vista
        Button elboton = (Button) findViewById(R.id.button);
        elboton.setOnClickListener(HizoClicEnElBoton()); //Reacciona al clic, es decir, cuando de un clic va a pasar algo, la funcion es el parentesis
        //elboton.setText("Nuevo texto");


    }*/



    /*Metodo para ingresar a la sesion
    private View.OnClickListener Ingresar() {
        return v -> {

        };
    }*/
/*
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("EM","Regresando de la pausa");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("EM","Poniendo en pausa");
    }

    //boilerplate code buscar termino

    //Con esta funcion hago que cuando le da clic cambia el texto
    private View.OnClickListener HizoClicEnElBoton() {

        return v -> {
            Button elboton = (Button) findViewById(R.id.button);
            elboton.setText("Nuevo Texto");

            Intent i = new Intent(PrimeraPantalla.this, SegundaActividad.class);
            startActivity(i);
        };
    }*/
    }

    //Metodo para registrar usuario
    private View.OnClickListener Registrar() {
        return v -> {
            Intent i = new Intent(Login.this, FormularioRegistro.class);
            startActivity(i);
        };
    }
}