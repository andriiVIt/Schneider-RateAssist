package GUI.Model;

import BE.Country;
import BLL.CountryLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CountryModel {

    CountryLogic countryLogic = new CountryLogic();
    private ObservableList<Country> countries = FXCollections.observableArrayList();

    public CountryModel() {
        try {
            countries.addAll(countryLogic.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Country> getCountries() {
        return countries;
    }

    public Country createCountry(Country country) throws SQLException {
        Country c = countryLogic.createCountry(country);
        countries.add(c);
        return c;
    }

    public void deleteCountry(Country country) throws SQLException {
        CountryLogic.deleteCountry(country);
        countries.remove(country);
    }
    public void updateCountryEmployee(int employeeId, int countryId) throws SQLException {
        countryLogic.updateCountryEmployee(employeeId, countryId);
    }
}
