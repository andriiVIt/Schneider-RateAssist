package GUI.Controller;

import BE.Employee;
import GUI.Model.EmployeeModel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ChengPasswordController {

    @FXML
    private MFXPasswordField newPasswordField;

    @FXML
    private MFXTextField userNameField;

    private EmployeeModel employeeModel;
    private Employee currentEmployee;

    public ChengPasswordController() {
        employeeModel = new EmployeeModel();
    }

    // Sets the current employee and fills the username field
    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        userNameField.setText(employee.getLoginName()); // Fill the field with the current username
    }

    // Saves the new username and password for the current employee
    @FXML
    public void saveInformation(ActionEvent actionEvent) {
        String newUsername = userNameField.getText();
        String newPassword = newPasswordField.getText();

        try {
            // Update the credentials of the current employee
            employeeModel.updateEmployeeCredentials(currentEmployee.getId(), newUsername, newPassword);
            // Close the window after successful update
            Stage stage = (Stage) userNameField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update employee credentials");
        }
    }

    // Cancels the action and closes the window
    @FXML
    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) userNameField.getScene().getWindow();
        stage.close();
    }
}
