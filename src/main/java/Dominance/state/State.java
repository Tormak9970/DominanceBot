package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;

public abstract class State {

    public abstract ControlsOutput exec(DataPacket data, Dominance bot);

}
