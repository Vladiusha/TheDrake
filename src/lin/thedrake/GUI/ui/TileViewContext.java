package lin.thedrake.GUI.ui;

import lin.thedrake.Move;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move);

}
