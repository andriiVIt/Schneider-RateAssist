package GUI.Model;

import BE.Calculation;
import BLL.CalculationLogic;

import java.sql.SQLException;

public class CalculationModel {
    private CalculationLogic calculationLogic = new CalculationLogic();
    // Creates a new calculation
    public Calculation createCalculation(Calculation calculation) throws SQLException {
        return calculationLogic.createCalculation(calculation);
    }
    // Retrieves the calculation for a specific employee by their ID
    public Calculation getCalculationByEmployeeId(int employeeId) throws SQLException {
        return calculationLogic.getCalculationByEmployeeId(employeeId);
    }
}
