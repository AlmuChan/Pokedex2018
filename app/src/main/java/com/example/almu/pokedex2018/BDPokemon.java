package com.example.almu.pokedex2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BDPokemon extends SQLiteOpenHelper {
    public BDPokemon(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    SQLiteDatabase db;
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCreate = "CREATE TABLE pokemon (" +
                "id INT,nombre TEXT,altura INT,peso INT, tipos TEXT)";
        db.execSQL(sqlCreate);

        this.db = db;
        inserts();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void inserts()
    {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        for(int i = 1; i < 152 ; i++) {
            Call<Pokemon> call = service.getAllPokemon("" + i);
            call.enqueue(new Callback<Pokemon>() {

                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    Pokemon pokemon = response.body();
                    String tipos = "";

                    if (pokemon.getTipos().length == 2) {
                        tipos += pokemon.getTipos()[0].getTipo().getNombre();
                        tipos += ";";
                        tipos += pokemon.getTipos()[1].getTipo().getNombre();
                    } else
                        tipos += pokemon.getTipos()[0].getTipo().getNombre();

                    // Guarda el nombre con el formato correcto
                    String nombreMayus = pokemon.getNombre().substring(0, 1).toUpperCase()
                            + pokemon.getNombre().substring(1);

                    db.execSQL("INSERT INTO pokemon(id,nombre,altura,peso,tipos) VALUES" +
                            "( " + pokemon.getId() + ",'" + nombreMayus + "'," + pokemon.getAltura()
                            + "," + pokemon.getPeso() + ",'" + tipos + "');");
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                }
            });
        }
    }
}