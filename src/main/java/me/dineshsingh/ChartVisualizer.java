package me.dineshsingh;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class ChartVisualizer {
    private final JTextField item;
    private JTextField amount;
    private JButton addDataButton, pieChartButton, barChartButton, lineChartButton, resetButton;
    private JPanel mainFrame, panel2, chartPanel;
    private DefaultTableModel model;
    private JTable table;
    private JFrame frame;

    public ChartVisualizer() {
        frame = new JFrame("Chart Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);


        mainFrame = new JPanel(new BorderLayout());

        item = new JTextField(10);
        amount = new JTextField(10);
        addDataButton = new JButton("Add Data");
        pieChartButton = new JButton("Pie Chart");
        barChartButton = new JButton("Bar Chart");
        lineChartButton = new JButton("Line Chart");
        resetButton = new JButton("Reset");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Item: "));
        inputPanel.add(item);
        inputPanel.add(new JLabel("Amount: "));
        inputPanel.add(amount);
        inputPanel.add(addDataButton);
        inputPanel.add(pieChartButton);
        inputPanel.add(barChartButton);
        inputPanel.add(lineChartButton);
        inputPanel.add(resetButton);

        panel2 = new JPanel(new BorderLayout());
        displayTable();

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Chart Visualization"));

        mainFrame.add(inputPanel, BorderLayout.NORTH);
        mainFrame.add(panel2, BorderLayout.WEST);
        mainFrame.add(chartPanel, BorderLayout.CENTER);

        frame.setContentPane(mainFrame);
        frame.setVisible(true);

        addDataButton.addActionListener(_ -> addDataToTable());
        pieChartButton.addActionListener(_ -> showPieChart());
        barChartButton.addActionListener(_ -> showBarChart());
        lineChartButton.addActionListener(_ -> showLineChart());
        resetButton.addActionListener(_ -> resetTable());
    }

    private void displayTable() {
        String[] columns = {"ITEM", "AMOUNT"};
        model = new DefaultTableModel(null, columns);
        table = new JTable(model);
        panel2.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void addDataToTable() {
        String itemName = item.getText();
        String amountData = amount.getText();

        if (itemName.isEmpty() || amountData.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both item and amount.");
            return;
        }

        try {
            double amountValue = Double.parseDouble(amountData);
            model.addRow(new Object[]{itemName, amountValue});
            item.setText("");
            amount.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Amount must be a valid number.");
        }
    }

    private void showPieChart() {
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (int i = 0; i < table.getRowCount(); i++) {
            String name = table.getValueAt(i, 0).toString();
            Double amt = Double.valueOf(table.getValueAt(i, 1).toString());
            pieDataset.setValue(name, amt);
        }

        JFreeChart pieChart = ChartFactory.createPieChart("Pie Chart", pieDataset, true, true, false);
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setCircular(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("#,##0"), new DecimalFormat("0.00%")));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setSectionOutlinesVisible(false);
        plot.setShadowPaint(Color.GRAY);

        updateChart(pieChart);
    }

    private void showBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < table.getRowCount(); i++) {
            String name = table.getValueAt(i, 0).toString();
            Double amt = Double.valueOf(table.getValueAt(i, 1).toString());
            dataset.addValue(amt, "Amount", name);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Bar Chart", "Items", "Amount", dataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.getRenderer().setSeriesPaint(0, new Color(0, 102, 204));

        updateChart(barChart);
    }

    private void showLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < table.getRowCount(); i++) {
            String name = table.getValueAt(i, 0).toString();
            Double amt = Double.valueOf(table.getValueAt(i, 1).toString());
            dataset.addValue(amt, "Amount", name);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Line Chart", "Items", "Amount", dataset, PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.getRenderer().setSeriesPaint(0, new Color(255, 51, 51));

        updateChart(lineChart);
    }

    private void updateChart(JFreeChart chart) {
        chartPanel.removeAll();
        ChartPanel chartDisplay = new ChartPanel(chart);
        chartDisplay.setPreferredSize(new Dimension(600, 400));
        chartPanel.add(chartDisplay, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void resetTable() {
        model.setRowCount(0);
        chartPanel.removeAll();
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    public static void main(String[] args) {
        new ChartVisualizer();
    }
}
