package com.example.almu.pokedex2018;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
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
                "id INT,nombre TEXT,altura INT,peso INT, tipos TEXT, habilidades TEXT, oculto INT)";
        db.execSQL(sqlCreate);

        this.db = db;
        initialInserts();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void initialInserts()
    {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        for(int i = 1; i < 152 ; i++) {
            Call<Pokemon> call = service.getAllPokemon("" + i);
            call.enqueue(new Callback<Pokemon>() {

                @Override
                public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                    Pokemon pokemon = response.body();
                    String tipos = "", habilidades = "";

                    // Tipos
                    if (pokemon.getTipos().length == 2) {
                        tipos += pokemon.getTipos()[0].getTipo().getNombre();
                        tipos += ";";
                        tipos += pokemon.getTipos()[1].getTipo().getNombre();
                    } else
                        tipos += pokemon.getTipos()[0].getTipo().getNombre();

                    // Habilidades
                    if (pokemon.getHabilidades().length == 2) {
                        habilidades += pokemon.getHabilidades()[0].getHabilidad().getNombre();
                        habilidades += ";";
                        habilidades += pokemon.getHabilidades()[1].getHabilidad().getNombre();
                    } else
                        habilidades += pokemon.getHabilidades()[0].getHabilidad().getNombre();

                    // Guarda el nombre con el formato correcto
                    String nombreMayus = pokemon.getNombre().substring(0, 1).toUpperCase()
                            + pokemon.getNombre().substring(1);

                    if(nombreMayus.contains("-"))
                        nombreMayus = nombreMayus.replace("-","");

                    db.execSQL("INSERT INTO pokemon(id,nombre,altura,peso,tipos,habilidades,oculto)"
                            + " VALUES" +
                            "( " + pokemon.getId() + ",'" + nombreMayus + "'," + pokemon.getAltura()
                            + "," + pokemon.getPeso() + ",'" + tipos + "','" + habilidades + "',"
                            + 0 + ");");
                }

                @Override
                public void onFailure(Call<Pokemon> call, Throwable t) {
                }
            });
        }
    }

    public Pokemon getPokemon(String strId) {
        List<Pokemon> pokemonList = new ArrayList<>();
        String[] args = new String[]{};
        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM pokemon WHERE id = " + strId + " ORDER BY id;", args);

        if(c.moveToFirst()) {
            do {
                Integer id = c.getInt(0);
                String nombre = c.getString(1);
                Integer altura = c.getInt(2);
                Integer peso = c.getInt(3);
                String tipoString = c.getString(4);
                String habilidadString = c.getString(5);
                Integer oculto = c.getInt(6);

                // Tipos
                Tipo[] tipo;
                if (tipoString.contains(";")) {
                    tipo = new Tipo[2];
                    tipo[0] = new Tipo(new Contenido(tipoString.split(";")[0]));
                    tipo[1] = new Tipo(new Contenido(tipoString.split(";")[1]));
                } else {
                    tipo = new Tipo[1];
                    tipo[0] = new Tipo(new Contenido(tipoString));
                }

                // Habilidades
                Habilidad[] habilidades;
                if (habilidadString.contains(";")) {
                    habilidades = new Habilidad[2];
                    habilidades[0] = new Habilidad(
                            new Contenido(habilidadString.split(";")[0]));
                    habilidades[1] = new Habilidad(
                            new Contenido(habilidadString.split(";")[1]));
                } else {
                    habilidades = new Habilidad[1];
                    habilidades[0] = new Habilidad(new Contenido(habilidadString));
                }

                Pokemon p = new Pokemon(id, nombre, altura, peso, tipo, habilidades, oculto);
                pokemonList.add(p);

            } while (c.moveToNext());

        }

        return pokemonList.get(0);
    }
}