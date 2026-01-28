import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ResourceViewer extends JFrame {
    private JPanel filePanel;

    public ResourceViewer(String year) {
        setTitle("Resources - " + year + " Year");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(filePanel);

        add(new JLabel("Resources for: " + year + " Year", JLabel.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadResources(year);
        setVisible(true);
    }

    private void loadResources(String year) {
        try (Connection conn = DBConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement("SELECT file_name FROM resources WHERE year = ?")) {

            ps.setString(1, year);
            ResultSet rs = ps.executeQuery();

            java.util.List<String> fileNames = new ArrayList<>();
            while (rs.next()) {
                fileNames.add(rs.getString("file_name"));
            }

            if (fileNames.isEmpty()) {
                filePanel.add(new JLabel("No resources available yet for " + year + " Year."));
                return;
            }

            for (String fileName : fileNames) {
                JLabel fileLabel = new JLabel("📄 " + fileName);
                fileLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                fileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                fileLabel.setForeground(Color.BLUE);

                fileLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setDialogTitle("Save As");
                        chooser.setSelectedFile(new File(fileName));

                        int result = chooser.showSaveDialog(ResourceViewer.this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File saveFile = chooser.getSelectedFile();
                            try (PreparedStatement ps2 = conn.prepareStatement("SELECT file_data FROM resources WHERE file_name = ? AND year = ?")) {
                                ps2.setString(1, fileName);
                                ps2.setString(2, year);
                                ResultSet rs2 = ps2.executeQuery();

                                if (rs2.next()) {
                                    byte[] fileBytes = rs2.getBytes("file_data");
                                    Files.write(saveFile.toPath(), fileBytes);
                                    JOptionPane.showMessageDialog(ResourceViewer.this, "Downloaded successfully.");
                                } else {
                                    JOptionPane.showMessageDialog(ResourceViewer.this, "File not found in database.");
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(ResourceViewer.this, "Error downloading file: " + ex.getMessage());
                            }
                        }
                    }
                });

                filePanel.add(fileLabel);
            }

        } catch (Exception e) {
            filePanel.add(new JLabel("Error loading resources: " + e.getMessage()));
        }
    }
}