package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.maneuver.DodgeManeuver;
import Dominance.vector.Vec3;

public class KickoffState extends MoveToPointState {

    DodgeManeuver dodge = null;

    @Override
    public ControlsOutput exec(DataPacket data, Dominance bot) {

        target = Vec3.ZERO;
        targetSpeed = 2300;
        allowWaveDash = false;

        if (!data.isKickoff) {
            dodge = null;
            data.bot.state = new AtbaState();

        }

        if (data.car.position.dist(target) < 840) {
            if (dodge == null) dodge = new DodgeManeuver(data);
            dodge.target = data.ball.position;
            return dodge.exec(data);
        }
        return super.exec(data, bot);
    }
}
