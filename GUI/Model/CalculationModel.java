package GUI.Model;

import BE.Calculation;
import BLL.CalculationLogic;

import java.sql.SQLException;

public class CalculationModel {
    private CalculationLogic calculationLogic = new CalculationLogic();

    public Calculation createCalculation(Calculation calculation) throws SQLException {
        return calculationLogic.createCalculation(calculation);
    }
}
