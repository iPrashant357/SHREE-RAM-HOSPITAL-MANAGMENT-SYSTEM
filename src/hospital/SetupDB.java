package hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDB {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASS", "");
        String serverUrl = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(serverUrl, user, pass);
             Statement st = conn.createStatement()) {

            // Create DB
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS hms");
            // Tables
            st.executeUpdate("CREATE TABLE IF NOT EXISTS hms.user_login (\n" +
                    "  id VARCHAR(100) PRIMARY KEY NOT NULL,\n" +
                    "  username VARCHAR(100) NOT NULL,\n" +
                    "  password VARCHAR(255) NOT NULL\n" +
                    ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS hms.doctor_record (\n" +
                    "  id VARCHAR(100) PRIMARY KEY NOT NULL,\n" +
                    "  DoctorName TEXT NOT NULL,\n" +
                    "  Specialization TEXT NOT NULL\n" +
                    ")");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS hms.patient_record (\n" +
                    "  id VARCHAR(50) PRIMARY KEY NOT NULL,\n" +
                    "  Name VARCHAR(100) NOT NULL,\n" +
                    "  Disease VARCHAR(100) NOT NULL,\n" +
                    "  Date VARCHAR(30) NOT NULL\n" +
                    ")");

            // Seed admin user (id=username='prashant', password='KKKISHAN')
            st.executeUpdate("INSERT IGNORE INTO hms.user_login (id, username, password) VALUES ('prashant','prashant','KKKISHAN')");

            System.out.println("Database hms is ready.");
        } catch (SQLException e) {
            System.err.println("Failed to setup database: " + e.getMessage());
            throw e;
        }
    }
}