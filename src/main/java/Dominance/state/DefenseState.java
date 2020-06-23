package Dominance.state;


import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.maneuver.HalfFlipManeuver;
import Dominance.vector.Vec3;

public class DefenseState extends MoveToPointState {
    @Override
    public ControlsOutput exec(DataPacket data, Dominance bot) {
        HalfFlipManeuver flip = null;
        int team = data.car.team;
        target = data.closestFullBoost.getLocation();
        targetSpeed = 2300;
        double defended;
        boolean isOrange;
        if(team == 1){
            defended = 500.0;
            isOrange = true;
        } else {
            defended = -500.0;
            isOrange = false;
        }

        Vec3 boostPad29 = new Vec3(-3072.0,  4096.0, 73.0);
        Vec3 boostPad30 = new Vec3(3072.0,  4096.0, 73.0);
        Vec3 boostPad3 = new Vec3(-3072.0, -4096.0, 73.0);
        Vec3 boostPad4 = new Vec3(3072.0, -4096.0, 73.0);

        if(isOrange){
            if(data.car.position.dist(boostPad29) > data.car.position.dist(boostPad30)){
                target = boostPad29;
            } else {
                target = boostPad30;
            }
        } else {
            if(data.car.position.dist(boostPad3) > data.car.position.dist(boostPad4)){
                target = boostPad3;
            } else {
                target = boostPad4;
            }
        }

        

        if ((data.ball.position.y > defended && isOrange) || (data.ball.position.y < defended && !isOrange)) {
            data.bot.state = new AtbaState();
        }

        return super.exec(data, bot);
    }
}
