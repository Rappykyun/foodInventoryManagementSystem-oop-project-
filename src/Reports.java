import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.StandardChartTheme;

public class Reports extends JPanel {

    private final Connection con;

    public Reports() {
        con = Connect.getConnection();
        if (con == null) {
            System.err.println("Failed to connect to the database. Exiting...");
            System.exit(1);
        }

        setCharts();
    }

    private void setCharts() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBorder(new EmptyBorder(40, 30, 10, 10)); // Adjust the border as needed
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel chartPanel1 = createChartPanel("Top 5 Food Types", fetchDataForTop5Types());
        JPanel chartPanel2 = createChartPanel("Near Expiring Foods", fetchDataForNearExpiring());

        mainPanel.add(chartPanel1);
        mainPanel.add(chartPanel2);

        add(mainPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(1000, 636));
    }

    private JPanel createChartPanel(String title, Map<String, Integer> data) {
        JFreeChart chart = createPieChart(title, data);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 450));
        chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Adjust the empty border as needed
        return chartPanel;
    }

private JFreeChart createPieChart(String title, Map<String, Integer> data) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    for (Map.Entry<String, Integer> entry : data.entrySet()) {
        dataset.setValue(entry.getKey(), entry.getValue());
    }

    JFreeChart chart = ChartFactory.createPieChart3D(
            title,
            dataset,
            true,
            true,
            false
    );

    // Set the font for the chart
    Font font = new Font("Arial", Font.BOLD, 12);
    chart.getTitle().setFont(font);
    chart.getLegend().setItemFont(font);

    // Customize the chart colors for a semi-dark theme
    chart.setBackgroundPaint(new Color(34, 34, 34)); // Dark background
    chart.getPlot().setBackgroundPaint(new Color(44, 44, 44)); // Slightly lighter plot background
    chart.getPlot().setOutlinePaint(new Color(68, 68, 68)); // Plot outline color
    chart.getPlot().setForegroundAlpha(0.8f); // Adjust transparency

    return chart;
}

    private Map<String, Integer> fetchDataForTop5Types() {
        Map<String, Integer> data = new HashMap<>();
        try (PreparedStatement pst = con.prepareStatement("SELECT type, SUM(quantity) AS total FROM food_tbl GROUP BY type ORDER BY total DESC LIMIT 5");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("type");
                int quantity = rs.getInt("total");
                data.put(type, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private Map<String, Integer> fetchDataForNearExpiring() {
        Map<String, Integer> data = new HashMap<>();
        try (PreparedStatement pst = con.prepareStatement("SELECT fname, COUNT(*) AS total FROM food_tbl WHERE expDate BETWEEN CURDATE() AND CURDATE() + INTERVAL 7 DAY GROUP BY fname");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("fname");
                int quantity = rs.getInt("total");
                data.put(name, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
