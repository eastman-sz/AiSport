package com.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/20.
 */
public class PermissionHelpler {

    public static  void requestPermissions(Activity activity){
        int permisson_1 = ContextCompat.checkSelfPermission(activity , Manifest.permission.ACCESS_FINE_LOCATION);
        int permisson_2 = ContextCompat.checkSelfPermission(activity , Manifest.permission.CAMERA);
        int permisson_3 = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisson_4 = ContextCompat.checkSelfPermission(activity , Manifest.permission.WRITE_SETTINGS);
        int permisson_5 = ContextCompat.checkSelfPermission(activity , Manifest.permission.READ_PHONE_STATE);
        int permisson_6 = ContextCompat.checkSelfPermission(activity , Manifest.permission.RECEIVE_SMS);
        ArrayList<String> permission_list = new ArrayList<String>();

        if (permisson_1 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permisson_2 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.CAMERA);
        }
        if (permisson_3 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permisson_4 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.WRITE_SETTINGS);
        }
        if (permisson_5 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (permisson_6 != PackageManager.PERMISSION_GRANTED){
            permission_list.add(Manifest.permission.RECEIVE_SMS);
        }

        if (permission_list.isEmpty()){
            return;
        }

        int size = permission_list.size();
        String[] permissions = new String[size];
        for (int i = 0 ; i < size ; i ++){
            permissions[i] = permission_list.get(i);
        }

        ActivityCompat.requestPermissions(activity , permissions , 100);

//        ActivityCompat.requestPermissions(activity , new String[]{Manifest.permission.ACCESS_FINE_LOCATION ,
//        Manifest.permission.CAMERA ,
//        Manifest.permission.READ_EXTERNAL_STORAGE ,
//                Manifest.permission.WRITE_SETTINGS ,
//                Manifest.permission.READ_PHONE_STATE
//        } , 100);
    }

    public static boolean hasValiedLocationPermisson(Activity activity){
        int permisson = ContextCompat.checkSelfPermission(activity , Manifest.permission.ACCESS_FINE_LOCATION);
        return permisson == PackageManager.PERMISSION_GRANTED;
    }

    public static void requetLocationPermission(Activity activity , int requestCode){
        if (hasValiedLocationPermisson(activity)){
            return;
        }
        ActivityCompat.requestPermissions(activity , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , requestCode);
    }


}
