import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EmployerPage {
    public EmployerPage() {
        JFrame frame = new JFrame("Employer - Post Jobs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 1));
        inputPanel.add(new JLabel("Job Title:"));
        JTextField jobTitleField = new JTextField();
        inputPanel.add(jobTitleField);

        inputPanel.add(new JLabel("Job Description:"));
        JTextArea jobDescriptionArea = new JTextArea(3, 20);
        inputPanel.add(new JScrollPane(jobDescriptionArea));

        frame.add(inputPanel, BorderLayout.CENTER);

        JButton postJobButton = new JButton("Post Job");
        frame.add(postJobButton, BorderLayout.SOUTH);

        postJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String jobTitle = jobTitleField.getText().trim();
                String jobDescription = jobDescriptionArea.getText().trim();

                if (!jobTitle.isEmpty() && !jobDescription.isEmpty()) {
                    saveJobToFile(jobTitle, jobDescription);
                    jobTitleField.setText("");
                    jobDescriptionArea.setText("");
                    JOptionPane.showMessageDialog(frame, "Job Posted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Both fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private void saveJobToFile(String jobTitle, String jobDescription) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("jobs.txt", true))) {
            writer.write(jobTitle + " - " + jobDescription);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
