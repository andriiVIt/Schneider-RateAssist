package GUI.Controller;

import BE.Team;
import GUI.Model.TeamModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class CreateTeamController {

    @FXML
    private MFXTextField newTeamField;
    @FXML
    private AnchorPane createTeamPane;

    private TeamModel teamModel;
    private Runnable refreshCallback;

    public void setModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void saveNewTeam(ActionEvent actionEvent) {
        String teamName = newTeamField.getText().trim();
        if (!teamName.isEmpty()) {
            try {
                Team newTeam = new Team(teamName);
                teamModel.createTeam(newTeam);
                showAlert(Alert.AlertType.INFORMATION, "Team Created", "New team has been successfully created.");

                if (refreshCallback != null) {
                    refreshCallback.run();
                }

                Stage stage = (Stage) createTeamPane.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the team: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter a team name.");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createTeamPane.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setScrollPane(ScrollPane scrollPane) {

    }

    public void setOnCloseRequestHandler(Stage stage) {
    }
}
