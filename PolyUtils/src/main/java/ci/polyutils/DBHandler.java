package ci.polyutils;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Set;
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
            CONN = DriverManager.getConnection("jdbc:sqlite:plugins/PolyPlugins/towns.db");
            CONN.createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS towns (
                        id TEXT PRIMARY KEY,
                        score INTEGER
                    )
            """);
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error initializing DB");
        }
    }

    public static ResultSet getTowns() {
        try {
            return CONN.createStatement().executeQuery("SELECT * FROM towns");
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error getting towns");
            return null;
        }
    }

    public static void addPoints(String town, int points) {
        try {
            PreparedStatement statement = CONN.prepareStatement("""
                UPDATE towns
                SET score=score+?
                WHERE id=?
            """);

            statement.setInt(1, points);
            statement.setString(2, town);

            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error adding " + points + " points to " + town);
        }
    }

    public static void resetPoints() {
        try {
            CONN.createStatement().execute("UPDATE towns SET score=0");
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error reseting points");
        }
    }

    public static void sync(Set<Team> teams) {
        try {
            PreparedStatement statement;

            for (Team team: teams) {
                statement = CONN.prepareStatement("""
                    INSERT OR IGNORE INTO towns VALUES(?, 0)
                """);

                statement.setString(1, team.getName());
                statement.execute();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error synchronizing teams and DB");
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
