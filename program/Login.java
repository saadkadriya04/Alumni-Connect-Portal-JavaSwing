import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    private JTextField tfusername;
    private JPasswordField tfpassword;
    private JComboBox<String> roleComboBox;
    private JButton login, cancel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/login_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "saad@786";

    public Login() {
        setTitle("Login Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("University Portal Login", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Role
        JLabel lblRole = new JLabel("Select Role:");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        mainPanel.add(lblRole, gbc);

        String[] roles = {"Student", "Faculty", "Alumni"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        mainPanel.add(roleComboBox, gbc);

        // Username
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(lblUsername, gbc);

        tfusername = new JTextField(20);
        tfusername.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        mainPanel.add(tfusername, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        mainPanel.add(lblPassword, gbc);

        tfpassword = new JPasswordField(20);
        tfpassword.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        mainPanel.add(tfpassword, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        login = new JButton("Login");
        login.setFont(new Font("Arial", Font.BOLD, 16));
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        mainPanel.add(login, gbc);

        gbc.gridx = 1;
        cancel = new JButton("Cancel");
        cancel.setFont(new Font("Arial", Font.BOLD, 16));
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        mainPanel.add(cancel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String username = tfusername.getText();
            String password = new String(tfpassword.getPassword());
            String role = roleComboBox.getSelectedItem().toString();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setString(3, role);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    JOptionPane.showMessageDialog(this, "Login Successful as " + role + "!");
                    dispose();

                    if (role.equals("Student")) {
                        new Studentproj(userId);
                    } else {
                        new Project(userId);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials or role mismatch!");
                }

                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            }
        } else if (ae.getSource() == cancel) {
            dispose();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}


