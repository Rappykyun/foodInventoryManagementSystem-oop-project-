import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Home extends JFrame {

    private Connection con;
    private JPanel notificationPanel;
    private JButton showNotificationButton;

    public Home() {
        initComponents();
        con = Connect.getConnection();
        if (con == null) {
            System.err.println("Failed to connect to the database. Exiting...");
            System.exit(1);
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("ShelfMate Food Inventory Management System");
        setSize(1000, 700);

        // Greeting message
        JLabel greetingLabel = new JLabel("Welcome, Master!!!");
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 36));

        showNotificationButton = new JButton("Notifications");
        showNotificationButton.addActionListener(this::showNotifications);

        // Create a panel for the greeting and button
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 20)); // Added spacing
        headerPanel.add(greetingLabel);
        headerPanel.add(showNotificationButton);

        // Set up the main content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // Initialize the notification panel
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(notificationPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
    }
    private void showNotifications(ActionEvent e) {
        // Clear previous notifications
        notificationPanel.removeAll();

        try {
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            // Query for items expiring within the next 7 days
            String query = "SELECT fname, expDate FROM food_tbl WHERE expDate BETWEEN ? AND ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setDate(1, currentDate);
                pst.setDate(2, addDays(currentDate, 7));

                ResultSet resultSet = pst.executeQuery();
                while (resultSet.next()) {
                    String itemName = resultSet.getString("fname");
                    String expDate = resultSet.getString("expDate");

                    // Add expiring item notification
                    addNotification("Expiring Soon", "Item '" + itemName + "' will expire on " + expDate);
                }
            }

            // Query for items already expired
            query = "SELECT fname, expDate FROM food_tbl WHERE expDate < ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setDate(1, currentDate);

                ResultSet resultSet = pst.executeQuery();
                while (resultSet.next()) {
                    String itemName = resultSet.getString("fname");
                    String expDate = resultSet.getString("expDate");

                    // Add expired item notification
                    addNotification("Expired", "Item '" + itemName + "' has expired on " + expDate);
                }
            }

            // Repaint the panel and show the notification panel in a translucent overlay
            notificationPanel.revalidate();
            notificationPanel.repaint();
            showNotificationOverlay();
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addNotification(String title, String message) {
        JPanel notificationPanelItem = new JPanel();
        notificationPanelItem.setLayout(new BorderLayout());
        notificationPanelItem.setBackground(Color.WHITE);
        notificationPanelItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel notificationLabel = new JLabel("<html><b>" + title + "</b><br>" + message + "</html>");
        notificationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        notificationLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Set preferred size to achieve consistent size for all notification items
        notificationPanelItem.setPreferredSize(new Dimension(400, 60));

        notificationPanelItem.add(notificationLabel, BorderLayout.CENTER);
        notificationPanel.add(notificationPanelItem);
    }

    private void showNotificationOverlay() {
        JDialog dialog = new JDialog(this, "Notifications", Dialog.ModalityType.MODELESS);
        dialog.setUndecorated(true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel titleBar = new JPanel();
        titleBar.setBackground(new Color(50, 50, 50));
        titleBar.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Notifications");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleBar.add(titleLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        titleBar.add(closeButton, BorderLayout.EAST);

        JPanel overlayPanel = new JPanel();
        overlayPanel.setLayout(new BorderLayout());
        overlayPanel.add(titleBar, BorderLayout.NORTH);
        overlayPanel.add(notificationPanel, BorderLayout.CENTER);

        dialog.add(overlayPanel);
        dialog.setVisible(true);

    }

    private java.sql.Date addDays(java.sql.Date date, int days) {
        long timeInMillis = date.getTime() + ((long) days * 24 * 60 * 60 * 1000);
        return new java.sql.Date(timeInMillis);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Home home = new Home();
            home.setVisible(true);
        });
    }
}
