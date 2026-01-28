import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.Map;
import java.util.HashMap;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Messaging extends JFrame {
    private int currentUserId;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private Map<String, Integer> nameToIdMap = new HashMap<>();

    public Messaging(int currentUserId) {
        this.currentUserId = currentUserId;
        setTitle("Inbox");
        setSize(300, 400);
        setLocationRelativeTo(null);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(userList);
        add(scrollPane);

        loadUsers();

        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedUser = userList.getSelectedValue();
                    int receiverId = nameToIdMap.get(selectedUser);
                    new ChatWindow(currentUserId, receiverId); // Chat between sender & selected user
                }
            }
        });

        setVisible(true);
    }

    private void loadUsers() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_db", "root", "saad@786");
            PreparedStatement ps = con.prepareStatement("SELECT id, username FROM users WHERE id != ?");
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("username");
                int id = rs.getInt("id");
                nameToIdMap.put(name, id);
                userListModel.addElement(name);
            }

            if (userListModel.isEmpty()) {
                userListModel.addElement("⚠ No other users found.");
            }

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            userListModel.addElement("⚠ Error loading users.");
            e.printStackTrace();
        }
    }
}
