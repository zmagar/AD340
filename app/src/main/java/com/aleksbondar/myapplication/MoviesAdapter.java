package com.aleksbondar.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

public class MoviesAdapter extends ArrayAdapter<String[]> {

private final Context context;
private final String[][] zombiemovies;
    public MoviesAdapter(@NonNull Context context, String[][] movies) {
        super(context, -1, movies);
        this.context = context;
        this.zombiemovies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_movies_list, parent, false);

        ImageView image = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView year = view.findViewById(R.id.year);


        Picasso.get().load(zombiemovies[position][3]).placeholder(R.drawable.ic_launcher_foreground).into(image);
        title.setText(zombiemovies[position][0]);
        year.setText(zombiemovies[position][1]);


view.setOnClickListener(v -> {
Intent intent = new Intent(v.getContext(), MoviesDetailsActivity.class);
intent.putExtra("Movie_descriptions", zombiemovies[position]);
v.getContext().startActivity(intent);
});
        return view;
    }
}
