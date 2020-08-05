package Dominance.state;

import Dominance.Dominance;
import Dominance.data.ControlsOutput;
import Dominance.data.DataPacket;
import Dominance.data.car.CarData;
import Dominance.util.Kinematics;
import Dominance.util.StaticRenderer;
import Dominance.vector.Vec3;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.BallPrediction;

import java.awt.*;
import java.util.ArrayList;

public class AtbaState extends MoveToPointState {

    boolean isReachable;
    boolean startDefense;
    boolean oppHasPossession;
    ArrayList<Vec3> teammates = new ArrayList<>();
    ArrayList<Vec3> opponents = new ArrayList<>();

    @Override
    public ControlsOutput exec(DataPacket data, Dominance bot) {

        Vec3 location;
        Vec3 carPos = data.car.position;
        target = data.ball.position;
        double targetTime = 0;
        int team = data.car.team;
        Vec3 idealContactPoint = Vec3.ZERO;
        Vec3 ballScoringVec = Vec3.ZERO;
        Vec3 getToScoringPos1 = Vec3.ZERO;
        Vec3 getToScoringPos2 = Vec3.ZERO;
        Vec3 endPoint = Vec3.ZERO;


        if(team == 1){
            startDefense = (data.car.position.y < data.ball.position.y) && (data.car.position.y > 1048);

        } else {
            startDefense = (data.car.position.y > data.ball.position.y) && (data.car.position.y < -1048);
        }

        if(startDefense && !(data.bot.state instanceof DefenseState)){/*data.bot.state = new DefenseState();*/}

        if(data.team == 0){
            goalPos = data.orangeGoalPos;
        }else{
            goalPos = data.blueGoalPos;
        }
        try {
            BallPrediction prediction = RLBotDll.getBallPrediction();
            for (int i = 0; i < 360; i++) {
                rlbot.flat.PredictionSlice slice = prediction.slices(i);
                rlbot.flat.Vector3 tempLocation = slice.physics().location();
                Vec3 castedTemp = Vec3.cast(tempLocation);
                double delta = slice.gameSeconds() - data.time;
                ballScoringVec = goalPos.minus(castedTemp);
                idealContactPoint = target.add(ballScoringVec.scaledToMag(92.75));
                if (tempLocation.z() < 150 && Kinematics.timeFromLength((idealContactPoint.minus(carPos).mag()), data.car.velocity.mag(), data.car.boost) < delta) {
                    getToScoringPos1 = data.ball.position.minus(carPos).cross(Vec3.UP).scaledToMag(550).add(data.ball.position.scaled(-1));
                    getToScoringPos2 = data.ball.position.minus(carPos).cross(Vec3.UP).scaledToMag(-550).add(data.ball.position.scaled(-1));
                    endPoint = data.ball.position.add(ballScoringVec.scaledToMag(500));
                    isReachable = true;
                    target = idealContactPoint;
                    targetTime = delta;
                    break;
                }
            }
            if(!isReachable) {
                target = data.ball.position;
            }
        } catch (Exception e) {/* Prediction is not available, so we fall back to current location */}

        if(target.equals(idealContactPoint) && data.car.orientation.foward.dot(ballScoringVec.scaled(-1).normalized()) >= .2){
            //System.out.println("would turn around here. angle is: " + data.car.orientation.foward.angleInDegrees(ballScoringVec) + " degrees.");
            if (getToScoringPos1.normalized().dot(ballScoringVec.scaled(-1).normalized()) > getToScoringPos2.normalized().dot(ballScoringVec.scaled(-1).normalized())){
                target = getToScoringPos1;
            } else {
                target = getToScoringPos2;
            }

        }



        //StaticRenderer.displayVector(Color.WHITE, data.ball.position, target, data);
        StaticRenderer.displayVector(Color.PINK, data.car.position, data.car.orientation.foward.scaledToMag(500).add(data.car.position.scaled(-1)), data);
        StaticRenderer.displayVector(Color.YELLOW, data.ball.position, getToScoringPos2, data);
        StaticRenderer.displayVector(Color.WHITE, data.car.position, getToScoringPos1, data);
        StaticRenderer.displayVector(Color.ORANGE, data.ball.position, getToScoringPos1, data);
        StaticRenderer.displayVector(Color.GREEN, data.ball.position, ballScoringVec, data);
        StaticRenderer.displayVector(Color.CYAN, data.car.position, idealContactPoint, data);

        targetSpeed = (target.minus(carPos).flatten().mag())/Math.max(targetTime, 0.001);
        if(data.ball.position.dist(carPos) > 600){
            super.canSpeedUp = true;
        }else{
            super.canSpeedUp = false;
            targetSpeed += 600;
        }


        for (CarData otherCar : data.allCars) {
            if (otherCar.team != data.car.team) {
                // An enemy
                opponents.add(otherCar.position);
            } else {
                //a teammate
                teammates.add(otherCar.position);
            }
        }

        oppHasPossession = false;

        for(int i = 0; i < opponents.size(); i++){

            if(opponents.get(i).x - data.ball.position.x < 10 && opponents.get(i).y - data.ball.position.y < 10){
                oppHasPossession = true;
                break;
            }

        }

        if (data.car.boost <= 30 && !oppHasPossession) {/*data.bot.state = new CollectBoostState();*/}
        return super.exec(data, bot);
    }
}


