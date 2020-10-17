package com.example.bogo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bogo.Activities.AddFriendActivity;
import com.example.bogo.Activities.MyFriendsActivity;
import com.example.bogo.R;
import java.util.ArrayList;

public class AddFriendAdapter extends ArrayAdapter<AddFriendActivity.NewFriend> {
    private final Context context;
    private final ArrayList<AddFriendActivity.NewFriend> values;

    public AddFriendAdapter(Context context, ArrayList<AddFriendActivity.NewFriend> values) {
        super(context, R.layout.adapter_add_friend,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_add_friend, parent, false);
        TextView txtFriendName = rowView.findViewById(R.id.txtFriendName);
        TextView txtFriendUser =  rowView.findViewById(R.id.txtFriendUser);
        ImageView imgFriend = rowView.findViewById(R.id.imgFotoAmigo);
        ImageButton imgAdd = rowView.findViewById(R.id.imgAdd);

        txtFriendName.setText(this.values.get(position).getNombre());
        txtFriendUser.setText(this.values.get(position).getUser());
        imgFriend.setImageResource(R.drawable.ic_profilepic);
        imgAdd.setImageResource(R.drawable.ic_plus);

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Amigo agregado", Toast.LENGTH_LONG).show();

            }
        });
        return rowView;

    }



}
