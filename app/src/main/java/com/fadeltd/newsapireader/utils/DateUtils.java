package com.fadeltd.newsapireader.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    @SuppressLint("SimpleDateFormat")
    public static Date isoToDate(String isoDate){
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(isoDate);
        }catch (ParseException e){
            return new Date();
        }
    }

    public static String getRelativeTime(String isoDate){
        return android.text.format.DateUtils.getRelativeTimeSpanString(isoToDate(isoDate).getTime(), System.currentTimeMillis(), android.text.format.DateUtils.MINUTE_IN_MILLIS).toString();
    }
}
