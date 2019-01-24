package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public abstract class Physical extends Entity {

	private static final float DEGREES_TO_RADIANS = 0.017453292519943295f;
	private static final float RADIANS_TO_DEGREES = 1f/DEGREES_TO_RADIANS;
	
	private final Body body;
	
	public void applyForce(Vector2 f) {
		if(!f.isZero()) {
			body.applyForceToCenter(f, true);
		}
	}
	
	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}
	
	public float getAngle() {
		return body.getAngle() * RADIANS_TO_DEGREES;
	}
	
	public float getAngleRad() {
		return body.getAngle();
	}
	
	public Vector2 getPosition() {
		return body.getWorldCenter();
	}
}
