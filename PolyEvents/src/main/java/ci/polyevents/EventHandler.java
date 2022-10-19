package ci.polyevents;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.logging.Level;

public class EventHandler {
    private static Connection CONN;

    public static ResultSet getEvents() {
        String sql = "SELECT * FROM events";

        try {
            return CONN.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting events");
            return null;
        }
    }

    public static ResultSet getEvent(String eventId) {
        String sql = "SELECT * FROM events WHERE event_id=?";

        try {
            PreparedStatement statement = CONN.prepareStatement(sql);
            statement.setString(1, eventId);
            return statement.executeQuery();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting event " + eventId);
            return null;
        }
    }

    public static ResultSet getPlayerByName(String displayName) {
        String sql = "SELECT * FROM players WHERE display_name=?";

        try {
            PreparedStatement statement = CONN.prepareStatement(sql);
            statement.setString(1, displayName);
            return statement.executeQuery();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting player by name" + displayName);
            return null;
        }
    }

    public static ResultSet getPlayerByEvent(String eventId, String displayName) {
        String sql = "SELECT * FROM players WHERE display_name=? AND event_id=?";

        try {
            PreparedStatement statement = CONN.prepareStatement(sql);
            statement.setString(1, displayName);
            statement.setString(2, eventId);
            return statement.executeQuery();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting player by name and event_id");
            return null;
        }
    }

    public static void createEvent(String eventId, String world, String location, String description) {
        String sql = "INSERT INTO events VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement statement = CONN.prepareStatement(sql);
            statement.setString(1, eventId);
            statement.setString(2, world);
            statement.setString(3, location);
            statement.setString(4, description);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error creating event " + eventId);
        }
    }

    public static void updatePlayer(String displayName, String world, String location, String eventId) {
        String sql = "INSERT OR REPLACE INTO players VALUES(?, ?, ?, ?)";

        try {
            PreparedStatement statement = CONN.prepareStatement(sql);

            statement.setString(1, displayName);
            statement.setString(2, eventId);
            statement.setString(3, world);
            statement.setString(4, location);

            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error adding player " + displayName + " to event " + eventId);
        }
    }

    public static void deleteEvent(String eventId) {
        String sql = "DELETE FROM events WHERE event_id=?";

        try {
            PreparedStatement statement =  CONN.prepareStatement(sql);
            statement.setString(1, eventId);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error deleting event " + eventId);
        }
    }

    public static void deletePlayer(String eventId, String displayName) {
        String sql = "DELETE FROM players WHERE event_id=? AND display_name=?";

        try {
            PreparedStatement statement = CONN.prepareStatement(sql);

            statement.setString(1, eventId);
            statement.setString(2, displayName);

            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error deleting player " + displayName + " to event " + eventId);
        }
    }

    public static void initDB() {
        try {
            Files.createDirectories(Paths.get("./plugins/PolyEvents"));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error creating the PolyEvents folder");
        }

        try {
            CONN = DriverManager.getConnection("jdbc:sqlite:plugins/PolyEvents/database.db");

            String sql1 = """
                    CREATE TABLE IF NOT EXISTS events (
                        event_id text PRIMARY KEY,
                        world text NOT NULL,
                        location text NOT NULL,
                        description text
                    )
                    """;

            String sql2 = """
                    CREATE TABLE IF NOT EXISTS players (
                        display_name text,
                        event_id text,
                        world text NOT NULL,
                        location text NOT NULL,
                        
                        PRIMARY KEY (display_name, event_id)
                        FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
                    )
                    """;

            CONN.createStatement().execute(sql1);
            CONN.createStatement().execute(sql2);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error initializing DB");
        }
    }

    public static void closeDB() {
        if (CONN == null)
            return;

        try {
            CONN.close();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error closing DB");
        }
    }
}
