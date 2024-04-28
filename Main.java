import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = getClass().getResource("GUI/view/AdminWindow.fxml");
        Parent root = FXMLLoader.load(Objects.requireNonNull(fxmlUrl));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Rate Assist");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}