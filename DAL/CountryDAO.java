package DAL;

import BE.Country;
import BE.Employee;
import BLL.CountryLogic;
import DAL.db.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {

    private static ConnectionManager connectionManager;

    public CountryDAO(){

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
            // Handle exceptions or throw them to be handled at a higher level
            e.printStackTrace();
            throw e;
        }
        return countries;
    }

    // Method to create a new country in the database
    public Country createCountry(Country country) throws SQLException {
        String query = "INSERT INTO Country (countryName) VALUES (?)";
        // This SQL returns the generated ID
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
            // Handle exceptions or throw them to be handled at a higher level
            e.printStackTrace();
            throw e;
        }
        return country;
    }
    public static void deleteCountry(Country country) throws SQLException {
        String sql = "DELETE FROM Country WHERE ID = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, country.getId());
            pst.executeUpdate();
        }
    }
}
