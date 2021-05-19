package com.aleksbondar.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class TrafficCamerasActivity extends AppCompatActivity {
    ListView camListView;
    CamAdapter camAdapter;
    ArrayList<TrafficCamera> cameras = new ArrayList<>();
    String seattleCamerasUrl = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafficcameras);

        camListView = findViewById(R.id.list);
        camAdapter = new CamAdapter(this, cameras);
        camListView.setAdapter(camAdapter);



        if (checkConnection()) {
            Log.v("myTag", "Connected");
            getCamsInfo(seattleCamerasUrl);
        } else {
            Log.v("myTag", "No Connection!");
            Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void getCamsInfo(String camerasUrl) {

        //from https://developer.android.com/training/volley/request

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

                } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            Log.v("cameras array", "test" + cameras);

                camAdapter.notifyDataSetChanged();

            }, error -> {
                Log.d("JSON", "Error: " + error.getMessage());
                Snackbar.make(camListView,
                        "Error...",
                        Snackbar.LENGTH_LONG
                ).show();
            });
        requestQueue.add(jsonObjectRequest);

    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }



}

