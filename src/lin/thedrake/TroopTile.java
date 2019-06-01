package lin.thedrake;

import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile {
    /**
     * Constructor.
     *
     * @param troop item
     * @param side  orange or blue
     * @param face  revers or avers
     */
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    /**
     * @return the color at which the playing unit on this tile
     */
    public PlayingSide side() {
        return this.side;
    }

    /**
     * @return the side on which the unit is rotated
     */
    public TroopFace face() {
        return this.face;
    }

    /**
     * @return unit standing on this tile
     */
    public Troop troop() {
        return this.troop;
    }

    /**
     * @return a new tile with the unit rotated to the opposite side
     * (from the rub on the face or cheeks to rub)
     */
    public TroopTile flipped() {
        return new TroopTile(troop, side, face == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, GameState state) {
        List<Move> result = new ArrayList<>();
        for (TroopAction action : troop.actions(face)) {
            result.addAll(action.movesFrom(origin, side, state));
        }

        return result;
    }


    @Override
    public boolean canStepOn() {
        return troop == null;
    }

    @Override
    public boolean hasTroop() {
        return troop != null;
    }
}
