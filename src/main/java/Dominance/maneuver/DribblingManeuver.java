package Dominance.maneuver;

import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;

public class DribblingManeuver implements Maneuver{
    @Override
    public ControlsOutput exec(DataPacket data) {
        return null;
    }

    @Override
    public boolean done() {
        return false;
    }
}
