package com.example.almu.pokedex2018;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    ImageView imageLogo;
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
                    .load("https://i.redd.it/fcys3yr59dax.gif")
                    //.placeholder(R.mipmap.desconocido)
                    .into(imageLogo);

        }

        changeScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeScreen();
    }

    protected void changeScreen() {
        final Intent intent = new Intent(this, MainActivity.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
            }
        }, 3000);
    }
}
