Employee Management System
This Java application provides a simple graphical interface for managing employee records within a MySQL database. It allows users to add new employees and view existing ones through a tabbed interface.

Features
Add Employee: Users can enter details such as name, age, position, salary, promotion due date, and notices for a new employee. These details are then saved to the database.
View Employees: Displays a list of all employees stored in the database along with their details.
Requirements
Java Development Kit (JDK)
MySQL Database Server
MySQL JDBC Driver
Setup
Ensure Java JDK is installed on your machine.
Set up a MySQL database server and create a database named ems.
Create a table named employees with columns corresponding to the employee details (name, age, position, salary, promotion_due_date, notice_board).
Add the MySQL JDBC Driver to your project's build path.
Running the Application
Compile and run the application using your preferred IDE or via command line:

javac EmployeeManagementSystem.java
java EmployeeManagementSystem
This will launch the GUI where you can start managing employee records.

Note
The database connection parameters (URL, username, password) are hardcoded in the connectDatabase method. Ensure these match your MySQL server configuration.

License
MIT License
