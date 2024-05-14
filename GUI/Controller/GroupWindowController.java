package GUI.Controller;

import BE.Country;
import BE.Rate;
import BE.Team;
import BLL.RateLogic;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.RateModel;
import GUI.Model.TeamModel;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;


public class GroupWindowController implements Initializable {


    public ComboBox<Country> countryBox;
    public ComboBox<Team> teamBox;
    public ComboBox<String> rateBox;
    private EmployeeModel employeeModel;
    private RateModel groupModel;
    private RateLogic rateLogic;

    public GroupWindowController() {

    }

    public void setRateModel(RateLogic rateLogic) {
        this.rateLogic = rateLogic;
    }
    public void setCountryModel(CountryModel countryModel) {
        countryBox.getItems().addAll(countryModel.getCountries());

        countryModel.getCountries().addListener((ListChangeListener<? super Country>) obs -> {
            countryBox.getItems().clear();
            countryBox.getItems().addAll(countryModel.getCountries());
        });
        setRateBox();
    }

    public void setTeamModel(TeamModel teamModel) {
//        teamBox.setTitle("Team");
        teamBox.getItems().addAll(teamModel.getTeams());

        teamModel.getTeams().addListener((ListChangeListener<? super Team>) obs -> {
            teamBox.getItems().clear();
            teamBox.getItems().addAll(teamModel.getTeams());
        });
    }

    public void setRateBox(){
        rateBox.getItems().addAll(Arrays.asList("Hourly", "Daily"));
    }
    public void setModel(RateModel groupModel) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void saveRate(ActionEvent actionEvent) throws SQLException {
        Country c = countryBox.getSelectionModel().getSelectedItem();
        Team t = teamBox.getSelectionModel().getSelectedItem();
        String rate = rateBox.getSelectionModel().getSelectedItem();

        Rate rateModel = new Rate(c, t, rate);

       rateLogic.createRate(rateModel);

        rateLogic.getListRatesEmployee(48);
        System.out.println("Hello hyi");
    }
}
