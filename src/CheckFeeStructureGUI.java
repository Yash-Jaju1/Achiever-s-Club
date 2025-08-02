// src/CheckFeeStructureGUI.java

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CheckFeeStructureGUI extends JFrame {

    private DatabaseHandler dbHandler;
    private JTable feeTable;
    private DefaultTableModel model;

    public CheckFeeStructureGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        setTitle("Fee Structure");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Column headings
        String[] columns = {"Class", "Stream", "Fee"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make table non-editable
            }
        };

        feeTable = new JTable(model);
        feeTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(feeTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadFeeData();
    }

    private void loadFeeData() {
        model.setRowCount(0); // clear existing

        String sql = "SELECT class_name, stream, fee FROM FEE_STRUCTURE ORDER BY CAST(class_name AS UNSIGNED), stream";

        try (Statement stmt = dbHandler.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String className = rs.getString("class_name");
                String stream = rs.getString("stream");
                long fee = rs.getLong("fee");

                model.addRow(new Object[]{className, stream, fee});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading fee structure: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Standalone test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                CheckFeeStructureGUI gui = new CheckFeeStructureGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "DB Connection failed: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
