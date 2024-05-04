package GUI.Controller;

import BE.Employee;
import GUI.Model.EmployeeModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
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
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private CheckComboBox countryComboBox;
    @FXML
    private CheckComboBox teamComboBox;
    @FXML
    private MFXButton logOutButton;
    @FXML
    private MFXButton group;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;

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

    private void populateGridPane()throws IOException {
        EmployeeModel employeeModel = new EmployeeModel();
        List<Employee> employees;
        try {
            employees = EmployeeModel.getEmployees();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch events from the database.", e);
        }

        int numRows = 4;
        int numColumns = 2;
        int employeesPerPage = numRows * numColumns;
        totalPages = (int) Math.ceil((double) employees.size() / employeesPerPage);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                int employeeIndex = currentPage * employeesPerPage + row * numColumns + col;
                if (employeeIndex >= employees.size()) {
                    break;
                }
                Pane pane = new Pane();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/EmployeeCardWindow.fxml"));

                fxmlLoader.setControllerFactory(clazz -> {
                    EmployeeCardController controller = new EmployeeCardController(scrollPane, employees.get(employeeIndex), employeeModel, this);
                    controller.setOnDeleteEmployeeCallback(deletedEmployee -> refreshEmployeeCards());
                    return controller;
                });
                Pane contentPane = fxmlLoader.load();
                pane.getChildren().add(contentPane);
                gridPane.add(pane, col, row);
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
            createEmployeeController.setEmployeeModel(new EmployeeModel());
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
        try {
            populateGridPane();
        } catch (IOException e) {
            // Better error handling should be considered
        }

    }

    public void groupButton(ActionEvent actionEvent) {
    }


}



