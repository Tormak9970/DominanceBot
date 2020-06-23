package Dominance.util;

import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.vector.Vec3;

public class Controllers {

    public static void pd_aerial_controller(Vec3 forward, Vec3 up, DataPacket data, ControlsOutput controls) {



        Vec3 localAngularVelocity = RLMath.toLocal(data.car.angularVelocity, Vec3.ZERO, data.car.orientation);
        Vec3 localForward = RLMath.toLocal(forward, Vec3.ZERO, data.car.orientation);
        Vec3 localUp = RLMath.toLocal(up, Vec3.ZERO, data.car.orientation);

        double yawAngle = Math.atan2(localForward.y, localForward.x);
        double rollAngle = Math.atan2(localUp.y, localUp.z);
        double pitchAngle = Math.atan2(localForward.z, localForward.x);

        if(up.magSqr() < 0.1 || forward.normalized().dot(data.car.orientation.foward) < 0.8) {
            rollAngle = 0;
        }

        //double P = 4;
        //double D = 1;

        double P = 6;
        double D = 1.5;
        double rollP = .75;
        double rollD = 0.5;

        double yaw = -localAngularVelocity.z * D + -yawAngle * P;
        double pitch = -localAngularVelocity.y * D + pitchAngle * P;
        double roll = -localAngularVelocity.z * rollD + -rollAngle * rollP;

        controls.withPitch(RLMath.clamp(pitch, -1, 1));
        controls.withYaw(RLMath.clamp(yaw, -1, 1));
        controls.withRoll(RLMath.clamp(roll, -1, 1));

    }

}
