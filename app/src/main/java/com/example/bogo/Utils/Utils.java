package com.example.bogo.Utils;

public class Utils
{

    public static final String PATH_VISITADOS = "visitados/";
    public static final String PATH_LUGARES = "lugares/";
    public static final String PATH_USUARIOS = "usuarios/";
    public static final String PATH_CHATS = "chats/";
    public static final String PATH_MENSAJES = "mensajes/" ;
    public static final String PATH_OBSERVADORES = "observadores/";
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
}
