package GUI.Controller;

import BE.Employee;
import GUI.Model.AdminModel;
import GUI.Model.EmployeeModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public MFXTextField userNameField;
    public MFXPasswordField passwordField;
    public MFXButton loginButton;
    public VBox loginVbox;
    private AdminModel adminModel;
    private EmployeeModel employeeModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminModel = new AdminModel();
        employeeModel = new EmployeeModel();
    }

    public void logIn(ActionEvent actionEvent) {
        String inputUsername = userNameField.getText();
        String inputPassword = passwordField.getText();

        if (adminModel.isValidAdmin(inputUsername, inputPassword)) {
            openAdminWindow();
        } else {
            try {
                if (employeeModel.isValidEmployee(inputUsername, inputPassword)) {
                    openEmployeeWindow(inputUsername);
                } else {
                    showAlert("Invalid Credentials", "Username or password is incorrect");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error occurred");
            }
        }
    }

    private void openAdminWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/MainWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Window");
            stage.show();

            closeCurrentStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEmployeeWindow(String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/WorkerWindow.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.setTitle("Employee Window");

            WorkerController controller = fxmlLoader.getController();
            Employee loggedInEmployee = employeeModel.getEmployeeByUsername(username);
            controller.setEmployeeData(loggedInEmployee);

            stage.show();
            closeCurrentStage();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private void closeCurrentStage() {
        Stage currentStage = (Stage) loginVbox.getScene().getWindow();
        currentStage.close();
    }
}
