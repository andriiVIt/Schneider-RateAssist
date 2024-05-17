package GUI.Controller;

import BE.*;
import BLL.RateLogic;
import GUI.Controller.Create.CreateCountryController;
import GUI.Controller.Create.CreateTeamController;
import GUI.Model.CalculationModel;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
    private Label calculatedRate;
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
    private CheckComboBox<Country> countryBox;
    @FXML
    private CheckComboBox<Team> teamBox;

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
    private TeamModel teamModel;
    private Employee employee;
    private Consumer<Employee> onDeleteEmployeeCallback;
    private ScrollPane scrollPane;
    private EmployeeModel employeeModel;
    private RateLogic rateLogic = new RateLogic(); // Initialize RateLogic
    private Runnable refreshCallback;
    private CalculationModel calculationModel = new CalculationModel();
    private Employee selectedEmployee;
//    private CalculationModel calculationModel;
    public void setModel(EmployeeModel employeeModel, ScrollPane scrollPane, TeamModel teamModel, CountryModel countryModel,CalculationModel calculationModel) {
        this.calculationModel = calculationModel;
        this.scrollPane = scrollPane;
        this.employeeModel = employeeModel;
        this.teamModel = teamModel;
        this.countryModel = countryModel;
        setTeamModel(teamModel);
        setCountryModel(countryModel);


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
                    StringBuilder ratesText = new StringBuilder("");
                    for (Rate rate : rates) {
                        ratesText.append(rate.getRate()).append(" ");
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

        // Завантаження та відображення розрахованої ставки
        try {
            Calculation calculation = calculationModel.getCalculationByEmployeeId(employee.getId());
            if (calculation != null) {
                calculatedRate.setText(String.format("%.2f", calculation.getRate()));
            } else {
                calculatedRate.setText("No rate calculated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            calculatedRate.setText("Failed to load calculated rate.");
        }
    }

    public void setOnDeleteEmployeeCallback(Consumer<Employee> onDeleteEmployeeCallback) {
        this.onDeleteEmployeeCallback = onDeleteEmployeeCallback;
    }
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
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
        if (employee != null) {
            if (changeNameField.getText() != null && !changeNameField.getText().isEmpty()) {
                employee.setName(changeNameField.getText());
            }
            if (changeSalaryField.getText() != null && !changeSalaryField.getText().isEmpty()) {
                employee.setSalary(Double.parseDouble(changeSalaryField.getText()));
            }
            if (changeOverheadField.getText() != null && !changeOverheadField.getText().isEmpty()) {
                employee.setOverheadPercentage(Double.parseDouble(changeOverheadField.getText()));
            }
            if (changeFixedAnnualAmountField.getText() != null && !changeFixedAnnualAmountField.getText().isEmpty()) {
                employee.setFixedAmount(Double.parseDouble(changeFixedAnnualAmountField.getText()));
            }
            if (changeAnEffectWorkHoursField.getText() != null && !changeAnEffectWorkHoursField.getText().isEmpty()) {
                employee.setWorkHours(Double.parseDouble(changeAnEffectWorkHoursField.getText()));
            }
            if (changeUtilizationPercentageField.getText() != null && !changeUtilizationPercentageField.getText().isEmpty()) {
                employee.setUtilization(Double.parseDouble(changeUtilizationPercentageField.getText()));
            }
            if (changeResourceTypeField.getText() != null && !changeResourceTypeField.getText().isEmpty()) {
                employee.setResourceType(changeResourceTypeField.getText());
            }

            try {
                // Оновлення інформації про працівника
                employeeModel.updateEmployee(employee);

                // Оновлення країни, якщо обрано
                List<Country> selectedCountries = countryBox.getCheckModel().getCheckedItems();
                if (!selectedCountries.isEmpty()) {
                    Country selectedCountry = selectedCountries.getFirst();
                    countryModel.updateCountryEmployee(employee.getId(), selectedCountry.getId());
                }

                // Оновлення команди, якщо обрано
                List<Team> selectedTeams = teamBox.getCheckModel().getCheckedItems();
                if (!selectedTeams.isEmpty()) {
                    Team selectedTeam = selectedTeams.getFirst();
                    teamModel.updateTeamEmployee(employee.getId(), selectedTeam.getId());
                }

                // Показуємо повідомлення про успішне оновлення
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee information updated successfully.");

                // Оновлюємо відображення даних працівника
                updateEmployeeDetails();

                // Виклик зворотного виклику для оновлення сторінки з працівниками
                if (refreshCallback != null) {
                    refreshCallback.run();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee information.");
            }
        }
    }
    private void updateEmployeeDetails() {
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
            createCountryController.setRefreshCallback(() -> {
                countryBox.getItems().clear();
                countryBox.getItems().addAll(countryModel.getCountries());
            });

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
            createTeamController.setRefreshCallback(() -> {
                teamBox.getItems().clear();
                teamBox.getItems().addAll(teamModel.getTeams());
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    public void deleteCountry(ActionEvent actionEvent) {
        Country selectedCountry = countryBox.getCheckModel().getCheckedItems().getFirst();
        if (selectedCountry != null) {
            try {
                countryModel.deleteCountry(selectedCountry);
                countryBox.getItems().remove(selectedCountry);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the selected country.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a country to delete.");
        }
    }

    public void deleteTeam(ActionEvent actionEvent) {
        Team selectedTeam = teamBox.getCheckModel().getCheckedItems().getFirst();
        if (selectedTeam != null) {
            try {
                teamModel.deleteTeam(selectedTeam);
                teamBox.getItems().remove(selectedTeam);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the selected team.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a team to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCountryModel(CountryModel countryModel) {
        this.countryModel = countryModel;
        countryBox.setTitle("Country");
        countryBox.getItems().addAll(countryModel.getCountries());

        countryModel.getCountries().addListener((ListChangeListener<? super Country>) obs -> {
            countryBox.getItems().clear();
            countryBox.getItems().addAll(countryModel.getCountries());
        });
    }

    public void setTeamModel(TeamModel teamModel) {
        this.teamModel = teamModel;
        teamBox.setTitle("Team");
        teamBox.getItems().addAll(teamModel.getTeams());

        teamModel.getTeams().addListener((ListChangeListener<? super Team>) obs -> {
            teamBox.getItems().clear();
            teamBox.getItems().addAll(teamModel.getTeams());
        });
    }

    public void calculatorButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/CalculatorWindow.fxml"));
            Parent calculatorParent = fxmlLoader.load();

            CalculatorController controller = fxmlLoader.getController();
            controller.setEmployee(this.employee); // Встановлюємо вибраного працівника з поточного об'єкта
            controller.setCalculationModel(new CalculationModel()); // Встановлюємо модель

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Rate Calculator");
            stage.setScene(new Scene(calculatorParent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling should be implemented
        }
    }

    public void updateCalculatedRate(double rate) {
        if (calculatedRate != null) {
            calculatedRate.setText(String.format("%.2f", rate));
        }
    }
    public void changePhoto(ActionEvent actionEvent) {
    }
}
