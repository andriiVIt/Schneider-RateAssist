package GUI.Controller;

import BE.Country;
import BE.Employee;
import BE.Team;
import BLL.CountryLogic;
import BLL.RateLogic;
import BLL.TeamLogic;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.RateModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class AdminController implements Initializable {
    @FXML
    private CheckComboBox <Country> countryComboBox;
    @FXML
    private CheckComboBox <Team> teamComboBox;
    @FXML
    private MFXButton logOutButton;
    @FXML
    private MFXButton SearchButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;

    private CountryLogic countryLogic = new CountryLogic();
    private TeamLogic teamLogic = new TeamLogic();
    List<Country> selectedCountries = null;
    List<Team> selectedTeams = null;

    private int currentPage, totalPages;
    public void previousPage(ActionEvent actionEvent) {
        if (currentPage > 0) {
            currentPage--;
            refreshEmployeeCards();
        }
    }

    public void nextPage(ActionEvent actionEvent) {
        if (currentPage < totalPages - 1) {
            currentPage++;
            refreshEmployeeCards();
        }
    }

    void refreshEmployeeCards() {
        try {
            gridPane.getChildren().clear(); // Clear existing content
            populateGridPane();
        } catch (IOException e) {
            // Better error handling should be considered
        }

    }

    public List<Integer> mapCountriesToIds(List<Country> selectedCountries) {
        List<Integer> listIds = selectedCountries.stream()
                .map(Country::getId)  // Using method reference to get country IDs
                .collect(Collectors.toList()); // Collecting the results into a List
        return listIds;
    }
    public List<Integer> mapTeamsToIds(List<Team> selectedTeams) {
        return selectedTeams.stream()
                .map(Team::getId)  // Using method reference to get team IDs
                .collect(Collectors.toList()); // Collecting the results into a List
    }
    void populateGridPane() throws IOException {
        EmployeeModel employeeModel = new EmployeeModel();
        List<Employee> employees;

        try {
            List<Integer> countryIds = selectedCountries != null ? mapCountriesToIds(selectedCountries) : null;
            List<Integer> teamIds = selectedTeams != null ? mapTeamsToIds(selectedTeams) : null;

            if (countryIds != null || teamIds != null) {
                employees = employeeModel.getEmployeesByListIds(countryIds, teamIds);
                selectedCountries = null;
                selectedTeams = null;
            } else {
                employees = employeeModel.getEmployees();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employees from the database.", e);
        }
        int numRows = 4;
        int numColumns = 2;
        int employeesPerPage = numRows * numColumns;
        totalPages = (int) Math.ceil((double) employees.size() / employeesPerPage);

        int startEmployeeIndex = currentPage * employeesPerPage;
        int endEmployeeIndex = Math.min(startEmployeeIndex + employeesPerPage, employees.size());

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                int employeeIndex = startEmployeeIndex + row * numColumns + col;
                if (employeeIndex >= endEmployeeIndex) {
                    break;
                }
                Pane pane = new Pane();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/EmployeeCardWindow.fxml"));

                List<Employee> finalEmployees = employees;
                fxmlLoader.setControllerFactory(clazz -> {
                    EmployeeCardController controller = new EmployeeCardController(scrollPane, finalEmployees.get(employeeIndex), employeeModel, this);
                    controller.setOnDeleteEmployeeCallback(deletedEmployee -> refreshEmployeeCards());
                    return controller;
                });
                try {
                    Pane contentPane = fxmlLoader.load();
                    pane.getChildren().add(contentPane);
                    gridPane.add(pane, col, row);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Failed to load EmployeeCardWindow.fxml for employee: " + finalEmployees.get(employeeIndex).getName());
                }
            }
        }
    }


    public void createEmployee(ActionEvent actionEvent) {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10); // Apply a blur effect to the scroll pane

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/CreateEmployeeWindow.fxml"));
            Parent createEmployeeParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Set the window modality
            stage.setTitle("Add employee");
            stage.setResizable(false); // Make the window not resizable
            stage.setScene(new Scene(createEmployeeParent));

            CreateEmployeeController createEmployeeController = fxmlLoader.getController();
            createEmployeeController.setEmployeeModel(new EmployeeModel());
            createEmployeeController.setCountryModel(new CountryModel());
            createEmployeeController.setTeamModel(new TeamModel());
            createEmployeeController.setRefreshCallback(this::refreshEmployeeCards);
            createEmployeeController.setScrollPane(scrollPane);
            createEmployeeController.setOnCloseRequestHandler(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    public void clickLogOut(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/LoginWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
            ((Stage) logOutButton.getScene().getWindow()).close(); // Close the current window

        } catch (IOException e) {
            throw new RuntimeException(e);  // Consider better error handling
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentPage = 0;  // Initialization correction, assuming paging starts at index 0
//        countryComboBox();
        try {
            populateGridPane();
            countryComboBox();
            TeamComboBox();
        } catch (IOException e) {
            // Better error handling should be considered
        }

    }





    public void groupWindowButton(ActionEvent actionEvent) {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/RateWindow.fxml"));
            Parent rateWindowParent = fxmlLoader.load();
            RateWindowController rateWindowController = fxmlLoader.getController();
            rateWindowController.setScrollPane(scrollPane); // Передаємо ScrollPane
            rateWindowController.setCountryModel(new CountryModel());
            rateWindowController.setTeamModel(new TeamModel());
            rateWindowController.setRateModel(new RateLogic());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Rate Window");
            stage.setScene(new Scene(rateWindowParent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }
    private void countryComboBox() {
        try {
            List<Country> listCountries = countryLogic.getAllCountries();
            countryComboBox.getItems().addAll(listCountries);
        } catch (SQLException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }
    private void TeamComboBox() {
        try {
            List<Team> listTeams = teamLogic.getAllTeams();
            teamComboBox.getItems().addAll(listTeams);
        } catch (SQLException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }
    public void SearchButton(ActionEvent actionEvent) {
        List<Country> listSelectedCountries = new ArrayList<>();
        for (int i = 0; i < countryComboBox.getItems().size(); i++) {
            if (countryComboBox.getItemBooleanProperty(i).getValue()) {
                listSelectedCountries.add(countryComboBox.getItems().get(i));
            }
        }

        List<Team> listSelectedTeams = new ArrayList<>();
        for (int i = 0; i < teamComboBox.getItems().size(); i++) {
            if (teamComboBox.getItemBooleanProperty(i).getValue()) {
                listSelectedTeams.add(teamComboBox.getItems().get(i));
            }
        }

        selectedCountries = listSelectedCountries.isEmpty() ? null : listSelectedCountries;
        selectedTeams = listSelectedTeams.isEmpty() ? null : listSelectedTeams;

        this.refreshEmployeeCards();
    }
}



