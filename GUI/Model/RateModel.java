package GUI.Model;

import BE.Rate;
import BLL.RateLogic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class RateModel {
    private RateLogic rateLogic;
    private ObservableList<Rate> rates;

    // Constructor that initializes RateLogic and the ObservableList for rates
    public RateModel() {
        rateLogic = new RateLogic();
        rates = FXCollections.observableArrayList();
    }

    // Returns the ObservableList of rates
    public ObservableList<Rate> getRates() {
        return rates;
    }

    // Creates a new rate and adds it to the ObservableList
    public Rate createRate(Rate rate) throws SQLException {
        Rate newRate = rateLogic.createRate(rate);
        rates.add(newRate);
        return newRate;
    }

    // Updates an existing rate in the ObservableList
    public void updateRate(Rate rate) throws SQLException {
        rateLogic.updateRate(rate);
        int index = rates.indexOf(rate);
        if (index != -1) {
            rates.set(index, rate);
        }
    }

    // Retrieves the list of rates for a specific employee and updates the ObservableList
    public List<Rate> getListRatesEmployee(int employeeId) throws SQLException {
        List<Rate> rates = rateLogic.getListRatesEmployee(employeeId);
        this.rates.setAll(rates); // Updates the ObservableList
        return rates;
    }
}
