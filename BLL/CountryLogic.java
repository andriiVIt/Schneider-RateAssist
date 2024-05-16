package BLL;

import BE.Country;
import DAL.CountryDAO;

import java.sql.SQLException;
import java.util.List;

public class CountryLogic {

    static CountryDAO countryDAO = new CountryDAO();

    public Country createCountry(Country country) throws SQLException {
        return countryDAO.createCountry(country);
    }

    public List<Country> getAllCountries() throws SQLException {
        return countryDAO.getAllCountries();
    }

    public static void deleteCountry(Country country) throws SQLException {
        countryDAO.deleteCountry(country);
    }
    public void updateCountryEmployee(int employeeId, int countryId) throws SQLException {
        countryDAO.updateCountryEmployee(employeeId, countryId);
    }
}
