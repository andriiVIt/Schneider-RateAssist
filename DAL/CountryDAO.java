package DAL;

import BE.Country;
import BE.Rate;
import BE.Team;
import DAL.Interface.ICountryDAO;
import DAL.db.ConnectionManager;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO implements ICountryDAO {

    private static ConnectionManager connectionManager;

    public CountryDAO() {
        connectionManager = new ConnectionManager();
    }
    // Method to retrieve all countries from the database
    public List<Country> getAllCountries() throws SQLException {
        List<Country> countries = new ArrayList<>();
        String query = "SELECT id, countryName FROM Country";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("countryName");
                Country country = new Country(id, name);
                countries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return countries;
    }
    // Method to create a new country
    public Country createCountry(Country country) throws SQLException {
        String query = "INSERT INTO Country (countryName) VALUES (?)";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, country.getCountryName());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating country failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    country.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating country failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return country;
    }
    // Method to delete rates by country ID
    public void deleteRatesByCountryId(int countryId) throws SQLException {
        String sql = "DELETE FROM Rate WHERE countryId = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, countryId);
            pst.executeUpdate();
        }
    }
    // Method to delete a country
    public void deleteCountry(Country country) throws SQLException {
        String sqlDeleteFromEmployeeCountry = "DELETE FROM EmployeeCountry WHERE countryid = ?";
        String sqlDeleteFromRate = "DELETE FROM Rate WHERE countryId = ?";
        String sqlDeleteCountry = "DELETE FROM Country WHERE ID = ?";

        try (Connection con = connectionManager.getConnection()) {
            con.setAutoCommit(false); // початок транзакції

            try (PreparedStatement pstDeleteFromEmployeeCountry = con.prepareStatement(sqlDeleteFromEmployeeCountry);
                 PreparedStatement pstDeleteFromRate = con.prepareStatement(sqlDeleteFromRate);
                 PreparedStatement pstDeleteCountry = con.prepareStatement(sqlDeleteCountry)) {

                // Видалення записів з EmployeeCountry
                pstDeleteFromEmployeeCountry.setInt(1, country.getId());
                pstDeleteFromEmployeeCountry.executeUpdate();

                // Видалення записів з Rate
                pstDeleteFromRate.setInt(1, country.getId());
                pstDeleteFromRate.executeUpdate();

                // Видалення запису з Country
                pstDeleteCountry.setInt(1, country.getId());
                pstDeleteCountry.executeUpdate();

                con.commit(); // підтвердження транзакції
            } catch (SQLException e) {
                con.rollback(); // відкат транзакції у разі помилки
                throw e;
            }
        }
    }
    // Method to update the country of an employee
    public void updateCountryEmployee(int employeeId, int countryId) throws SQLException {
        String sql = "UPDATE EmployeeCountry SET countryid = ? WHERE employeeid = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, countryId);
            pst.setInt(2, employeeId);
            pst.executeUpdate();
        }
    }
    // Method to retrieve countries for a specific team ID
    public List<Country> getCountriesForTeamId(int teamId) throws SQLException {
        List<Country> countries = new ArrayList<>();
        String query = "SELECT c.id, c.countryName FROM Rate r\n" +
                "                JOIN Country c ON c.id = r.countryId\n" +
                "                WHERE r.teamId = ?";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, teamId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("countryName");
                    Country country = new Country(id, name);
                    countries.add(country);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return countries;
    }
}
