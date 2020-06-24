package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.data.car.CarData;
import Dominance.maneuver.HalfFlipManeuver;
import Dominance.maneuver.WaveDashManeuver;
import Dominance.util.RLMath;
import Dominance.util.StaticRenderer;
import Dominance.vector.Vec3;

import java.awt.*;

public class MoveToPointState extends State {

    public Vec3 target = Vec3.ZERO;
    public double targetSpeed = 1410;
    public boolean allowWaveDash = true;
    public boolean allowHalfFlip = true;
    Vec3 goalPos;
    WaveDashManeuver dash;
    HalfFlipManeuver flip;

    private double lastWaveDashEnd = 0;
    private double lastHalfFlipEnd = 0;
    double TIME_BETWEEN_WaveDashes = 3;
    double TIME_BETWEEN_HALFFLIP = 8.0;

    @Override
    public ControlsOutput exec(DataPacket data, Dominance bot) {

        ControlsOutput controls = new ControlsOutput();

        CarData myCar = data.car;
        Vec3 carPosition = myCar.position;

        //makes bot go down from wall
        Vec3 target = this.target;
        if (myCar.hasWheelContact && !myCar.isUpright) {
            target = carPosition.flatten().scaled(0.9);
        }


        StaticRenderer.displayVector(Color.RED, carPosition, target, data);
        //StaticRenderer.displayCurrentManeuver(Color.BLUE, 100, 160, 1, data);

        // Subtract the two positions to get a vector pointing from the car to the ball.
        Vec3 carToTarget = target.minus(carPosition);
        Vec3 localTarget = RLMath.toLocal(target, data.car.position, data.car.orientation);

        double dist = carToTarget.mag();
        double forwardDotTarget = myCar.orientation.foward.dot(carToTarget.normalized());

        //dodge conditions

        if (allowWaveDash
                && myCar.hasWheelContact
                && myCar.isUpright
                && dist > 2200
                && forwardDotTarget > 0.9
                && myCar.velocity.mag() > 650
                && data.time > lastWaveDashEnd + TIME_BETWEEN_WaveDashes) {
            //bot.setManeuver(dash = new WaveDashManeuver(data, target));
            //lastWaveDashEnd = data.time;
        }

        //halfFlip conditions

        if (allowHalfFlip
                && myCar.hasWheelContact
                && myCar.isUpright
                && dist > 2200
                && forwardDotTarget < -0.9
                && data.time > lastHalfFlipEnd + TIME_BETWEEN_HALFFLIP
                && !(bot.currentManuever == flip)) {
            //bot.setManeuver(flip = new HalfFlipManeuver(data, target));
            //lastHalfFlipEnd = data.time;
        }

        double currentSpeed = myCar.velocity.dot(carToTarget.normalized());
        if (currentSpeed < targetSpeed) {
            //we need to go faster
            controls.withThrottle(1.0);
            if (targetSpeed > 610  && currentSpeed + 60 < targetSpeed && forwardDotTarget > 0.8) {
                controls.withBoost(true);
            }
        }
        else {
            //we need to slow down
            double extraSpeed = currentSpeed - targetSpeed;
            controls.withThrottle(0.3 - extraSpeed / 500);

        }

        String speed = "Speed: " + currentSpeed;
        StaticRenderer.displayText(Color.GREEN, 100, 120, 1, speed, data);
        double carToBall = data.car.position.dist(data.ball.position);

        String carToBallDist = "Car to Ball: " + carToBall;
        StaticRenderer.displayText(Color.ORANGE, 100, 140, 1, carToBallDist, data);

        if(targetSpeed < 420 && carToBall < 320){
            controls.withThrottle(1.0);
        }

        double turningAngle = myCar.orientation.foward.angle(target.minus(myCar.position));
        double steerAngle = Math.atan2(localTarget.y, localTarget.x);
        if (Math.abs(steerAngle) > Math.PI / 2) {
            controls.withSlide();
        }
        controls.withSteer(-steerAngle * 5);

        return controls;
    }
}
