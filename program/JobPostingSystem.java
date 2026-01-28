import javax.swing.*;

public class JobPostingSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EmployerPage();
            new StudentPage();
        });
    }
}
