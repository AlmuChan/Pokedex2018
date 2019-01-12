package com.example.almu.pokedex2018;

import android.content.Intent;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAcceder;
    EditText txtUsuario, txtPassword;
    TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAcceder = findViewById(R.id.btnAcceder);
        txtPassword = findViewById(R.id.etxtPassword);
        txtUsuario = findViewById(R.id.etxtUsuario);
        txtError = findViewById(R.id.txtError);

        btnAcceder.setOnClickListener(this);
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
        }
    }

    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("0"))
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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

                DataOutputStream printout;
                DataInputStream input;
                URL url = new URL("http://almudenalopezsanchez.000webhostapp.com/consultaLogin.php");
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();
                //Creo el Objeto JSON
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("usuario", strings[0]);
                jsonParam.put("password", strings[1]);
                // Envio los parametros por post.
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
                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena

                    String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                    return resultJSON;
                }
            }
            catch (Exception e) {
            }
            return null;
        }
    }
}
