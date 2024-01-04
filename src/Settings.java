import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

public class Settings extends JPanel {

    static final String THEME_PREFERENCE_KEY = "selectedTheme";
    private JComboBox<String> jComboBox1;
    private String selectedTheme;

    public Settings() {
        initComponents();
        loadSelectedTheme();
        applySelectedTheme(selectedTheme);
    }

    public JPanel getPanel() {
        return this;  // Return the Settings panel itself
    }

    private void applySelectedTheme(String themeName) {
        String themeClass = ThemeManager.getThemeClass(themeName);
        if (themeClass != null) {
            try {
                UIManager.setLookAndFeel(themeClass);
                SwingUtilities.updateComponentTreeUI(this); // Update UI for the Settings panel
                // Also update UI for the entire application
                updateUIForEntireApplication();
                saveSelectedTheme(themeName);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                System.err.println("Error applying theme: " + themeName);
                e.printStackTrace();
            }
        } else {
            System.err.println("Unsupported theme: " + themeName);
        }
    }

    private void updateUIForEntireApplication() {
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }

    private void initComponents() {
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        jComboBox1 = new JComboBox<>(ThemeManager.getAvailableThemes().toArray(new String[0]));

        jLabel1.setFont(new Font("Arial", Font.BOLD, 36));
        jLabel1.setText("SETTINGS");

        jLabel2.setFont(new Font("Arial", Font.BOLD, 14));
        jLabel2.setText("THEME");

        jComboBox1.setFont(new Font("Arial", Font.BOLD, 12));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(76, 76, 76)
                                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(102, 102, 102)
                                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(614, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel1)
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(459, Short.MAX_VALUE))
        );
    }

    private void jComboBox1ActionPerformed(ActionEvent evt) {
        selectedTheme = jComboBox1.getSelectedItem().toString();
        applySelectedTheme(selectedTheme);
    }

    private void saveSelectedTheme(String themeName) {
        Preferences prefs = Preferences.userNodeForPackage(Settings.class);
        prefs.put(THEME_PREFERENCE_KEY, themeName);
    }

    private void loadSelectedTheme() {
        Preferences prefs = Preferences.userNodeForPackage(Settings.class);
        selectedTheme = prefs.get(THEME_PREFERENCE_KEY, "Flat Dark"); // Default to "Flat Dark" if not found
        jComboBox1.setSelectedItem(selectedTheme);
        applySelectedTheme(selectedTheme);
    }
}
