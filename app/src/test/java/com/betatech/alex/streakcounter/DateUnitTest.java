package com.betatech.alex.streakcounter;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DateUnitTest {
    public static final String TAG = DateUnitTest.class.getSimpleName();

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getCurrentDateInstance() throws ParseException {
        String df= DateFormat.getDateInstance().format(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("MMM d, yyyy"); //MMM can cause problem, if df is in other format
        Date d = format.parse(df);
    }

    @Test
    public void getYesterdayDateInstance() throws ParseException {
        final long oneDay = 1000*60*60*24; // Number of milli seconds in 1 day
        String df= DateFormat.getDateInstance().format(System.currentTimeMillis() - oneDay );
        DateFormat format = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        Date d = format.parse(df);
    }
}