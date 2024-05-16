package BLL;

import BE.Team;
import DAL.TeamDAO;

import java.sql.SQLException;
import java.util.List;

public class TeamLogic {
    static TeamDAO teamDAO = new TeamDAO();

    public Team createTeam(Team team) throws SQLException {
        return teamDAO.createTeam(team);
    }

    public List<Team> getAllTeams() throws SQLException {
        return teamDAO.getAllTeams(); // Переконайтесь, що цей метод повертає List<Team>
    }
    public static void  deleteTeam(Team team)throws  SQLException{

        TeamDAO.deleteTeam(team);
    }
    public void updateTeamEmployee(int employeeId, int teamId) throws SQLException {
        teamDAO.updateTeamEmployee(employeeId, teamId);
    }
}
