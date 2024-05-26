package BLL;

import BE.Calculation;
import DAL.CalculationDAO;
import DAL.Interface.ICalculationDAO;

import java.sql.SQLException;

public class CalculationLogic {
    // Create a static instance of the ICalculationDAO interface using CalculationDAO implementation
    private static ICalculationDAO calculationDAO = new CalculationDAO();

    // Method to create a new calculation
    public Calculation createCalculation(Calculation calculation) throws SQLException {

        return calculationDAO.createCalculation(calculation); // Calls createCalculation method from CalculationDAO to add a new calculation
    }
    public Calculation getCalculationByEmployeeId(int employeeId) throws SQLException {
        return calculationDAO.getCalculationByEmployeeId(employeeId);// Calls getCalculationByEmployeeId method from CalculationDAO to get the calculation for a specific employee
    }
}
