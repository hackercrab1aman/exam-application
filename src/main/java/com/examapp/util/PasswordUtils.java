package com.examapp.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    
    // Hash password using MD5
    public static String hashPassword(String password) {
        try {
            System.out.println("Hashing password: " + password); // Debug print
            
            // Get MD5 MessageDigest instance
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            // Add password bytes to digest
            md.update(password.getBytes());
            
            // Get the hash's bytes
            byte[] bytes = md.digest();
            
            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            
            String hashedPassword = sb.toString();
            System.out.println("Password hash generated: " + hashedPassword); // Debug print
            
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage()); // Debug print
            e.printStackTrace();
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    // Verify password against stored hash
    public static boolean checkPassword(String password, String storedHash) {
        System.out.println("Checking password against stored hash: " + storedHash); // Debug print
        
        String hashedPassword = hashPassword(password);
        boolean matches = hashedPassword.equals(storedHash);
        
        System.out.println("Password check result: " + matches); // Debug print
        
        return matches;
    }
    
    // For SQL script hash generation
    public static void main(String[] args) {
        System.out.println("admin123 hash: " + hashPassword("admin123"));
        System.out.println("student123 hash: " + hashPassword("student123"));
    }
}