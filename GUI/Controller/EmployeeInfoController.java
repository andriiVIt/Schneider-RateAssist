package GUI.Controller;

import BE.Country;
import BE.Employee;
import BE.Team;
import BE.Rate;
import BLL.RateLogic;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EmployeeInfoController implements Initializable {
    @FXML
    private Label rateTitle;
    @FXML
    private MFXTextField changeNameField;
    @FXML
    private MFXTextField changeSalaryField;
    @FXML
    private MFXTextField changeOverheadField;
    @FXML
    private MFXTextField changeFixedAnnualAmountField;
    @FXML
    private MFXTextField changeAnEffectWorkHoursField;
    @FXML
    private MFXTextField changeUtilizationPercentageField;
    @FXML
    private MFXTextField changeResourceTypeField;
    @FXML
    private CheckComboBox countryBox;
    @FXML
    private CheckComboBox teamBox;
    @FXML
    private MFXButton saveInformation;
    @FXML
    private ImageView workerImage;
    @FXML
    private Label nameField;
    @FXML
    private AnchorPane employeeInfoAnchorPane;
    @FXML
    private Label annualSalaryField;
    @FXML
    private Label overheadMultiplierField;
    @FXML
    private Label configurableFixedAnnualAmountField;
    @FXML
    private Label annualEffectiveWorkingHoursField;
    @FXML
    private Label utilizationPercentageField;

    private CountryModel countryModel;
    private Country country;
    private Team team;
    private TeamModel teamModel;
    private Employee employee;
    private Consumer<Employee> onDeleteEmployeeCallback;
    private ScrollPane scrollPane;
    private EmployeeModel employeeModel;
    private RateLogic rateLogic = new RateLogic(); // Initialize RateLogic

    public void setModel(EmployeeModel employeeModel, ScrollPane scrollPane, TeamModel teamModel, CountryModel countryModel) {
        this.scrollPane = scrollPane;
        this.employeeModel = employeeModel;
        this.teamModel = teamModel;
        this.countryModel = countryModel;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (nameField != null) {
            nameField.setText(employee.getName());
        }
        if (annualSalaryField != null) {
            annualSalaryField.setText(String.valueOf(employee.getSalary()));
        }
        if (overheadMultiplierField != null) {
            overheadMultiplierField.setText(String.valueOf(employee.getOverheadPercentage()));
        }
        if (configurableFixedAnnualAmountField != null) {
            configurableFixedAnnualAmountField.setText(String.valueOf(employee.getFixedAmount()));
        }
        if (annualEffectiveWorkingHoursField != null) {
            annualEffectiveWorkingHoursField.setText(String.valueOf(employee.getWorkHours()));
        }
        if (utilizationPercentageField != null) {
            utilizationPercentageField.setText(String.valueOf(employee.getUtilization()));
        }
        if (workerImage != null && employee.getImageData() != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(employee.getImageData());
            Image image = new Image(inputStream);
            workerImage.setImage(image);
        }
        if (rateTitle != null) {
            try {
                List<Rate> rates = rateLogic.getListRatesEmployee(employee.getId());
                if (rates != null && !rates.isEmpty()) {
                    StringBuilder ratesText = new StringBuilder("Rates: ");
                    for (Rate rate : rates) {
                        ratesText.append(rate.getRate()).append(", ");
                    }
                    // Remove the trailing comma and space
                    if (ratesText.length() > 7) {
                        ratesText.setLength(ratesText.length() - 2);
                    }
                    rateTitle.setText(ratesText.toString());
                } else {
                    rateTitle.setText("Rates: No rates available.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                rateTitle.setText("Failed to load rates.");
            }
        }
    }

    public void setOnDeleteEmployeeCallback(Consumer<Employee> onDeleteEmployeeCallback) {
        this.onDeleteEmployeeCallback = onDeleteEmployeeCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void Cancel(ActionEvent actionEvent) {
        BlurEffectUtil.removeBlurEffect(scrollPane);
        Stage stage = (Stage) employeeInfoAnchorPane.getScene().getWindow();
        stage.close();
    }

    public void saveNewInformation(ActionEvent actionEvent) {
        // Implement save logic here
    }

    public void AddCountry(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/CreateCountryWindow.fxml"));
            Parent createCountryParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Set window modality
            stage.setResizable(false); // Window is not resizable
            stage.setTitle("Create Country");
            stage.setScene(new Scene(createCountryParent));

            CreateCountryController createCountryController = fxmlLoader.getController();
            createCountryController.setModel(new CountryModel());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    public void AddTeam(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/CreateTeamWindow.fxml"));
            Parent createCountryParent = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Set window modality
            stage.setResizable(false); // Window is not resizable
            stage.setTitle("Create Team");
            stage.setScene(new Scene(createCountryParent));

            CreateTeamController createTeamController = fxmlLoader.getController();
            createTeamController.setModel(new TeamModel());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    public void deleteCountry(ActionEvent actionEvent) {
        try {
            countryModel.deleteCountry(country);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTeam(ActionEvent actionEvent) {
        try {
            teamModel.deleteTeam(team);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
