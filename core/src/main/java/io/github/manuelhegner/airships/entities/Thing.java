package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public abstract class Thing<SELF extends Thing<SELF, TYPE>, TYPE extends ThingType<TYPE, SELF>> extends Entity {

	protected final TYPE type;
	protected Vector2 position = new Vector2(0, 0);
	protected Vector2 direction = new Vector2(0, 1);
	protected Vector2 speed = new Vector2(0,0);
	
	@Override
	public void update(float delta) {
		float drag = 0.5f
				* 0.2f //c_w
				* speed.len2() //v²
				* 1.2f //air density
				* type.width * type.width //area of thing
				* delta //time
				/ 100000f; //mass of thing
		
		if(drag > 0) {
			speed.add(speed.cpy().nor().scl(-drag));
		}
		position.mulAdd(speed, delta);
	}
	
	@Override
	public void render(float delta, SpriteBatch batch) {
		type.render(delta, batch, (SELF)this);
	}
	
	@Override
	public void dispose() {
		type.dispose();
	}
}
