import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentPage {
    public StudentPage() {
        JFrame frame = new JFrame("Student - View Jobs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> jobListModel = new DefaultListModel<>();
        JList<String> jobList = new JList<>(jobListModel);
        frame.add(new JScrollPane(jobList), BorderLayout.CENTER);

        loadJobsFromFile(jobListModel);

        frame.setVisible(true);
    }

    private void loadJobsFromFile(DefaultListModel<String> jobListModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("jobs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jobListModel.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
