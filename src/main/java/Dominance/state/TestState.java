package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.maneuver.HalfFlipManeuver;
import Dominance.maneuver.WaveDashManeuver;
import Dominance.util.Controllers;
import Dominance.vector.Vec3;
import rlbot.cppinterop.RLBotDll;
import rlbot.gamestate.*;
import rlbot.manager.BotLoopRenderer;

import java.awt.*;

public class TestState extends State {

    public TestState(){

    }
    @Override

    public ControlsOutput exec(DataPacket data, Dominance bot) {
        ControlsOutput controls = new ControlsOutput();
        bot.setManeuver(new WaveDashManeuver(data, data.car.orientation.foward.flatten()));

        return controls;
    }

    /*private double lastTime = 0.0;
    private double stateSetLastTime = 0.0;
    private Vec3 target;
    private Vec3 randVec;
    private DesiredVector3 dRandVec;

    if (data.time - lastTime > 5){
            lastTime = data.time;
            randVec = new Vec3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
            randVec = randVec.scaledToMag(250.0);

            dRandVec = new DesiredVector3((float) randVec.x, (float) randVec.y, (float) randVec.z);
            dRandVec.withZ(dRandVec.getZ() + 1000f);

        }
        ControlsOutput controls = new ControlsOutput();



        GameState game = new GameState();
        CarState car = new CarState();
        BallState ball = new BallState();
        PhysicsState physics = new PhysicsState();
        PhysicsState bPhysics = new PhysicsState();
        DesiredVector3 dVec3 = new DesiredVector3(0f, 0f, 1000f);
        DesiredVector3 dVelocity = new DesiredVector3(0f, 0f, 0f);

        ball.withPhysics(bPhysics.withLocation(dRandVec).withVelocity(dVelocity));
        car.withPhysics(physics.withLocation(dVec3).withVelocity(dVelocity));

        game.withBallState(ball);
        game.withCarState(bot.getIndex(), car);

        game.withGameInfoState(new GameInfoState().withWorldGravityZ(0.00001f));
        if (data.time - stateSetLastTime > 5){
            stateSetLastTime = data.time;
            RLBotDll.setGameState(game.buildPacket());
        }
     */
}
