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

    TeamModel tM;
    private MainWindowController mainWindowController;
    private CountryModel countryModel;
    private EmployeeModel employeeModel;
    private final CalculationModel calcModel = new CalculationModel();

    private Integer selectedTeam;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void populateListTeams() {

        List<Team> finalEmployees = tM.getTeamsSimple();

        System.out.println();
        for (Team team : finalEmployees) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/TeamCard.fxml"));


            fxmlLoader.setControllerFactory(clazz -> {
                TeamCardController t = new TeamCardController(team, this);
                return t;
            });
            try {
                Pane contentPane = fxmlLoader.load();
                this.teamBox.getChildren().add(contentPane);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load EmployeeCardWindow.fxml for employee: " + team.getTeamName());
            }
        }
    }



    public void setTeamModel(TeamModel teamModel) {
        System.out.println();
        this.tM = teamModel;
        this.populateListTeams();
    }

    public void setCountryModel(CountryModel countryModel) {
        this.countryModel = countryModel;
    }
    public void setEmployeeModel(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    public void setOnCloseRequestHandler(Stage stage) {
    }

    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void selectTeam(int teamId) {
        selectedTeam = teamId;
        this.countryBox.getItems().clear();
        List<Country> countryList = this.countryModel.getCountriesForTeamId(teamId);

        // Create a ComboBox for the countries


        // Add the countries to the ComboBox
        for (Country country : countryList) {
            countryBox.getItems().add(country);
        }

        // Optionally, set the first item as the selected item
//        if (!countryList.isEmpty()) {
//            countryBox.setValue(countryList.get(0).getCountryName());
//        }

        // Add the ComboBox to your UI
        // Assuming you have a layout to add the ComboBox to, for example, a VBox
//        VBox layout = new VBox();
//        layout.getChildren().add(countryBox);

        // Print the list of countries (for debugging purposes)
        System.out.println(countryList);
    }

    public void selectedCountry(ActionEvent actionEvent) throws SQLException {
        // Check if countryBox and its selection model are not null
        if (this.countryBox != null && this.countryBox.getSelectionModel() != null) {
            Country c = this.countryBox.getSelectionModel().getSelectedItem();

            // Check if the selected country is not null
            if (c != null) {
                List<Integer> countrieIds = new ArrayList<>();
                countrieIds.add(c.getId());

                // Check if selectedTeam is not null
                if (selectedTeam != null) {
                    List<Integer> teamIds = new ArrayList<>();
                    teamIds.add(selectedTeam);

                    // Check if employeeModel is not null
                    if (employeeModel != null) {
                        List<Employee> regularList = new ArrayList<>(employeeModel.getEmployeesByListIds(countrieIds, teamIds));

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

    private void setRates(List<Employee> listEemployees) throws SQLException {

        ArrayList<Double> rates = new ArrayList<>();

        String rateForTeam = findGroupRate(listEemployees);

        for (Employee employee : listEemployees) {
            Calculation calc = calcModel.getCalculationByEmployeeId(employee.getId());
            if (calc!= null){
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

    public String findGroupRate(List<Employee> employees) throws SQLException {
        RateLogic rateLogic = new RateLogic();

        for (Employee employee : employees) {
            List<Rate> employeeRates = rateLogic.getListRatesEmployee(employee.getId());

            if (employeeRates != null && !employeeRates.isEmpty()) {
                Rate singleRate = employeeRates.get(0);
                return singleRate.getRate();
            }
        }

        // Handle the case where no rates are found
        return null;
    }



    public void setEmpoyees(List<Employee> employees) {
        System.out.println("Setting employees" + " " + employees.toArray().length);

        // Clear the existing children in the employeeBox
        this.employeeBox.getChildren().clear();
        for (Employee employee : employees) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/EmployeeCardWindow.fxml"));
            fxmlLoader.setControllerFactory(clazz -> {
                EmployeeCardController controller = new EmployeeCardController(scrollPane, employee, employeeModel, mainWindowController);
//                controller.setOnDeleteEmployeeCallback(deletedEmployee -> refreshEmployeeCards());
                return controller;
            });
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
