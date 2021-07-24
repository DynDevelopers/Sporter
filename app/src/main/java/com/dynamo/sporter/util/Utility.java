package com.dynamo.sporter.util;

import android.annotation.SuppressLint;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Utility {

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static String getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf1.format(timestamp);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTime(int hour, int minute) {
        Time time = new Time(hour, minute,0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("hh:mm a");
        return formatter.format(time);
    }

    public static String getFullName(String firstname, String lastname) {
        return (firstname.charAt(0)+"").toUpperCase() + firstname.substring(1) + " " + (lastname.charAt(0)+"").toUpperCase() + lastname.substring(1);
    }
}
