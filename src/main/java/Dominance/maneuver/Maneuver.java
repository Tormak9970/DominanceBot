package Dominance.maneuver;

import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;

public interface Maneuver {

    ControlsOutput exec(DataPacket data);

    boolean done();
}
