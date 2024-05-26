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

    // Constructor that initializes the team list from the database
    public TeamModel() {
        try {
            List<Team> teamList = teamLogic.getAllTeams(); // Retrieve the team list
            teams.addAll(teamList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Returns the ObservableList of teams
    public ObservableList<Team> getTeams() {
        return teams;
    }

    // Retrieves all teams as a simple list
    public List<Team> getTeamsSimple() {
        List<Team> t;
        try {
            t = teamLogic.getAllTeams();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    // Creates a new team and adds it to the ObservableList
    public Team createTeam(Team team) throws SQLException {
        Team c = teamLogic.createTeam(team);
        teams.add(c);
        return c;
    }
    // Deletes a team and removes it from the ObservableList
    public void deleteTeam(Team team) throws SQLException {
        teamLogic.deleteTeam(team);
        teams.remove(team);
    }
    // Updates the team assignment for an employee
    public void updateTeamEmployee(int employeeId, int teamId) throws SQLException {
        teamLogic.updateTeamEmployee(employeeId, teamId);
    }
}
