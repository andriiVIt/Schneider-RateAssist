package GUI.Controller;

import BE.Employee;
import GUI.Model.EmployeeModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EmployeeCardController implements Initializable {
    @FXML
    private Pane gridPane;
    @FXML
    private ImageView workerImage;
    @FXML
    private Label nameTitle;
    @FXML
    private MFXButton deleteButton;
    @FXML
    private MFXButton employeeView;
    @FXML
    private Label countryTitle;
    @FXML
    private Label teamTitle;

    private final ScrollPane scrollPane;
    private final Employee employee;
    private final EmployeeModel employeeModel;
    private final AdminController adminController;
    private Consumer<Employee>onDeleteEmployeeCallback;



    public EmployeeCardController(ScrollPane scrollPane, Employee employee, EmployeeModel employeeModel, AdminController adminController) {
        this.scrollPane = scrollPane;
        this.employee = employee;
        this.employeeModel = employeeModel;
        this.adminController = adminController;
    }

    public void deleteEmployee(ActionEvent actionEvent) {
    }

    public void viewEmployee(ActionEvent actionEvent) {
    }

    public void setOnDeleteEmployeeCallback(Consumer<Employee>onDeleteEmployeeCallback) {
        this.onDeleteEmployeeCallback = onDeleteEmployeeCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Consumer<Employee> getOnDeleteEmployeeCallback() {
        return onDeleteEmployeeCallback;
    }
}
