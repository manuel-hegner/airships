package io.github.manuelhegner.airships.entities;

import org.checkerframework.checker.units.qual.Speed;
import org.lwjgl.util.vector.Vector2f;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import io.github.manuelhegner.airships.render.Renderable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public abstract class Thing<SELF extends Thing<SELF, TYPE>, TYPE extends ThingType<TYPE, SELF>> extends Physical {

	protected final TYPE type;
	
	
	public Thing(World box2d, TYPE type) {
		super(box2d.createBody(type.getBodyDef()));
		this.type = type;
	}
	
	@Override
	public Renderable update(float delta) {
		float drag = 0.5f
				* 0.2f //c_w
				* getVelocity().len2() //v²
				* 1.2f //air density
				* type.width * type.width; //area of thing
				//* delta //time
				/// 100000f; //mass of thing
		
		if(drag > 0) {
			applyForce(getVelocity().cpy().setLength(-drag));
		}
		
		return createRenderable();
	}
	
	protected abstract Renderable createRenderable();

	@Override
	public void dispose() {
		type.dispose();
	}
}
