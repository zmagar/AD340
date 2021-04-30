package com.aleksbondar.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GridView gridView;

    private EditText enterTextField;

    String[] buttons = {"Movies", "Button 2", "Button 3", "Button 4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTextField = findViewById(R.id.textEntry);
        Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(v -> {
            String s = enterTextField.getText().toString();

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

        });

        gridView = findViewById(R.id.grid_view);

        Adapter adapter = new Adapter(MainActivity.this, buttons);
        gridView.setAdapter(adapter);

    }
}

