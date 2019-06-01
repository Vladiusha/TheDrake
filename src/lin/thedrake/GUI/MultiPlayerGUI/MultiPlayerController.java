package lin.thedrake.GUI.MultiPlayerGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lin.thedrake.*;
import lin.thedrake.GUI.GUIConnector;
import lin.thedrake.GUI.ui.BoardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiPlayerController {
    public HBox multiPlayer_mainPane;
    public VBox box_center;
    public HBox box_top;
    public HBox box_bottom;


    public StackPane orange_buffer;
    public Pane img_empty_orange_buffer;
    public Pane img_orange_buffered_1;
    public Pane img_orange_buffered_2;
    public Pane img_orange_buffered_3;
    public Pane img_orange_buffered_4;
    public Pane img_orange_buffered_5;
    public Pane img_orange_buffered_6;
    public Pane img_orange_buffered_7;
    private int orange_buffered_count = 0;

    public StackPane orange_stack;
    public Pane img_orange_archer_front;
    public Pane img_orange_swordsman_front;
    public Pane img_orange_spearman_front;
    public Pane img_orange_monk_front;
    public Pane img_orange_clubman_front_2;
    public Pane img_orange_clubman_front_1;
    public Pane img_orange_drake_front;
    public Pane img_empty_orange_stack;
    private int orange_stack_count = 7;


    public StackPane blue_buffer;
    public Pane img_empty_blue_buffer;
    public Pane img_blue_buffered_1;
    public Pane img_blue_buffered_2;
    public Pane img_blue_buffered_3;
    public Pane img_blue_buffered_4;
    public Pane img_blue_buffered_5;
    public Pane img_blue_buffered_6;
    public Pane img_blue_buffered_7;
    private int blue_buffered_count = 0;


    public StackPane blue_stack;
    public Pane img_blue_archer_front;
    public Pane img_blue_swordsman_front;
    public Pane img_blue_spearman_front;
    public Pane img_blue_monk_front;
    public Pane img_blue_clubman_front_2;
    public Pane img_blue_clubman_front_1;
    public Pane img_blue_drake_front;
    public Pane img_empty_blue_stack;
    private int blue_stack_count = 7;

    public HBox orange_selector;
    public HBox blue_selector;

    public Label label_draw;
    public Label label_orange_win;
    public Label label_blue_win;
    public Pane img_fight;

    private BoardView boardView = null;
    private boolean blueTeam = true;
    private GameResult result = GameResult.IN_PLAY;

    private List<String> orangeBuffer = new ArrayList<>();
    private List<String> blueBuffer = new ArrayList<>();

    public void initialize() {
        multiPlayer_mainPane.getStyleClass().add("bg" + ((new Random()).nextInt(2) + 1));
        //multiPlayer_mainPane.getStyleClass().add("multiPlayerScreen.jpg");

        boardView = MultiPlayerCreator.getBoardView();
        box_center.getChildren().add(1, boardView);
        img_fight.setVisible(true);
        blue_selector.setVisible(true);
        //todo
    }

    public void update(PlayingSide side, GameResult result, int blueStack, int blueCaptured, int orangeStack, int orangeCaptured) {
        blueTeam = side == PlayingSide.BLUE;
        if (blueTeam) {
            orange_selector.setVisible(false);
            blue_selector.setVisible(true);
        } else {
            orange_selector.setVisible(true);
            blue_selector.setVisible(false);
        }
        if (result != this.result) {
            updateProgressInfo(result);
        }
        if (this.blue_stack_count != blueStack) {
            updateBlueStack(blueStack);
        }
        if (this.blue_buffered_count != blueCaptured) {
            updateBlueBuffer(blueCaptured);
        }
        if (this.orange_stack_count != orangeStack) {
            updateOrangeStack(orangeStack);
        }
        if (this.orange_buffered_count != orangeCaptured) {
            updateOrangeBuffer(orangeCaptured);
        }
    }

    @FXML
    private void clickMenu(ActionEvent event) {
        GUIConnector.guiController.returnFromMultiPlayer();
    }

    @FXML
    private void clickRestart(ActionEvent event) {
        GUIConnector.guiController.restartMultiPlayer();
    }

    @FXML
    private void clickBlueStack(MouseEvent event) {
        if (!blueTeam || blue_stack_count == 0 || result == GameResult.VICTORY || result == GameResult.DRAW) return;
        boardView.stackTileSelected();
    }

    @FXML
    private void clickOrangeStack(MouseEvent event) {
        if (blueTeam || orange_stack_count == 0 || result == GameResult.VICTORY || result == GameResult.DRAW) return;
        boardView.stackTileSelected();
    }

    private void updateBlueStack(int newData) {
        for (int i = 0; i < blue_stack_count - newData; i++) {
            blue_stack.getChildren().remove(newData);
        }
        blue_stack_count = newData;
        if (blue_stack_count == 0) {
            blue_stack.getChildren().get(0).setVisible(true);
            return;
        }
        for (int i = 0; i < blue_stack_count; i++) {
            StackPane.setMargin(blue_stack.getChildren().get(i), new Insets(0, 0, (blue_stack_count - 1 - i) * 50, 0));
        }
    }

    private void updateOrangeStack(int newData) {
        for (int i = 0; i < orange_stack_count - newData; i++) {
            orange_stack.getChildren().remove(newData);
        }
        orange_stack_count = newData;
        if (orange_stack_count == 0) {
            orange_stack.getChildren().get(0).setVisible(true);
            return;
        }
        for (int i = 0; i < orange_stack_count; i++) {
            StackPane.setMargin(orange_stack.getChildren().get(i), new Insets(0, 0, (orange_stack_count - 1 - i) * 50, 0));
        }
    }

    private void updateBlueBuffer(int newData) {
        switch (newData) {
            case 0: {
                return;
            }
            case 1: {
                img_empty_blue_buffer.setVisible(false);
                img_blue_buffered_1.getStyleClass().add(getTileString(true));
                img_blue_buffered_1.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            case 2: {
                img_blue_buffered_2.getStyleClass().add(getTileString(true));
                img_blue_buffered_2.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            case 3: {
                img_blue_buffered_3.getStyleClass().add(getTileString(true));
                img_blue_buffered_3.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            case 4: {
                img_blue_buffered_4.getStyleClass().add(getTileString(true));
                img_blue_buffered_4.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            case 5: {
                img_blue_buffered_5.getStyleClass().add(getTileString(true));
                img_blue_buffered_5.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            case 6: {
                img_blue_buffered_6.getStyleClass().add(getTileString(true));
                img_blue_buffered_6.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            case 7: {
                img_blue_buffered_7.getStyleClass().add(getTileString(true));
                img_blue_buffered_7.setVisible(true);
                blue_buffered_count = newData;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    private void updateOrangeBuffer(int newData) {
        switch (newData) {
            case 0: {
                return;
            }
            case 1: {
                img_empty_orange_buffer.setVisible(false);
                img_orange_buffered_1.getStyleClass().add(getTileString(false));
                img_orange_buffered_1.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            case 2: {
                img_orange_buffered_2.getStyleClass().add(getTileString(false));
                img_orange_buffered_2.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            case 3: {
                img_orange_buffered_3.getStyleClass().add(getTileString(false));
                img_orange_buffered_3.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            case 4: {
                img_orange_buffered_4.getStyleClass().add(getTileString(false));
                img_orange_buffered_4.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            case 5: {
                img_orange_buffered_5.getStyleClass().add(getTileString(false));
                img_orange_buffered_5.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            case 6: {
                img_orange_buffered_6.getStyleClass().add(getTileString(false));
                img_orange_buffered_6.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            case 7: {
                img_orange_buffered_7.getStyleClass().add(getTileString(false));
                img_orange_buffered_7.setVisible(true);
                orange_buffered_count = newData;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    private void updateProgressInfo(GameResult newResult) {
        if (newResult == GameResult.VICTORY) {
            img_fight.setVisible(false);
            if (blueTeam) {
                label_orange_win.setVisible(true);
            } else {
                label_blue_win.setVisible(true);
            }
            result = newResult;
            blue_selector.setVisible(false);
            orange_selector.setVisible(false);
        }
        if (result == GameResult.DRAW) {
            label_draw.setVisible(true);
            blue_selector.setVisible(false);
            orange_selector.setVisible(false);
        }
    }

    private String getTileString(boolean blue) {
        if (blue) {
            List<String> tmp = boardView.capturedTiles(true);
            for (String name : blueBuffer) {
                for (int i = 0; i < tmp.size(); i++) {
                    if (tmp.get(i).equals(name)) {
                        tmp.remove(i);
                        break;
                    }
                }
            }
            if (tmp.size() < 1) {
                throw new IllegalArgumentException();
            }
            blueBuffer = boardView.capturedTiles(true);
            return "orange" + tmp.get(0);
        } else {
            List<String> tmp = boardView.capturedTiles(false);
            for (String name : orangeBuffer) {
                for (int i = 0; i < tmp.size(); i++) {
                    if (tmp.get(i).equals(name)) {
                        tmp.remove(i);
                        break;
                    }
                }
            }
            if (tmp.size() < 1) {
                throw new IllegalArgumentException();
            }
            orangeBuffer = boardView.capturedTiles(false);
            return "blue" + tmp.get(0);
        }
    }
}
