import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class MarkAttendanceGUI extends JFrame {

    private DatabaseHandler dbHandler;
    private JTextField regNoField;
    private JComboBox<String> statusBox;
    private JTextField dateField;  // format: YYYY-MM-DD

    public MarkAttendanceGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        setTitle("Mark Attendance");
        setSize(380, 210);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Row 0: Student ID
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Student ID:"), gbc);
        regNoField = new JTextField(12);
        gbc.gridx = 1; panel.add(regNoField, gbc);

        // Row 1: Date
        gbc.gridx = 0; gbc.gridy = ++y; panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(12);
        dateField.setText(LocalDate.now().toString());  // defaults to today
        gbc.gridx = 1; panel.add(dateField, gbc);

        // Row 2: Status ComboBox
        gbc.gridx = 0; gbc.gridy = ++y; panel.add(new JLabel("Status:"), gbc);
        statusBox = new JComboBox<>(new String[]{"Present", "Absent"});
        gbc.gridx = 1; panel.add(statusBox, gbc);

        // Row 3: Save button
        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton markBtn = new JButton("Mark Attendance");
        panel.add(markBtn, gbc);

        add(panel);

        markBtn.addActionListener(e -> saveAttendance());
    }

    private void saveAttendance() {
        String regNoText = regNoField.getText().trim();
        String dateText = dateField.getText().trim();
        String status = (String) statusBox.getSelectedItem();

        if (regNoText.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long regNo;
        try {
            regNo = Long.parseLong(regNoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Validate date
        try {
            LocalDate.parse(dateText); // will throw if format is invalid
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO ATTENDANCE (reg_no, attendance_date, status) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = VALUES(status)";
        try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql)) {
            pst.setLong(1, regNo);
            pst.setDate(2, java.sql.Date.valueOf(dateText));
            pst.setString(3, status.toUpperCase());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance marked as " + status + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Standalone test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                MarkAttendanceGUI gui = new MarkAttendanceGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}