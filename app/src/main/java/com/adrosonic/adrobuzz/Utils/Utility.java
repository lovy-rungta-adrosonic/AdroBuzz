package com.adrosonic.adrobuzz.Utils;

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
}
