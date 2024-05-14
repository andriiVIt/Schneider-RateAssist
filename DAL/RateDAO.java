package DAL;

import BE.Country;
import BE.Rate;
import BE.Team;
import DAL.db.ConnectionManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RateDAO {

    private static ConnectionManager connectionManager;

    public RateDAO(){

        connectionManager = new ConnectionManager();
    }

    public Rate createRate(Rate rate) throws SQLException {
        // SQL INSERT statement to insert a new rate into the database
        String sql = "INSERT INTO Rate (countryId, teamId, rate) VALUES (?, ?, ?)";

        // Get a connection from the connection manager
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the parameters for the PreparedStatement from the Rate object
            pstmt.setInt(1, rate.getCountry().getId());  // Assuming Country has an getId() method
            pstmt.setInt(2, rate.getTeam().getId());     // Assuming Team has an getId() method
            pstmt.setString(3, rate.getRate());          // Set the rate string

            pstmt.executeUpdate();
            return rate; // Return the Rate object with updated data
        } catch (SQLException e) {
            // Handle exceptions or throw them to be handled at a higher level
            e.printStackTrace();
            throw e;
        }
    }

    public List<Rate> getListRatesEmployee(int employeeId) throws SQLException {
        List<Rate> rates = new ArrayList<>();
        String sql = "SELECT r.rate, c.countryName, r.countryId, r.teamId " +
                "FROM Employee e " +
                "JOIN EmployeeTeam et ON e.id = et.employeeId " +
                "JOIN EmployeeCountry ec ON e.id = ec.employeeId " +
                "JOIN Team t ON et.teamId = t.id " +
                "JOIN Country c ON ec.countryId = c.id " +
                "JOIN Rate r ON r.teamId = t.id AND r.countryId = c.id " +
                "WHERE e.id = ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Country country = new Country();
                    country.setId(rs.getInt("countryId"));
                    country.setCountryName(rs.getString("countryName"));

                    Team team = new Team();
                    team.setId(rs.getInt("teamId"));

                    Rate rate = new Rate();
                    rate.setCountry(country);
                    rate.setTeam(team);
                    rate.setRate(rs.getString("rate"));

                    rates.add(rate);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error when fetching rates for employee: " + e.getMessage(), e);
        }
        return rates;
    }
}
