package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.util.Controllers;
import Dominance.vector.Vec3;
import rlbot.manager.BotLoopRenderer;

import java.awt.*;

public class AerialState extends MoveToPointState{

    private Vec3 airCarToTarget;

    @Override
    public ControlsOutput exec(DataPacket data, Dominance bot) {

        ControlsOutput controls = new ControlsOutput();


        target = data.ball.position;
        airCarToTarget = target.minus(data.car.position);

        Controllers.pd_aerial_controller(airCarToTarget, Vec3.UP, data, controls);
        //BotLoopRenderer renderer = BotLoopRenderer.forBotLoop(data.bot);
        //renderer.drawLine3d(Color.white, data.car.position, airCarToTarget);



        return super.exec(data, bot);
    }
}
