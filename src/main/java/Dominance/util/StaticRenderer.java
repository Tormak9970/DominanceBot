package Dominance.util;

import Dominance.data.DataPacket;
import Dominance.vector.Vec3;
import rlbot.manager.BotLoopRenderer;

import java.awt.*;

public class StaticRenderer {

    public static void displayText(Color color, int x, int y, int size, String text, DataPacket data){
        rlbot.render.Renderer render = BotLoopRenderer.forBotLoop(data.bot);
        render.drawString2d(text, color, new Point(x, y), size, size);
    }

    public static void displayVector(Color color, Vec3 v1, Vec3 v2, DataPacket data){
        rlbot.render.Renderer render = BotLoopRenderer.forBotLoop(data.bot);
        render.drawLine3d(color, v1, v2);
    }

    public static void displayCurrentManeuver(Color color, int x, int y, int size, DataPacket data){
        String maneuver = data.bot.getManeuverAsString();

        rlbot.render.Renderer render = BotLoopRenderer.forBotLoop(data.bot);
        render.drawString2d(maneuver, color, new Point(x, y), size, size);
    }
}
