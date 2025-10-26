package com.raad.primerproyecto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.raad.primerproyecto.notas.AddNoteActivity;
import com.raad.primerproyecto.notas.ListNoteActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PantallaPrincipal extends AppCompatActivity {


    private static final int REQ_CODE = 1;
    ImageView ivImagen;
    TextView tvAutor;

    //Para crear una notificacion
    Intent intent;
    NotificationCompat.Builder builder;
    PendingIntent pendingIntent;
    NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ivImagen = findViewById(R.id.ivImagen);
        tvAutor = findViewById(R.id.tvAutor);
        Button btnCargarImagen = findViewById(R.id.btnCargarImagen);
        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
        Button btnCrearNota = findViewById(R.id.btnCrearNota);
        Button btnVerNotas = findViewById(R.id.btnVerNotas);


        btnCargarImagen.setOnClickListener(cargarImagen());
        btnTomarFoto.setOnClickListener(tomarFoto());
        btnCrearNota.setOnClickListener(paginaNota());
        btnVerNotas.setOnClickListener(verNotas());
    }

    private View.OnClickListener verNotas() {
        return v-> {
            Intent i = new Intent(PantallaPrincipal.this, ListNoteActivity.class);
            startActivity(i);
        };
    }

    private View.OnClickListener paginaNota() {
        return v-> {
            Intent i = new Intent(PantallaPrincipal.this, AddNoteActivity.class);
            startActivity(i);
        };
    }

    private View.OnClickListener tomarFoto() {
        return v -> {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,REQ_CODE);
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQ_CODE && resultCode == RESULT_OK){
            Bitmap foto = (Bitmap) data.getExtras().get("data");
            tvAutor.setText("YO (FOTO LOCAL)");
            ivImagen.setImageBitmap(foto);
            Log.i("EM","SE TOMO UNA FOTO");
        }else{
            Toast.makeText(this,"No Jalo",Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private View.OnClickListener cargarImagen() {
        return v-> {

            //TextView tvAutor = findViewById(R.id.tvAutor);
            //ivImagen = findViewById(R.id.ivImagen);

            //Preparamos la solicitud apuntando a la API
            Request request = new Request.Builder().url("https://picsum.photos/v2/list").build();

            //Cliente HTTP
            OkHttpClient client = new OkHttpClient();

            //Se crea una llamada, es decir, se envia la peticion HTTP
            client.newCall(request).enqueue(new Callback() {


                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) return; //Se verifica si la respuesta fue exitosa, sino se regresa

                    try {
                        String json = response.body().string(); //Obtenemos el cuerpo de respuesta JSON como un String
                        JSONArray array = new JSONArray(json); //Convertimos el String en un arreglo JSON

                        //Elige un indice aleatorio de entre todos los del arreglo, en este caso una foto aleatoria
                        JSONObject foto = array.getJSONObject(new Random().nextInt(array.length()));
                        String autor = foto.getString("author"); //Extraemos el autor de la foto
                        String imagenurl = foto.getString("download_url"); //Extraemos el enlace de la foto

                        runOnUiThread(()-> {
                            tvAutor.setText(autor);
                            Glide.with(PantallaPrincipal.this).load(imagenurl).into(ivImagen);

                        });
                        Log.i("EM","SE RECIBIO UNA FOTO DE LA WEB");

                    }catch (Exception e){
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(PantallaPrincipal.this, "Error al procesar JSON", Toast.LENGTH_SHORT).show());

                    }

                }

                //Este metodo se llama si existe algun fallo al realizar la solicitud como problemas en la red
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    runOnUiThread(()->
                            Toast.makeText(PantallaPrincipal.this,"ERROR AL CARGAR",Toast.LENGTH_SHORT).show());
                }
            });
        };

    }
}