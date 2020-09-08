package com.example.bogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MyRedeemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_redeem);

        LinearLayout llMyRedeem = findViewById(R.id.llMyRedeem);

        for(int i = 0; i < 5; i++)
        {
            View child = getLayoutInflater().inflate(R.layout.layout_my_redeem, null);
            llMyRedeem.addView(child);
        }

    }
}