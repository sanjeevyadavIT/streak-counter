/*
 * <Count streak by daily working on a task>
 *  Copyright (C) <2019>  <Sanjeev Yadav>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.betatech.alex.streakcounter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Contains function to modify date
 */
public final class DateUtils {

    private DateUtils(){/*Utility class*/}

    /**
     * Returns current date with hour,minutes,seconds all set to zero
     */
    public static Date getCurrentDateWithoutTime(){
        String df= DateFormat.getDateInstance().format(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH); //MMM can cause problem, if df is in other format
        try {
            return format.parse(df);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(); //For now sending blank date
        }
    }

    /**
     * Returns yesterday date, with hour,minutes,seconds all set to zero
     */
    public static Date getYesterdayDateWithoutTime(){
        final long oneDay = 1000*60*60*24; // Number of milli seconds in 1 day
        String df= DateFormat.getDateInstance().format(System.currentTimeMillis() - oneDay );
        DateFormat format = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        try {
            return format.parse(df);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(); //For now sending blank date
        }
    }

    /**
     * Subtract two dates, and return difference in number of days
     *
     * @param date1 first date
     * @param date2 second date
     *
     * @return difference in number of days
     */
    public static long subtractDates(Date date1, Date date2) {
        if(date2 == null){
            //probably the first time the task has been created
            return 0L;
        }
        return (date1.getTime() - date2.getTime())/ (1000 * 60 * 60 * 24);
    }

    /**
     * return Date object when hour, minute and am/pm is specified
     *
     * @param hour hour of the day
     * @param minutes minutes
     * @param marker denoting am or pm
     *
     * @return a Date object by parsing above arguments
     */
    public static Date getFormattedTime(int hour,int minutes,int seconds, String marker){
        StringBuilder time = new StringBuilder();
        time.append(hour).append(":").append(minutes).append(":").append(seconds).append(" ").append(marker);
        DateFormat format = new SimpleDateFormat("h:mm:ss a");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return format.parse(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getFormattedTime(int hour,int minutes,int seconds){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minutes);
        calendar.set(Calendar.SECOND,seconds);
        return calendar.getTime();
    }

    /**
     * return string when Date object is specified,
     * string should be in format h:mm a
     *
     * @param time a date object containing the time only part
     *
     * @return a string if time isn't null else return nul
     */
    public static String getFormattedTime(Date time) {
        if(time == null) return "Time";
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "pm";
        } else if (hour == 12){
            timeSet = "PM";
        }else{
            timeSet = "am";
        }

        return new StringBuilder().append(hour).append(":")
                .append(minutes < 10? "0"+minutes : minutes).append(" ").append(timeSet).toString();
    }


    // FIXME: 10/14/2018 Testing function, removed it from production
    public static Date getPastDateWithoutTime(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        removeTimeAttributesFromDate(calendar);
        return calendar.getTime();
    }


    /**
     * Set HOUR = 0, MINUTE = 0, SECONDS = 0, MILLISECOND = 0 from calendar
     */
    private static void removeTimeAttributesFromDate(Calendar calendar) {
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
    }

    /**
     * Check if two dates are equal
     *
     * @return true if both dates are equal
     */
     public static boolean areSameDate(Date firstDate, Date secondDate){
         Calendar cal1 = Calendar.getInstance();
         Calendar cal2 = Calendar.getInstance();
         cal1.setTime(firstDate);
         cal2.setTime(secondDate);

         return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                 cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                 cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
     }

}
