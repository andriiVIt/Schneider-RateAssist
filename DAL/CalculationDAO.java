package DAL;

import BE.Calculation;
import DAL.Interface.ICalculationDAO;
import DAL.db.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculationDAO implements ICalculationDAO {

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
    public Calculation getCalculationByEmployeeId(int employeeId) throws SQLException {
        String sql = "SELECT * FROM CalculatedRates WHERE employeeId = ?";
        try (Connection con = connectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    double rate = rs.getDouble("rate");
                    return new Calculation(id, employeeId, rate);
                } else {
                    return null;
                }
            }
        }
    }
}
