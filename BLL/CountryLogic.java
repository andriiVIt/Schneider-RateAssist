package BLL;

import BE.Country;
import BE.Employee;
import BE.Team;
import DAL.CountryDAO;
import DAL.TeamDAO;

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
    public static void  deleteCountry(Country country)throws  SQLException{

        CountryDAO.deleteCountry(country);
    }
}
