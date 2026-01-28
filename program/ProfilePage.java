import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class ProfilePage extends JFrame {

    public ProfilePage(int userId) {
        setTitle("User Profile");
        setSize(400, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name: ");
        JLabel mobileLabel = new JLabel("Mobile: ");
        JLabel sectionLabel = new JLabel("Section: ");
        JLabel yearLabel = new JLabel("Year: ");
        JLabel roleLabel = new JLabel("role: ");
        JLabel profilePicLabel = new JLabel();
        profilePicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        nameLabel.setBounds(30, 30, 300, 30);
        mobileLabel.setBounds(30, 70, 300, 30);
        sectionLabel.setBounds(30, 110, 300, 30);
        yearLabel.setBounds(30, 150, 300, 30);
        roleLabel.setBounds(30, 190, 300, 30);
        profilePicLabel.setBounds(100, 230, 150, 150);

        add(nameLabel);
        add(mobileLabel);
        add(sectionLabel);
        add(yearLabel);
        add(roleLabel);
        add(profilePicLabel);

        // Fetch data from MySQL
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_db", "root", "saad@786");
            PreparedStatement ps = con.prepareStatement("SELECT username, mobile, section, batch, role, profile_pic_path FROM users WHERE id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("username"));
                mobileLabel.setText("Mobile: " + rs.getString("mobile"));
                sectionLabel.setText("Section: " + rs.getString("section"));
                yearLabel.setText("Year: " + rs.getString("batch"));
                roleLabel.setText("role: " + rs.getString("role"));

                String imgPath = rs.getString("profile_pic_path");
                if (imgPath != null && new File(imgPath).exists()) {
                    ImageIcon icon = new ImageIcon(imgPath);
                    Image scaledImg = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    profilePicLabel.setIcon(new ImageIcon(scaledImg));
                } else {
                    profilePicLabel.setText("No Image");
                    profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No profile found!");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage());
        }

        setVisible(true);
    }
}
