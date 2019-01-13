package com.example.almu.pokedex2018;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class CapturePokemon extends AppCompatActivity
        implements PokemonDetailFragment.OnFragmentInteractionListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private ImageView imageShake;
    private String urlGif = "https://cdn57.androidauthority.net/wp-content/uploads/2015/05/" +
            "Chop-Twice-for-flashlight.gif";
    private LinearLayout linearLayout;
    int numC;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_pokemon);

        imageShake = findViewById(R.id.ivShake);
        linearLayout = findViewById(R.id.linearCaptura);
        if(imageShake != null) {
            GlideApp.with(this)
                    .load(urlGif)
                    .into(imageShake);
        }

        // Uso de la clase ShakeDetector y del acelerómetro al agitar el móvil.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            // Al agitar el móvil se realiza lo siguiente.
            @Override
            public void onShake(int count) {
                Random rnd = new Random();
                BDPokemon pokemons = new BDPokemon(CapturePokemon.this,
                        "BDPokemon", null, 1);
                SQLiteDatabase db = pokemons.getWritableDatabase();

                int numCapturado = rnd.nextInt(152);

                db.execSQL("UPDATE pokemon SET oculto = 1 WHERE id = " + numCapturado + ";");

                Toast.makeText(CapturePokemon.this, "¡Pokémon capturado!",
                        Toast.LENGTH_SHORT).show();

                // Coge usuario de preferencias
                SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
                String user = preferences.getString("user", "");

                numC = numCapturado;
                usuario = user;

                ObtenerPokemonsCapturados login = new ObtenerPokemonsCapturados();
                login.execute(user);

                // Sonido al capturar
                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(CapturePokemon.this);
                if(pref.getBoolean("checkSonido", true))
                    PokeUtils.playSound(numCapturado);

                Bundle bundle = new Bundle();
                bundle.putString("id", "" + numCapturado);
                PokemonDetailFragment detallePoke = PokemonDetailFragment.newInstance();
                detallePoke.setArguments(bundle);

                if(detallePoke != null){
                    try{
                        getSupportFragmentManager().beginTransaction()
                            .replace(R.id.captureScreen, detallePoke)
                            .addToBackStack(null)
                            .commit();
                        linearLayout.setVisibility(View.GONE);
                    }catch(IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector,
                mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            if(getSupportFragmentManager().getBackStackEntryCount() == 1)
                linearLayout.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }

    public class ActualizarPokes extends AsyncTask<String,Void,Void> {


        @Override
        protected Void doInBackground(String... strings) {

            try {
                HttpURLConnection urlConn;
                URL url = new URL("http://almudenalopezsanchez.000webhostapp.com/actualizarPokemons.php");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("pokesCapturados", strings[0]);
                jsonParam.put("usuario", strings[1]);

                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int respuesta = urlConn.getResponseCode();

                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result.append(line);
                    }
                    br.close();

                }
            }
            catch (Exception e) {
            }
            return null;
        }
    }

    public class ObtenerPokemonsCapturados extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {

            // Actualiza pokemon en usuario db
            ActualizarPokes actualizarPokes = new ActualizarPokes();
            actualizarPokes.execute(s + ";" + numC, usuario);
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpURLConnection urlConn;
                URL url = new URL("http://almudenalopezsanchez.000webhostapp.com/obtenerPokesCapturados.php");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("usuario", strings[0]);

                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int respuesta = urlConn.getResponseCode();

                StringBuilder result = new StringBuilder();

                if (respuesta == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        result.append(line);
                    }
                    br.close();

                    JSONObject respuestaJSON = new JSONObject(result.toString());

                    JSONObject resultJSON = respuestaJSON.getJSONObject("pokesCapturados");

                    return resultJSON.getString("pokesCapturados");

                }
            }
            catch (Exception e) {
            }
            return null;
        }
    }
}
