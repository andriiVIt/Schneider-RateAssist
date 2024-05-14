package GUI.Controller;

import GUI.Model.CountryModel;
import GUI.util.BlurEffectUtil;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateCountryController {

    @FXML
    private MFXTextField newCountryField;
    @FXML
    private AnchorPane createCountryPane;

    public void setModel(CountryModel countryModel) {

    }

    public void saveNewCountry(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createCountryPane.getScene().getWindow();
        stage.close();
    }
}
