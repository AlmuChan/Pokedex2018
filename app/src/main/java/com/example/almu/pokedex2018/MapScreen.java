package com.example.almu.pokedex2018;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import java.util.List;

public class MapScreen extends AppCompatActivity implements
        PermissionsListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private String token = "pk.eyJ1IjoiYWx1bTI2IiwiYSI6ImNqcGp1N2o3bDAza3UzcW41YnR6bzI2NnIifQ.mbyex_W6B8HxCtskCBfsXg";
    private PermissionsManager permissionsManager;

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

                enableLocationComponent();

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitud, longitud))
                        .title("Ubicación de Pokémon"));

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

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate
            locationComponent.activateLocationComponent(this);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            //locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "Necesitas activar la ubicación", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            Toast.makeText(this, "Tienes que dar permiso para usar la ubicación", Toast.LENGTH_LONG).show();
            finish();
        }
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
