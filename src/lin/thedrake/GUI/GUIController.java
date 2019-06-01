package lin.thedrake.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lin.thedrake.GUI.MultiPlayerGUI.MultiPlayerConnector;

import java.io.IOException;

public class GUIController {
    public Button button_singleplayer;
    public Button button_multiplayer;
    public Button button_play_online;
    public Button button_exit;

    public StackPane pane_main;

    public VBox start_menu;
    public StackPane pane_menu;

    @FXML
    private void clickSinglePlayer(ActionEvent event) {
        //System.out.println("clickSinglePlayer");

    }

    @FXML
    private void clickMultiPlayer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(GUIController.class.getResource("./MultiPlayerGUI/multiPlayer.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        root.setId("gamePlace");
        MultiPlayerConnector.controller = loader.getController();
        pane_main.getChildren().addAll(root);
        start_menu.setVisible(false);
    }

    @FXML
    private void clickPlayOnline(ActionEvent event) {
        //System.out.println("clickPlayOnline");
        //todo
    }

    public void returnFromMultiPlayer() {
        pane_main.getChildren().removeIf(node -> node.getId().equals("gamePlace"));
        MultiPlayerConnector.controller = null;
        start_menu.setVisible(true);
    }

    public void restartMultiPlayer() {
        pane_main.getChildren().removeIf(node -> node.getId().equals("gamePlace"));
        MultiPlayerConnector.controller = null;
        clickMultiPlayer(new ActionEvent());
    }
}
