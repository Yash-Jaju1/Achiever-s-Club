// src/CheckProfileGUI.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CheckProfileGUI extends JFrame {

    private DatabaseHandler dbHandler;
    private JTextField regNoField;
    private JTextArea profileArea;

    public CheckProfileGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        setTitle("Check Profile");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter Student ID (reg_no):");
        regNoField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        topPanel.add(label);
        topPanel.add(regNoField);
        topPanel.add(searchBtn);

        profileArea = new JTextArea();
        profileArea.setEditable(false);
        profileArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(profileArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> searchStudentProfile());
    }

    private void searchStudentProfile() {
        String regNoText = regNoField.getText().trim();
        if (regNoText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a student ID.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long regNo;
        try {
            regNo = Long.parseLong(regNoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Student ID must be a number.",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT * FROM STUDENT_PROFILE WHERE reg_no = ?";
        try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sql)) {
            pst.setLong(1, regNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Student ID: ").append(regNo).append("\n");
                sb.append("Name: ").append(rs.getString("stu_name")).append("\n");
                sb.append("Class: ").append(rs.getInt("stu_class")).append("\n");
                sb.append("Stream: ").append(rs.getString("stu_stream")).append("\n");
                sb.append("Father's Name: ").append(rs.getString("stu_father")).append("\n");
                sb.append("Mother's Name: ").append(rs.getString("stu_mother")).append("\n");
                sb.append("Date of Birth: ").append(rs.getDate("d_o_b")).append("\n");
                sb.append("Date of Joining: ").append(rs.getDate("d_o_j")).append("\n");
                sb.append("Phone No: ").append(rs.getString("phn_no")).append("\n");
                sb.append("Email: ").append(rs.getString("email")).append("\n");

                profileArea.setText(sb.toString());
            } else {
                profileArea.setText("");
                JOptionPane.showMessageDialog(this,
                        "No student found with ID " + regNo,
                        "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // For standalone testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                CheckProfileGUI gui = new CheckProfileGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Database connection failed: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}