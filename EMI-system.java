import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EmployeeManagementSystem extends JFrame {
    private Connection connection;

    public EmployeeManagementSystem() {
        // Establish the database connection
        connectDatabase();

        // Set up the frame
        setTitle("Employee Management Information System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Set up the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Employee", createAddEmployeePanel());
        tabbedPane.addTab("View Employees", createViewEmployeesPanel());
        tabbedPane.addTab("Manage Promotions & Salaries", createManagePromotionsAndSalariesPanel());

        // Add the tabbed pane to the frame
        add(tabbedPane);
    }

    private JPanel createAddEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Add components to the panel
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(20);
        JLabel positionLabel = new JLabel("Position:");
        JTextField positionField = new JTextField(20);
        JLabel salaryLabel = new JLabel("Salary:");
        JTextField salaryField = new JTextField(20);
        JLabel promotionDueDateLabel = new JLabel("Promotion Due Date (YYYY-MM-DD):");
        JTextField promotionDueDateField = new JTextField(20);
        JLabel noticeBoardLabel = new JLabel("Notice Board:");
        JTextArea noticeBoardArea = new JTextArea(5, 20);
        JScrollPane noticeBoardScrollPane = new JScrollPane(noticeBoardArea);

        JButton addButton = new JButton("Add Employee");
        addButton.setToolTipText("Click to add employee to the database");

        // Set up layout
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(nameLabel)
                    .addComponent(ageLabel)
                    .addComponent(positionLabel)
                    .addComponent(salaryLabel)
                    .addComponent(promotionDueDateLabel)
                    .addComponent(noticeBoardLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameField)
                    .addComponent(ageField)
                    .addComponent(positionField)
                    .addComponent(salaryField)
                    .addComponent(promotionDueDateField)
                    .addComponent(noticeBoardScrollPane)
                    .addComponent(addButton))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(ageLabel)
                    .addComponent(ageField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(positionLabel)
                    .addComponent(positionField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(salaryLabel)
                    .addComponent(salaryField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(promotionDueDateLabel)
                    .addComponent(promotionDueDateField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(noticeBoardLabel)
                    .addComponent(noticeBoardScrollPane))
                .addComponent(addButton)
        );

        // Add action listener
        addButton.addActionListener(e -> {
            try {
                int age = Integer.parseInt(ageField.getText());
                double salary = Double.parseDouble(salaryField.getText());
                java.sql.Date promotionDueDate = parseDate(promotionDueDateField.getText());
                addEmployee(nameField.getText(), age, positionField.getText(), salary, promotionDueDate, noticeBoardArea.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid number format: " + ex.getMessage());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DD.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error adding employee: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createViewEmployeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton viewButton = new JButton("View Employees");
        viewButton.setToolTipText("Click to view all employees");
        panel.add(viewButton, BorderLayout.SOUTH);

        viewButton.addActionListener(e -> {
            try {
                String query = "SELECT * FROM employees";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                textArea.setText(""); // Clear the text area
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    String position = resultSet.getString("position");
                    double salary = resultSet.getDouble("salary");
                    Date promotionDueDate = resultSet.getDate("promotion_due_date");
                    String noticeBoard = resultSet.getString("notice_board");

                    textArea.append("ID: " + id + "\n");
                    textArea.append("Name: " + name + "\n");
                    textArea.append("Age: " + age + "\n");
                    textArea.append("Position: " + position + "\n");
                    textArea.append("Salary: " + salary + "\n");
                    textArea.append("Promotion Due Date: " + promotionDueDate + "\n");
                    textArea.append("Notice Board: " + noticeBoard + "\n");
                    textArea.append("\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error retrieving employees: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createManagePromotionsAndSalariesPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Add components for managing promotions and salaries
        JLabel idLabel = new JLabel("Employee ID:");
        JTextField idField = new JTextField(20);
        JLabel newPositionLabel = new JLabel("New Position:");
        JTextField newPositionField = new JTextField(20);
        JLabel newSalaryLabel = new JLabel("New Salary:");
        JTextField newSalaryField = new JTextField(20);
        JLabel newPromotionDateLabel = new JLabel("New Promotion Date (YYYY-MM-DD):");
        JTextField newPromotionDateField = new JTextField(20);
        JButton updateButton = new JButton("Update");

        // Set up layout
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(idLabel)
                    .addComponent(newPositionLabel)
                    .addComponent(newSalaryLabel)
                    .addComponent(newPromotionDateLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(idField)
                    .addComponent(newPositionField)
                    .addComponent(newSalaryField)
                    .addComponent(newPromotionDateField)
                    .addComponent(updateButton))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(newPositionLabel)
                    .addComponent(newPositionField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(newSalaryLabel)
                    .addComponent(newSalaryField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(newPromotionDateLabel)
                    .addComponent(newPromotionDateField))
                .addComponent(updateButton)
        );

        // Add action listener for update button
        updateButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String newPosition = newPositionField.getText();
                double newSalary = Double.parseDouble(newSalaryField.getText());
                java.sql.Date newPromotionDate = parseDate(newPromotionDateField.getText());
                updateEmployeeDetails(id, newPosition, newSalary, newPromotionDate);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid number format: " + ex.getMessage());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DD.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error updating employee: " + ex.getMessage());
            }
        });

        return panel;
    }

    private void connectDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/ems";
            String user = "root";
            String password = ""; // default is empty password
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to database: " + e.getMessage());
        }
    }

    private void addEmployee(String name, int age, String position, double salary, java.sql.Date promotionDueDate, String noticeBoard) {
        try {
            String query = "INSERT INTO employees (name, age, position, salary, promotion_due_date, notice_board) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, position);
            preparedStatement.setDouble(4, salary);
            preparedStatement.setDate(5, promotionDueDate);
            preparedStatement.setString(6, noticeBoard);
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage());
        }
    }

    private void updateEmployeeDetails(int id, String newPosition, double newSalary, java.sql.Date newPromotionDate) {
        try {
            String query = "UPDATE employees SET position = ?, salary = ?, promotion_due_date = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newPosition);
            preparedStatement.setDouble(2, newSalary);
            preparedStatement.setDate(3, newPromotionDate);
            preparedStatement.setInt(4, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage());
        }
    }

    private java.sql.Date parseDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = format.parse(date);
        return new java.sql.Date(parsed.getTime());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagementSystem().setVisible(true));
    }
}
