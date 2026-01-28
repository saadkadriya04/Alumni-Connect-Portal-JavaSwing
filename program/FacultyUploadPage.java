import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class FacultyUploadPage extends JFrame {
    private JComboBox<String> yearComboBox;
    private JTextField filePathField;
    private File selectedFile;

    public FacultyUploadPage() {
        setTitle("Upload Study Material");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel yearLabel = new JLabel("Select Year:");
        yearLabel.setBounds(30, 30, 100, 25);
        add(yearLabel);

        String[] years = {"1st", "2nd", "3rd", "4th"};
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setBounds(150, 30, 150, 25);
        add(yearComboBox);

        JLabel fileLabel = new JLabel("Selected File:");
        fileLabel.setBounds(30, 80, 100, 25);
        add(fileLabel);

        filePathField = new JTextField();
        filePathField.setBounds(150, 80, 200, 25);
        filePathField.setEditable(false);
        add(filePathField);

        JButton browseButton = new JButton("Browse");
        browseButton.setBounds(360, 80, 90, 25);
        browseButton.addActionListener(e -> chooseFile());
        add(browseButton);

        JButton uploadButton = new JButton("Upload");
        uploadButton.setBounds(200, 150, 100, 30);
        uploadButton.addActionListener(e -> uploadFileToDatabase());
        add(uploadButton);

        setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void uploadFileToDatabase() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file to upload.");
            return;
        }

        String year = yearComboBox.getSelectedItem().toString();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_db", "root", "saad@786");
             FileInputStream fis = new FileInputStream(selectedFile)) {

            String query = "INSERT INTO resources (year, file_name, file_data) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, year);
            pstmt.setString(2, selectedFile.getName());
            pstmt.setBinaryStream(3, fis, (int) selectedFile.length());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "File uploaded successfully!");
                filePathField.setText("");
                selectedFile = null;
            } else {
                JOptionPane.showMessageDialog(this, "Upload failed. Try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
