package GUI.Controller;

import BE.Employee;
import BE.Calculation;
import GUI.Model.CalculationModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class CalculatorController {

    @FXML
    private TextField markupField;

    @FXML
    private TextField gmField;

    @FXML
    private Label hourlyRateLabel;

    private Employee selectedEmployee;
    private CalculationModel calculationModel;
    private double calculatedRate;
    private EmployeeInfoController employeeInfoController;

    // Метод для налаштування працівника
    public void setEmployee(Employee employee) {
        this.selectedEmployee = employee;
    }

    public void setEmployeeInfoController(EmployeeInfoController employeeInfoController) {
        this.employeeInfoController = employeeInfoController;
    }
    public void setCalculationModel(CalculationModel calculationModel) {
        this.calculationModel = calculationModel;
    }

    // Метод для обчислення погодинної ставки
    public void calculateHourlyRate() {
        if (selectedEmployee == null) {
            showError("Employee is not set.");
            return;
        }

        try {
            double markup = Double.parseDouble(markupField.getText()) / 100.0;
            double gm = Double.parseDouble(gmField.getText()) / 100.0;

            double salary = selectedEmployee.getSalary();
            double overhead = selectedEmployee.getOverheadPercentage();
            double fixedAmount = selectedEmployee.getFixedAmount();
            double workHours = selectedEmployee.getWorkHours();
            double utilization = selectedEmployee.getUtilization();

            double cost = salary * (1 + overhead) + fixedAmount;
            double effectiveHours = workHours * utilization;
            double baseRate = cost / effectiveHours;
            calculatedRate = baseRate * (1 + markup) * (1 + gm);

            hourlyRateLabel.setText(String.format("%.2f", calculatedRate));
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for markup and GM.");
        }
    }

    // Метод для збереження розрахованої ставки
    public void saveRate() {
        if (selectedEmployee == null) {
            showError("Employee is not set.");
            return;
        }

        try {
            Calculation calculation = new Calculation(selectedEmployee.getId(), calculatedRate);
            calculationModel.createCalculation(calculation);

            if (employeeInfoController != null) {
                employeeInfoController.updateCalculatedRate(calculatedRate);
            }

            showInfo("Rate saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to save rate.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void cancelButton(ActionEvent actionEvent) {

    }
}
