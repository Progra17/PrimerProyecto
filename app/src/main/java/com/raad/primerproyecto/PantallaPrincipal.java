package com.raad.primerproyecto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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

        Button btnCargarImagen = findViewById(R.id.btnCargarImagen);




        btnCargarImagen.setOnClickListener(cargarImagen());
    }

    private View.OnClickListener cargarImagen() {
        return v-> {

            TextView tvAutor = findViewById(R.id.tvAutor);
            ImageView ivImagen = findViewById(R.id.ivImagen);

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