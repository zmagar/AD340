package com.aleksbondar.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CamAdapter extends ArrayAdapter<TrafficCamera> {
    private final Context context;
    private ArrayList<TrafficCamera> trafficCameras;
   // LayoutInflater inflater;

    // Constructor required by the Adapter class
    public CamAdapter(@NonNull Context context, @NonNull ArrayList<TrafficCamera> trafficCameras) {
        super(context, -1, trafficCameras);
        this.context = context;
        this.trafficCameras = trafficCameras;
        //inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.trafficcamera_list, parent, false);

        TextView camId = view.findViewById(R.id.camId);
        camId.setText(trafficCameras.get(position).camId);
        ImageView image = view.findViewById(R.id.image);
        TextView description = view.findViewById(R.id.description);
        String imageUrl = "https://www.seattle.gov/trafficcams/images/" + trafficCameras.get(position).imageUrl;
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).into(image);
        description.setText(trafficCameras.get(position).description);

        System.out.println(trafficCameras);

        return view;

    }

}
