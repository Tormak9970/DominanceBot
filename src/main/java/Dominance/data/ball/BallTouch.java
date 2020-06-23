package Dominance.data.ball;


import rlbot.flat.Touch;
import Dominance.vector.Vec3;

/**
 * Basic information about the ball's latest touch.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class BallTouch {
	public final Vec3 position;
	public final Vec3 normal;
	public final String playerName;
	public final float gameSeconds;
	public final int playerIndex;
	public final int team;

	public BallTouch(final Touch touch) {
		this.position = new Vec3(touch.location());
		this.normal = new Vec3(touch.normal());
		this.playerName = touch.playerName();
		this.gameSeconds = touch.gameSeconds();
		this.playerIndex = touch.playerIndex();
		this.team = touch.team();
	}
}
