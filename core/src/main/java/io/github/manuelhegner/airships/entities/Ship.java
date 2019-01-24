package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.render.ShipRenderable;
import io.github.manuelhegner.airships.scenes.Scene;

public class Ship extends Thing<Ship, ShipType> {
	public Ship(World box2d) {
		super(box2d, new ShipType());
	}

	@Override
	public Renderable update(float delta) {
		/*if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			rotation+=(90*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			rotation-=(90*delta);
		}*/
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			applyForce(new Vector2(0,1).rotateRad(getAngleRad()).scl(80f));
		}
		return super.update(delta);
	}

	@Override
	protected ShipRenderable createRenderable() {
		return ShipRenderable.builder()
			.luid(luid)
			.type(type)
			.translate(getPosition().cpy())
			.rotate(getAngle())
			.build();
	}
}
