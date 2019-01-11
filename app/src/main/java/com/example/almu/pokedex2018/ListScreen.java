package com.example.almu.pokedex2018;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class ListScreen extends AppCompatActivity implements
        PokemonDetailFragment.OnFragmentInteractionListener {
    RecyclerView rv;
    LinearLayoutManager llm;
    SQLiteDatabase db;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        // RecyclerView y LinearLayoutManager
        rv = findViewById(R.id.reciclador);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        BDPokemon pokemons = new BDPokemon(this, "BDPokemon", null, 1);
        db = pokemons.getWritableDatabase();

        // Datos para las cardview en una lista que irá al adapter
        // Cargamos la lista con Retrofit y la pokeApi
        List<Pokemon> pokemonList = cargarDatos();

        RVAdapter adapter = new RVAdapter(pokemonList);
        rv.setAdapter(adapter);

        // Recarga con gesto de lista de pokémon
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                ListScreen.Recarga r  = new ListScreen.Recarga();
                r.execute();
            }
        });
    }

    private List<Pokemon> cargarDatos() {
        List<Pokemon> pokemonList = new ArrayList<>();
        String[] args = new String[]{};
        Cursor c = db.rawQuery("SELECT * FROM pokemon ORDER BY id;", args);
        if(c.moveToFirst()) {
            // Recorremos el cursor hasta que no haya mas registros
            do {
                Integer id = c.getInt(0);
                String nombre = c.getString(1);
                double altura = c.getDouble(2);
                double peso = c.getDouble(3);
                String tipoString = c.getString(4);
                String habilidadString = c.getString(5);
                Integer oculto = c.getInt(6);
                double latitud = c.getDouble(7);
                double longitud = c.getDouble(8);

                // Tipos
                Tipo[] tipo;
                if(tipoString.contains(";"))
                {
                    tipo = new Tipo[2];
                    tipo[0] = new Tipo(new Contenido(tipoString.split(";")[0]));
                    tipo[1] = new Tipo(new Contenido(tipoString.split(";")[1]));
                }
                else{
                    tipo = new Tipo[1];
                    tipo[0] = new Tipo(new Contenido(tipoString));
                }

                // Habilidades
                Habilidad[] habilidades;
                if(habilidadString.contains(";"))
                {
                    habilidades = new Habilidad[2];
                    habilidades[0] = new Habilidad(
                            new Contenido(habilidadString.split(";")[0]));
                    habilidades[1] = new Habilidad(
                            new Contenido(habilidadString.split(";")[1]));
                }
                else{
                    habilidades = new Habilidad[1];
                    habilidades[0] = new Habilidad(new Contenido(habilidadString));
                }

                Pokemon p = new Pokemon(id,nombre,altura,peso,tipo,habilidades,oculto,latitud,longitud);
                pokemonList.add(p);

            }while(c.moveToNext());
        }

        return pokemonList;
    }

    // AsyncTask que recarga la lista del recyclerview
    private class Recarga extends AsyncTask<Void, Void, List<Pokemon>> {

        @Override
        protected List<Pokemon> doInBackground(Void... params) {
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            return cargarDatos();
        }

        @Override
        protected void onPostExecute(List<Pokemon> list) {
            RVAdapter adapter = new RVAdapter(list);
            rv.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            rv.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cuando se vuelva a esta pantalla, se recargará la lista automáticamente.
        Recarga r  = new Recarga();
        r.execute();
    }
}
