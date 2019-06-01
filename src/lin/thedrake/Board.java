package lin.thedrake;

public class Board {
    /**
     * Constructor. Create a square game board of the specified dimension,
     * where all the tiles are empty, then BoardTile.EMPTY
     *
     * @param dimension board size
     */
    public Board(int dimension) {
        //if(dimension < 0)
        //	throw new IllegalArgumentException("The dimension needs to be positive.");
        this.dimension = dimension;
        map = new BoardTile[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                map[i][j] = BoardTile.EMPTY;
            }
        }
    }

    private Board(int dimension, BoardTile[][] map) {
        this.dimension = dimension;
        this.map = map;
    }

    private final BoardTile[][] map;
    private final int dimension;

    /**
     * @return size of board
     */
    public int dimension() {
        return this.dimension;
    }

    /**
     * @param pos position on board
     * @return the tile at the selected position
     */
    public BoardTile at(BoardPos pos) {
        return map[pos.i()][pos.j()];
    }

    /**
     * @param ats new items
     * @return a new game board with new tiles. All other tiles remain the same
     */
    public Board withTiles(TileAt... ats) {
        BoardTile[][] result = new BoardTile[dimension][dimension];
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                result[i][j] = this.at(new BoardPos(this.dimension, i, j));
            }
        }
        for (TileAt item : ats) {
            result[item.pos.i()][item.pos.j()] = item.tile;
        }
        return new Board(dimension, result);
    }

    /**
     * @return an instance of the PositionFactory for production positions on this game board
     */
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

