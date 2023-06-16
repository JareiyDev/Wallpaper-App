package com.phbet.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static Integer checkNetWork(Activity activity){
        //0 : Network not available
        //1 : Network available
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            System.out.println("Thắng Network available");
            return 1;
        } else {
            System.out.println("Thắng Network not available");
            return 0;
        }
    }

    public static Integer checkIPAdress(Activity activity){
        //0 : Unknown/Other
        //1 : Philipine
        try{
            TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = telephonyManager.getNetworkCountryIso();
            System.out.println("Thắng " + countryCode);
            if (countryCode != null) {
                if (countryCode.equalsIgnoreCase("ph")){
                    return 1;
                }
                else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
        catch (Exception e){
            return 0;
        }
    }
    public static void getRealeaseKey(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    "com.phbet.wallpaper", // Thay thế bằng tên gói ứng dụng của bạn
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
