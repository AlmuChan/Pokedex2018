package com.example.almu.pokedex2018;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;

    RecyclerView rv;
    LinearLayoutManager llm;
    SQLiteDatabase db;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Añadimos permisos que requiera la app obligatoriamente para su funcionamiento
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(CAMERA);
        permissions.add(RECORD_AUDIO);
        permissions.add(READ_CONTACTS);

        //Lista de permisos no dados metidos para usarlos después
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Manda un mensaje a tu pokémon favorito", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        // Recarga de lista de pokémon
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                Recarga r  = new Recarga();
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
                Integer altura = c.getInt(2);
                Integer peso = c.getInt(3);
                String tipoString = c.getString(4);
                String habilidadString = c.getString(5);
                Integer oculto = c.getInt(6);

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

                Pokemon p = new Pokemon(id,nombre,altura,peso,tipo,habilidades,oculto);
                pokemonList.add(p);

            }while(c.moveToNext());
        }

        return pokemonList;
    }

    // AsyncTask que recarga la lista del recyclerview
    private class Recarga extends AsyncTask<Void, Void, List<Pokemon>> {

        @Override
        protected List<Pokemon> doInBackground(Void... params) {
           /* try {
                Thread.sleep(3000);
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

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
        int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("La app requiere estos permisos para " +
                                            "funcionar. Por favor acéptalos",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(
                                                new String[permissionsRejected.size()]),
                                                ALL_PERMISSIONS_RESULT);
                                    }
                                }
                            });
                            return;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_capture) {
            Intent intent = new Intent(this, CapturePokemon.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            getContacts();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Contactos que tienen instalada la app en su dispositivo.
    public void getContacts(){
        String s;

        String[] datos = new String[] { ContactsContract.Data._ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE };

        String selectionClause = ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";

        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                datos,
                selectionClause,
                null,
                sortOrder);

        s = "";

        while(c.moveToNext()){
            s += "Identificador: " +
                    c.getString(0) +
                    " Nombre: " +
                    c.getString(1) +
                    " Número: " +
                    c.getString(2)+
                    " Tipo: " +
                    c.getString(3)+"\n";
        }
        c.close();

        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

        // Mostrar sólo cuales tienen la app instalada
        // TODO
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
