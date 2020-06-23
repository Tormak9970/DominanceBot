package Dominance.maneuver;

import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.util.Controllers;
import Dominance.vector.Vec3;

public class WaveDashManeuver implements Maneuver {

    public Vec3 target;

    boolean done = false;
    double startTime;
    double jumpDur = 0.03;
    boolean backWheelContact = false;


    public WaveDashManeuver(DataPacket data){
        startTime = data.time;
        this.target = data.car.orientation.foward;
    }

    public WaveDashManeuver(DataPacket data, Vec3 target){
        startTime = data.time;
        this.target = target;
    }

    @Override
    public ControlsOutput exec(DataPacket data) {

        ControlsOutput controls = new ControlsOutput();
        controls.withSlide();
        controls.withThrottle(1.0);

        double currentTime = data.time - startTime;
        double t_JumpEnd = jumpDur;

        if (currentTime < t_JumpEnd){

            controls.withJump();
        }

        //Vec3 targetAngle = new Vec3(data.car.position.x + 50, data.car.position.y,data.car.position.z + 175);
        ///Vec3 targetAngle = data.car.orientation.foward.flatten().add(Vec3.UP.scaled(-0.4));
        Vec3 carToTarget = target.minus(data.car.position);
        Vec3 targetAngle = carToTarget.flatten().normalized().add(Vec3.UP.scaled(0.4));
        Controllers.pd_aerial_controller(targetAngle, Vec3.UP, data, controls);

        backWheelContact = data.car.position.z - data.car.orientation.foward.z * data.car.length < 16  && data.car.velocity.z < 0;

        if (backWheelContact && currentTime > t_JumpEnd && !data.car.hasWheelContact){
            controls.withJump();
        }

        if(currentTime > t_JumpEnd && data.car.hasWheelContact){
            done = true;
        }

        return controls;
    }

    @Override
    public boolean done() {
        return done;
    }
}
