package com.dev4lazy.pricecollector.model.utils;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateConverterTest2 {

    @Test
    public void date2StringWithFormat() {
        DateConverter dateConverter = new DateConverter();
        String dateString = dateConverter.date2StringWithFormat( new Date(), "Mmmm yyyy");
        assertTrue( dateString.equals("Październik 2021") );
    }
}