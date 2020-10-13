package com.example.bogo.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager
{
    public static final int READ_CONTACTS_PERMISSION = 1;
    public static final int CAMERA_PERMISSION = 2;
    public static final int READ_STORAGE_PERMISSION = 3;
    public static final int LOCATION_PERMISSION = 11;
    public static final int REQUEST_CHECK_SETTINGS = 22;
    public static final int IMAGE_PICKER_REQUEST = 5;
    public static final int REQUEST_IMAGE_CAPTURE = 4;

    public static void requestPermission(Activity context, String permiso, String justificacion, int idCode)
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if( ActivityCompat.shouldShowRequestPermissionRationale(context, permiso) )
            {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            // request the permission.
            ActivityCompat.requestPermissions(context, new String[]{permiso}, idCode);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS es una
            // constante definida en la aplicación, se debe usar
            // en el callback para identificar el permiso
        }
    }
}