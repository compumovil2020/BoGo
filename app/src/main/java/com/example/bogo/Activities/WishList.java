package com.example.bogo.Activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.bogo.R;

public class WishList extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wish_list, container, false);

        LinearLayout llWishPlace = view.findViewById(R.id.llWishList);

        for(int i = 0; i < 3; i++)
        {
            View child = getLayoutInflater().inflate(R.layout.layout_wish_list, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 10);
            child.setLayoutParams(params);

            ConstraintLayout ClWishlist1 = child.findViewById(R.id.constraintLayout5);
            ClWishlist1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PlaceDescriptionActivity.class);
                    startActivity(intent);
                }
            });

            ConstraintLayout ClWishlist2 = child.findViewById(R.id.ConstraintLayout6);
            ClWishlist2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PlaceDescriptionActivity.class);
                    startActivity(intent);
                }
            });
            llWishPlace.addView(child);
        }


        return view;
    }

}