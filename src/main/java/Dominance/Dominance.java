package Dominance;

import Dominance.maneuver.Maneuver;
import Dominance.state.AtbaState;
import Dominance.state.KickoffState;
import Dominance.state.State;
import Dominance.state.TestState;
import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import Dominance.boost.BoostManager;
import Dominance.data.DataPacket;
import Dominance.data.ControlsOutput;

import java.awt.*;

import static Dominance.util.StaticRenderer.displayText;

public class Dominance implements Bot {

    private final int playerIndex;

    //public State state = new AtbaState();
    public State state = new TestState();
    public Maneuver currentManuever;
    public boolean isManuever = false;

    public void setManeuver(Maneuver m){
        currentManuever = m;
        isManuever = true;
    }

    public String getManeuverAsString(){
        return "Current Maneuver: " + currentManuever;
    }

    public Maneuver getManeuver(){
        return currentManuever;
    }


    public Dominance(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the ball.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput(DataPacket data) {
        //System.out.println("run");
        if(data.isKickoff && !(state instanceof KickoffState)) {
            //state = new KickoffState();
        }
        String posX = "X: " + data.car.position.x + "";
        String posY = "Y: " + data.car.position.y + "";
        String posZ = "Z: " + data.car.position.z + "";
        displayText(Color.WHITE, 100, 100, 1, posX, data);
        displayText(Color.WHITE, 100, 80, 1, posY, data);
        displayText(Color.WHITE, 100, 60, 1, posZ, data);

        if(isManuever){
            isManuever = !currentManuever.done();
            return currentManuever.exec(data);
        } else {
            return state.exec(data, this);
        }



    }

    @Override
    public int getIndex() {
        return this.playerIndex;
    }

    /**
     * This is the most important function. It will automatically get called by the framework with fresh data
     * every frame. Respond with appropriate controls!
     */
    @Override
    public ControllerState processInput(GameTickPacket packet) {

        if (packet.playersLength() <= playerIndex || packet.ball() == null || !packet.gameInfo().isRoundActive()) {
            // Just return immediately if something looks wrong with the data. This helps us avoid stack traces.
            return new ControlsOutput();
        }

        // Update the boost manager and tile manager with the latest data
        BoostManager.loadGameTickPacket(packet);

        // Translate the raw packet data (which is in an unpleasant format) into our custom DataPacket class.
        // The DataPacket might not include everything from GameTickPacket, so improve it if you need to!
        DataPacket dataPacket = new DataPacket(this, packet);



        // Do the actual logic using our dataPacket.
        return processInput(dataPacket);
    }

    @Override
    public void retire() {
        System.out.println(" Retiring Dominance#" + playerIndex);
    }
}
