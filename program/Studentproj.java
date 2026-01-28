import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class Studentproj extends JFrame {
    private JLabel noticeLabel;
    private JList<String> jobList;
    private DefaultListModel<String> jobListModel;

    private final String[] notices = {
            "🚀 Welcome to the University Portal!",
            "📢 Admissions are now open for 2025.",
            "📅 Upcoming Faculty Meeting on March 25.",
            "🎉 Student Festival scheduled for April 5-7.",
            "💼 Check out the latest job postings!"
    };
    private int noticeIndex = 0;
    private int userId;

    public Studentproj(int userId) {
        this.userId = userId;

        setTitle("University Portal - Student Dashboard");
        setSize(1540, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1540, 850);
        add(layeredPane);

        // Notice Bar
        JPanel noticePanel = new JPanel();
        noticePanel.setBackground(Color.BLACK);
        noticePanel.setBounds(0, 0, 1540, 40);
        noticeLabel = new JLabel(notices[0]);
        noticeLabel.setForeground(Color.YELLOW);
        noticeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        noticePanel.add(noticeLabel);
        layeredPane.add(noticePanel, JLayeredPane.DEFAULT_LAYER);

        startNoticeTicker();

        // Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        if (i1.getImageLoadStatus() != MediaTracker.COMPLETE) {
            JLabel errorLabel = new JLabel("Background image not found!");
            errorLabel.setBounds(0, 40, 1540, 810);
            layeredPane.add(errorLabel, JLayeredPane.DEFAULT_LAYER);
        } else {
            Image i2 = i1.getImage().getScaledInstance(1540, 810, Image.SCALE_SMOOTH);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel backgroundImage = new JLabel(i3);
            backgroundImage.setBounds(0, 40, 1540, 810);
            layeredPane.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);
        }

        // Job Listings Panel
        JPanel jobPanel = new JPanel(new BorderLayout());
        jobPanel.setBounds(50, 100, 400, 600);
        jobPanel.setBackground(Color.WHITE);
        jobPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel jobTitle = new JLabel("📌 Available Jobs", SwingConstants.CENTER);
        jobTitle.setFont(new Font("Arial", Font.BOLD, 20));
        jobListModel = new DefaultListModel<>();
        jobList = new JList<>(jobListModel);
        jobList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(jobList);

        jobPanel.add(jobTitle, BorderLayout.NORTH);
        jobPanel.add(scrollPane, BorderLayout.CENTER);
        layeredPane.add(jobPanel, JLayeredPane.PALETTE_LAYER);

        loadJobsFromFile();

        // MENU BAR
        JMenuBar mb = new JMenuBar();

        // Profile Menu
        JMenu profileMenu = new JMenu("Profile");
        profileMenu.setForeground(Color.BLUE);
        JMenuItem viewProfile = new JMenuItem("View Profile");
        viewProfile.addActionListener(e -> new ProfilePage(userId));
        profileMenu.add(viewProfile);
        mb.add(profileMenu);

        // Resources Menu
        JMenu resources = new JMenu("Resources");
        resources.setForeground(Color.RED);
        JMenuItem FirstYear = new JMenuItem("1st Year");
        FirstYear.addActionListener(e -> new ResourceViewer("1st"));
        resources.add(FirstYear);
        JMenuItem SecondYear = new JMenuItem("2nd Year");
        SecondYear.addActionListener(e -> new ResourceViewer("2nd"));
        resources.add(SecondYear);
        JMenuItem ThirdYear = new JMenuItem("3rd Year");
        ThirdYear.addActionListener(e -> new ResourceViewer("3rd"));
        resources.add(ThirdYear);
        JMenuItem FourthYear = new JMenuItem("4th Year");
        FourthYear.addActionListener(e -> new ResourceViewer("4th"));
        resources.add(FourthYear);
        mb.add(resources);

        JMenu messagingMenu = new JMenu("Messaging");
        messagingMenu.setForeground(new Color(0, 128, 0));
        JMenuItem openMessaging = new JMenuItem("Open Chat");
        openMessaging.addActionListener(e -> new Messaging(userId));
        messagingMenu.add(openMessaging);
        mb.add(messagingMenu);



        // Add glue to shift the next menu to the right
        mb.add(Box.createHorizontalGlue());

        // Account Menu with Logout
        JMenu accountMenu = new JMenu("Account");
        accountMenu.setForeground(Color.DARK_GRAY);
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();         // Close the student dashboard
                new Login();       // Redirect to login screen
            }
        });
        accountMenu.add(logoutItem);
        mb.add(accountMenu);

        setJMenuBar(mb);
        setVisible(true);
    }

    // Notice Bar Timer
    private void startNoticeTicker() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    noticeLabel.setText(notices[noticeIndex]);
                    noticeIndex = (noticeIndex + 1) % notices.length;
                });
            }
        }, 0, 3000);
    }

    private void loadJobsFromFile() {
        String filePath = "E:\\PROJECTS\\6TH SEM\\program\\jobs.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            jobListModel.addElement("Error: jobs.txt file not found!");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean hasJobs = false;
            while ((line = reader.readLine()) != null) {
                jobListModel.addElement(line);
                hasJobs = true;
            }
            if (!hasJobs) {
                jobListModel.addElement("No jobs available at the moment.");
            }
        } catch (IOException e) {
            jobListModel.addElement("Error loading jobs.");
        }
    }

    public static void main(String[] args) {
        // For testing
        SwingUtilities.invokeLater(() -> new Studentproj(1)); // Pass userId = 1
    }
}
