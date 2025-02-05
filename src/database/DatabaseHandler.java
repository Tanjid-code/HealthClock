package database; // Change this based on your project structure

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/prescription_db";
    private static final String USER = "root"; // Change if necessary
    private static final String PASSWORD = ""; // Set your MySQL password

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void savePrescription(String patientName, String medicineName, String dosage, String time, int durationDays) {
        String query = "INSERT INTO prescriptions (patient_name, medicine_name, dosage, time, duration_days) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, patientName);
            pstmt.setString(2, medicineName);
            pstmt.setString(3, dosage);
            pstmt.setString(4, time);
            pstmt.setInt(5, durationDays);
            pstmt.executeUpdate();
            System.out.println("Data saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
