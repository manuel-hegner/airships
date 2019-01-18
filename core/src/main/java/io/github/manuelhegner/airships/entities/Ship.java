package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.render.ShipRenderable;
import io.github.manuelhegner.airships.scenes.Scene;

public class Ship extends Thing<Ship, ShipType> {
	public Ship() {
		super(new ShipType());
	}

	@Override
	public Renderable update(float delta) {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			rotation+=(90*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			rotation-=(90*delta);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			speed.mulAdd(new Vector2(0,1).rotate(rotation), 80f*delta);
		}
		return super.update(delta);
	}

	@Override
	protected ShipRenderable createRenderable() {
		return ShipRenderable.builder()
			.luid(luid)
			.type(type)
			.translate(position.cpy())
			.rotate(rotation)
			.build();
	}
}
