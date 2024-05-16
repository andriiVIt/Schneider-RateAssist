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

public class CountryDAO {

    private static ConnectionManager connectionManager;

    public CountryDAO() {
        connectionManager = new ConnectionManager();
    }

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

    public void deleteRatesByCountryId(int countryId) throws SQLException {
        String sql = "DELETE FROM Rate WHERE countryId = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, countryId);
            pst.executeUpdate();
        }
    }

    public void deleteCountry(Country country) throws SQLException {
        String sql = "DELETE FROM Country WHERE ID = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            deleteRatesByCountryId(country.getId());
            pst.setInt(1, country.getId());
            pst.executeUpdate();
        }
    }
    public void updateCountryEmployee(int employeeId, int countryId) throws SQLException {
        String sql = "UPDATE EmployeeCountry SET countryid = ? WHERE employeeid = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, countryId);
            pst.setInt(2, employeeId);
            pst.executeUpdate();
        }
    }

}
