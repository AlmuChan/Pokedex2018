package com.example.almu.pokedex2018;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
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

        // Fragment manager
        //fm = this.getSupportFragmentManager();

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

                // Sonido al capturar
                playSound(numCapturado);

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

    public void playSound(int numCapturado) {
        String idFormatted = String.format("%03d", numCapturado);
        String url = "http://pokedream.com/pokedex/images/cries/" + idFormatted + ".mp3";
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
        }
        mediaPlayer.prepareAsync();
        //You can show progress dialog here untill it prepared to play
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }
}
