package com.raad.primerproyecto.notas;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raad.primerproyecto.DbHelper;
import com.raad.primerproyecto.R;

import java.util.ArrayList;
//ESTA CLASE ES EN LA CUAL SE VERA LA LISTA DE LAS NOTAS, ES DECIR, ORDENADAS DE MANERA VERTICAL

public class ListNoteActivity extends AppCompatActivity {


    DbHelper db;
    RecyclerView listaNotas;
    NotaContactosAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaNotas = findViewById(R.id.rvListaNotas);
        listaNotas.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        listaNotas.addItemDecoration(divider);

        db = new DbHelper(ListNoteActivity.this);


        adapter = new NotaContactosAdapter(db.mostrarNotas());

        listaNotas.setAdapter(adapter);



    }

    // Refrescamos la lista cada vez que se regresa a esta pantalla
    @Override
    protected void onResume() {
        super.onResume();
        adapter = new NotaContactosAdapter(db.mostrarNotas());
        listaNotas.setAdapter(adapter);
    }

}