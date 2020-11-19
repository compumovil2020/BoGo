package com.example.bogo.Utils;

public class Utils
{

    public static final String PATH_VISITADOS = "visitados/";
    public static final String PATH_LUGARES = "lugares/";
    public static final String PATH_USUARIOS = "usuarios/";
    public static final String PATH_CHATS = "chats/";
    public static final String PATH_MENSAJES = "mensajes/" ;
    public static final String PATH_OBSERVADORES = "observadores/";

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
}
