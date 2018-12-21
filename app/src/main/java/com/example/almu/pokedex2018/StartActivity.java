package com.example.almu.pokedex2018;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class StartActivity extends AppCompatActivity {

    ImageView imageLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Desactivar barras de titulo y poner a pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageLogo = findViewById(R.id.imagenLogo);

        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);

        setContentView(R.layout.activity_start);

        if(imageLogo != null) {
            Glide.with(this)
                    //.asGif()
                    .load(R.drawable.ic_menu_camera)
                    //.apply(new RequestOptions().placeholder(
                    //       R.drawable.ic_launcher_background).error(R.drawable.ic_menu_camera))
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
