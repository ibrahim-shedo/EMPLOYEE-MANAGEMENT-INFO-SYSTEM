import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeManagementSystem extends JFrame {
    private Connection connection;

    public EmployeeManagementSystem() {
        // Establish the database connection
        connectDatabase();

        // Set up the frame
        setTitle("Employee Management Information System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setIcon(new ImageIcon("icons/exit.png")); // Add an icon
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Set up the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Employee", createAddEmployeePanel());
        tabbedPane.addTab("View Employees", createViewEmployeesPanel());

        // Add tabbed pane to the frame
        add(tabbedPane);

        // Add action listener for exit menu item
        exitMenuItem.addActionListener(e -> System.exit(0));
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

    private JPanel createAddEmployeePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add New Employee"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField(20);
        JLabel positionLabel = new JLabel("Position:");
        JTextField positionField = new JTextField(20);
        JLabel salaryLabel = new JLabel("Salary:");
        JTextField salaryField = new JTextField(20);
        JLabel promotionDueDateLabel = new JLabel("Promotion Due Date:");
        JTextField promotionDueDateField = new JTextField(20);
        JLabel noticeBoardLabel = new JLabel("Notice Board:");
        JTextArea noticeBoardArea = new JTextArea(5, 20);
        JScrollPane noticeBoardScrollPane = new JScrollPane(noticeBoardArea);

        JButton addButton = new JButton("Add Employee");
        addButton.setIcon(new ImageIcon("icons/add.png")); // Add an icon

        // Position components using GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(ageLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(ageField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(positionLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(positionField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(salaryLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(salaryField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(promotionDueDateLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(promotionDueDateField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(noticeBoardLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(noticeBoardScrollPane, gbc);
        gbc.gridx = 1; gbc.gridy = 6; panel.add(addButton, gbc);

        // Add action listener
        addButton.addActionListener(e -> {
            try {
                int age = Integer.parseInt(ageField.getText());
                double salary = Double.parseDouble(salaryField.getText());
                java.sql.Date promotionDueDate = java.sql.Date.valueOf(promotionDueDateField.getText());
                addEmployee(nameField.getText(), age, positionField.getText(), salary, promotionDueDate, noticeBoardArea.getText());
            } catch (NumberFormatException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error adding employee: " + ex.getMessage());
            }
        });

        return panel;
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

    private JPanel createViewEmployeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Employee List"));

        // Create table model and table
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Position", "Salary", "Promotion Due Date", "Notice Board"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton viewButton = new JButton("View Employees");
        viewButton.setIcon(new ImageIcon("icons/view.png")); // Add an icon
        viewButton.setToolTipText("Click to view all employees");
        panel.add(viewButton, BorderLayout.SOUTH);

        viewButton.addActionListener(e -> {
            try {
                String query = "SELECT * FROM employees";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                model.setRowCount(0); // Clear the table model
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int age = resultSet.getInt("age");
                    String position = resultSet.getString("position");
                    double salary = resultSet.getDouble("salary");
                    Date promotionDueDate = resultSet.getDate("promotion_due_date");
                    String noticeBoard = resultSet.getString("notice_board");

                    model.addRow(new Object[]{id, name, age, position, salary, promotionDueDate, noticeBoard});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error retrieving employees: " + ex.getMessage());
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagementSystem().setVisible(true));
    }
}
