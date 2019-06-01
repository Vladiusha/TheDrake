package lin.thedrake.GUI.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import lin.thedrake.*;
import lin.thedrake.GUI.MultiPlayerGUI.MultiPlayerConnector;

import java.util.ArrayList;
import java.util.List;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selected;

    public BoardView(GameState gameState) {
        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);

        PositionFactory positionFactory = gameState.board().positionFactory();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int i = x;
                int j = 3 - y;
                BoardPos boardPos = positionFactory.pos(i, j);
                add(new TileView(boardPos, gameState.tileAt(boardPos), this), x, y);
            }
        }

        setHgap(5);
        setVgap(5);
        setPadding(new Insets(15));
        setAlignment(Pos.CENTER);
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);

    }

    @Override
    public void tileViewSelected(TileView tileView) {
        if (selected != null && selected != tileView) {
            selected.unselect();
        }

        selected = tileView;
        clearMoves();
        showMoves(validMoves.boardMoves(tileView.position()));
    }

    public void stackTileSelected() {
        clearMoves();
        showMoves(validMoves.movesFromStack());
    }

    @Override
    public void executeMove(Move move) {
        if (selected != null) {
            selected.unselect();
            selected = null;
        }
        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);

        MultiPlayerConnector.controller.update(gameState.sideOnTurn(),
                gameState.result(),
                gameState.army(PlayingSide.BLUE).stack().size(),
                gameState.army(PlayingSide.BLUE).captured().size(),
                gameState.army(PlayingSide.ORANGE).stack().size(),
                gameState.army(PlayingSide.ORANGE).captured().size());

        updateTiles();
    }

    public List<String> capturedTiles(boolean blue) {
        if (blue) {
            List<String> tmp = new ArrayList<>();
            for (Troop troop : gameState.army(PlayingSide.BLUE).captured()) {
                tmp.add(troop.name());
            }
            return tmp;
        } else {
            List<String> tmp = new ArrayList<>();
            for (Troop troop : gameState.army(PlayingSide.ORANGE).captured()) {
                tmp.add(troop.name());
            }
            return tmp;
        }
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }
    }
}
