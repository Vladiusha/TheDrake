package lin.thedrake.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GUIController.class.getResource("./gui.fxml"));
        Parent root = loader.load();

        GUIConnector.guiController = loader.getController();

        GUIConnector.guiController.button_exit.setOnAction(event -> Platform.exit());

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("The Drake");
        stage.setFullScreen(true);
        stage.show();
        stage.setResizable(false);

    }
}
