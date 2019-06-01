package lin.thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameState implements JSONSerializable {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(Board board, Army blueArmy, Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(Board board, Army blueArmy, Army orangeArmy, PlayingSide sideOnTurn, GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }
        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;
        return blueArmy;
    }

    /**
     * @param pos position of tile
     * @return null if not free or Tile
     */
    public Tile tileAt(BoardPos pos) {
        if (pos.equals(TilePos.OFF_BOARD)) return BoardTile.MOUNTAIN;
        if (orangeArmy.boardTroops().at(pos).isPresent()) {
            return orangeArmy.boardTroops().at(pos).get();
        }
        if (blueArmy.boardTroops().at(pos).isPresent()) {
            return blueArmy.boardTroops().at(pos).get();
        }
        return board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        if (origin.equals(TilePos.OFF_BOARD)) return false;
        if (result != GameResult.IN_PLAY) return false;
        if (orangeArmy.boardTroops().isPreparatoryPhase()) return false;
        if (blueArmy.boardTroops().isPreparatoryPhase()) return false;
        return armyOnTurn().boardTroops().at(origin).isPresent();
    }

//	private boolean canStepTo(TilePos target) {
//		if (target.equals(TilePos.OFF_BOARD)) return false;
//		return result == GameResult.IN_PLAY &&
//			   orangeArmy.boardTroops().at(target).isEmpty() &&
//			   blueArmy.boardTroops().at(target).isEmpty() &&
//			   board.at((BoardPos)target).canStepOn();
//	}

    private boolean canStepTo(TilePos target) {
        if (result != GameResult.IN_PLAY)
            return false;

        if (target == TilePos.OFF_BOARD)
            return false;

        return tileAt((BoardPos) target).canStepOn();
    }


    private boolean canCaptureOn(TilePos target) {
        if (target.equals(TilePos.OFF_BOARD)) return false;
        return result == GameResult.IN_PLAY && armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        if (!canStepTo(target)) return false;
        if (armyOnTurn().stack().isEmpty()) return false;
        if (!armyOnTurn().boardTroops().isLeaderPlaced()) {
            if (sideOnTurn == PlayingSide.ORANGE) {
                return target.j() == board.dimension() - 1;
            } else {
                return target.j() == 0;
            }
        }
        if (armyOnTurn().boardTroops().isPreparatoryPhase()) {
            for (TilePos item : armyOnTurn().boardTroops().leaderPosition().neighbours()) {
                if (target.equals(item)) {
                    return true;
                }
            }
            return false;
        }
        for (BoardPos item : armyOnTurn().boardTroops().troopPositions()) {
            for (BoardPos position : item.neighbours()) {
                if (target.equals(position)) {
                    return true;
                }
            }
        }
        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(armyNotOnTurn(), armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(armyNotOnTurn().removeTroop(target), armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }
        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(armyNotOnTurn().removeTroop(target), armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }
        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(armyNotOnTurn(), armyOnTurn().placeFromStack(target), GameResult.IN_PLAY);
        }
        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(armyNotOnTurn(), armyOnTurn(), GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(armyOnTurn(), armyNotOnTurn(), GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        String json = "{" +
                resultToJSON() + "," +
                boardToJSON() + "," +
                armyToJSON(true) + "," +
                armyToJSON(false) +
                "}";
        writer.printf(json);
    }

    private String resultToJSON() {
        if (result == GameResult.IN_PLAY) return "\"result\":\"IN_PLAY\"";
        if (result == GameResult.VICTORY) return "\"result\":\"VICTORY\"";
        return "\"result\":\"DRAW\"";
    }

    private String boardToJSON() {
        StringBuilder json = new StringBuilder();
        json.append("\"board\":{\"dimension\":").append(board.dimension()).append(",\"tiles\":[");
        for (int i = 0; i < board.dimension(); i++) {
            for (int j = 0; j < board.dimension(); j++) {
                if ((board.at(new BoardPos(board.dimension(), j, i)).equals(BoardTile.EMPTY))) {
                    json.append("\"empty\",");
                } else {
                    json.append("\"mountain\",");
                }
            }
        }
        json.deleteCharAt(json.length() - 1);
        json.append("]}");
        return json.toString();
    }

    private String armyToJSON(boolean blueArmy) {
        StringBuilder json = new StringBuilder();
        Army army;
        if (blueArmy) {
            army = this.blueArmy;
            json.append("\"blueArmy\":{\"boardTroops\":{");
        } else {
            army = this.orangeArmy;
            json.append("\"orangeArmy\":{\"boardTroops\":{");
        }
        json.append("\"side\":").append(army.boardTroops().playingSide() == PlayingSide.BLUE ? "\"BLUE\"," :
                "\"ORANGE\",");
        json.append("\"leaderPosition\":\"").append(army.boardTroops().leaderPosition().toString()).append("\",");

        json.append("\"guards\":").append(army.boardTroops().guards()).append(",");

        json.append("\"troopMap\":{");
        List<BoardPos> sl = new ArrayList<>(army.boardTroops().troopPositions());
        sl.sort((o1, o2) -> {
            if (o1.i() > o2.i()) return 1;
            if (o1.i() < o2.i()) return -1;
            return Integer.compare(o1.j(), o2.j());
        });
        for (BoardPos item : sl) {
            json.append("\"").append(item.toString()).append("\":{");
            TroopTile tile = army.boardTroops().at(item).isPresent() ? army.boardTroops().at(item).get() : null;
            if (tile != null) {
                json.append("\"troop\":\"").append(tile.troop().name()).append("\",");
                json.append("\"side\":\"").append(tile.side() == PlayingSide.BLUE ? "BLUE\"," : "ORANGE\",");
                json.append("\"face\":\"").append(tile.face() == TroopFace.AVERS ? "AVERS\"}," : "REVERS\"},");
            }
        }
        if (!army.boardTroops().troopPositions().isEmpty()) json.deleteCharAt(json.length() - 1);
        json.append("}},");

        json.append("\"stack\":[");
        for (Troop item : army.stack()) {
            json.append("\"").append(item.name()).append("\",");
        }
        if (!army.stack().isEmpty()) json.deleteCharAt(json.length() - 1);
        json.append("],");

        json.append("\"captured\":[");
        for (Troop item : army.captured()) {
            json.append("\"").append(item.name()).append("\",");
        }
        if (!army.captured().isEmpty()) json.deleteCharAt(json.length() - 1);
        json.append("]}");
        return json.toString();
    }
}