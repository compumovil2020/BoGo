package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bogo.MyFriendsActivity;
import com.example.bogo.PlaceDescriptionActivity;
import com.example.bogo.R;
import java.util.ArrayList;

public class FriendAdapter extends ArrayAdapter<MyFriendsActivity.Friend> {
    private final Context context;
    private final ArrayList<MyFriendsActivity.Friend> values;

    public FriendAdapter(Context context, ArrayList<MyFriendsActivity.Friend> values) {
        super(context, R.layout.adapter_friend,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_friend, parent, false);
        TextView txtFriendName = rowView.findViewById(R.id.txtFriendName);
        TextView txtFriendUser =  rowView.findViewById(R.id.txtFriendUser);
        ImageView imgFriend = rowView.findViewById(R.id.imgFotoAmigo);
        ConstraintLayout friend = rowView.findViewById(R.id.clFriend);

        txtFriendName.setText(this.values.get(position).getNombre());
        txtFriendUser.setText(this.values.get(position).getUser());
        imgFriend.setImageResource(R.drawable.ic_profilepic);

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Abriendo Chat!", Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }
}

