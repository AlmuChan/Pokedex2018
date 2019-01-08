package com.example.almu.pokedex2018;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class StartActivity extends AppCompatActivity {

    ImageView imageLogo;
    TextView tv_fraseStart;
    String[] frases = {"Dándole una chuche a Squirtle...","Sacando a pasear a Arcanine...",
        "Peinando a Ninetales...","Bañando a Eeve...", "Despertando a Snorlax...",
        "Abrazando a Jigglypuff...","Encendiendo la cola de Charmander...",
        "Probándole ropa a Pikachu...","Dándole su hueso a Cubone..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Desactivar barras de titulo y poner a pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);
        imageLogo = findViewById(R.id.imagenLogo);

        if(imageLogo != null) {
            GlideApp.with(this)
                    .load("https://i.gifer.com/4OKl.gif")
                    //.placeholder(R.mipmap.desconocido)
                    .into(imageLogo);

        }

        BDPokemon pokemons = new BDPokemon(this, "BDPokemon", null, 1);
        SQLiteDatabase db = pokemons.getWritableDatabase();

        // Añadir progressdialog donde ponga loading para que de sensación de carga

        changeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //changeScreen();
    }

    protected void changeScreen() {
        final Intent intent = new Intent(this, MainActivity.class);
        int duration = 3000;

        SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        if (preferences.getBoolean("firstLogin", true)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstLogin", false);
            editor.commit();
            duration = 20000;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, duration);

        //Texto random intro
        Random rdm = new Random();
        tv_fraseStart = findViewById(R.id.tv_fraseStart);
        tv_fraseStart.setText(frases[rdm.nextInt(frases.length)]);
    }
}
