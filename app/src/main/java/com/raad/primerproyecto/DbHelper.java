package com.raad.primerproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "appdb";
    private static final int DB_VERSION = 1;
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

        db.execSQL(query);
    }

    //Lo que se va a hacer cuando la BD sea modificada
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
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
}
