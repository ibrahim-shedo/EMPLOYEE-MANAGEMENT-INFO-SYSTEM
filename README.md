# Employee Management System

## Overview
The Employee Management System is a Java Swing-based desktop application designed to manage employee information. It provides functionalities to add employees, view employee details, and manage promotions and salaries. The application uses MySQL as the backend database to store and retrieve employee data.

## Features

### Add Employee:
- Add new employee details, including name, age, position, salary, promotion due date, and a notice board message.
- Validates inputs for age, salary, and date formats.

### View Employees:
- Display a list of all employees stored in the database.
- Shows employee ID, name, age, position, salary, promotion due date, and notice board message.

### Manage Promotions & Salaries:
- Update existing employee details such as position, salary, and promotion due date.

## Dependencies
- Java Development Kit (JDK) 8 or higher
- MySQL database
- MySQL JDBC Driver (Connector/J)

## Database Setup

1. Create a database named `ems` in your MySQL server.
2. Create a table named `employees` with the following structure:

```sql
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    position VARCHAR(100),
    salary DOUBLE,
    promotion_due_date DATE,
    notice_board TEXT
);
How to Run
Clone the repository or download the code.
Compile and run the Java code using your preferred IDE or command line:
sh
Copy code
javac EmployeeManagementSystem.java
java EmployeeManagementSystem
Ensure that the MySQL server is running and accessible.
Configuration
The database connection details are hardcoded in the connectDatabase method. Modify the following variables if needed:

java
Copy code
String url = "jdbc:mysql://localhost:3306/ems";
String user = "root";
String password = ""; // default is an empty password
Usage
Add Employee: Navigate to the "Add Employee" tab, fill in the employee details, and click "Add Employee."
View Employees: Navigate to the "View Employees" tab and click "View Employees" to display the list of employees.
Manage Promotions & Salaries: Navigate to the "Manage Promotions & Salaries" tab, enter the employee ID and new details, then click "Update."
Error Handling
Displays error messages for invalid inputs, such as incorrect number formats or invalid date formats.
Alerts user if there are issues connecting to the database or executing SQL queries.
Future Improvements
Implement search functionality to find employees by various criteria.
Add authentication and authorization for different user roles (e.g., admin, manager).
Improve the user interface and user experience.
License
This project is licensed under the MIT License. See the LICENSE file for details.

Author
Ibrahim Mohamed Shedoh

Contact
For questions or feedback, please contact [ibrezma.shado@gmail.com].
