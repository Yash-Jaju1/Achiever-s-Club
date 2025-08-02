// src/RemoveStudentGUI.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RemoveStudentGUI extends JFrame {

    private DatabaseHandler dbHandler;

    // Receive dbHandler for efficient usage
    public RemoveStudentGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        setTitle("Remove Student");
        setSize(350, 170);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel regNoLabel = new JLabel("Enter Student ID (reg_no):");
        JTextField regNoField = new JTextField(15);

        JButton removeBtn = new JButton("Remove Student");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(regNoLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        add(regNoField, gbc);

        gbc.gridy = 2; gbc.gridwidth = 2;
        add(removeBtn, gbc);

        removeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String regNoText = regNoField.getText().trim();

                if (regNoText.isEmpty()) {
                    JOptionPane.showMessageDialog(RemoveStudentGUI.this,
                            "Please enter a Student ID.",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                long regNo;
                try {
                    regNo = Long.parseLong(regNoText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(RemoveStudentGUI.this,
                            "Student ID must be a number.",
                            "Input Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Fetch name (if any)
                String studentName = fetchStudentName(regNo);
                String message;
                if (studentName != null) {
                    message = "Are you sure you want to remove student " + studentName + " (ID: " + regNo + ")?";
                } else {
                    message = "Student ID " + regNo + " not found. Remove anyway (may not do anything)?";
                }

                int confirm = JOptionPane.showConfirmDialog(RemoveStudentGUI.this,
                        message,
                        "Confirm Remove",
                        JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }

                try {
                    boolean removed = removeStudentById(regNo);
                    if (removed) {
                        JOptionPane.showMessageDialog(RemoveStudentGUI.this,
                                "Student " + (studentName == null ? "" : studentName + " ") + "(ID " + regNo + ") removed successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        regNoField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(RemoveStudentGUI.this,
                                "Student ID " + regNo + " not found.",
                                "Not Found",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(RemoveStudentGUI.this,
                            "Error removing student: " + ex.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /** Returns student name, or null if not found */
    private String fetchStudentName(long regNo) {
        String sql = "SELECT stu_name FROM STUDENT_PROFILE WHERE reg_no = ?";
        try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql)) {
            pst.setLong(1, regNo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("stu_name");
        } catch (SQLException e) { /* ignore, will fallback to null */ }
        return null;
    }

    private boolean removeStudentById(long regNo) throws SQLException {
        String sql = "DELETE FROM STUDENT_PROFILE WHERE reg_no = ?";
        try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql)) {
            pst.setLong(1, regNo);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public static void main(String[] args) {
        // For testing standalone
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                RemoveStudentGUI gui = new RemoveStudentGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}