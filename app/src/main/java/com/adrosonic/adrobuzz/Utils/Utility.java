package com.adrosonic.adrobuzz.Utils;

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

}
