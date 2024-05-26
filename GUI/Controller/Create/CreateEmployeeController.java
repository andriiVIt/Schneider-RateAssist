package GUI.Controller.Create;

import BE.Country;
import BE.Employee;
import BE.Team;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import GUI.util.Message;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateEmployeeController implements Initializable {
    @FXML
    private MFXTextField loginNameField;
    @FXML
    private MFXPasswordField passwordField;
    @FXML
    private MFXTextField fixedAmountField;
    @FXML
    private MFXTextField salaryField;
    @FXML
    private MFXTextField overheadField;
    @FXML
    private MFXTextField workHoursField;
    @FXML
    private MFXTextField utilizationField;
    @FXML
    private MFXTextField resourceTypeField;
    @FXML
    private Label namePhoto;
    @FXML
    private MFXTextField nameField;
    @FXML
    private AnchorPane createEmployeeAnchorPane;
    @FXML
    private CheckComboBox<Country> locationBox;
    @FXML
    private CheckComboBox<Team> teamBox;

    // instance variables
    private TeamModel teamModel;
    private CountryModel countryModel;
    private EmployeeModel employeeModel;
    private byte[] imageData;
    private ScrollPane scrollPane;
    private Runnable refreshCallback;

    // Sets the CountryModel instance and updates the location box
    public void setCountryModel(CountryModel countryModel) {
        this.countryModel = countryModel;
        locationBox.setTitle("Country");
        locationBox.getItems().addAll(countryModel.getCountries());

        countryModel.getCountries().addListener((ListChangeListener<? super Country>) obs -> {
            locationBox.getItems().clear();
            locationBox.getItems().addAll(countryModel.getCountries());
        });
    }

    // Sets the TeamModel instance and updates the team box
    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
        teamBox.setTitle("Team");
        teamBox.getItems().addAll(teamModel.getTeams());

        teamModel.getTeams().addListener((ListChangeListener<? super Team>) obs -> {
            teamBox.getItems().clear();
            teamBox.getItems().addAll(teamModel.getTeams());
        });
    }
    // Handles the action of selecting a photo file
    public void selectPhotoButton(ActionEvent actionEvent) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Images File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imageData = readBytesFromFile(selectedFile);
            namePhoto.setText(selectedFile.getName());
        } else {
            namePhoto.setText("No file selected");
        }
    }

    // Creates a new employee with the provided information
    public void createEmployee(ActionEvent actionEvent) {
        String name = nameField.getText();
        String loginName = loginNameField.getText();
        String password = passwordField.getText();
        double salary = Double.parseDouble(salaryField.getText());
        double fixedAmount = Double.parseDouble(fixedAmountField.getText());
        double overhead = Double.parseDouble(overheadField.getText());
        double workHours = Double.parseDouble(workHoursField.getText());
        double utilization = Double.parseDouble(utilizationField.getText());
        String resourceType = resourceTypeField.getText();

        try {
            Employee newEmployee = new Employee(name, salary, overhead, workHours, utilization, resourceType, fixedAmount, imageData, loginName, password);
            employeeModel.createEmployee(newEmployee);

            // Assign countries and teams
            List<Country> selectedCountries = locationBox.getCheckModel().getCheckedItems();
            for (Country country : selectedCountries) {
                employeeModel.assignCountryEmployee(country, newEmployee);
            }

            List<Team> selectedTeams = teamBox.getCheckModel().getCheckedItems();
            for (Team team : selectedTeams) {
                employeeModel.assignTeamEmployee(team, newEmployee);
            }

            // Refresh and close window
            if (refreshCallback != null) {
                refreshCallback.run();
            }
            Stage stage = (Stage) createEmployeeAnchorPane.getScene().getWindow();
            BlurEffectUtil.removeBlurEffect(scrollPane);
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Message.showAlert("Error", "Failed to create the employee in the database.", Alert.AlertType.ERROR);
        }
    }

    // Cancels the action and closes the window
    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createEmployeeAnchorPane.getScene().getWindow();
        BlurEffectUtil.removeBlurEffect(scrollPane);
        stage.close();
    }

    // Sets the EmployeeModel instance
    public void setEmployeeModel(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    // Sets the refresh callback function
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    // Sets the scroll pane for blur effect handling
    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    // Sets the onClose request handler for the stage
    public void setOnCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event -> BlurEffectUtil.removeBlurEffect(scrollPane));
    }
    // Reads bytes from the selected file
    public static byte[] readBytesFromFile(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        is.close();
        bos.close();
        return bos.toByteArray();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // Opens the window to create a new country
    public void createCountry(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/CreateCountryWindow.fxml"));
            Parent createCountryParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Create Country");
            stage.setScene(new Scene(createCountryParent));

            CreateCountryController createCountryController = fxmlLoader.getController();
            createCountryController.setModel(new CountryModel());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Opens the window to create a new team
    public void createTeam(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/CreateTeamWindow.fxml"));
            Parent createTeamParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Create Team");
            stage.setScene(new Scene(createTeamParent));

            CreateTeamController createTeamController = fxmlLoader.getController();
            createTeamController.setModel(new TeamModel());
            createTeamController.setScrollPane(scrollPane);
            createTeamController.setOnCloseRequestHandler(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
