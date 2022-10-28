package ci.polyevents;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.logging.Level;

public class DBHandler {
    private static Connection CONN;

    public static void initDB() {
        try {
            Files.createDirectories(Paths.get("./plugins/PolyPlugins"));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error creating the PolyPlugins folder");
        }

        try {
            CONN = DriverManager.getConnection("jdbc:sqlite:plugins/PolyPlugins/database.db");
            Statement statement = CONN.createStatement();

            String sql1 = """
                    CREATE TABLE IF NOT EXISTS events (
                        id TEXT PRIMARY KEY,
                        world text NOT NULL,
                        location TEXT NOT NULL,
                        description TEXT
                    )
            """;

            String sql2 = """
                    CREATE TABLE IF NOT EXISTS players (
                        display_name TEXT,
                        id TEXT,
                        world TEXT NOT NULL,
                        location TEXT NOT NULL,
                        
                        PRIMARY KEY (display_name, id)
                        FOREIGN KEY (id) REFERENCES events(id) ON DELETE CASCADE
                    )
            """;

            statement.execute(sql1);
            statement.execute(sql2);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error initializing DB");
        }
    }

    public static ResultSet getEvent(String eventId) {
        try {
            PreparedStatement statement = CONN.prepareStatement(
                    "SELECT * FROM events WHERE id=?"
            );

            statement.setString(1, eventId);
            return statement.executeQuery();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting event " + eventId);
            return null;
        }
    }

    public static ResultSet getEvents() {
        try {
            return CONN.createStatement().executeQuery("SELECT * FROM events");
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting events");
            return null;
        }
    }

    public static ResultSet getPlayerByName(String displayName) {
        try {
            PreparedStatement statement = CONN.prepareStatement(
                    "SELECT * FROM players WHERE display_name=?"
            );

            statement.setString(1, displayName);
            return statement.executeQuery();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting player by name" + displayName);
            return null;
        }
    }

    public static ResultSet getPlayerByEvent(String eventId, String displayName) {
        try {
            PreparedStatement statement = CONN.prepareStatement(
                    "SELECT * FROM players WHERE display_name=? AND id=?"
            );

            statement.setString(1, displayName);
            statement.setString(2, eventId);

            return statement.executeQuery();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting player by name and id");
            return null;
        }
    }

    public static void createEvent(String eventId, String world, String location, String description) {
        try {
            PreparedStatement statement = CONN.prepareStatement(
                    "INSERT INTO events VALUES(?, ?, ?, ?)"
            );

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
        try {
            PreparedStatement statement = CONN.prepareStatement(
                    "INSERT OR REPLACE INTO players VALUES(?, ?, ?, ?)"
            );

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
        try {
            PreparedStatement statement =  CONN.prepareStatement(
                    "DELETE FROM events WHERE id=?"
            );

            statement.setString(1, eventId);
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error deleting event " + eventId);
        }
    }

    public static void deletePlayer(String eventId, String displayName) {
        try {
            PreparedStatement statement = CONN.prepareStatement(
                    "DELETE FROM players WHERE id=? AND display_name=?"
            );

            statement.setString(1, eventId);
            statement.setString(2, displayName);

            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error deleting player " + displayName + " to event " + eventId);
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
