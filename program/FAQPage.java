import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FAQPage {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FAQFrame().setVisible(true));
    }
}

class FAQFrame extends JFrame {
    public FAQFrame() {
        setTitle("FAQ Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addFAQ(panel, "What is Java?", "Java is a programming language and computing platform.");
        addFAQ(panel, "What is Swing?", "Swing is a Java toolkit for building GUI applications.");
        addFAQ(panel, "What is AWT?", "AWT (Abstract Window Toolkit) is Java's original platform-independent GUI toolkit.");

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addFAQ(JPanel panel, String question, String answer) {
        JButton button = new JButton(question);
        JLabel label = new JLabel(" ");
        label.setVisible(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(label.isVisible() ? " " : answer);
                label.setVisible(!label.isVisible());
                panel.revalidate();
                panel.repaint();
            }
        });

        panel.add(button);
        panel.add(label);
    }
}
