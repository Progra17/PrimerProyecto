package com.raad.primerproyecto.notas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.raad.primerproyecto.DbHelper;
import com.raad.primerproyecto.R;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etDate;
    Button btnGuardarNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        etTitle = findViewById(R.id.etTitulo);
        etDescription = findViewById(R.id.etDescripcion);
        etDate = findViewById(R.id.etDate);
        btnGuardarNota = findViewById(R.id.btnGuardarNota);


        etDate.setOnClickListener(seleccionarFecha());
        btnGuardarNota.setOnClickListener(GuardarNota());

    }

    private View.OnClickListener seleccionarFecha() {
        return v-> {
            String strTitle = etTitle.getText().toString();
            String strDescription = etDescription.getText().toString();

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = year + "/" + (month + 1) + "/"+ dayOfMonth;
                    etDate.setText(date);
                }
            },year,month,day);
            datePickerDialog.show();
        };
    }


    private View.OnClickListener GuardarNota() {
        return v-> {

            String strTitle =etTitle.getText().toString();
            String strDescription = etDescription.getText().toString();
            String strDate = etDate.getText().toString();

            if(strTitle.isBlank()){
                Toast.makeText(AddNoteActivity.this,"TITULO VACIO",Toast.LENGTH_SHORT).show();
                return;
            }

            if(strDescription.isBlank()){
                Toast.makeText(AddNoteActivity.this,"DESCRIPCION VACIA",Toast.LENGTH_SHORT).show();
                return;
            }

            if(strDate.isBlank()){
                Toast.makeText(AddNoteActivity.this,"FECHA VACIA",Toast.LENGTH_SHORT).show();
                return;
            }

            //Creamos el objeto de la clase nota
            Notes nota = new Notes();
            nota.setTitle(strTitle);
            nota.setDescription(strDescription);
            nota.setDate(strDate);

            //Instanciamos el objeto de DBHelper para poder utilizar el metodo de crear nota
            DbHelper db = new DbHelper(AddNoteActivity.this);
            long id = db.insertarNotas(nota);

            if(id > 0){
                Toast.makeText(AddNoteActivity.this,"REGISTRO EXITOSO",Toast.LENGTH_LONG).show();
                finish();

            }else{
                Toast.makeText(AddNoteActivity.this,"REGISTRO FALLIDO",Toast.LENGTH_LONG).show();
            }


        };
    }
}