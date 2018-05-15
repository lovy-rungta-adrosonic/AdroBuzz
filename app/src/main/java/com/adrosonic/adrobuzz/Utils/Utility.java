package com.adrosonic.adrobuzz.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.util.Date;

/**
 * Created by Lovy on 20-04-2018.
 */

public class Utility {

    public static int compareDate(Date date1, Date date2) {
        if (date1.getYear() == date2.getYear() &&
                date1.getMonth() == date2.getMonth() &&
                date1.getDate() == date2.getDate()) {
            return 0 ;
        }
        else
            return 1;
    }

    public static  String  filePath(){
        File r = android.os.Environment.getExternalStorageDirectory();
        File file = new File(r.getAbsolutePath() + "/Adro/Text");
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = r.getAbsolutePath() + "/Adro/Text/" +
                "AdroBuzz" + ".txt";
        return filePath;
    }

    public static boolean checkIfInternetConnected(Context context){
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo=connMgr.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
        {
            return true;
        }
        else{
            return false;
        }
    }

    public static void noInternetConnection(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        builder.setMessage("No internet connection found. Make sure Wi-Fi or cellular data is turned on for application to work properly.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setCancelable(false);
        builder.create().show();
    }
}
