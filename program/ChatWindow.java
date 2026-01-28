import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class ChatWindow extends JFrame {
    private int senderId;
    private int receiverId;

    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ChatWindow(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;

        setTitle("Chat Window");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Send");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        setVisible(true);

        loadMessages();
        startAutoRefresh();
    }

    private void loadMessages() {
        chatArea.setText("");
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_db", "root", "saad@786");
            String sql = "SELECT u.username, m.message, m.timestamp " +
                    "FROM messages m " +
                    "JOIN users u ON u.id = m.sender_id " +
                    "WHERE (m.sender_id = ? AND m.receiver_id = ?) " +
                    "   OR (m.sender_id = ? AND m.receiver_id = ?) " +
                    "ORDER BY m.timestamp";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setInt(3, receiverId);
            ps.setInt(4, senderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("username");
                String message = rs.getString("message");
                String timestamp = rs.getString("timestamp");
                chatArea.append("[" + timestamp + "] " + name + ": " + message + "\n");
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            chatArea.append("⚠️ Error loading messages.\n");
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty()) return;

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_db", "root", "saad@786");
            PreparedStatement ps = con.prepareStatement("INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)");
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setString(3, message);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            chatArea.append("⚠️ Error sending message.\n");
            e.printStackTrace();
        }

        inputField.setText("");
        loadMessages();
    }

    private void startAutoRefresh() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> loadMessages());
            }
        }, 0, 3000); // Refresh every 3 seconds
    }
}
