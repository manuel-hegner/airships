package io.github.manuelhegner.airships.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.manuelhegner.airships.entities.Ship;

public class BattleScene implements Scene {

	private List<Ship> ships = new ArrayList<>();
	
	public BattleScene() {
		for(int i=0;i<10;i++) {
			ships.add(new Ship());
			ships.get(i).getPosition().add(100*i, 0);
		}
	}
	
	@Override
	public void update(float delta) {
		for(Ship ship:ships)
			ship.update(delta);
	}

	@Override
	public void render(float delta, SpriteBatch batch) {
		for(Ship ship:ships)
			ship.render(delta, batch);
	}

	@Override
	public void dispose() {
		for(Ship ship:ships)
			ship.dispose();
	}

}
