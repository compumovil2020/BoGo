package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class PlaceList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        LinearLayout llPlace1 = findViewById(R.id.llPlace1);

        for(int i = 0; i < 3; i++)
        {
            final View child = getLayoutInflater().inflate(R.layout.layout_place, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 10);
            child.setLayoutParams(params);

            ConstraintLayout Clplace1 = child.findViewById(R.id.constraintLayout3);
            Clplace1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(),PlaceDescriptionActivity.class);
                    startActivity(intent);
                }

            });

            ConstraintLayout Clplace2 = child.findViewById(R.id.constraintLayout4);
            Clplace2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), PlaceDescriptionActivity.class);
                    startActivity(intent);
                }
            });

            llPlace1.addView(child);


        }
    }
}