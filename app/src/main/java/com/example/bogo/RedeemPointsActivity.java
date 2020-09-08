package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class RedeemPointsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_points);


        LinearLayout llRedeemPoints = findViewById(R.id.llLayoutRedeemPoints);

        for(int i = 0; i < 5; i++)
        {
            View child = getLayoutInflater().inflate(R.layout.layout_redeem_points, null);
            llRedeemPoints.addView(child);
        }
    }


}