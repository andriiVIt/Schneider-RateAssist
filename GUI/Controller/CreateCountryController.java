package GUI.Controller;

import BE.Country;
import GUI.Model.CountryModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class CreateCountryController implements Initializable {

    @FXML
    private MFXTextField newCountryField;
    @FXML
    private AnchorPane createCountryPane;

    private CountryModel countryModel;
    private Runnable refreshCallback;

    public void setModel(CountryModel countryModel) {
        this.countryModel = countryModel;
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void saveNewCountry(ActionEvent actionEvent) {
        String countryName = newCountryField.getText().trim();
        if (!countryName.isEmpty()) {
            try {
                Country newCountry = new Country(countryName);
                countryModel.createCountry(newCountry);
                showAlert(Alert.AlertType.INFORMATION, "Country Created", "New country has been successfully created.");

                if (refreshCallback != null) {
                    refreshCallback.run();
                }

                Stage stage = (Stage) createCountryPane.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the country: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a country name.");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createCountryPane.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
