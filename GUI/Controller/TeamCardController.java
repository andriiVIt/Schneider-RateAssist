package GUI.Controller;

import BE.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class TeamCardController implements Initializable {
    private final Team team;

    public TeamsViewController teamsViewController;
    public AnchorPane calculatorPane;
    public Label id;
    @FXML
    private Label idOfLable;

    public TeamCardController(Team team, TeamsViewController teamsViewController) {
        this.team = team;
        this.teamsViewController = teamsViewController;
    }

    // Initializes the controller and sets the team details in the UI
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String t = team.getTeamName();
        this.idOfLable.setText(t);

        this.id.setText(((Integer) team.getId()).toString());
    }

    // Handles the action when a team is selected
    public void setOnSelectTeam() {
        teamsViewController.selectTeam(team.getId());
    }
}
