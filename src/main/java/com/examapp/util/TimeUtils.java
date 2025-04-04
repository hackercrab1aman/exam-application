package com.examapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for time-related operations
 * Provides methods for formatting time displays
 */
public class TimeUtils {
    
    /**
     * Format seconds into a readable time string (MM:SS)
     * @param seconds the number of seconds
     * @return formatted time string
     */
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    
    /**
     * Format seconds into a readable time string with hours (HH:MM:SS)
     * @param seconds the number of seconds
     * @return formatted time string
     */
    public static String formatTimeWithHours(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int remainingSeconds = seconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
    }
    
    /**
     * Format a Date object into a readable date string
     * @param date the Date object to format
     * @return formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    /**
     * Format a Date object into a short date string (yyyy-MM-dd)
     * @param date the Date object to format
     * @return formatted short date string
     */
    public static String formatShortDate(Date date) {
        if (date == null) {
            return "";
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
    /**
     * Calculate the difference between two dates in minutes
     * @param startDate the start date
     * @param endDate the end date
     * @return the difference in minutes
     */
    public static long getMinutesBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return diffInMillis / (60 * 1000);
    }
    
    /**
     * Get the current time in a formatted string
     * @return formatted current time string
     */
    public static String getCurrentTime() {
        return formatDate(new Date());
    }
    
    /**
     * Add minutes to a Date object
     * @param date the original date
     * @param minutes the number of minutes to add
     * @return the new date
     */
    public static Date addMinutes(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        
        return new Date(date.getTime() + (minutes * 60 * 1000));
    }
}
