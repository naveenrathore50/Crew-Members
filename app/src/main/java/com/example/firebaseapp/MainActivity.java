package com.example.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
      Button Loginm ;
      Button registerm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Loginm=findViewById(R.id.Loginm);
        registerm=findViewById(R.id.registerm);
        registerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     startActivity( new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        Loginm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

    }
}