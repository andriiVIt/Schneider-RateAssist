package GUI.Model;

import BE.Country;
import BE.Team;
import BLL.CountryLogic;
import BLL.TeamLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class CountryModel {

    CountryLogic countryLogic = new CountryLogic();
    private ObservableList<Country> countries = FXCollections.observableArrayList();

    public CountryModel() {
        try {
            countries.addAll(countryLogic.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//            List<Country> countryList = countryLogic.getAllCountries(); // Отримуємо список
//            countries.addAll(countryList);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }


    public ObservableList<Country> getCountries() {
        return countries;
    }

    public Country createCountry(Country country) throws SQLException {
        Country c = countryLogic.createCountry(country);
        countries.add(c);
        return c;
    }
    public void  deleteCountry(Country country) throws SQLException {
        CountryLogic.deleteCountry(country);
    }
}
