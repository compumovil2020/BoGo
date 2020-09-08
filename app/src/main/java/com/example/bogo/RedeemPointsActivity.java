package com.example.bogo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RedeemPointsActivity extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_redeem_points, container, false);

        LinearLayout llRedeemPoints = view.findViewById(R.id.llLayoutRedeemPoints);

        for(int i = 0; i < 5; i++)
        {
            View child = getLayoutInflater().inflate(R.layout.layout_redeem_points, null);

            ConstraintLayout cl11 = child.findViewById(R.id.constraintLayout11);
            cl11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Redimido!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), MyRedeemActivity.class);
                    startActivity(intent);
                }
            });

            ConstraintLayout cl12 = child.findViewById(R.id.constraintLayout12);
            cl12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Redimido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), MyRedeemActivity.class);
                    startActivity(intent);
                }
            });

            ConstraintLayout cl13 = child.findViewById(R.id.constraintLayout13);
            cl13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Redimido!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), MyRedeemActivity.class);
                    startActivity(intent);
                }
            });

            llRedeemPoints.addView(child);
        }

        return view;
    }



}