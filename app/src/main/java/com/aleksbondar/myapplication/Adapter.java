package com.aleksbondar.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

public class Adapter extends BaseAdapter {
    private Context context;
    private String[] buttons;

    public Adapter(Context c, String[] buttons) {
        context = c;
        this.buttons = buttons;

    }

    @Override
    public int getCount() {
        return buttons.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            button = new Button(context);
        }

        else {
            button = (Button) convertView;
        }

       button.setText(buttons[position]);
        button.setId(position);
        button.setOnClickListener(v -> {
            Button b = (Button) v;
            String s = b.getText().toString();
            Intent intent;
            switch (s) {
                case "Movies":
                    intent = new Intent(v.getContext(), MoviesActivity.class);
                    v.getContext().startActivity(intent);
                    break;
                case "Traffic Cameras":
                    intent = new Intent(v.getContext(), TrafficCamerasActivity.class);
                    v.getContext().startActivity(intent);
                    break;
                default:
                    Toast.makeText(context, s, Toast.LENGTH_SHORT ).show();
            }
//            if (s == "Movies") {
//                Intent intent = new Intent(v.getContext(), MoviesActivity.class);
//                v.getContext().startActivity(intent);
//            }
//            if (s == "Traffic Cameras") {
//                Intent intent = new Intent(v.getContext(), TrafficCamerasActivity.class);
//                v.getContext().startActivity(intent);
//            }
//            else Toast.makeText(context, s, Toast.LENGTH_SHORT ).show();


        });


        return button;
    }
}
