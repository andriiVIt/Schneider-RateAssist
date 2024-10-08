package GUI.Controller;

import BE.*;
import BLL.RateLogic;
import GUI.Model.CalculationModel;
import GUI.Model.EmployeeModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerController implements Initializable {

    @FXML
    private Label country;
    @FXML
    private Label team;
    @FXML
    private Label rate;
    @FXML
    private Label name;
    @FXML
    private Label DayRate;
    @FXML
    private Label resourceType;
    @FXML
    private Label annualWorkHours;
    @FXML
    private Label utilizationPercentage;
    @FXML
    private Label fixedAnnualAmount;
    @FXML
    private Label overheadMultipliers;
    @FXML
    private Label annualSalary;

    @FXML
    private ImageView workerImage;

    private EmployeeModel employeeModel;
    private RateLogic rateLogic;
    private CalculationModel calculationModel;
    private Employee currentEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeModel = new EmployeeModel();
        rateLogic = new RateLogic();
        calculationModel = new CalculationModel();
    }

    // Sets the employee data in the UI
    public void setEmployeeData(Employee employee) {
        this.currentEmployee = employee; // Save the current employee

        if (name != null) {
            name.setText(employee.getName());
        }
        if (annualSalary != null) {
            annualSalary.setText(String.valueOf(employee.getSalary()));
        }
        if (overheadMultipliers != null) {
            overheadMultipliers.setText(String.valueOf(employee.getOverheadPercentage()));
        }
        if (fixedAnnualAmount != null) {
            fixedAnnualAmount.setText(String.valueOf(employee.getFixedAmount()));
        }
        if (annualWorkHours != null) {
            annualWorkHours.setText(String.valueOf(employee.getWorkHours()));
        }
        if (utilizationPercentage != null) {
            utilizationPercentage.setText(String.valueOf(employee.getUtilization()));
        }
        if (resourceType != null) {
            resourceType.setText(employee.getResourceType());
        }
        if (workerImage != null && employee.getImageData() != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(employee.getImageData());
            Image image = new Image(inputStream);
            workerImage.setImage(image);
        }

        try {
            List<Rate> rates = rateLogic.getListRatesEmployee(employee.getId());
            if (rates != null && !rates.isEmpty()) {
                StringBuilder ratesText = new StringBuilder(" ");
                for (Rate rate : rates) {
                    ratesText.append(rate.getRate()).append(", ");
                }
                if (ratesText.length() > 7) {
                    ratesText.setLength(ratesText.length() - 2);
                }
                this.rate.setText(ratesText.toString());

                Country employeeCountry = rates.get(0).getCountry(); // Get the country from the first rate
                if (employeeCountry != null) {
                    country.setText(employeeCountry.getCountryName());
                } else {
                    country.setText("Not available");
                }

                Team employeeTeam = rates.get(0).getTeam(); // Get the team from the first rate
                if (employeeTeam != null) {
                    team.setText(employeeTeam.getTeamName());
                } else {
                    team.setText("Not available");
                }
            } else {
                this.rate.setText("No rates available.");
                country.setText("Not available");
                team.setText("Not available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.rate.setText("Failed to load rates.");
            country.setText("Failed to load country.");
            team.setText("Failed to load team.");
        }

        // Load and display the calculated rate
        if (DayRate != null) {
            try {
                Calculation calculation = calculationModel.getCalculationByEmployeeId(employee.getId());
                if (calculation != null) {
                    DayRate.setText(String.format("%.2f", calculation.getRate()));
                } else {
                    DayRate.setText("No rate calculated.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                DayRate.setText("Failed to load calculated rate.");
            }
        }
    }

    // Handles the log out action
    @FXML
    public void clickLogOut(ActionEvent actionEvent) {
        // Log out logic
        System.out.println("Log Out clicked");
        // For example, close the employee window and return to the login window
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    // Opens the window to change the password
    @FXML
    public void changePassword(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/ChangPasswordWindow.fxml"));
        Parent parent = fxmlLoader.load();

        ChengPasswordController chengPasswordController = fxmlLoader.getController();
        chengPasswordController.setEmployee(currentEmployee); // Pass the current employee to the controller

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Set the window modality
        stage.setTitle("New Password");
        stage.setResizable(false); // Make the window not resizable
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
