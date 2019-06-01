package lin.thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {
    public SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> result = new ArrayList<>();
        //TilePos prev = origin;
        TilePos target = origin;
        for (int i = 0; i < state.board().dimension(); i++) {
            target = target.stepByPlayingSide(offset(), side);
            if (state.canStep(origin, target)) {
                result.add(new StepOnly(origin, ((BoardPos) target)));
            } else if (state.canCapture(origin, target)) {
                result.add(new StepAndCapture(origin, (BoardPos) target));
                return result;
            } else {
                return result;
            }
        }

        return result;
    }
}
