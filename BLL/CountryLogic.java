package BLL;

import BE.Country;
import BE.Employee;
import DAL.CountryDAO;

import java.sql.SQLException;
import java.util.List;

public class CountryLogic {

    static CountryDAO countryDAO = new CountryDAO();

    public Country createCountry(Country country) throws SQLException {
        return countryDAO.createCountry(country);
    }

    public List<Country> getAllCountries() throws SQLException {
        return countryDAO.getAllCountries(); // Переконайтесь, що цей метод повертає List<Country>
    }

}
