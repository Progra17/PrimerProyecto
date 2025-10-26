package com.raad.primerproyecto.notas;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raad.primerproyecto.R;

import java.util.ArrayList;

public class NotaContactosAdapter extends RecyclerView.Adapter<NotaContactosAdapter.NotesAdapter> {

    ArrayList<Notes> listaNotas;
    //CONSTRUCTOR
    public NotaContactosAdapter(ArrayList<Notes> listaNotas) {
        this.listaNotas = listaNotas;
    }


    //INDICA QUE LAYOUT SE ESTARA INFLANDO, EN ESTE CASO, ES AQUELLA QUE SOLO DEFINE LOS OBJETOS QUE USARA EL RECYCLER VIEW
    @NonNull
    @Override
    public NotesAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_note,null,false);
        return new NotesAdapter(view) ;
    }

    //LLENAMOS LOS DATOS
    @Override
    public void onBindViewHolder(@NonNull NotesAdapter holder, int position) {
        holder.tvVerTitulo.setText(listaNotas.get(position).getTitle());
        holder.tvVerDescripcion.setText(listaNotas.get(position).getDescription());
        holder.tvVerFecha.setText(listaNotas.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return listaNotas.size();
    }



    //BUSCAMOS LOS CAMPOS A LLENAR
    public class NotesAdapter extends RecyclerView.ViewHolder {

        TextView tvVerTitulo,tvVerDescripcion,tvVerFecha;

        public NotesAdapter(@NonNull View itemView) {
            super(itemView);
            tvVerTitulo = itemView.findViewById(R.id.tvVerTitulo);
            tvVerDescripcion = itemView.findViewById(R.id.tvVerDescripcion);
            tvVerFecha = itemView.findViewById(R.id.tvVerFecha);

            //SE AGREGA UN ONCLICKLISTENER PARA EL MOMENTO EN QUE SE DE CLIC AL ITEM EN EL RECYCLER VIEW, SE EJECUTE EL CODIGO
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("PRUEBA","ESTAMOS DENTRO DEL ONCLICKLISTENER DEL ADAPTER");
                    Context context = v.getContext();
                    Intent i = new Intent(context,UpdateNote.class);
                    //SE MANDA COMO EXTRA EL ID DE LA NOTA, CON EL NOMBRE "ID", ESTE SERÁ RECIBIDO Y ASIGANDO EN LA CLASE UPDATENOTE
                    i.putExtra("ID",listaNotas.get(getAdapterPosition()).getId());
                    context.startActivity(i);
                }
            });
        }
    }
}
