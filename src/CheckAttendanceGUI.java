// src/CheckAttendanceGUI.java

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CheckAttendanceGUI extends JFrame {
    private DatabaseHandler dbHandler;
    private JTextField regNoField, fromField, toField;
    private JTable attendanceTable;
    private DefaultTableModel model;

    public CheckAttendanceGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        setTitle("Check Attendance");
        setSize(600, 400); // Wide enough for form and table
        setMinimumSize(new Dimension(500, 330)); // Avoid shrinking too small
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // --- Input Form Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student ID:"), gbc);

        gbc.weightx = 1.0;
        regNoField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(regNoField, gbc);

        gbc.weightx = 0;
        gbc.gridx = 2;
        inputPanel.add(new JLabel("From (YYYY-MM-DD):"), gbc);

        gbc.weightx = 1.0;
        fromField = new JTextField(10);
        gbc.gridx = 3;
        inputPanel.add(fromField, gbc);

        gbc.weightx = 0;
        gbc.gridx = 4;
        inputPanel.add(new JLabel("To (YYYY-MM-DD):"), gbc);

        gbc.weightx = 1.0;
        toField = new JTextField(10);
        gbc.gridx = 5;
        inputPanel.add(toField, gbc);

        gbc.weightx = 0;
        JButton checkBtn = new JButton("Check");
        gbc.gridx = 6;
        inputPanel.add(checkBtn, gbc);

        // --- Attendance Table ---
        String[] columns = {"Date", "Status"};
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        attendanceTable = new JTable(model);
        attendanceTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        // --- Arrange in Frame ---
        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Action ---
        checkBtn.addActionListener(e -> loadAttendance());
    }

    private void loadAttendance() {
        model.setRowCount(0);

        String regNoText = regNoField.getText().trim();
        if (regNoText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long regNo;
        try {
            regNo = Long.parseLong(regNoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fromDate = fromField.getText().trim();
        String toDate = toField.getText().trim();

        StringBuilder sql = new StringBuilder("SELECT attendance_date, status FROM ATTENDANCE WHERE reg_no = ?");
        if (!fromDate.isEmpty()) sql.append(" AND attendance_date >= ?");
        if (!toDate.isEmpty()) sql.append(" AND attendance_date <= ?");
        sql.append(" ORDER BY attendance_date");

        try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql.toString())) {
            pst.setLong(1, regNo);
            int idx = 2;
            if (!fromDate.isEmpty()) pst.setDate(idx++, java.sql.Date.valueOf(fromDate));
            if (!toDate.isEmpty()) pst.setDate(idx++, java.sql.Date.valueOf(toDate));

            ResultSet rs = pst.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{rs.getDate("attendance_date"), rs.getString("status")});
            }
            if (!found) JOptionPane.showMessageDialog(this, "No attendance records found.", "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Standalone test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                CheckAttendanceGUI gui = new CheckAttendanceGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
