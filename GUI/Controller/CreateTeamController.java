package GUI.Controller;

import GUI.Model.TeamModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateTeamController {
    public AnchorPane createTeamPane;
    @FXML
    private MFXTextField newTeamField;
      @FXML
    private MFXButton cancelButton;

    public void saveNewTeam(ActionEvent actionEvent) {
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createTeamPane.getScene().getWindow();
        stage.close();
    }

    public void setModel(TeamModel teamModel) {
    }

    public void setScrollPane(ScrollPane scrollPane) {
    }

    public void setOnCloseRequestHandler(Stage stage) {
    }
}
