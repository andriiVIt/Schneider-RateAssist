package GUI.Controller;

import BE.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URL;
import java.util.Random;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String t = team.getTeamName();
        this.idOfLable.setText(t);

        this.id.setText(((Integer) team.getId()).toString());
//
//na opislya
//        // Generate a random color
//        Random rand = new Random();
//        Color randomColor = new Color((int) rand.nextDouble(), (int) rand.nextDouble(), (int) rand.nextDouble(), (int) 0.3);
//
//        // Create a BackgroundFill with rounded corners
//        Insets noPadding = new Insets(0, 0, 0, 0);
//        BackgroundFill backgroundFill = new BackgroundFill(randomColor, new CornerRadii(10));
//        Background background = new Background(backgroundFill);

        // Set the background of the AnchorPane
//        this.calculatorPane.setBackground(background);
    }

    public void setOnSelectTeam() {
        teamsViewController.selectTeam(team.getId());
    }
}
