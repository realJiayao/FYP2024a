package com.example.fyp2024;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class Map_Location_Activity extends AppCompatActivity {

    public static final String USERID = "com.example.fyp2024.USERID";
    public static final String USER_NAME = "com.example.fyp2024.USER_NAME";
    public static final String USER_EMAIL = "com.example.fyp2024.USER_EMAIL";


    String name;
    String email;
    String userID;

    private FusedLocationProviderClient fusedLocationClient;
    private MapView mapView;

    MyLocationNewOverlay myLocationOverlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null) {
            Intent intent = getIntent();
            userID = intent.getStringExtra(Home.USERID);
            name = intent.getStringExtra(Home.USER_NAME);
            email = intent.getStringExtra(Home.USER_EMAIL);
        }else {
            Toast.makeText(Map_Location_Activity.this, "Data missed, such as Id!", Toast.LENGTH_SHORT).show();
        }


        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Log.d("Pression", "No Pression");
            return;
        }
        Log.d("Pression", "Has Pression");
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            mapController.setCenter(startPoint);
                            Log.d("Map", "Success");
//                            Marker startMarker = new Marker(mapView);
//                            startMarker.setPosition(startPoint);
//                            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//                            startMarker.setTitle("You are here");
//                            mapView.getOverlays().add(startMarker);
                        }
                    }
                });




        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
//        myLocationOverlay.enableOrientation();
        mapView.getOverlays().add(myLocationOverlay);


        ImageButton locateMeButton = findViewById(R.id.btn_locate_me);
        locateMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocationOverlay.getMyLocation() != null) {
                    GeoPoint userLocation = myLocationOverlay.getMyLocation();
                    mapView.getController().animateTo(userLocation);
                    mapView.getController().setZoom(15);
                }
            }
        });

    }

    public void Map_BackBtn(View v){
        Log.d("Map Activity","Map Back Btn called");

        Intent intent = new Intent(Map_Location_Activity.this, Home.class);
        intent.putExtra(USERID, userID);
        intent.putExtra(USER_NAME, name);
        intent.putExtra(USER_EMAIL, email);
        startActivity(intent);
    }


}