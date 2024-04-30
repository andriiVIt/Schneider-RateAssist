package GUI.Controller;

import GUI.Model.EmployeeModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class CreateEmployeeController {
    public MFXTextField fixedAmountField;
    public MFXTextField locationField;
    public MFXTextField salaryField;
    public MFXTextField overheadField;
    public MFXTextField teamField;
    public MFXTextField workHoursField;
    public MFXTextField utilizationField;
    public MFXTextField resourceTypeField;
    public Label namePhoto;
    public MFXTextField nameField;
    private Runnable refreshCallback;
    public void selectPhotoButton(ActionEvent actionEvent) {
    }

    public void createEmployee(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
    }

    public void setEmployeeModel(EmployeeModel employeeModel) {
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void setScrollPane(ScrollPane scrollPane) {
    }

    public void setOnCloseRequestHandler(Stage stage) {
    }
}
