// src/PayFeeGUI.java

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

public class PayFeeGUI extends JFrame {

    private DatabaseHandler dbHandler;

    private JTextField regNoField;
    private JLabel studentNameLabel;
    private JLabel totalFeeLabel;
    private JLabel feePaidLabel;
    private JLabel feeDueLabel;
    private JTextField payAmountField;
    private JButton fetchBtn, payBtn;

    private long currentRegNo = -1;
    private long totalFee = 0;
    private long feePaid = 0;
    private long feeDue = 0;
    private String studentName = "";

    public PayFeeGUI(DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;

        setTitle("Pay Fee");
        setSize(470, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Row 0: Student ID Label, TextField, and Fetch Button
        gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel("Enter Student ID (reg_no):"), gbc);

        gbc.weightx = 1.0;
        regNoField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = y;
        panel.add(regNoField, gbc);

        gbc.weightx = 0;
        fetchBtn = new JButton("Fetch Fee Info");
        gbc.gridx = 2; gbc.gridy = y;
        panel.add(fetchBtn, gbc);

        // Row 1: Student Name
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(new JLabel("Student Name:"), gbc);

        studentNameLabel = new JLabel("-");
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(studentNameLabel, gbc);
        gbc.gridwidth = 1;

        // Row 2: Total Fee
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(new JLabel("Total Fee:"), gbc);

        totalFeeLabel = new JLabel("-");
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(totalFeeLabel, gbc);
        gbc.gridwidth = 1;

        // Row 3: Fee Paid
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(new JLabel("Fee Paid:"), gbc);

        feePaidLabel = new JLabel("-");
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(feePaidLabel, gbc);
        gbc.gridwidth = 1;

        // Row 4: Fee Due
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(new JLabel("Fee Due:"), gbc);

        feeDueLabel = new JLabel("-");
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(feeDueLabel, gbc);
        gbc.gridwidth = 1;

        // Row 5: Payment Amount Input
        gbc.gridx = 0; gbc.gridy = ++y;
        panel.add(new JLabel("Enter Payment Amount:"), gbc);

        payAmountField = new JTextField();
        payAmountField.setEnabled(false);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panel.add(payAmountField, gbc);
        gbc.gridwidth = 1;

        // Row 6: Pay Fee Button
        payBtn = new JButton("Pay Fee");
        payBtn.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 3;
        panel.add(payBtn, gbc);

        add(panel);
        pack();

        // Button actions
        fetchBtn.addActionListener(e -> fetchFeeDetails());
        payBtn.addActionListener(e -> processPayment());
    }

    private void fetchFeeDetails() {
        String regNoText = regNoField.getText().trim();
        if (regNoText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Student ID", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long regNo;
        try {
            regNo = Long.parseLong(regNoText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get student name
            String sqlStudent = "SELECT stu_name FROM STUDENT_PROFILE WHERE reg_no = ?";
            try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sqlStudent)) {
                pst.setLong(1, regNo);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        studentName = rs.getString("stu_name");
                    } else {
                        JOptionPane.showMessageDialog(this, "Student not found", "Error", JOptionPane.ERROR_MESSAGE);
                        clearFeeInfo();
                        return;
                    }
                }
            }

            // Get fee details
            String sqlFee = "SELECT total_fee, fee_paid, fee_due FROM FEE_DETAILS WHERE reg_no = ?";
            try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(sqlFee)) {
                pst.setLong(1, regNo);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        totalFee = rs.getLong("total_fee");
                        feePaid = rs.getLong("fee_paid");
                        feeDue = rs.getLong("fee_due");
                    } else {
                        JOptionPane.showMessageDialog(this, "Fee details not found for this student", "Error", JOptionPane.ERROR_MESSAGE);
                        clearFeeInfo();
                        return;
                    }
                }
            }

            // Display fetched data
            studentNameLabel.setText(studentName);
            totalFeeLabel.setText(formatCurrency(totalFee));
            feePaidLabel.setText(formatCurrency(feePaid));
            feeDueLabel.setText(formatCurrency(feeDue));

            currentRegNo = regNo;
            payAmountField.setEnabled(true);
            payBtn.setEnabled(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            clearFeeInfo();
        }
    }

    private void processPayment() {
        String payText = payAmountField.getText().trim();
        if (payText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter payment amount", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long payAmount;
        try {
            payAmount = Long.parseLong(payText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Payment amount should be a number", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (payAmount <= 0) {
            JOptionPane.showMessageDialog(this, "Payment amount must be positive", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (payAmount > feeDue) {
            JOptionPane.showMessageDialog(this, "Payment amount exceeds fee due", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long newPaid = feePaid + payAmount;
            long newDue = feeDue - payAmount;

            String updateSql = "UPDATE FEE_DETAILS SET fee_paid = ?, fee_due = ? WHERE reg_no = ?";
            try (PreparedStatement pst = dbHandler.getConnection().prepareStatement(updateSql)) {
                pst.setLong(1, newPaid);
                pst.setLong(2, newDue);
                pst.setLong(3, currentRegNo);
                int rows = pst.executeUpdate();
                if (rows > 0) {
                    feePaid = newPaid;
                    feeDue = newDue;
                    feePaidLabel.setText(formatCurrency(feePaid));
                    feeDueLabel.setText(formatCurrency(feeDue));
                    payAmountField.setText("");

                    TextToSpeech tts = new TextToSpeech();
                    tts.speak("Your payment of " + payAmount + " rupees is successful. Thank you!");
                    tts.close();

                    JOptionPane.showMessageDialog(this, "Payment Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Payment failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFeeInfo() {
        currentRegNo = -1;
        studentName = "";
        totalFee = feePaid = feeDue = 0;
        studentNameLabel.setText("-");
        totalFeeLabel.setText("-");
        feePaidLabel.setText("-");
        feeDueLabel.setText("-");
        payAmountField.setText("");
        payAmountField.setEnabled(false);
        payBtn.setEnabled(false);
    }

    private String formatCurrency(long amount) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHandler dbHandler = new DatabaseHandler();
                PayFeeGUI gui = new PayFeeGUI(dbHandler);
                gui.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
