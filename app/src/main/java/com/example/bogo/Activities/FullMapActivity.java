package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bogo.R;

public class FullMapActivity extends Fragment {

    Button buttonLocation1;
    Button buttonLocation2;
    Button buttonLocation3;
    Button buttonLocation4;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_full_map, container, false);

        buttonLocation1 = view.findViewById(R.id.buttonLocation1);
        buttonLocation2 = view.findViewById(R.id.buttonLocation2);
        buttonLocation3 = view.findViewById(R.id.buttonLocation3);
        buttonLocation4 = view.findViewById(R.id.buttonLocation4);

        buttonLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceDescriptionActivity.class);
                startActivity(i);
            }
        });

        buttonLocation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceDescriptionActivity.class);
                startActivity(i);
            }
        });

        buttonLocation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceDescriptionActivity.class);
                startActivity(i);
            }
        });

        buttonLocation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlaceDescriptionActivity.class);
                startActivity(i);
            }
        });


        return view;
    }
}