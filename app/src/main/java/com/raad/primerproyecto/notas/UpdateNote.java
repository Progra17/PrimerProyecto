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

public class UpdateNote extends AppCompatActivity {

    EditText etActualizarTitle, etActualizarDescrpition, etActualizarDate;
    Button btnActualizarNota, btnBorrarNota;
    Notes nota;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etActualizarTitle = findViewById(R.id.etActualizarTitulo);
        etActualizarDescrpition = findViewById(R.id.etActualizarDescripcion);
        etActualizarDate = findViewById(R.id.etActualizarDate);
        btnActualizarNota = findViewById(R.id.btnActualizarNota);
        btnBorrarNota = findViewById(R.id.btnBorrarNota);

        etActualizarDate.setOnClickListener(actualizarFecha());
        btnActualizarNota.setOnClickListener(actualizarNota());
        btnBorrarNota.setOnClickListener(borrarNota());

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                //SI EL ID NO ES NULO, RECIBE EL "EXTRA" QUE SE MANDO DESDE NOTACONTACTOSADAPTER
                //NOTA PARA EL FUTURO, el "key" DEBE COINICIDR EN DONDE SE RECIBE LO EXTRA Y DESDE DONDE SE MANDA
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        DbHelper db = new DbHelper(UpdateNote.this);
        nota = db.verNota(id);

        if (nota != null) {
            etActualizarTitle.setText(nota.getTitle());
            etActualizarDescrpition.setText(nota.getDescription());
            etActualizarDate.setText(nota.getDate());
        }

    }

    private View.OnClickListener borrarNota() {
        return v->{

            //Instanciamos el objeto de DBHelper para poder utilizar el metodo de crear nota
            DbHelper db = new DbHelper(UpdateNote.this);
            boolean eliminado = db.borrarNota(id);
            if(eliminado){
                Toast.makeText(UpdateNote.this, "NOTA ELIMINADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(UpdateNote.this, "ERROR AL TRATAR DE ELIMINAR", Toast.LENGTH_SHORT).show();
            }

        };
    }

    private View.OnClickListener actualizarFecha() {
        return v -> {
            String strTitle = etActualizarTitle.getText().toString();
            String strDescription = etActualizarDescrpition.getText().toString();

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateNote.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                    etActualizarDate.setText(date);
                }
            }, year, month, day);
            datePickerDialog.show();
        };
    }

    private View.OnClickListener actualizarNota() {
        return v -> {

            String strTitle = etActualizarTitle.getText().toString();
            String strDescription = etActualizarDescrpition.getText().toString();
            String strDate = etActualizarDate.getText().toString();

            if (strTitle.isBlank()) {
                Toast.makeText(UpdateNote.this, "TITULO VACIO", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strDescription.isBlank()) {
                Toast.makeText(UpdateNote.this, "DESCRIPCION VACIA", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strDate.isBlank()) {
                Toast.makeText(UpdateNote.this, "FECHA VACIA", Toast.LENGTH_SHORT).show();
                return;
            }

            //Creamos el objeto de la clase nota
            Notes nota = new Notes();
            nota.setId(id);
            nota.setTitle(strTitle);
            nota.setDescription(strDescription);
            nota.setDate(strDate);

            //Instanciamos el objeto de DBHelper para poder utilizar el metodo de crear nota
            DbHelper db = new DbHelper(UpdateNote.this);
            boolean estatus = db.actualizarNota(nota);

            if (estatus) {
                Toast.makeText(UpdateNote.this, "NOTA ACTUALIZADA CON EXITO", Toast.LENGTH_LONG).show();
                finish();

            } else {
                Toast.makeText(UpdateNote.this, "ACTUALIZACION DE NOTA FALLIDA", Toast.LENGTH_LONG).show();
            }


        };

    }

}
