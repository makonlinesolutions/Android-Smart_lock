package com.smartlock.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUitl {
    public static String getTime(Date date, String formate) {
        SimpleDateFormat format = new SimpleDateFormat(formate);
        return format.format(date);
    }

    public static String getTime(long time) {
        return getTime(time, "yy:MM:dd hh:mm");
    }

    public static String getTime(long time, String formate) {
        Date date = new Date(time);
        return getTime(date, formate);
    }
}
