package com.example.bogo.Utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bogo.R;

public class Utils
{

    public static final String PATH_VISITADOS = "visitados/";
    public static final String PATH_LUGARES = "lugares/";
    public static final String PATH_USUARIOS = "usuarios/";
    public static final String PATH_CHATS = "chats/";
    public static final String PATH_MENSAJES = "mensajes/" ;
    public static final String PATH_OBSERVADORES = "observadores/";
    public static final String PATH_RESENIAS = "resenias/";
    public static final int RADIUS_OF_EARTH_KM = 6371;

    public static boolean validateEmail(String email)
    {
        if(!email.isEmpty())
        {
            if(!email.matches("[\\w.-]+@[\\w]+(\\.[\\w]{2,})+"))
                return false;
            return true;
        }
        else return false;
    }

    public static boolean validatePassword(String password)
    {
        if(!password.isEmpty())
        {
            if(password.length() < 6)
                return false;
            return true;
        }
        else return false;
    }

    public static double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result*1000.0)/1000.0;
    }

    public static LinearLayout getStarRate(double rating, Context context)
    {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        int whole = (int) rating;
        double resto = rating - whole;
        for(int i = 0; i < whole; i++)
        {
            ImageView img = new ImageView(context);
            img.setImageResource(R.drawable.star1_svg);
            img.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            ll.addView(img);
        }
        int restoAdd = 0;
        if( resto >= 0.5)
        {
            ImageView img = new ImageView(context);
            img.setImageResource(R.drawable.halfstar_svg);
            img.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            ll.addView(img);
            restoAdd = 1;
        }
        for(int i = 0; i < 5 - whole - restoAdd; i++)
        {
            ImageView img = new ImageView(context);
            img.setImageResource(R.drawable.star0_svg);
            img.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            ll.addView(img);
        }

        return ll;
    }
}
