package io.github.manuelhegner.airships.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import io.github.manuelhegner.airships.entities.Ship;
import io.github.manuelhegner.airships.render.GroupRenderable;
import io.github.manuelhegner.airships.render.GroupRenderable.GroupRenderableBuilder;

public class BattleScene extends Scene {

	private World box2d = new World(Vector2.Zero, true);
	private List<Ship> ships = new ArrayList<>();
	
	public BattleScene() {
		for(int i=0;i<10;i++) {
			ships.add(new Ship(box2d));
			ships.get(i).getPosition().add(100*i, 0);
		}
	}
	
	@Override
	public GroupRenderable update(float delta) {
		GroupRenderableBuilder<?, ?> group = GroupRenderable.builder();
		group.luid(luid);
		for(Ship ship:ships)
			group.item(ship.update(delta));
		return group.build();
	}

	@Override
	public void dispose() {
		for(Ship ship:ships)
			ship.dispose();
	}
}