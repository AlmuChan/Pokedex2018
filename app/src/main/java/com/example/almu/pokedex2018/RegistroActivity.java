package com.example.almu.pokedex2018;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegistroActivity extends AppCompatActivity {
    EditText txtUsuario;
    EditText txtPassword;
    EditText txtTelefono;
    TextView txtResultado;
    Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUsuario = findViewById(R.id.et_usuario);
        txtPassword = findViewById(R.id.et_password);
        txtTelefono = findViewById(R.id.et_telefono);
        txtResultado = findViewById(R.id.tv_resultado);
        btnRegistro = findViewById(R.id.btn_registro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtUsuario.getText().toString().equals("") &&
                        !txtPassword.getText().toString().equals("") &&
                        !txtTelefono.getText().toString().equals("") &&
                        (txtTelefono.getText().toString().length() == 9 ||
                        txtTelefono.getText().toString().length() == 12)) {
                    InsertarUsuario insertarUsuario = new InsertarUsuario();
                    insertarUsuario.execute(txtUsuario.getText().toString(),
                            txtPassword.getText().toString(),
                            txtTelefono.getText().toString());
                }
                else
                    txtResultado.setText("Los campos no pueden estar vacíos.");
            }
        });


    }

    public class InsertarUsuario extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1"))
            {
                txtResultado.setText("Usuario insertado correctamente");

                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            else
            {
                txtResultado.setText("No se pudo insertar el usuario");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String devuelve = "1";
            try {
                HttpURLConnection urlConn;

                DataOutputStream printout;
                DataInputStream input;
                URL url = new URL("http://almudenalopezsanchez.000webhostapp.com/insertar_login.php");

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
                jsonParam.put("telefono", strings[2]);
                // Envio los parámetros post.
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

                    JSONObject respuestaJSON = new JSONObject(result.toString());

                    String resultJSON = respuestaJSON.getString("estado");

                    devuelve = resultJSON;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return devuelve;
        }
    }
}
