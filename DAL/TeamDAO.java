package DAL;

import BE.Team;
import DAL.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    private ConnectionManager connectionManager;

    public TeamDAO() {
        connectionManager = new ConnectionManager();
    }

    // Метод для отримання всіх команд з бази даних
    public List<Team> getAllTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT id, teamName FROM Team";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("teamName");
                Team team = new Team(id, name);
                teams.add(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return teams;
    }

    // Метод для створення нової команди в базі даних і повернення її згенерованого ID
    public Team createTeam(Team team) throws SQLException {
        String sql = "INSERT INTO Team (teamName) VALUES (?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, team.getTeamName());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Створення команди не вдалося, жодного рядка не змінено.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.setId(generatedKeys.getInt(1)); // Встановлення ID команди
                    return team; // Повертаємо обновлений об'єкт команди
                } else {
                    throw new SQLException("Створення команди не вдалося, ID не отримано.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}

