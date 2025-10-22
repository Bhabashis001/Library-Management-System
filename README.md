# Library-Management-System
Developed a Library Management System using Java and MySQL, managing 100+ books and 50+ members with efficient CRUD operations. Built modules to add,search,update, and delete books with borrower tracking and issue/return functionality. Integrated JDBC for reliable database connectivity, achieving a 100% success rate in execution during testing.

**Developed in November 2024**  
**Technology Stack:** Java (Core), MySQL / MariaDB, JDBC, Apache Server  

---
## **Project Overview**

This Library Management System is a Java-based desktop application that interacts with a MySQL/MariaDB database to manage books and members.  
It supports **full CRUD operations** for books, members, and issued books, along with **issue/return functionality**.

**Features:**
- Add, search, update, and delete books
- Add, view, update, and delete members
- Track issued books with borrower details
- Safe deletion using **ON DELETE CASCADE** for relational integrity
- JDBC integration for reliable database transactions
- 150+ Indian books and 50+ member entries preloaded for testing

---

## **Setup Instructions**

### **1. Database Setup**
1. Install **MySQL** or **MariaDB**.  
2. Open a terminal and login:
mysql -u root -p

Execute the database script:
SOURCE path/to/library_management_system.sql;

This creates:
books table with 150 books
members table with 50 members

issued_books table with 60 issued records

2. Java Setup
Install Java JDK 25 or later.

Install VS Code with Java Extension Pack.

Add MySQL Connector/J .jar to your project classpath.

Update database credentials in App.java:

String url = "jdbc:mysql://localhost:3306/library_management_system";
String user = "root";
String password = "your_password";

Compile and run:
javac App.java
java App

3. Usage
Choose menu options to add, view, update, delete books or members.

Issue or return books for registered members.

Deleting a book or member automatically removes dependent issued records.

