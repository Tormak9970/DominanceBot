package Dominance.util;

public class AccelModel {

    public double AccelModelTime;
    public double AccelModelVel;
    public double AccelModelPos;
    public AccelModel(double time, double vel, double pos){
        AccelModelPos = pos;
        AccelModelVel = vel;
        AccelModelTime = time;
    }

    public static AccelModel fromString(String str){
        double time;
        double vel;
        double pos;
        int firstNumEnd = str.indexOf(",");
        int secondNumEnd = str.lastIndexOf(",");

        time = Double.parseDouble(str.substring(0, firstNumEnd));
        vel = Double.parseDouble(str.substring(firstNumEnd + 1, secondNumEnd));
        pos = Double.parseDouble(str.substring(secondNumEnd + 1));

        return new AccelModel(time, vel, pos);
    }
}
