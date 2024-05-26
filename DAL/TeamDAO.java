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

    // Method to retrieve all teams from the database
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

    // Method to create a new team in the database and return its generated ID
    @Override
    public Team createTeam(Team team) throws SQLException {
        String sql = "INSERT INTO Team (teamName) VALUES (?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, team.getTeamName());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create command, no row changed.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.setId(generatedKeys.getInt(1));
                    return team;
                } else {
                    throw new SQLException("Team creation failed, ID not received.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    // Method to delete a team and related records from the database
    public void deleteTeam(Team team) throws SQLException {
        String sqlDeleteFromEmployeeTeam = "DELETE FROM EmployeeTeam WHERE teamid = ?";
        String sqlDeleteFromRate = "DELETE FROM Rate WHERE teamId = ?";
        String sqlDeleteTeam = "DELETE FROM Team WHERE ID = ?";

        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement pstDeleteFromEmployeeTeam = con.prepareStatement(sqlDeleteFromEmployeeTeam);
                 PreparedStatement pstDeleteFromRate = con.prepareStatement(sqlDeleteFromRate);
                 PreparedStatement pstDeleteTeam = con.prepareStatement(sqlDeleteTeam)) {


                pstDeleteFromEmployeeTeam.setInt(1, team.getId());
                pstDeleteFromEmployeeTeam.executeUpdate();


                pstDeleteFromRate.setInt(1, team.getId());
                pstDeleteFromRate.executeUpdate();


                pstDeleteTeam.setInt(1, team.getId());
                pstDeleteTeam.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
    // Method to update the team assignment of an employee
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

