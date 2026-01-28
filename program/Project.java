import javax.swing.*;
import java.awt.*;

public class Project extends JFrame {
    private int userId;

    public Project(int userId) {
        this.userId = userId; // Store userId if you want to use it later

        setTitle("University Portal - Faculty/Alumni Dashboard");
        setSize(1540, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Use absolute positioning

        // Background Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/third.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1540, 810, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 40, 1540, 810);
        add(image);

        // Menu Bar
        JMenuBar mb = new JMenuBar();

        /*JMenu newInformation = new JMenu("New Information");
        newInformation.setForeground(Color.BLUE);
        mb.add(newInformation);

        JMenuItem facultyInfo = new JMenuItem("Faculty Information");
        newInformation.add(facultyInfo);

        JMenuItem studentInfo = new JMenuItem("Student Information");
        newInformation.add(studentInfo);*/

        JMenu profileMenu = new JMenu("Profile");
        profileMenu.setForeground(Color.BLUE);
        JMenuItem viewProfile = new JMenuItem("View Profile");
        viewProfile.addActionListener(e -> new ProfilePage(userId));
        profileMenu.add(viewProfile);
        mb.add(profileMenu);

        //JMenuItem studentDetails = new JMenuItem("Student Details");
        //userDetails.add(studentDetails);

        JMenu resources = new JMenu("Resources");
        resources.setForeground(Color.RED);
        mb.add(resources);

       /* JMenuItem facultyLeave = new JMenuItem("Faculty Leave");
        resources.add(facultyLeave);

        JMenuItem studentLeave = new JMenuItem("Student Leave");
        resources.add(studentLeave);*/

        JMenuItem uploadResource = new JMenuItem("Upload Study Material");
        resources.add(uploadResource);
        uploadResource.addActionListener(e -> new FacultyUploadPage());

        // Post Menu
        JMenu postMenu = new JMenu("Post");
        postMenu.setForeground(Color.RED);
        mb.add(postMenu);

        JMenuItem postJob = new JMenuItem("Post Job");
        postMenu.add(postJob);
        postJob.addActionListener(e -> new EmployerPage());

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

    public static void main(String[] args) {
        new Project(1); // Sample test call with userId = 1
    }
}
