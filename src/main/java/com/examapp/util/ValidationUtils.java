package com.examapp.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 * Provides methods for validating various types of user input
 */
public class ValidationUtils {
    
    // Regular expression for validating email addresses
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    // Regular expression for validating usernames (alphanumeric, 3-20 characters)
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[A-Za-z0-9_]{3,20}$");
    
    /**
     * Check if a string is a valid email address
     * @param email the email address to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Check if a string is a valid username
     * @param username the username to validate
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Check if a string is not empty or null
     * @param text the string to check
     * @return true if the string is not empty, false otherwise
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
    
    /**
     * Check if a string is a valid integer
     * @param text the string to check
     * @return true if the string is a valid integer, false otherwise
     */
    public static boolean isInteger(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Check if a string is a valid positive integer
     * @param text the string to check
     * @return true if the string is a valid positive integer, false otherwise
     */
    public static boolean isPositiveInteger(String text) {
        if (!isInteger(text)) {
            return false;
        }
        
        return Integer.parseInt(text) > 0;
    }
    
    /**
     * Sanitize a string to prevent SQL injection
     * @param input the input string to sanitize
     * @return the sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        
        // Replace potentially dangerous characters
        return input.replaceAll("['\"\\\\;]", "");
    }
    
    /**
     * Truncate a string to a maximum length
     * @param text the string to truncate
     * @param maxLength the maximum length
     * @return the truncated string
     */
    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        
        return text.substring(0, maxLength);
    }
    
    /**
     * Check if a string contains HTML tags
     * @param text the string to check
     * @return true if the string contains HTML tags, false otherwise
     */
    public static boolean containsHtml(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        return text.matches(".*<[^>]+>.*");
    }
}
