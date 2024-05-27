package GUI.Model;

import BE.Country;
import BLL.CountryLogic;
import GUI.Exceptions.CountryCreationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class CountryModel {

    CountryLogic countryLogic = new CountryLogic();
    private ObservableList<Country> countries = FXCollections.observableArrayList();

    // Constructor that initializes the country list from the database
    public CountryModel() {
        try {
            countries.addAll(countryLogic.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // Returns the list of countries as an ObservableList
    public ObservableList<Country> getCountries() {
        return countries;
    }

    // Creates a new country and adds it to the list
    public Country createCountry(Country country) throws CountryCreationException {
        try {
            Country createdCountry = countryLogic.createCountry(country);
            countries.add(createdCountry);
            return createdCountry;
        } catch (SQLException e) {
            throw new CountryCreationException("Failed to create country", e);
        }
    }
    // Deletes a country from the list and database
    public void deleteCountry(Country country) throws SQLException {
        CountryLogic.deleteCountry(country);
        countries.remove(country);
    }
    // Updates the country assignment for an employee
    public void updateCountryEmployee(int employeeId, int countryId) throws SQLException {
        countryLogic.updateCountryEmployee(employeeId, countryId);
    }
    // Updates the country assignment for an employee
    public List<Country> getCountriesForTeamId(int teamId) {
        return this.countryLogic.getCountriesForTeamId(teamId);
    }
}
