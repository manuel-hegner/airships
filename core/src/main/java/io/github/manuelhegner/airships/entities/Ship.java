package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Ship extends Thing<Ship, ShipType> {


	public Ship() {
		super(new ShipType());
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			direction.rotate(90*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			direction.rotate(-90*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			speed.mulAdd(direction, 80f*delta);
		}
		
	}
}
