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

public class FormularioRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnCrearRegistro = findViewById(R.id.btnCrearRegistro);

        btnCancelar.setOnClickListener(Cancelar());
        btnCrearRegistro.setOnClickListener(CrearRegistro());

    }

    private View.OnClickListener CrearRegistro() {
        return v-> {

            //Obtenemos la infomracion de nuestros campos en pantalla
            EditText etNombre = findViewById(R.id.etNombre);
            EditText etContra = findViewById(R.id.etContra);
            EditText etEdad = findViewById(R.id.etEdad);
            EditText etCorreo = findViewById(R.id.etCorreo);

            //Creamos el objeto Usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(etNombre.getText().toString());
            usuario.setEdad(Integer.parseInt(etEdad.getText().toString()));
            usuario.setCorreo(etCorreo.getText().toString());
            usuario.setContra(etContra.getText().toString());

            //Crear en la base de datos
            DbHelper db = new DbHelper(FormularioRegistro.this);
            long id = db.crear(usuario);

            if(id > 0){
                Toast.makeText(FormularioRegistro.this,"REGISTRO EXITOSO",Toast.LENGTH_LONG).show();
                finish();

            }else{
                Toast.makeText(FormularioRegistro.this,"REGISTRO FALLIDO",Toast.LENGTH_LONG).show();
            }
        };
    }

    //Funcion para cancelar el registro en el formulario
    private View.OnClickListener Cancelar() {
        return v-> {
          finish();
        };

    }
}