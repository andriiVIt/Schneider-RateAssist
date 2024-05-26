package BLL;

import BE.Rate;
import DAL.Interface.IRateDAO;
import DAL.RateDAO;

import java.sql.SQLException;
import java.util.List;

public class RateLogic {

    // Create a static instance of the IRateDAO interface using RateDAO implementation
    private static IRateDAO rateDAO = new RateDAO(); // Initialize RateDAO

    // Method to create a new rate
    public Rate createRate(Rate rate) throws SQLException {
        return rateDAO.createRate(rate); // Calls createRate from RateDAO
    }

    // Method to get list of rates for an employee
    public List<Rate> getListRatesEmployee(int employeeId) throws SQLException {
        return rateDAO.getListRatesEmployee(employeeId); // Calls getListRatesEmployee from RateDAO
    }
    // Method to update an existing rate
    public void updateRate(Rate rate) throws SQLException {
        rateDAO.updateRate(rate);
    }
}