package GUI.Controller;

import BE.*;
import BLL.RateLogic;
import GUI.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TeamsViewController implements Initializable {
    public VBox teamBox;
    public ComboBox<Country> countryBox;
    public VBox employeeBox;
    public Label dailyRate;
    public Label hourlyRate;

    private ScrollPane scrollPane;

    private TeamModel tM;
    private MainWindowController mainWindowController;
    private CountryModel countryModel;
    private EmployeeModel employeeModel;
    private final CalculationModel calcModel = new CalculationModel();

    private Integer selectedTeam;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // Populates the team list in the teamBox
    public void populateListTeams() {
        List<Team> finalEmployees = tM.getTeamsSimple();

        for (Team team : finalEmployees) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/TeamCard.fxml"));

            fxmlLoader.setControllerFactory(clazz -> new TeamCardController(team, this));
            try {
                Pane contentPane = fxmlLoader.load();

                // Adds an event handler to change color on selection
                contentPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    handleTeamClick(contentPane);
                    selectTeam(team.getId());
                });

                this.teamBox.getChildren().add(contentPane);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load TeamCard.fxml for team: " + team.getTeamName());
            }
        }
    }

    // Handles the click event on a team pane to highlight the selected team
    private void handleTeamClick(Pane teamPane) {
        // Resets color for all panes
        for (javafx.scene.Node node : teamBox.getChildren()) {
            if (node instanceof Pane) {
                node.setStyle(""); // Set default style or color
            }
        }
        // Sets new color for the clicked pane
        teamPane.setStyle("-fx-background-color: lightblue;");
    }

    // Sets the TeamModel instance and populates the team list
    public void setTeamModel(TeamModel teamModel) {
        this.tM = teamModel;
        this.populateListTeams();
    }

    // Sets the CountryModel instance
    public void setCountryModel(CountryModel countryModel) {
        this.countryModel = countryModel;
    }

    // Sets the EmployeeModel instance
    public void setEmployeeModel(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    // Sets the onCloseRequest handler for the stage
    public void setOnCloseRequestHandler(Stage stage) {
    }

    // Sets the MainWindowController instance
    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    // Selects a team and populates the country combo box with countries associated with the team
    public void selectTeam(int teamId) {
        selectedTeam = teamId;
        this.countryBox.getItems().clear();
        List<Country> countryList = this.countryModel.getCountriesForTeamId(teamId);

        for (Country country : countryList) {
            countryBox.getItems().add(country);
        }

        System.out.println(countryList);
    }

    // Handles the selection of a country and updates the employee list and rates
    public void selectedCountry(ActionEvent actionEvent) throws SQLException {
        if (this.countryBox != null && this.countryBox.getSelectionModel() != null) {
            Country c = this.countryBox.getSelectionModel().getSelectedItem();

            if (c != null) {
                List<Integer> countryIds = new ArrayList<>();
                countryIds.add(c.getId());

                if (selectedTeam != null) {
                    List<Integer> teamIds = new ArrayList<>();
                    teamIds.add(selectedTeam);

                    if (employeeModel != null) {
                        List<Employee> regularList = new ArrayList<>(employeeModel.getEmployeesByListIds(countryIds, teamIds));

                        this.setEmpoyees(regularList);
                        this.setRates(regularList);
                    } else {
                        System.err.println("employeeModel is null");
                    }
                } else {
                    System.err.println("selectedTeam is null");
                }
            } else {
                System.err.println("Selected country is null");
            }
        } else {
            System.err.println("countryBox or its selection model is null");
        }
    }

    // Sets the rates for the selected employees
    private void setRates(List<Employee> listEmployees) throws SQLException {
        ArrayList<Double> rates = new ArrayList<>();

        String rateForTeam = findGroupRate(listEmployees);

        for (Employee employee : listEmployees) {
            Calculation calc = calcModel.getCalculationByEmployeeId(employee.getId());
            if (calc != null) {
                rates.add(calc.getRate());
            } else {
                System.err.println("calcModel is null for id: " + employee.getId());
            }
        }

        double combinedRate = 0;

        for (Double rate : rates) {
            combinedRate += rate;
        }

        rateForTeam = rateForTeam + " " + combinedRate;

        this.hourlyRate.setText(rateForTeam);
    }

    // Finds the group rate for the selected employees
    public String findGroupRate(List<Employee> employees) throws SQLException {
        RateLogic rateLogic = new RateLogic();

        for (Employee employee : employees) {
            List<Rate> employeeRates = rateLogic.getListRatesEmployee(employee.getId());

            if (employeeRates != null && !employeeRates.isEmpty()) {
                Rate singleRate = employeeRates.get(0);
                return singleRate.getRate();
            }
        }

        return null;
    }

    // Sets the employees in the employeeBox
    public void setEmpoyees(List<Employee> employees) {
        System.out.println("Setting employees" + " " + employees.toArray().length);

        this.employeeBox.getChildren().clear();
        for (Employee employee : employees) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/EmployeeCardWindow.fxml"));
            fxmlLoader.setControllerFactory(clazz -> new EmployeeCardController(scrollPane, employee, employeeModel, mainWindowController));
            try {
                Pane contentPane = fxmlLoader.load();
                this.employeeBox.getChildren().add(contentPane);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load EmployeeCardWindow.fxml for employee: " + employee.getName());
            }
        }
    }
}
