import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;

public class MainGUI extends JFrame {
    private DatabaseHandler dbHandler;

    public MainGUI() {
        setTitle("😎 ACHIEVER'S CLUB 😎");
        setSize(450, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            dbHandler = new DatabaseHandler();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("😎 ACHIEVER'S CLUB 😎", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Add Student Button
        JButton addStudentBtn = new JButton("Add Student");
        addStudentBtn.setMaximumSize(new Dimension(250, 40));
        addStudentBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addStudentBtn.addActionListener(e -> openAddStudentDialog());

        // Remove Student Button
        JButton removeStudentBtn = new JButton("Remove Student");
        removeStudentBtn.setMaximumSize(new Dimension(250, 40));
        removeStudentBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeStudentBtn.addActionListener(e -> {
            RemoveStudentGUI removeGui = new RemoveStudentGUI(dbHandler);
            removeGui.setVisible(true);
        });

        // Check Profile Button
        JButton checkProfileBtn = new JButton("Check Profile");
        checkProfileBtn.setMaximumSize(new Dimension(250, 40));
        checkProfileBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkProfileBtn.addActionListener(e -> {
            CheckProfileGUI checkProfileGUI = new CheckProfileGUI(dbHandler);
            checkProfileGUI.setVisible(true);
        });

        // Edit Profile Button
        JButton editProfileBtn = new JButton("Edit Profile");
        editProfileBtn.setMaximumSize(new Dimension(250, 40));
        editProfileBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        editProfileBtn.addActionListener(e -> {
            EditProfileGUI editProfileGUI = new EditProfileGUI(dbHandler);
            editProfileGUI.setVisible(true);
        });

        JButton payFeeBtn = new JButton("Pay Fee");
        payFeeBtn.setMaximumSize(new Dimension(250, 40));
        payFeeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        payFeeBtn.addActionListener(e -> {
            PayFeeGUI payFeeGUI = new PayFeeGUI(dbHandler);
            payFeeGUI.setVisible(true);
        });

        JButton checkFeeStructureBtn = new JButton("Check Fee Structure");
        checkFeeStructureBtn.setMaximumSize(new Dimension(250, 40));
        checkFeeStructureBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkFeeStructureBtn.addActionListener(e -> {
            CheckFeeStructureGUI feeGUI = new CheckFeeStructureGUI(dbHandler);
            feeGUI.setVisible(true);
        });

        JButton markAttendanceBtn = new JButton("Mark Attendance");
        markAttendanceBtn.setMaximumSize(new Dimension(250, 40));
        markAttendanceBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        markAttendanceBtn.addActionListener(e -> {
            MarkAttendanceGUI gui = new MarkAttendanceGUI(dbHandler);
            gui.setVisible(true);
        });

        JButton checkAttendanceBtn = new JButton("Check Attendance");
        checkAttendanceBtn.setMaximumSize(new Dimension(250, 40));
        checkAttendanceBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkAttendanceBtn.addActionListener(e -> {
            CheckAttendanceGUI gui = new CheckAttendanceGUI(dbHandler);
            gui.setVisible(true);
        });

        // Add all buttons vertically
        mainPanel.add(addStudentBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(removeStudentBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(checkProfileBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(editProfileBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(payFeeBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(checkFeeStructureBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(markAttendanceBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        mainPanel.add(checkAttendanceBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        getContentPane().add(mainPanel);
    }

    private void openAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add Student", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Field labels & inputs
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);

        JLabel classLabel = new JLabel("Class (Number):");
        JTextField classField = new JTextField(20);

        JLabel streamLabel = new JLabel("Stream:");
        JTextField streamField = new JTextField(20);
        streamField.setText("NONE");

        JLabel fatherLabel = new JLabel("Father's Name:");
        JTextField fatherField = new JTextField(20);

        JLabel motherLabel = new JLabel("Mother's Name:");
        JTextField motherField = new JTextField(20);

        JLabel dobLabel = new JLabel("Date of Birth (YYYY-MM-DD):");
        JTextField dobField = new JTextField(20);
        dobField.setText("YYYY-MM-DD");

        JLabel phoneLabel = new JLabel("Phone No:");
        JTextField phoneField = new JTextField(20);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; dialog.add(nameLabel, gbc);    gbc.gridx = 1; dialog.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(classLabel, gbc); gbc.gridx = 1; dialog.add(classField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(streamLabel, gbc);gbc.gridx = 1; dialog.add(streamField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(fatherLabel, gbc);gbc.gridx = 1; dialog.add(fatherField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(motherLabel, gbc);gbc.gridx = 1; dialog.add(motherField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(dobLabel, gbc);   gbc.gridx = 1; dialog.add(dobField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(phoneLabel, gbc); gbc.gridx = 1; dialog.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = ++y; dialog.add(emailLabel, gbc); gbc.gridx = 1; dialog.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton saveBtn = new JButton("Save Student");
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String classText = classField.getText().trim();
                String stream = streamField.getText().trim();
                String father = fatherField.getText().trim();
                String mother = motherField.getText().trim();
                String dobText = dobField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();

                if (name.isEmpty() || classText.isEmpty() || dobText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in Name, Class, and DOB.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int classNumber;
                LocalDate dob;
                try {
                    classNumber = Integer.parseInt(classText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Class must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    dob = LocalDate.parse(dobText);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "DOB must be YYYY-MM-DD!", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                LocalDate doj = LocalDate.now();

                Student student = new Student(name, classNumber, stream, father, mother,
                        dob, doj, phone, email);
                try {
                    long reg_no = dbHandler.addStudent(student);

                    TextToSpeech tts = new TextToSpeech();
                    tts.speak("Your Student I D is " + reg_no + " please remember it for future references");
                    tts.close();

                    JOptionPane.showMessageDialog(dialog, "Student added (ID: " + reg_no + ")");
                    dialog.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Failed to add student: " + ex.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}
