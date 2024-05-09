package GUI.Controller;

import BE.Employee;
import GUI.Model.EmployeeModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EmployeeInfoController implements Initializable {
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

    private Employee employee;
    private Consumer<Employee> onDeleteEmployeeCallback;
    private ScrollPane  scrollPane;
    private EmployeeModel employeeModel;

    public void setModel(EmployeeModel employeeModel, ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
        this.employeeModel = employeeModel;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (nameField != null) {
            nameField.setText(employee.getName());
        }
        if (annualSalaryField != null){
            annualSalaryField.setText(String.valueOf(employee.getSalary()));
        }
        if (overheadMultiplierField != null){
            overheadMultiplierField.setText(String.valueOf(employee.getOverheadPercentage()));
        }
        if (configurableFixedAnnualAmountField != null){
            configurableFixedAnnualAmountField.setText(String.valueOf(employee.getFixedAmount()));
        }
        if (annualEffectiveWorkingHoursField != null){
            annualEffectiveWorkingHoursField.setText(String.valueOf(employee.getWorkHours()));

        }
        if (utilizationPercentageField != null){
            utilizationPercentageField.setText(String.valueOf(employee.getUtilization()));
        }
        if (workerImage != null && employee.getImageData() != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(employee.getImageData());
            Image image = new Image(inputStream);
            workerImage.setImage(image);
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

    }

    public void AddCountry(ActionEvent actionEvent) {
    }

    public void AddTeam(ActionEvent actionEvent) {
    }
}
