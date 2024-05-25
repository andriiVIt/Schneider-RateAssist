package BLL;

import BE.Team;
import DAL.Interface.ITeamDAO;
import DAL.TeamDAO;

import java.sql.SQLException;
import java.util.List;

public class TeamLogic {
    static ITeamDAO teamDAO = new TeamDAO();

    public Team createTeam(Team team) throws SQLException {
        return teamDAO.createTeam(team);
    }

    public List<Team> getAllTeams() throws SQLException {
        return teamDAO.getAllTeams(); // Переконайтесь, що цей метод повертає List<Team>
    }
    public void deleteTeam(Team team) throws SQLException {
        teamDAO.deleteTeam(team);
    }
    public void updateTeamEmployee(int employeeId, int teamId) throws SQLException {
        teamDAO.updateTeamEmployee(employeeId, teamId);
    }
}
