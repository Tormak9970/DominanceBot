package Dominance.util;

public class Kinematics {

    public static double timeFromLength(double distance, double initialVelocity, double boost){

        //return distance/Math.max(initialVelocity, 300.0);
        return from_length(distance, initialVelocity, boost).DriveManeuverTime;
    }

    //constants

    public static double BOOST_CONSUMPTION_RATE = 33.3;
    public static double INV_BOOST_CONSUMPTION_RATE = 1 / BOOST_CONSUMPTION_RATE;
    public static double BOOST_ACCELERATION = 1066;
    public static double MAX_CAR_VEL = 2300;
    public static double INV_MAX_CAR_VEL = 1 / MAX_CAR_VEL;
    public static double TOP_DRIVE_VEL = 1410;
    public static AccelModel[] ACCELERATION_LUT = LUT.accelTable;
    public static AccelModel[] BOOST_ACCELERATION_LUT = LUT.boostAccelTable;

    public static DriveManuever from_length(double length, double initialV, double boost) {

        double no_boost_time = boost * INV_BOOST_CONSUMPTION_RATE;
        AccelModel initial_conditions = LUT.getVelocity(BOOST_ACCELERATION_LUT, initialV);
        AccelModel no_boost = LUT.getTime(BOOST_ACCELERATION_LUT, initial_conditions.AccelModelTime + no_boost_time);

        //assume we have boost the entire time
        AccelModel end_loc_1 = LUT.getPosition(BOOST_ACCELERATION_LUT, initial_conditions.AccelModelPos + length);

        if (end_loc_1.AccelModelTime > no_boost.AccelModelTime){
            if (no_boost.AccelModelVel > TOP_DRIVE_VEL){
                double extra_time = (length - (no_boost.AccelModelPos - initial_conditions.AccelModelPos)) / no_boost.AccelModelVel;
                return new DriveManuever(length, no_boost.AccelModelVel, no_boost.AccelModelTime + extra_time, boost);
            }else {
                AccelModel initial_no_boost = LUT.getVelocity(ACCELERATION_LUT, no_boost.AccelModelVel);
                AccelModel final_no_boost = LUT.getPosition(ACCELERATION_LUT, initial_no_boost.AccelModelPos + length - (no_boost.AccelModelPos - initial_conditions.AccelModelPos));
                //extra_time = (length - (final_no_boost.position - initial_no_boost.position +
                //initial_conditions.position - no_boost.position)) /final_no_boost.velocity

                return new DriveManuever(length, final_no_boost.AccelModelVel, no_boost.AccelModelTime - initial_conditions.AccelModelTime
                        + initial_no_boost.AccelModelTime - final_no_boost.AccelModelTime, boost);
            }
        }else{
            double extra_time = (length - (end_loc_1.AccelModelPos - initial_conditions.AccelModelPos)) / RLMath.not_zero(end_loc_1.AccelModelVel);
            double time = end_loc_1.AccelModelTime - initial_conditions.AccelModelTime;
            return new DriveManuever(length, end_loc_1.AccelModelVel, time + extra_time, time * BOOST_CONSUMPTION_RATE);
        }
    }



}
