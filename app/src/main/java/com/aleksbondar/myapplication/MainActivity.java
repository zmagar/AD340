package com.aleksbondar.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    GridView gridView;

    private EditText enterNameField;
    private EditText enterEmailField;
    private EditText enterPassField;

    String[] buttons = {"Movies", "Traffic Cameras", "Map with Cameras", "Button 4"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String sharedPrefFile = "com.aleksbondar.myapplication.sharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterNameField = findViewById(R.id.name);
        enterEmailField = findViewById(R.id.email);
        enterPassField = findViewById(R.id.password);

       // Context context = getApplicationContext();
        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        editor = sharedPreferences.edit();




        enterNameField.setText(sharedPreferences.getString("name", ""));
        enterEmailField.setText(sharedPreferences.getString("email", ""));
        enterPassField.setText(sharedPreferences.getString("password", ""));





                Button buttonSend = findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(v -> {
            signIn();

        });

        gridView = findViewById(R.id.grid_view);

        Adapter adapter = new Adapter(MainActivity.this, buttons);
        gridView.setAdapter(adapter);

    }

    private void signIn() {
        Log.d("FIREBASE", "signIn");
        String name = enterNameField.getText().toString();
        String email = enterEmailField.getText().toString();
        String password = enterPassField.getText().toString();


        // 1 - validate display name, email, and password entries
        if (!isLoginValid(name, email, password)) return;



        // 2 - save valid entries to shared preferences
        editor =  sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();



        // 3 - sign into Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            // update profile. displayname is the value entered in UI
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("FIREBASE", "User profile updated.");
                                                // Go to FirebaseActivity
                                                startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                                            }
                                        }
                                    });

                        } else {
                            Log.d("FIREBASE", "sign-in failed");

                            Toast.makeText(MainActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }


                });


    }

    private boolean isLoginValid(String name, String email, String pass) {
        return (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty());
    }

    protected void onDestroy() {
        super.onDestroy();
    }


}

