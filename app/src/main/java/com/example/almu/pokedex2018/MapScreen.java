package com.example.almu.pokedex2018;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.io.IOException;
import java.net.URL;
import java.util.Random;


public class MapScreen extends AppCompatActivity {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private String token = "pk.eyJ1IjoiYWx1bTI2IiwiYSI6ImNqcGp1N2o3bDAza3UzcW41YnR6bzI2NnIifQ.mbyex_W6B8HxCtskCBfsXg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, token);

        setContentView(R.layout.activity_map_screen);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                MapScreen.this.mapboxMap = mapboxMap;

                double latitud = 40.416775, longitud = -3.703790;
                Bundle bundle = getIntent().getExtras();

                // Si venimos de detalles de un pokémon
                if(bundle != null) {
                    latitud = bundle.getDouble("latitud");
                    longitud = bundle.getDouble("longitud");
                }
                /*
                Bitmap icon = null;
                try {
                    icon = BitmapFactory.decodeStream(
                            new URL("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png")
                                    .openConnection().getInputStream());
                } catch (IOException e) {

                }

                icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_menu_camera);

                mapboxMap.addImage("Marcador", icon);
                */
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitud, longitud))
                        .title("Título del marcador")
                        .snippet("Contenido del marcador"));

                // Cámara
                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(latitud, longitud))
                        .zoom(5)
                        .tilt(20)
                        .build();

                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),
                        2000);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
