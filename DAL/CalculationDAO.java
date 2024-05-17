package DAL;

import BE.Calculation;
import DAL.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalculationDAO {

    private static ConnectionManager connectionManager;

    public CalculationDAO() {
        connectionManager = new ConnectionManager();
    }

    public Calculation createCalculation(Calculation calculation) throws SQLException {
        String sql = "INSERT INTO CalculatedRates (employeeId, rate) VALUES (?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, calculation.getEmployeeId());
            pstmt.setDouble(2, calculation.getRate());

            pstmt.executeUpdate();
            return calculation;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
