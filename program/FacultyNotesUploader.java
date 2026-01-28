import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FacultyNotesUploader {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadFrame().setVisible(true));
    }
}

class UploadFrame extends JFrame {
    public UploadFrame() {
        setTitle("Faculty Notes Upload");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel instructionLabel = new JLabel("Upload Notes:");
        JButton uploadButton = new JButton("Upload File");
        JLabel statusLabel = new JLabel("No file selected");

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    statusLabel.setText("Selected File: " + selectedFile.getName());
                }
            }
        });

        panel.add(instructionLabel);
        panel.add(uploadButton);
        panel.add(statusLabel);

        add(panel, BorderLayout.CENTER);
    }
}
