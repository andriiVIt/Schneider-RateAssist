package GUI.Controller;

import BE.Country;
import BE.Employee;
import BE.Team;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import GUI.util.Message;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateEmployeeController implements Initializable {
    @FXML
    private MFXTextField fixedAmountField;
    @FXML
    private MFXTextField salaryField;
    @FXML
    private MFXTextField overheadField;
    @FXML
    private MFXTextField workHoursField;
    @FXML
    private MFXTextField utilizationField;
    @FXML
    private MFXTextField resourceTypeField;
    @FXML
    private Label namePhoto;
    @FXML
    private MFXTextField nameField;
    @FXML
    private AnchorPane createEmployeeAnchorPane;
    @FXML
    private CheckComboBox locationBox;
    @FXML
    private CheckComboBox teamBox;

    // instance variables
    private CountryModel countryModel;
    private EmployeeModel employeeModel;
    private byte[] imageData;
    private ScrollPane scrollPane;
    private Runnable refreshCallback;


    public void  setTeamModel(TeamModel teamModel){
        teamBox.setTitle("Team");
        teamBox.getItems().addAll(teamModel.getTeams());

        teamModel.getTeams().addListener((ListChangeListener<? super Team>) obs->{
            teamBox.getItems().clear();
            teamBox.getItems().addAll(teamModel .getTeams());
        });
    }
    public void setCountryModel(CountryModel countryModel) {
        locationBox.setTitle("Country");
        locationBox.getItems().addAll(countryModel.getCountries());

        countryModel.getCountries().addListener((ListChangeListener<? super Country>) obs->{
            locationBox.getItems().clear();
            locationBox.getItems().addAll(countryModel .getCountries());
        });


    }

    public void selectPhotoButton(ActionEvent actionEvent) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Images File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            imageData = readBytesFromFile(selectedFile);
            namePhoto.setText(selectedFile.getName()); // Оновлюємо текст мітки з іменем файлу
        } else {
            namePhoto.setText("No file selected"); // Показати текст, коли файл не вибрано
        }
    }

    public void createEmployee(ActionEvent actionEvent) {
        String name = nameField.getText();

        double salary = Double.parseDouble(salaryField.getText());
        double overhead = Double.parseDouble(overheadField.getText());

        double workHours = Double.parseDouble(workHoursField.getText());
        double utilization = Double.parseDouble(utilizationField.getText());
        String resourceType = resourceTypeField.getText();
        String note = " "; // Додаткове поле для нотатків



        // Додавання можливості написати нотатку
        TextArea textArea = new TextArea();
        textArea.setPromptText("Write a note here...");
        textArea.setWrapText(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Note");
        alert.setHeaderText("Please write any special notes here:");
        alert.getDialogPane().setContent(textArea);

        ButtonType skipButtonType = new ButtonType("Skip", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType writeNoteButtonType = new ButtonType("Write Note", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(writeNoteButtonType, skipButtonType);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == writeNoteButtonType) {
            note = textArea.getText();
        }

        try {
            // Створення нового співробітника та додавання його до бази даних
            Employee newEmployee = new Employee(name, salary, overhead, workHours, utilization, resourceType, note, imageData);
            employeeModel.createEmployee(newEmployee);

            // Виклик зворотного виклику для оновлення GUI
            if (refreshCallback != null) {
                refreshCallback.run();
            }

            // Закриття вікна створення співробітника
            Stage stage = (Stage) createEmployeeAnchorPane.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Message.showAlert("Error", "Failed to create the employee in the database.", Alert.AlertType.ERROR);
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createEmployeeAnchorPane.getScene().getWindow();
        BlurEffectUtil.removeBlurEffect(scrollPane);
        stage.close();
    }

    public void setEmployeeModel(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void setOnCloseRequestHandler(Stage stage) {
        stage.setOnCloseRequest(event -> BlurEffectUtil.removeBlurEffect(scrollPane));
    }
    private static byte[] readBytesFromFile(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        is.close();
        bos.close();
        return bos.toByteArray();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
