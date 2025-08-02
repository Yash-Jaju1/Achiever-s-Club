import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class EditProfileGUI extends JFrame {
    private DatabaseHandler dbHandler;
    private JTextField regNoField, nameField, classField, streamField, fatherField, motherField, dobField, dojField, phoneField, emailField;
    private JButton searchBtn, saveBtn;

    public EditProfileGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        setTitle("Edit Profile");
        setSize(420, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: RegNo search
        gbc.gridx=0; gbc.gridy=0; panel.add(new JLabel("Student ID:"), gbc);
        regNoField = new JTextField(12); gbc.gridx=1; panel.add(regNoField, gbc);
        searchBtn = new JButton("Fetch");
        gbc.gridx=2; panel.add(searchBtn, gbc);

        // Form fields for editing (disabled initially)
        nameField = new JTextField(20);
        classField = new JTextField(20);
        streamField = new JTextField(20);
        fatherField = new JTextField(20);
        motherField = new JTextField(20);
        dobField = new JTextField(20);
        dojField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);

        String[] labels = {"Name:", "Class:", "Stream:", "Father:", "Mother:",
                "DOB (YYYY-MM-DD):", "DOJ (YYYY-MM-DD):", "Phone:", "Email:"};
        JTextField[] fields = {nameField, classField, streamField, fatherField, motherField,
                dobField, dojField, phoneField, emailField};

        for (int i=0; i<labels.length; i++) {
            gbc.gridx=0; gbc.gridy=i+1; panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx=1; gbc.gridwidth=2; panel.add(fields[i], gbc);
            gbc.gridwidth=1;
            fields[i].setEnabled(false);
        }

        // Save Button
        gbc.gridx=0; gbc.gridy=labels.length+1; gbc.gridwidth=3;
        saveBtn = new JButton("Save Changes");
        saveBtn.setEnabled(false);
        panel.add(saveBtn, gbc);

        add(panel);

        // SEARCH logic
        searchBtn.addActionListener(e -> {
            String regNoText = regNoField.getText().trim();
            if (regNoText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Student ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            long regNo;
            try {
                regNo = Long.parseLong(regNoText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Student ID must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sql = "SELECT * FROM STUDENT_PROFILE WHERE reg_no = ?";
            try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql)) {
                pst.setLong(1, regNo);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("stu_name")); nameField.setEnabled(true);
                    classField.setText(rs.getString("stu_class")); classField.setEnabled(true);
                    streamField.setText(rs.getString("stu_stream")); streamField.setEnabled(true);
                    fatherField.setText(rs.getString("stu_father")); fatherField.setEnabled(true);
                    motherField.setText(rs.getString("stu_mother")); motherField.setEnabled(true);
                    dobField.setText(rs.getDate("d_o_b").toString()); dobField.setEnabled(true);
                    dojField.setText(rs.getDate("d_o_j").toString()); dojField.setEnabled(true);
                    phoneField.setText(rs.getString("phn_no")); phoneField.setEnabled(true);
                    emailField.setText(rs.getString("email")); emailField.setEnabled(true);
                    saveBtn.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Student not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                    enableFields(false, fields);
                    saveBtn.setEnabled(false);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // SAVE logic
        saveBtn.addActionListener(e -> {
            String regNoText = regNoField.getText().trim();
            if (regNoText.isEmpty()) return;
            long regNo = Long.parseLong(regNoText);
            String sql = "UPDATE STUDENT_PROFILE SET stu_name=?, stu_class=?, stu_stream=?, stu_father=?, " +
                    "stu_mother=?, d_o_b=?, d_o_j=?, phn_no=?, email=? WHERE reg_no=?";
            try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql)) {
                pst.setString(1, nameField.getText().trim());
                pst.setInt(2, Integer.parseInt(classField.getText().trim()));
                pst.setString(3, streamField.getText().trim());
                pst.setString(4, fatherField.getText().trim());
                pst.setString(5, motherField.getText().trim());
                pst.setDate(6, java.sql.Date.valueOf(dobField.getText().trim()));
                pst.setDate(7, java.sql.Date.valueOf(dojField.getText().trim()));
                pst.setString(8, phoneField.getText().trim());
                pst.setString(9, emailField.getText().trim());
                pst.setLong(10, regNo);
                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Student profile updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No changes made.", "Info", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void enableFields(boolean enabled, JTextField[] fields) {
        for (JTextField field : fields) field.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                EditProfileGUI gui = new EditProfileGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "DB Error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
