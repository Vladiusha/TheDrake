package lin.thedrake.GUI.MultiPlayerGUI;

import lin.thedrake.*;
import lin.thedrake.GUI.ui.BoardView;

public class MultiPlayerCreator {
    public MultiPlayerCreator() {}

    public static BoardView getBoardView() {
        GameState gameState = createSampleGameState();
        return new BoardView(gameState);
    }

    private static GameState createSampleGameState() {
        Board board = new Board(4);
        PositionFactory positionFactory = board.positionFactory();
        board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }
}
