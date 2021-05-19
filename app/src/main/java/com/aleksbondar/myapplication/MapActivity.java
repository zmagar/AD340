package com.aleksbondar.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    // Seattle by default to make things possibly easier
    private final LatLng defaultLocation = new LatLng(47.608013, -122.335167);

    private GoogleMap gMap;
    private static final int PERMISSIONS_REQUEST = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String seattleCamerasUrl = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
    ArrayList<TrafficCamera> cameras = new ArrayList<>();
    //Button locationButton;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

        getCamsInfo(seattleCamerasUrl);


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        gMap = googleMap;

        getLocationPermission();
        gMap.setMyLocationEnabled(true);

        updateLocationUI();
        getDeviceLocation();


    }

    private void getDeviceLocation() {
        if (locationPermissionGranted) {
            @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(),
                                    lastKnownLocation.getLongitude()), 10));
                        }
                    } else {
                        Log.d("location permission", "Current location is null. Using defaults.");
                        gMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, 14));
                        gMap.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                }
            });
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (gMap == null) {
            return;
        }
        if (locationPermissionGranted) {
            if (lastKnownLocation == null) {
                gMap.addMarker(new MarkerOptions().position(defaultLocation));
                gMap.setMinZoomPreference(12);
            } else {
                LatLng curLoc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
                gMap.addMarker(new MarkerOptions().position(curLoc));
                gMap.setMinZoomPreference(12);
            }
        } else {
            gMap.setMyLocationEnabled(false);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            lastKnownLocation = null;
            getLocationPermission();

        }

    }


    private void getCamsInfo(String camerasUrl) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, camerasUrl, null, response -> {
            Log.v("Cameras", response.toString());

            try {
                JSONArray camArray = response.getJSONArray("Features");
                Log.v("camArray", camArray.toString());



                for (int idx = 1; idx < camArray.length(); idx++) {

                    JSONObject camData = camArray.getJSONObject(idx);
                    Log.v("camData", camData.toString());

                    JSONArray coord = camData.getJSONArray("PointCoordinate");

                    double[] camCoordinates = {coord.getDouble(0), coord.getDouble(1)};

                    JSONArray arr = camData.getJSONArray("Cameras");


                    TrafficCamera camera = new TrafficCamera(arr.getJSONObject(0).getString("Id"),
                            arr.getJSONObject(0).getString("Description"),
                            arr.getJSONObject(0).getString("ImageUrl"),
                            camCoordinates);
                    Log.v("Camera ", camera.imageUrl);
                    cameras.add(camera);
                }
                showMarkers();

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            Log.v("cameras array", "test" + cameras);

        }, error -> {
            Log.d("JSON", "Error: " + error.getMessage());
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void showMarkers() {
        for (int i = 0; i < cameras.size(); i++) {
            TrafficCamera trafficCamera = cameras.get(i);
            LatLng latLng = new LatLng(trafficCamera.getCoordinates()[0], trafficCamera.getCoordinates()[1]);

            Marker marker = gMap.addMarker(new MarkerOptions().position(latLng)
                    .title(trafficCamera.getDescription())
                    .snippet("https://www.seattle.gov/trafficcams/images/" + trafficCamera.getImageUrl()));
            marker.setTag(i);
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {

        updateLocationUI();

        return true;
    }
}