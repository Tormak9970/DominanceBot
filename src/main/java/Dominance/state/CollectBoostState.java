package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;

public class CollectBoostState extends MoveToPointState{

    @Override
    public ControlsOutput exec(DataPacket data, Dominance bot) {

        target = data.closestFullBoost.getLocation();
        targetSpeed = 2300;

        if (data.car.boost >= 60) {
            data.bot.state = new AtbaState();
        }

        return super.exec(data, bot);
    }
}
