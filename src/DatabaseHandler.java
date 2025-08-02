// src/DatabaseHandler.java

import java.sql.*;
import java.time.LocalDate;

public class DatabaseHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/project?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";       // Change to your MySQL username
    private static final String PASS = "yash2003";   // Change to your MySQL password

    private Connection conn;

    public DatabaseHandler() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found");
        }
        conn = DriverManager.getConnection(URL, USER, PASS);
    }

    public Connection getConnection() {
        return conn;
    }

    // Add a student and return the generated student ID (reg_no)
    public long addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO STUDENT_PROFILE " +
                "(stu_name, stu_class, stu_stream, stu_father, stu_mother, d_o_b, d_o_j, phn_no, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        long newRegNo;

        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, student.getName());
            pst.setInt(2, student.getClassNumber());
            pst.setString(3, student.getStream());
            pst.setString(4, student.getFather());
            pst.setString(5, student.getMother());
            pst.setDate(6, Date.valueOf(student.getDob()));
            pst.setDate(7, Date.valueOf(student.getDoj()));
            pst.setString(8, student.getPhone());
            pst.setString(9, student.getEmail());

            int affectedRows = pst.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Add student failed, no rows affected.");

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next())
                    newRegNo = generatedKeys.getLong(1);
                else
                    throw new SQLException("Failed to obtain student ID (reg_no).");
            }
        }

        // After adding student, insert fee details
        String feeInsertSql = "INSERT INTO FEE_DETAILS (reg_no, total_fee, fee_due, fee_paid) " +
                "SELECT ?, fee, fee, 0 FROM FEE_STRUCTURE " +
                "WHERE class_name = ? AND (stream = ? OR stream = 'N/A') LIMIT 1";
        try (PreparedStatement pst = conn.prepareStatement(feeInsertSql)) {
            pst.setLong(1, newRegNo);
            pst.setString(2, String.valueOf(student.getClassNumber()));  // class as string
            pst.setString(3, student.getStream());
            pst.executeUpdate();
        }

        return newRegNo;
    }

    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
