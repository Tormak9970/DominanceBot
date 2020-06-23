package Dominance.maneuver;

import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.util.Controllers;
import Dominance.util.RLMath;
import Dominance.vector.Vec3;

public class HalfFlipManeuver implements Maneuver {

    public Vec3 target;
    double roleDelay = 0.4;

    boolean done = false;
    double startTime;

    double firstJumpDur = 0.11;
    //double firstJumpDur = 0.21;
    double postFirstJumpPauseDur = 0.025;

    double secondJumpDur = 0.2;
    double postSecondJumpPauseDur = 1.2;

    public HalfFlipManeuver(DataPacket data) {
        this.target = new Vec3(-data.car.orientation.foward.x, -data.car.orientation.foward.y, data.car.orientation.foward.z);
        startTime = data.time;
    }

    public HalfFlipManeuver(DataPacket data, Vec3 target) {
        startTime = data.time;
        this.target = target;
    }

    @Override
    public ControlsOutput exec(DataPacket data) {

        double t_firstJumpEnd = firstJumpDur;
        double t_secondJumpBegin = t_firstJumpEnd + postFirstJumpPauseDur;
        double t_secondJumpEnd = t_secondJumpBegin + secondJumpDur;
        double t_halfFlipEnd = t_secondJumpEnd + postSecondJumpPauseDur;

        double currentTime = data.time - startTime;

        ControlsOutput controls = new ControlsOutput();
        controls.withThrottle(-1.0);

        if (currentTime < t_firstJumpEnd) {
            controls.withThrottle(-1.0);
            controls.withJump(true);

        } else if (t_firstJumpEnd <= currentTime && currentTime < t_secondJumpBegin) {
            //Wait
        }else if (t_secondJumpBegin <= currentTime && currentTime < t_secondJumpBegin + 0.12) {
            if (target == null) {
                controls.withPitch(1.0);

            } else {
                Vec3 localTarget = RLMath.toLocal(target, data.car.position, data.car.orientation);
                Vec3 direction = localTarget.withZ(0).normalized();

                controls.withPitch(-direction.x);

            }
            if (currentTime >= t_secondJumpBegin) {
                controls.withPitch(1.0);
                controls.withJump(true);
            }
        } else if (t_secondJumpBegin + 0.12 <= currentTime && currentTime < t_halfFlipEnd) {
            Controllers.pd_aerial_controller(target, Vec3.UP, data, controls);

            if(data.car.orientation.up.dot(Vec3.UP) == 1){
                controls.withBoost();
            }

        } else if (currentTime >= t_halfFlipEnd) {
            done = data.car.hasWheelContact;
        }

        return controls;
    }

    @Override
    public boolean done() {
        return done;
    }
}
