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
        long result = 0;
        if ( date!=null ) {
            result = date.getTime();
        }
        return result;
    }

    @TypeConverter
    public String date2String( Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd HH:mm:ss"
        return dateFormat.format(date);
    }

    public String date2StringWithFormat( Date date,  String dateFormatPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( dateFormatPattern ); //""
        return dateFormat.format(date);
    }

    public String date2StringWithMontName( Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("Mmmm yyyy"); //""
        return dateFormat.format(date);
    }

    public Date string2Date( String dateInString) throws ParseException {
        String datePattern = "yyyy-MM-dd";
        return stringWithFormat2Date(dateInString,datePattern);
    }

    public Date stringWithFormat2Date( String dateInString, String dateFormatPattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat( dateFormatPattern );
        return dateFormat.parse( dateInString );
    }

}