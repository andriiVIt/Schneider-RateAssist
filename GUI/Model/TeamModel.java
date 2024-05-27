package GUI.Model;

import BE.Team;
import BLL.TeamLogic;
import GUI.Exceptions.TeamCreationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class TeamModel {
    private final TeamLogic teamLogic = new TeamLogic();
    private final ObservableList<Team> teams = FXCollections.observableArrayList();

    // Constructor that initializes the team list from the database
    public TeamModel() {
        try {
            List<Team> teamList = teamLogic.getAllTeams(); // Retrieve the team list
            teams.addAll(teamList);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load teams from the database", e);
        }
    }

    // Returns the ObservableList of teams
    public ObservableList<Team> getTeams() {
        return teams;
    }

    // Retrieves all teams as a simple list
    public List<Team> getTeamsSimple() {
        try {
            return teamLogic.getAllTeams();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve teams", e);
        }
    }

    // Creates a new team and adds it to the ObservableList
    public Team createTeam(Team team) throws TeamCreationException {
        try {
            Team createdTeam = teamLogic.createTeam(team);
            teams.add(createdTeam);
            return createdTeam;
        } catch (SQLException e) {
            throw new TeamCreationException("Failed to create team", e);
        }
    }

    // Deletes a team and removes it from the ObservableList
    public void deleteTeam(Team team) throws TeamCreationException {
        try {
            teamLogic.deleteTeam(team);
            teams.remove(team);
        } catch (SQLException e) {
            throw new TeamCreationException("Failed to delete team", e);
        }
    }

    // Updates the team assignment for an employee
    public void updateTeamEmployee(int employeeId, int teamId) throws TeamCreationException {
        try {
            teamLogic.updateTeamEmployee(employeeId, teamId);
        } catch (SQLException e) {
            throw new TeamCreationException("Failed to update team assignment for employee", e);
        }
    }
}
