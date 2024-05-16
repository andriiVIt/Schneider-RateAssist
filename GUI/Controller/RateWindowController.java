package GUI.Controller;

import BE.Country;
import BE.Rate;
import BE.Team;
import BLL.RateLogic;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.RateModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class RateWindowController implements Initializable {

    public ComboBox<Country> countryBox;
    public ComboBox<Team> teamBox;
    public ComboBox<String> rateBox;
    public AnchorPane rateWindow;
    private EmployeeModel employeeModel;
    private RateModel groupModel;
    private RateLogic rateLogic;
    private ScrollPane scrollPane;

    public RateWindowController() {
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
        teamBox.getItems().addAll(teamModel.getTeams());

        teamModel.getTeams().addListener((ListChangeListener<? super Team>) obs -> {
            teamBox.getItems().clear();
            teamBox.getItems().addAll(teamModel.getTeams());
        });
    }

    public void setRateBox() {
        rateBox.getItems().addAll(Arrays.asList("Hourly", "Daily"));
    }

    public void setModel(RateModel groupModel) {
        this.groupModel = groupModel;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
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

    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) rateWindow.getScene().getWindow();
        if (scrollPane != null) {
            BlurEffectUtil.removeBlurEffect(scrollPane);
        }
        stage.close();
    }
}