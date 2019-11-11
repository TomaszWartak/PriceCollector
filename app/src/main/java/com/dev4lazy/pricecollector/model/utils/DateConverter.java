package com.dev4lazy.pricecollector.model.utils;

import android.icu.text.SimpleDateFormat;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.util.Date;

//import android.net.ParseException;

public class DateConverter {

    @TypeConverter
    public Date long2Date( Long timeStamp) {
        return new Date(timeStamp);
    }

    @TypeConverter
    public Long date2Long(Date date) {
        long result = -1;
        if ( date!=null) {
            result = date.getTime();
        }
        return result;
    }

    @TypeConverter
    public String date2String( Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd HH:mm:ss"
        return dateFormat.format(date);
    }

    public Date string2Date( String dateInString) throws ParseException {
        String datePattern = "yyyy-MM-dd";
        return string2DateWithFormat(dateInString,datePattern);
    }

    public Date string2DateWithFormat( String dateInString, String dateFPattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFPattern);
        return dateFormat.parse(dateInString);
    }

}