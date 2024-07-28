##Employee Management System
This Java application is an Employee Management System that uses a MySQL database to store and manage employee information. The system allows users to add new employees and view existing employees through a graphical user interface (GUI) built with Swing.

##Features
Add Employee: Add new employee records to the database.
View Employees: View all employee records stored in the database.
Exit: Close the application.
Prerequisites
Java Development Kit (JDK) 8 or higher
MySQL database server
MySQL JDBC Driver



##Installation
Clone the repository:

sh
Copy code
git clone https://github.com/your-username/employee-management-system.git
cd employee-management-system
Set up the MySQL database:

##Create a database named ems:

sql
Copy code
CREATE DATABASE ems;
Create the employees table:

sql
Copy code
USE ems;

CREATE TABLE employees (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  age INT NOT NULL,
  position VARCHAR(100) NOT NULL,
  salary DOUBLE NOT NULL,
  promotion_due_date DATE NOT NULL,
  notice_board TEXT
);
Configure the database connection:

##Update the connectDatabase method in EmployeeManagementSystem.java with your MySQL database credentials:

java
Copy code
private void connectDatabase() {
    try {
        String url = "jdbc:mysql://localhost:3306/ems";
        String user = "your-username";
        String password = "your-password"; // default is empty password
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Database connected successfully.");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
    }
}




##Usage
Compile and run the application:

sh
Copy code
javac EmployeeManagementSystem.java
java EmployeeManagementSystem
Add Employee:

Navigate to the "Add Employee" tab.
Fill in the employee details and click "Add Employee".
View Employees:

Navigate to the "View Employees" tab.
Click "View Employees" to display all employee records.
Exit:

Use the "File" menu and select "Exit" to close the application.
Code Overview
Main Class: EmployeeManagementSystem
Constructor:

Establishes database connection.
Sets up the main frame, menu bar, and tabbed pane.
connectDatabase():

Connects to the MySQL database using JDBC.
createAddEmployeePanel():

Creates the "Add Employee" panel with input fields and an "Add Employee" button.
Adds the employee details to the database when the button is clicked.
addEmployee():

Inserts a new employee record into the employees table.
createViewEmployeesPanel():

Creates the "View Employees" panel with a text area and a "View Employees" button.
Retrieves and displays all employee records when the button is clicked.
main():

Launches the application.
License
This project is licensed under the MIT License. See the LICENSE file for details.

Acknowledgments
This project uses the MySQL JDBC Driver to connect to the MySQL database.
Swing is used for the graphical user interface.
Contributing
Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make your changes.
Commit your changes (git commit -am 'Add new feature').
Push to the branch (git push origin feature-branch).
Open a pull request.
