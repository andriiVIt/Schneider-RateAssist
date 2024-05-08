package BLL;

import BE.Team;

import DAL.TeamDAO;

import java.sql.SQLException;
import java.util.List;

public class TeamLogic {
    static TeamDAO teamDAO = new TeamDAO();

    public int createTeam(Team team) throws SQLException {
        return teamDAO.createTeam(String.valueOf(team));
    }

    public List<Team> getAllTeams() throws SQLException {
        return teamDAO.getAllTeams();
    }
}
