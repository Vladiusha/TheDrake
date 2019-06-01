package lin.thedrake;

import java.util.List;

public interface Tile {
    /**
     * Returns True, if this tile is free and can be at her to enter.
     */
    public boolean canStepOn();

    /**
     * Returns True if this tile contains a troop.
     */
    public boolean hasTroop();

    public List<Move> movesFrom(BoardPos pos, GameState state);
}