package BLL;

import BE.Calculation;
import DAL.CalculationDAO;

import java.sql.SQLException;

public class CalculationLogic {

    private static CalculationDAO calculationDAO = new CalculationDAO(); // Initialize CalculationDAO

    // Method to create a new calculation
    public Calculation createCalculation(Calculation calculation) throws SQLException {
        return calculationDAO.createCalculation(calculation); // Calls createCalculation from CalculationDAO
    }
}
