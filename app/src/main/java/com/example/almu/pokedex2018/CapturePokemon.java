package com.example.almu.pokedex2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;

public class CapturePokemon extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private ImageView imageShake;
    private String urlGif = "https://cdn57.androidauthority.net/wp-content/uploads/2015/05/" +
            "Chop-Twice-for-flashlight.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_pokemon);

        imageShake = findViewById(R.id.ivShake);
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

                Toast.makeText(CapturePokemon.this, "¡Pokémon "
                                + numCapturado + " capturado!", Toast.LENGTH_SHORT).show();
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
}
