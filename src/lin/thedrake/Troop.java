package lin.thedrake;

import java.util.List;

/**
 * Represents fight game unit
 */

public class Troop {
    private final String name;
    private final Offset2D aversePivot;
    private final Offset2D reversePivot;
    private final List<TroopAction> aversActions;
    private final List<TroopAction> reversActions;

    public String name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face) {
        switch (face) {
            case AVERS:
                return aversePivot;
            case REVERS:
                return reversePivot;
        }
        return null;
    }

    public List<TroopAction> actions(TroopFace face) {
        return face == TroopFace.AVERS ? aversActions : reversActions;
    }

    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversePivot = aversPivot;
        this.reversePivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.reversePivot = pivot;
        this.aversePivot = pivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.reversePivot = new Offset2D(1, 1);
        this.aversePivot = new Offset2D(1, 1);
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

}
