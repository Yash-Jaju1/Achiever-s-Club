# Achiever’s Club Java Application

## Overview

This is a Java Swing desktop application for managing student profiles, fees, attendance, and related features for an Achiever’s Club, backed by a MySQL database.

The application includes:

- Adding and removing students with full detail management
- Viewing and editing student profiles
- Managing fee records: payments, dues, and fee structure
- Marking and checking attendance records
- Text-to-Speech announcements for confirmations (using FreeTTS)

## Project Structure

    AchieversClub/

        ├── src/ # Java source files (.java)

        ├── lib/ # External JAR libraries (MySQL Connector, FreeTTS, etc.)

        ├── README.md # This file

## Setup Instructions

### 1. Database Setup

- Install MySQL Server if not already installed.
- Create the database and required tables by running the following SQL commands in your MySQL console or workbench:

CREATE DATABASE project;
USE project;

CREATE TABLE STUDENT_PROFILE (
reg_no BIGINT NOT NULL AUTO_INCREMENT,
stu_name VARCHAR(100) NOT NULL,
stu_class BIGINT NOT NULL,
stu_stream VARCHAR(15),
stu_father VARCHAR(100) NOT NULL,
stu_mother VARCHAR(100) NOT NULL,
d_o_b DATE NOT NULL,
d_o_j DATE NOT NULL,
phn_no BIGINT NOT NULL,
email VARCHAR(100) NOT NULL,
PRIMARY KEY (reg_no)
);

CREATE TABLE FEE_DETAILS(
reg_no BIGINT NOT NULL PRIMARY KEY,
total_fee BIGINT,
fee_due BIGINT,
fee_paid BIGINT,
FOREIGN KEY (reg_no) REFERENCES STUDENT_PROFILE(reg_no) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ATTENDANCE(
attendance_id INT AUTO_INCREMENT PRIMARY KEY,
reg_no BIGINT NOT NULL,
attendance_date DATE NOT NULL,
status VARCHAR(10) NOT NULL,
UNIQUE KEY (reg_no, attendance_date),
FOREIGN KEY (reg_no) REFERENCES STUDENT_PROFILE(reg_no) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE FEE_STRUCTURE(
id INT PRIMARY KEY,
class_name VARCHAR(20),
stream VARCHAR(20),
fee BIGINT
);

-- Insert fee structure data (run only once):
INSERT INTO FEE_STRUCTURE VALUES
(1, '6', 'N/A', 10000), (2, '7', 'N/A', 15000), (3, '8', 'N/A', 20000),
(4, '9', 'N/A', 30000), (5, '10', 'N/A', 35000), (6, '11', 'Science', 45000),
(7, '11', 'Commerce', 40000), (8, '11', 'Humanities', 40000), (9, '12', 'Science', 50000),
(10, '12', 'Commerce', 45000), (11, '12', 'Humanities', 40000);

### 2. Project Dependencies

Download and place these library JAR files in the `lib/` folder:

- `mysql-connector-j-9.4.0.jar` (MySQL Java connector)
- FreeTTS JAR files:
    - `freetts.jar`
    - `cmulex.jar`
    - `cmudict04.jar`
    - `cmutimelex.jar`
    - `cmunistress.jar`
    - `en_us.jar`
    - `cmu_us_kal.jar`

### 3. Compilation

From your project root directory, compile your Java source files using:

- **Windows:**

javac -cp "lib/mysql-connector-j-9.4.0.jar;lib/freetts.jar;lib/cmulex.jar;lib/cmudict04.jar;lib/cmutimelex.jar;lib/cmunistress.jar;lib/en_us.jar;lib/cmu_us_kal.jar" src/*.java

- **macOS/Linux:**

javac -cp "lib/mysql-connector-j-9.4.0.jar:lib/freetts.jar:lib/cmulex.jar:lib/cmudict04.jar:lib/cmutimelex.jar:lib/cmunistress.jar:lib/en_us.jar:lib/cmu_us_kal.jar" src/*.java


### 4. Running the Application

To run your application, execute:

- **Windows:**

java -cp "src;lib/mysql-connector-j-9.4.0.jar;lib/freetts.jar;lib/cmulex.jar;lib/cmudict04.jar;lib/cmutimelex.jar;lib/cmunistress.jar;lib/en_us.jar;lib/cmu_us_kal.jar" AchieverClubApp


- **macOS/Linux:**

java -cp "src:lib/mysql-connector-j-9.4.0.jar:lib/freetts.jar:lib/cmulex.jar:lib/cmudict04.jar:lib/cmutimelex.jar:lib/cmunistress.jar:lib/en_us.jar:lib/cmu_us_kal.jar" AchieverClubApp


*(Replace `AchieverClubApp` with your actual main class if different.)*

## Features Included

- Add, remove, check, and edit student profiles
- Pay fees and manage fee structures
- Mark and check attendance records
- Text-to-Speech announcements using FreeTTS

## Notes

- Ensure your MySQL username and password in `DatabaseHandler.java` match your local setup.
- Keep all necessary FreeTTS JARs included in your classpath for proper speech functionality.
- The app assumes your MySQL server is running locally on port 3306.

## Troubleshooting

- If you encounter `ClassNotFoundException` or missing resource errors, verify that all required JAR files are in the `lib` folder and properly added to the classpath.
- For database connection errors, verify your MySQL server is running and credentials are set correctly.
- If TTS is not working, confirm all FreeTTS voice JARs including `cmu_us_kal.jar` are included.

---

**Happy coding! Thank you for using the Achiever’s Club Java Application. 🚀**