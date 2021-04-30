package com.aleksbondar.myapplication;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MoviesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movies_details);


        ImageView image = findViewById(R.id.image);
        TextView title = findViewById(R.id.title);
        TextView year = findViewById(R.id.year);
        TextView director = findViewById(R.id.director);
        TextView description = findViewById(R.id.description);


        String[] moviesDescriptions = getIntent().getStringArrayExtra("Movie_descriptions");

        //Loading info into appropriate fields from the passed array:

        Picasso.get().load(moviesDescriptions[3]).placeholder(R.drawable.ic_launcher_foreground).into(image);
        title.setText(moviesDescriptions[0]);
        year.setText(moviesDescriptions[1]);
        director.setText("Director: " + moviesDescriptions[2]);
        description.setText(moviesDescriptions[4]);
        description.setMovementMethod(new ScrollingMovementMethod());



    }
}
