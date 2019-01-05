package com.example.almu.pokedex2018;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Random;

public class ShakeService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // Aceleración sin gravedad
    private float mAccelCurrent; // Aceleración actual con gravedad
    private float mAccelLast; // Última aceleración con gravedad

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;

        // Si el móvil ha sido agitado entonces ejecuta lo siguiente
        if (mAccel > 11) {
            Random rnd = new Random();
            BDPokemon pokemons = new BDPokemon(this, "BDPokemon", null, 1);
            SQLiteDatabase db = pokemons.getWritableDatabase();

            db.execSQL("UPDATE pokemon SET oculto = 1 WHERE id = " + rnd.nextInt(152) + ";");

            //int color = Color.argb(255, rnd.nextInt(256),
            //        rnd.nextInt(256), rnd.nextInt(256));
            //ServiceActivity.tvShakeService.setText("Service detects the Shake Action!! " +
            //        "Color is also changed..!!!");
            //ServiceActivity.tvShakeService.setTextColor(color);
            Toast.makeText(this, "¡Pokémon capturado!", Toast.LENGTH_SHORT).show();
        }
    }
}
