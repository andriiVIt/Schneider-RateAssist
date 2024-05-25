package DAL.Interface;

import BE.Rate;
import java.sql.SQLException;
import java.util.List;

public interface IRateDAO {
    Rate createRate(Rate rate) throws SQLException;
    List<Rate> getListRatesEmployee(int employeeId) throws SQLException;
    void updateRate(Rate rate) throws SQLException;
}