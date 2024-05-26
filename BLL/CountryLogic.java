package BLL;

import BE.Country;
import DAL.CountryDAO;
import DAL.Interface.ICountryDAO;

import java.sql.SQLException;
import java.util.List;

public class CountryLogic {
    // Create a static instance of the ICountryDAO interface using CountryDAO implementation
    static ICountryDAO countryDAO = new CountryDAO();
    // Method to create a new country
    public Country createCountry(Country country) throws SQLException {
        // Calls createCountry method from CountryDAO to add a new country
        return countryDAO.createCountry(country);
    }
    // Method to retrieve all countries from the database
    public List<Country> getAllCountries() throws SQLException {
        // Calls getAllCountries method from CountryDAO to get the list of all countries
        return countryDAO.getAllCountries();
    }
    // Method to delete a country
    public static void deleteCountry(Country country) throws SQLException {
        // Calls deleteCountry method from CountryDAO to remove a country
        countryDAO.deleteCountry(country);
    }
    // Method to update the country of an employee
    public void updateCountryEmployee(int employeeId, int countryId) throws SQLException {
        // Calls updateCountryEmployee method from CountryDAO to update the country for a specific employee

        countryDAO.updateCountryEmployee(employeeId, countryId);
    }
    // Method to retrieve countries by team ID
    public List<Country> getCountriesForTeamId(int teamId) {
        // Initialize a list to hold the countries
        List<Country> l;
        try {
            // Calls getCountriesForTeamId method from CountryDAO to get countries for a specific team

            l = countryDAO.getCountriesForTeamId(teamId);
        } catch (SQLException e) {
            // If there is an SQL exception, throw a runtime exception
            throw new RuntimeException(e);
        }
        // Return the list of countries
        return l;
    }
}
