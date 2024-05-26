package GUI.Controller.Create;

import BE.Team;
import GUI.Model.TeamModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateTeamController {

    @FXML
    private MFXTextField newTeamField;
    @FXML
    private AnchorPane createTeamPane;

    private TeamModel teamModel;
    private Runnable refreshCallback;

    // Sets the TeamModel instance
    public void setModel(TeamModel teamModel) {
        this.teamModel = teamModel;
    }

    // Sets the refresh callback function
    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    // Saves the new team entered by the user
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
    // Cancels the action and closes the window
    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) createTeamPane.getScene().getWindow();
        stage.close();
    }
    // Shows an alert dialog with the specified type, title, and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Sets the scroll pane for blur effect handling (empty method)
    public void setScrollPane(ScrollPane scrollPane) {

    }

    // Sets the onClose request handler for the stage (empty method)
    public void setOnCloseRequestHandler(Stage stage) {
    }
}
