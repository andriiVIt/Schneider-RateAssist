package BLL;

import BE.Team;
import DAL.Interface.ITeamDAO;
import DAL.TeamDAO;

import java.sql.SQLException;
import java.util.List;

public class TeamLogic {
    static ITeamDAO teamDAO = new TeamDAO();
    // Method to create a new team
    public Team createTeam(Team team) throws SQLException {
        return teamDAO.createTeam(team);
    }
    // Method to retrieve all teams from the database
    public List<Team> getAllTeams() throws SQLException {
        return teamDAO.getAllTeams(); // Переконайтесь, що цей метод повертає List<Team>
    }
    // Method to delete a team
    public void deleteTeam(Team team) throws SQLException {
        teamDAO.deleteTeam(team);
    }
    // Method to update the team of an employee
    public void updateTeamEmployee(int employeeId, int teamId) throws SQLException {
        teamDAO.updateTeamEmployee(employeeId, teamId);
    }
}
