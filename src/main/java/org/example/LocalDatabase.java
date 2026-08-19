package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Class for JDBC - for establishing connection between SQLite database and calendar application,
//methods are mainly used by Appointments class

public class LocalDatabase {
    private static final String url = "jdbc:sqlite:data/calendar.db";
    private static Connection connection;

    public static void buildConnection() {
        try {
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("Connected to local database");
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection to local database closed");
        } catch (SQLException s) {
            System.out.println("Failed to close connection");
        }
    }

    public static void initializeAppointmentTable() {
        buildConnection();
        String sql = "CREATE TABLE IF NOT EXISTS appointments (\n" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT,\n " +
                " title TEXT NOT NULL,\n " +
                " timeStart TEXT NOT NULL,\n " +
                " timeEnd TEXT NOT NULL,\n " +
                " description TEXT\n" +
                ");";

        try (Statement st = connection.createStatement()) {
            st.execute(sql);
            System.out.println("Table created");

        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static int add(Appointment a) {
        buildConnection();
        String sql = "INSERT INTO appointments(title, timeStart, timeEnd, description) " +
                "VALUES(?, ?, ?, ?)";

        try(PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, a.getTitle());
            pst.setString(2, a.getTimeStart().toString());
            pst.setString(3, a.getTimeEnd().toString());
            pst.setString(4, a.getDescription());
            pst.executeUpdate();
            System.out.println("Appointment data inserted");
        } catch(SQLException s) {
            System.out.println(s.getMessage());
        } finally {
            closeConnection();
        }

        buildConnection();
        String sql2 = "SELECT MAX(id) AS latest_entry FROM appointments";
        int id = 0;
        try (
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql2);
        ) {
            id = rs.getInt("latest_entry");
            System.out.println("Id assigned to appointment");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        } finally {
            closeConnection();
        }
        return id;
    }

    public static void delete(Appointment a) {
        buildConnection();
        String sql = "DELETE FROM appointments WHERE id = " + a.getId();

        try (Statement st = connection.createStatement()) {
            st.execute(sql);
            System.out.println("Appointment deleted");
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static List<Appointment> appointmentList() {
        buildConnection();
        String sql = "SELECT * FROM appointments";
        List<Appointment> res = new ArrayList<>();
        try (
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
        ) {
            int id;
            String timeStart;
            String timeEnd;
            String title;
            String description;
            Appointment a;

            while (rs.next()) {
                id = rs.getInt("id");
                timeStart = rs.getString("timeStart");
                timeEnd = rs.getString("timeEnd");
                title = rs.getString("title");
                description = rs.getString("description");

                a = new Appointment(id, title, timeStart, timeEnd, description);
                res.add(a);
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
        closeConnection();
        return res;
    }

    public static void update(Appointment a) {
        buildConnection();

        String sql = "UPDATE appointments SET title = ?, timeStart = ?, timeEnd = ?, description = ? WHERE id = ?";

        try(PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, a.getTitle());
            pst.setString(2, a.getTimeStart().toString());
            pst.setString(3, a.getTimeEnd().toString());
            pst.setString(4, a.getDescription());
            pst.setInt(5, (a.getId()));
            pst.executeUpdate();
            System.out.println("Appointment data updated");
        } catch(SQLException s) {
            System.out.println(s.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static void main(String[] args) {

    }
}
