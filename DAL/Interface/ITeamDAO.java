package DAL.Interface;

import BE.Team;
import java.sql.SQLException;
import java.util.List;

public interface ITeamDAO {
    List<Team> getAllTeams() throws SQLException;
    Team createTeam(Team team) throws SQLException;
    void deleteTeam(Team team) throws SQLException;
    void updateTeamEmployee(int employeeId, int teamId) throws SQLException;
}