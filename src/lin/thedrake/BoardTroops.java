package lin.thedrake;

import java.util.*;

/**
 * Represents game board.
 * Implements interaction with tiles on game field.
 */


public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    /**
     * Constructor
     * Uses on game start
     *
     * @param playingSide orange or blue
     */
    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    /**
     * Implicit constructor
     * Uses during the game
     *
     * @param playingSide    orange or blue
     * @param troopMap       map of game unit tiles on game field
     * @param leaderPosition actual leader tile position
     * @param guards         quantity of guards
     */
    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap,
                       TilePos leaderPosition, int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    /**
     * @param pos position of tile
     * @return If position isn't empty returns tile on current position
     */
    public Optional<TroopTile> at(TilePos pos) {
        if (troopMap.containsKey(pos))
            return Optional.of(troopMap.get(pos));
        return Optional.empty();
    }


    /**
     * @return playing side of current board
     */
    public PlayingSide playingSide() {
        return this.playingSide;
    }


    /**
     * @return actual leader position
     */
    public TilePos leaderPosition() {
        return leaderPosition;
    }


    /**
     * @return actual guards quantity
     */
    public int guards() {
        return guards;
    }


    /**
     * @return state of leader
     * true - placed
     * false - unplaced
     */
    public boolean isLeaderPlaced() {
        return !leaderPosition.equals(TilePos.OFF_BOARD);
    }


    /**
     * @return is allowed to multiPlayerGamePlace guards
     */
    public boolean isPlacingGuards() {
        return (!leaderPosition.equals(TilePos.OFF_BOARD) && guards < 2);
    }


    boolean isPreparatoryPhase() {
        return !(isLeaderPlaced() && guards > 1);
    }

    /**
     * @return set of already places positions on game field
     */
    public Set<BoardPos> troopPositions() {
        Set<BoardPos> troopPositions = new HashSet<>();
        for (Map.Entry<BoardPos, TroopTile> entry : troopMap.entrySet()) {
            troopPositions.add(entry.getKey());
        }
        return troopPositions;
    }


    /**
     * Places troop on given position
     *
     * @param troop  new troop
     * @param target placement of new troop
     * @return new instance of game field
     */
    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (troopMap.containsKey(target))
            throw new IllegalArgumentException("Another element is placed on this tile");

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));

        return (new BoardTroops(playingSide, newTroopMap,
                (leaderPosition.equals(TilePos.OFF_BOARD)) ? target : leaderPosition,
                (!leaderPosition.equals(TilePos.OFF_BOARD) && guards < 2) ? guards + 1 : guards));
        //by the game rules guards can be placed only after leader
        //and quantity can't be more than 2
    }


    /**
     * Replaces troop on another position
     *
     * @param origin origin position on troop
     * @param target target position on troop
     * @return new instance of game field
     */
    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (leaderPosition == TilePos.OFF_BOARD)
            throw new IllegalStateException("Board is not ready : Leader wasn't set");
        if (guards < 2)
            throw new IllegalStateException("Board is not ready : Few guards");

        if (troopMap.get(origin) == null)
            throw new IllegalArgumentException("Origin position is empty");

        if (troopMap.get(target) != null)
            throw new IllegalArgumentException("Target position is not empty");

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile originTroopTile = newTroops.remove(origin);

        newTroops.put(target, originTroopTile.flipped());

        return (new BoardTroops(playingSide, newTroops,
                (leaderPosition.equals(origin)) ? target : leaderPosition, //if leader moved his position
                guards));                                                  //must be updated
    }


    /**
     * Flips troop on given position
     *
     * @param origin position of troop on action
     * @return new instance of game field
     */
    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }


    /**
     * Removes troop from given position
     *
     * @param target position of troop on action
     * @return new instance of game field
     */
    public BoardTroops removeTroop(BoardPos target) {
        if (leaderPosition.equals(TilePos.OFF_BOARD))
            throw new IllegalStateException("Board is not ready : Leader wasn't set");
        if (guards < 2)
            throw new IllegalStateException("Board is not ready : Few guards");
        if (troopMap.get(target) == null)
            throw new IllegalArgumentException("Target position is empty");

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);


        return new BoardTroops(playingSide(), newTroops,
                (leaderPosition.equals(target)) ? TilePos.OFF_BOARD : leaderPosition, //if leader is removed
                guards);                                                              //must be updated attribute
        //of instance
    }
}
