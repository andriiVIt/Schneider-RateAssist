package GUI.Controller;

import BE.Country;
import BE.Employee;
import BE.Rate;
import BE.Team;
import BLL.RateLogic;
import GUI.Model.CountryModel;
import GUI.Model.EmployeeModel;
import GUI.Model.TeamModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EmployeeCardController implements Initializable {
    @FXML
    private Label rateTitle;
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
    private final MainWindowController adminController;
    private final RateLogic rateLogic = new RateLogic();
    private Consumer<Employee> onDeleteEmployeeCallback;

    public EmployeeCardController(ScrollPane scrollPane, Employee employee, EmployeeModel employeeModel, MainWindowController adminController) {
        this.scrollPane = scrollPane;
        this.employee = employee;
        this.employeeModel = employeeModel;
        this.adminController = adminController;
    }

    public void deleteEmployee(ActionEvent actionEvent) {
        try {
            employeeModel.deleteEmployee(employee);
            adminController.refreshEmployeeCards();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewEmployee(ActionEvent actionEvent) {
        BlurEffectUtil.applyBlurEffect(scrollPane, 10);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/view/EmployeeInfo.fxml"));
            Parent createEventParent = fxmlLoader.load();

            EmployeeInfoController employeeInfoController = fxmlLoader.getController();
            employeeInfoController.setModel(employeeModel, scrollPane, new TeamModel(), new CountryModel()); // Передача всіх чотирьох аргументів
            employeeInfoController.setEmployee(employee);
            employeeInfoController.setOnDeleteEmployeeCallback(deletedEmployee -> {
                if (onDeleteEmployeeCallback != null)
                    onDeleteEmployeeCallback.accept(deletedEmployee);
            });

            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle(employee.getName());
            Scene scene = new Scene(createEventParent);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnDeleteEmployeeCallback(Consumer<Employee> onDeleteEmployeeCallback) {
        this.onDeleteEmployeeCallback = onDeleteEmployeeCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameTitle.setText(employee.getName());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(employee.getImageData());
        Image image = new Image(inputStream);
        workerImage.setImage(image);
        workerImage.setFitWidth(120);
        workerImage.setFitHeight(120);
        workerImage.setPreserveRatio(false);

        try {
            List<Rate> rates = rateLogic.getListRatesEmployee(employee.getId());
            if (rates != null && !rates.isEmpty()) {
                StringBuilder ratesText = new StringBuilder("Rates: ");
                for (Rate rate : rates) {
                    ratesText.append(rate.getRate()).append(", ");
                }
                if (ratesText.length() > 7) {
                    ratesText.setLength(ratesText.length() - 2);
                }
                rateTitle.setText(ratesText.toString());

                Country country = rates.getFirst().getCountry(); // Візьмемо країну з першої ставки
                if (country != null) {
                    countryTitle.setText(country.getCountryName());
                } else {
                    countryTitle.setText("Not available");
                }

                Team team = rates.getFirst().getTeam(); // Візьмемо команду з першої ставки
                if (team != null) {
                    teamTitle.setText(team.getTeamName());
                } else {
                    teamTitle.setText("Not available");
                }
            } else {
                rateTitle.setText("No rates available.");
                countryTitle.setText("Not available");
                teamTitle.setText("Not available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rateTitle.setText("Failed to load rates.");
            countryTitle.setText("Failed to load country.");
            teamTitle.setText("Failed to load team.");
        }
    }

    public Consumer<Employee> getOnDeleteEmployeeCallback() {
        return onDeleteEmployeeCallback;
    }
}
