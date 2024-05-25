package GUI.Model;

import BE.Country;
import BE.Team;
import BLL.CountryLogic;
import BLL.TeamLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class TeamModel {
    TeamLogic teamLogic = new TeamLogic();
    private ObservableList<Team> teams = FXCollections.observableArrayList();

    public TeamModel() {
        try {
            List<Team> teamList = teamLogic.getAllTeams(); // Отримуємо список
            teams.addAll(teamList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ObservableList<Team> getTeams() {
        return teams;
    }

    public List<Team> getTeamsSimple() {
        List<Team> t;
        try {
            t = teamLogic.getAllTeams();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public Team createTeam(Team team) throws SQLException {
        Team c = teamLogic.createTeam(team);
        teams.add(c);
        return c;
    }
    public void deleteTeam(Team team) throws SQLException {
        teamLogic.deleteTeam(team);
        teams.remove(team);
    }
    public void updateTeamEmployee(int employeeId, int teamId) throws SQLException {
        teamLogic.updateTeamEmployee(employeeId, teamId);
    }
}
