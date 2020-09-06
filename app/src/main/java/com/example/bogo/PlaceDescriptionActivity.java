package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PlaceDescriptionActivity extends AppCompatActivity {

    LinearLayout llResenas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);

        llResenas = findViewById(R.id.llOtrasResenas);


        for(int i = 0; i < 10; i++)
        {
            View child = getLayoutInflater().inflate(R.layout.review_layout, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 50);
            child.setLayoutParams(params);
            llResenas.addView(child);
        }

    }
}