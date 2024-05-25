package BLL;

import BE.Calculation;
import DAL.CalculationDAO;
import DAL.Interface.ICalculationDAO;

import java.sql.SQLException;

public class CalculationLogic {

    private static ICalculationDAO calculationDAO = new CalculationDAO();

    // Method to create a new calculation
    public Calculation createCalculation(Calculation calculation) throws SQLException {
        return calculationDAO.createCalculation(calculation); // Calls createCalculation from CalculationDAO
    }
    public Calculation getCalculationByEmployeeId(int employeeId) throws SQLException {
        return calculationDAO.getCalculationByEmployeeId(employeeId);
    }
}
