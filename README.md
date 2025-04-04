Exam Management System
Overview
A comprehensive Java Swing desktop application for conducting online exams with robust proctoring features. This system supports multiple user roles (students and teachers) and provides tools for creating, taking, and evaluating exams in a secure environment.

Features
User Management
Dual-role Support: Separate interfaces for students and teachers
Secure Authentication: MD5 password encryption with masked password entry
User Registration: Self-registration for students, code-protected registration for teachers (Teacher code: TEACHER123)
Teacher Features
Exam Creation: Create multi-choice question exams with customizable settings
Question Management: Add, edit, and organize questions with multiple-choice options
Time Control: Set custom duration for exams (default: 30 minutes)
Results Analysis: View and analyze student performance
Student Features
Exam Dashboard: Browse available exams with descriptions
Timed Sessions: Auto-submission after time expiration
Progress Tracking: View past exam results and performance statistics
User-friendly Interface: Intuitive exam navigation and submission
Security Features
Proctoring System: Prevents application switching during exams
Webcam Monitoring: Optional image capture during exams
Activity Logging: Keyboard and mouse activity monitoring
Security Violation Tracking: Auto-detection and logging of suspicious behavior
Technical Highlights
MVC Architecture: Clean separation of UI, models, and database access
MySQL Integration: Persistent data storage with robust schema design
Cross-platform Compatibility: Works on Windows, macOS, and Linux
No External Dependencies: Standalone application with minimal requirements
Installation
System Requirements
Java 11 or higher
MySQL 8.0 or higher
Minimum 4GB RAM, 100MB disk space
Quick Install
Download the installer from the Releases section
Run the installer and follow the setup wizard
Configure database connection when prompted (or use embedded database)
Launch the application from desktop shortcut or start menu
Manual Installation
Ensure Java is installed on your system
Create a MySQL database using the provided database.sql script
Download the JAR file from Releases
Run with: java -jar exam-application.jar
Database Setup
The application supports three database configurations:

Local Database (Default)
Host: localhost
Port: 3306
Database: exam_app
Username: root
Password: (your MySQL root password)
Cloud Database
The application supports connection to a cloud-hosted MySQL instance for multi-location access.

Embedded Database
For testing or standalone use, the application can use an embedded database with no external dependencies.

Default Accounts
Teacher Account
Username: admin
Password: admin123
Student Account
Username: student
Password: student123
Usage Guide
For Teachers
Login with teacher credentials
Navigate to "Create Exam" to create a new exam
Add questions with multiple-choice options
Set the correct answer for each question
Publish the exam to make it available to students
View results in the "Exam Results" section
For Students
Login with student credentials
Browse available exams in the dashboard
Select an exam to start
Answer questions within the time limit
Submit answers or wait for auto-submission when time expires
View results immediately after submission
Security Recommendations
Change default passwords immediately after installation
Regularly update the application to the latest version
Use the proctoring features during important examinations
Keep the database credentials secure
License
This project is licensed under the MIT License - see the LICENSE file for details.

Contact & Support
For issues, feature requests, or contributions, please:

Open an Issue on GitHub
Email: amanoct2022cse@gmail.com
© 2025 Exam Management System. All rights reserved.
