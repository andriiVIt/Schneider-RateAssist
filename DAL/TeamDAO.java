package DAL;

import BE.Country;
import BE.Team;
import DAL.Interface.ITeamDAO;
import DAL.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO implements ITeamDAO {

    private static ConnectionManager connectionManager;

    public TeamDAO() {
        connectionManager = new ConnectionManager();
    }

    // Метод для отримання всіх команд з бази даних
    @Override
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
    @Override
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

    public void deleteTeam(Team team) throws SQLException {
        String sqlDeleteFromEmployeeTeam = "DELETE FROM EmployeeTeam WHERE teamid = ?";
        String sqlDeleteFromRate = "DELETE FROM Rate WHERE teamId = ?";
        String sqlDeleteTeam = "DELETE FROM Team WHERE ID = ?";

        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false); // початок транзакції

            try (PreparedStatement pstDeleteFromEmployeeTeam = con.prepareStatement(sqlDeleteFromEmployeeTeam);
                 PreparedStatement pstDeleteFromRate = con.prepareStatement(sqlDeleteFromRate);
                 PreparedStatement pstDeleteTeam = con.prepareStatement(sqlDeleteTeam)) {

                // Видалення записів з EmployeeTeam
                pstDeleteFromEmployeeTeam.setInt(1, team.getId());
                pstDeleteFromEmployeeTeam.executeUpdate();

                // Видалення записів з Rate
                pstDeleteFromRate.setInt(1, team.getId());
                pstDeleteFromRate.executeUpdate();

                // Видалення запису з Team
                pstDeleteTeam.setInt(1, team.getId());
                pstDeleteTeam.executeUpdate();

                con.commit(); // підтвердження транзакції
            } catch (SQLException e) {
                con.rollback(); // відкат транзакції у разі помилки
                throw e;
            }
        }
    }
    @Override
    public void updateTeamEmployee(int employeeId, int teamId) throws SQLException {
        String sql = "UPDATE EmployeeTeam SET teamid = ? WHERE employeeid = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, teamId);
            pst.setInt(2, employeeId);
            pst.executeUpdate();
        }
    }
}

