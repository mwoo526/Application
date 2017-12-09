package org.androidtown.application.helper;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by MinWoo on 2017-12-09.
 */

public class DateTimeHelper {

    private static DateTimeHelper current = null;

    public static DateTimeHelper getInstance(){
        if(current ==null){
            current = new DateTimeHelper();
        }
        return current;
    }

    public static void freeInstance(){
        current=null;
    }
    private DateTimeHelper(){
        super();
    }

    public int[] getDate(){
        Calendar calendar=java.util.Calendar.getInstance(Locale.KOREA);
        int yy = calendar.get(java.util.Calendar.YEAR);
        int mm = calendar.get(java.util.Calendar.MONTH);
        int dd = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        int[] result = {yy,mm,dd};
        return result;
    }
    public int[] getTime(){
        Calendar calendar=Calendar.getInstance();
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);

        int[] result = {hh,mi,ss};
        return result;
    }
}
