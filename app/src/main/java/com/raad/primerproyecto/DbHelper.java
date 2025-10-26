package com.raad.primerproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raad.primerproyecto.notas.Notes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "appdb";
    //HAY QUE ACTUALIZAR A VERSION 2 DESPUES DE CREAR LA TABLA DE NOTAS
    private static final int DB_VERSION = 3;

    //INFO PARA TABLA NOTAS
    private static final String TABLE_NOTAS = "notas";
    private static final String ID_NOTA = "id_nota";
    private static final String TITLE_COL = "titulo";
    private static final String DESCRPITION_COL = "descripcion";
    private static final String DATE_COL = "fecha";



    //INFO PARA TABLA USUARIO
    private static final String TABLE_USUARIO = "usuario";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "nombre";
    private static final String EDAD_COL = "edad";
    private static final String CORREO_COL = "correo";
    private static final String CONTRA_COL = "contra";

    //Creamos el constructor
    public DbHelper (Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    //Metodo para crear la BD la primera vez que se instancia la aplicacion
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USUARIO + "("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COL + " TEXT,"
                + EDAD_COL + " INTEGER,"
                + CORREO_COL + " TEXT,"
                + CONTRA_COL + " TEXT)";

        String query2 = " CREATE TABLE " + TABLE_NOTAS + "("
                + ID_NOTA + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE_COL + " TEXT,"
                + DESCRPITION_COL + " TEXT,"
                + DATE_COL + " TEXT)";

        db.execSQL(query);
        db.execSQL(query2);
    }



    //Lo que se va a hacer cuando la BD sea modificada
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTAS);
        onCreate(db);
    }


    //Metodo para INSERTAR o CREAR un registro en la base de datos
    public long crear(Usuario usuario) {

        long id = 0;

        try {
            //Obtenemos acceso a la BD de manera que podamos escribir sobre ella
            SQLiteDatabase db = this.getWritableDatabase();

            //Creamos el SQL para insertar el registro
            ContentValues datos = new ContentValues();
            datos.put(NAME_COL,usuario.getNombre());
            datos.put(EDAD_COL,usuario.getEdad());
            datos.put(CORREO_COL,usuario.getCorreo());
            datos.put(CONTRA_COL,usuario.getContra());

            //Actualizar la BD, primero decimos en que tabla se actualizara, null y despues los datos a ingresar
            id = db.insert(TABLE_USUARIO,null,datos);

            //Cerramos la BD
            db.close();
        }catch (Exception ex){
            ex.toString();
        }

        return id;
    }

    // AQUI CREARE EL CODIGO PARA GUARDAR LAS NOTAS
    public long insertarNotas (Notes nota){

        long id = 0;

        try{

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues datos = new ContentValues();
            datos.put(TITLE_COL, nota.getTitle());
            datos.put(DESCRPITION_COL, nota.getDescription());
            datos.put(DATE_COL, nota.getDate());

            id = db.insert(TABLE_NOTAS,null,datos);

            db.close();

        }catch (Exception ex){
            ex.toString();
        }

        return id;
    }

    public ArrayList<Notes> mostrarNotas(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Notes> listaNotas = new ArrayList<>();
        Notes nota = null;
        Cursor cursorNotas = null;
        String consulta = "SELECT * FROM " + TABLE_NOTAS+ " ORDER BY "+ DATE_COL+" DESC";
        cursorNotas = db.rawQuery(consulta,null);

        if(cursorNotas.moveToFirst()){
            do {
                nota = new Notes();

                nota.setId(cursorNotas.getInt(0));
                nota.setTitle(cursorNotas.getString(1));
                nota.setDescription(cursorNotas.getString(2));
                nota.setDate(cursorNotas.getString(3));
                listaNotas.add(nota);
            }while (cursorNotas.moveToNext());
        }

        cursorNotas.close();
        return listaNotas;
    }


    //METODO PARA HACER LOGIN
    public boolean validarLogin(String correo, String contra){
        //Obtenemos acceso a la BD de manera que podamos leer sobre ella
        SQLiteDatabase db = this.getReadableDatabase();

        //La consulta traera el ID del usuario si es que el correo y el usuario coincide con algun registro en la BD
        String query = "SELECT " + ID_COL + " FROM " + TABLE_USUARIO +" WHERE " + CORREO_COL + " =? AND "
                + CONTRA_COL + " =?";

        //Es la infomracion que se manda desde el login
        String[] informacion = {correo, contra};

        //Instanciamos que el usuario no existe
        boolean existe = false;

        try{
            //Creamos un objeto de tipo cursor donde se almacenan los resultados generados por la consulta sql
            Cursor cursor = db.rawQuery(query,informacion);

            //Si encuentra resultados, el cursor apunta al primer resultado
            //En este caso no debe haber mas de un resultado, asi que seria como decir que si encontro al usuario
            if(cursor.moveToFirst()){
                //Si encuentra resultado, quiere decir que las credenciales coinicden con un registro de la tabla
                existe = true;
            }

            //Cerramos la BD
            db.close();
        }catch (SQLiteException e){
            e.printStackTrace();
        }

        //Retornamos si existe o no el usuario
        return existe;
    }

    public Notes verNota(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Notes nota = null;
        Cursor cursorNota;

        cursorNota = db.rawQuery("SELECT * FROM "+TABLE_NOTAS+" WHERE "+ID_NOTA+" = "+ id +" LIMIT 1",null);
        if(cursorNota.moveToFirst()){
            nota = new Notes();
            nota.setId(cursorNota.getInt(0));
            nota.setTitle(cursorNota.getString(1));
            nota.setDescription(cursorNota.getString(2));
            nota.setDate(cursorNota.getString(3));
            //Log.i("PRUEBA","ESTAMOS DENTRO DEL CURSOR");
        }

        db.close();

        return nota;
    }


    public boolean actualizarNota(Notes n){
        boolean estatus = false;

        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues datos = new ContentValues();
            datos.put(ID_NOTA,n.getId());
            datos.put(TITLE_COL,n.getTitle());
            datos.put(DESCRPITION_COL,n.getDescription());
            datos.put(DATE_COL,n.getDate());

            db.update(TABLE_NOTAS,datos,ID_NOTA + " = ?", new String[]{String.valueOf(n.getId())});
            estatus = true;

        }catch (SQLiteException ex){
            ex.toString();
        }

        db.close();
        return estatus;
    }

    public boolean borrarNota(int id){
        boolean eliminado = false;
        SQLiteDatabase db = this.getWritableDatabase();

        try {

            int fila = db.delete(TABLE_NOTAS, ID_NOTA + " =? ",new String[] {String.valueOf(id)});
            if(fila > 0){
                eliminado = true;
            }
        }catch (SQLiteException ex){
            ex.toString();
        }

        db.close();

        return eliminado;

    }

}
