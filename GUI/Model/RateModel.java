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

    public RateModel() {
        rateLogic = new RateLogic();
        rates = FXCollections.observableArrayList();
    }

    public ObservableList<Rate> getRates() {
        return rates;
    }

    public Rate createRate(Rate rate) throws SQLException {
        Rate newRate = rateLogic.createRate(rate);
        rates.add(newRate);
        return newRate;
    }

    public void updateRate(Rate rate) throws SQLException {
        rateLogic.updateRate(rate);
        int index = rates.indexOf(rate);
        if (index != -1) {
            rates.set(index, rate);
        }
    }

    public List<Rate> getListRatesEmployee(int employeeId) throws SQLException {
        List<Rate> rates = rateLogic.getListRatesEmployee(employeeId);
        this.rates.setAll(rates); // Оновлюємо ObservableList
        return rates;
    }
}
