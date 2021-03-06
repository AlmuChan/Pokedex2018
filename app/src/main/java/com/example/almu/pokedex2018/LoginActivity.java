package com.example.almu.pokedex2018;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAcceder;
    Button btnRegistro;
    EditText txtUsuario, txtPassword;
    TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAcceder = findViewById(R.id.btnAcceder);
        btnRegistro = findViewById(R.id.btnRegistro);
        txtPassword = findViewById(R.id.etxtPassword);
        txtUsuario = findViewById(R.id.etxtUsuario);
        txtError = findViewById(R.id.txtError);

        SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        if (preferences.getBoolean("remember", false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        btnAcceder.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnAcceder:
                ObtenerWebService obtenerWebService = new ObtenerWebService();
                obtenerWebService.execute(txtUsuario.getText().toString(),
                        txtPassword.getText().toString());

                break;
            case R.id.btnRegistro:
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                break;
        }
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0"))
            {
                SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("remember", true);
                editor.putString("user", txtUsuario.getText().toString());
                editor.putString("password", txtPassword.getText().toString());
                editor.commit();

                ObtenerPokemonsCapturados servicio = new ObtenerPokemonsCapturados();
                servicio.execute(txtUsuario.getText().toString());
            }
            else
            {
                txtError.setText("Usuario y/o contraseña incorrectos.");
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpURLConnection urlConn;
                URL url = new URL("http://almudenalopezsanchez.000webhostapp.com/consultaLogin.php");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("usuario", strings[0]);
                jsonParam.put("password", strings[1]);

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

                    String resultJSON = respuestaJSON.getString("estado");

                    return resultJSON;
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

            BDPokemon pokemons = new BDPokemon(LoginActivity.this, "BDPokemon", null, 1);
            SQLiteDatabase db = pokemons.getWritableDatabase();

            for(int i = 1; i <152; i++)
            {
                db.execSQL("UPDATE pokemon SET oculto = 0 WHERE id = " + i + ";");
            }

            if(!s.equals(""))
            {
                String[] pokes = s.split(";");
                for(int i = 0; i < pokes.length; i++)
                {
                    db.execSQL("UPDATE pokemon SET oculto = 1 WHERE id = " + pokes[i] + ";");
                }
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

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

    @Override
    public void onBackPressed() {

    }
}
