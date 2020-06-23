package Dominance.util;

import Dominance.data.car.CarOrientation;
import Dominance.vector.Vec3;

public class RLMath {
    public static double sign(double v) {
        return v >= 0 ? 1 : -1;
    }

    public static Vec3 toLocal(Vec3 target, Vec3 from, CarOrientation orientation) {
        Vec3 carToTarget = target.minus(from);
        return new Vec3 (
                carToTarget.dot(orientation.foward),
                carToTarget.dot(orientation.left),
                carToTarget.dot(orientation.up)
        );
    }

    public static double not_zero(double d){
        return d == 0 ? Math.ulp(1) : d;
    }

    public static double clamp(double n, double a, double b){
        return Math.max(Math.min(n,b), a);
    }
}
