package DAL;

import BE.Calculation;
import DAL.Interface.ICalculationDAO;
import DAL.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculationDAO implements ICalculationDAO {

    // Static instance of ConnectionManager to manage database connections
    private static ConnectionManager connectionManager;

    // Constructor to initialize ConnectionManager
    public CalculationDAO() {
        connectionManager = new ConnectionManager();
    }

    // Method to create a new calculation record in the database
    public Calculation createCalculation(Calculation calculation) throws SQLException {
        // SQL query to insert a new record into CalculatedRates table
        String sql = "INSERT INTO CalculatedRates (employeeId, rate) VALUES (?, ?)";

        // Try-with-resources statement to ensure resources are closed after usage
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            pstmt.setInt(1, calculation.getEmployeeId());
            pstmt.setDouble(2, calculation.getRate());

            // Execute the update to insert the record
            pstmt.executeUpdate();
            // Return the calculation object
            return calculation;
        } catch (SQLException e) {
            // Print stack trace and rethrow the exception
            e.printStackTrace();
            throw e;
        }
    }

    // Method to retrieve a calculation by employee ID
    public Calculation getCalculationByEmployeeId(int employeeId) throws SQLException {
        // SQL query to select a record from CalculatedRates table by employee ID
        String sql = "SELECT * FROM CalculatedRates WHERE employeeId = ?";

        // Try-with-resources statement to ensure resources are closed after usage
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            // Set parameter for the prepared statement
            pstmt.setInt(1, employeeId);

            // Execute the query and process the result set
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve values from the result set
                    int id = rs.getInt("id");
                    double rate = rs.getDouble("rate");
                    // Return a new Calculation object
                    return new Calculation(id, employeeId, rate);
                } else {
                    // Return null if no record is found
                    return null;
                }
            }
        }
    }
}
