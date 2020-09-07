package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class PlaceMapActivity extends AppCompatActivity {
    Button btnCarOption, btnBusOption, btnWalkOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);

        btnCarOption = findViewById(R.id.btnCarOption);
        btnBusOption = findViewById(R.id.btnBusOption);
        btnWalkOption = findViewById(R.id.btnWalOption);
    }
}