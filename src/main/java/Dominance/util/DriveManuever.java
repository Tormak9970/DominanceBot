package Dominance.util;

public class DriveManuever {

    public double DriveManeuverDistance;
    public double DriveManeuverVelocity;
    public double DriveManeuverTime;
    public double DriveManeuverBoost;

    public DriveManuever(double dist, double vel, double time, double boost) {
        DriveManeuverDistance = dist;
        DriveManeuverVelocity = vel;
        DriveManeuverTime = time;
        DriveManeuverBoost = boost;
    }
}
