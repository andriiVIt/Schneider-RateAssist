package DAL.Interface;

import BE.Calculation;
import java.sql.SQLException;

public interface ICalculationDAO {
    Calculation createCalculation(Calculation calculation) throws SQLException;
    Calculation getCalculationByEmployeeId(int employeeId) throws SQLException;
}