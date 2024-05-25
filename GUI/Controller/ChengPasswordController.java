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

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        userNameField.setText(employee.getLoginName()); // Заповнити поле поточним іменем користувача
    }

    @FXML
    public void saveInformation(ActionEvent actionEvent) {
        String newUsername = userNameField.getText();
        String newPassword = newPasswordField.getText();

        try {
            // Оновлюємо облікові дані поточного працівника
            employeeModel.updateEmployeeCredentials(currentEmployee.getId(), newUsername, newPassword);
            // Закриваємо вікно після успішного оновлення
            Stage stage = (Stage) userNameField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to update employee credentials");
        }
    }

    @FXML
    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) userNameField.getScene().getWindow();
        stage.close();
    }
}
