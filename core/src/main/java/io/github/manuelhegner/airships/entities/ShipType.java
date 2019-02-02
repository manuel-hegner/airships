package io.github.manuelhegner.airships.entities;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.util.Point;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.util.concurrent.AtomicDouble;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter @RequiredArgsConstructor @SuperBuilder
public class ShipType extends ThingType<ShipType, Ship> {
	private final File image;
	private final String name;
	private final int length;
	private final float width;
	private final float height;
	private final float centerX;
	private final float centerY;
	private final BodyDef body;
	@Singular
	private final List<Point[]> shapes;
	private List<Float> dragCoefficients;
	
	/*public ShipType() {
		img = new TextureRegion(new Texture(Gdx.files.local("assets/airship.png")));
		img.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}
	
	@Override
	public void dispose() {
		img.getTexture().dispose();
	}*/

	@Override
	protected BodyDef getBodyDef() {
		return null;
	}

	public void calculateDragCoefficients() {
		World world = new World(Vector2.Zero, false);
		body.type = BodyType.DynamicBody;
		
		Body phys = world.createBody(body);
		for(Point[] sh : shapes) {
			PolygonShape pol = new PolygonShape();
			pol.set(Arrays.stream(sh).map(p->new Vector2(p.getX(), p.getY())).toArray(Vector2[]::new));
			phys.createFixture(pol, 1000);
			pol.dispose();
		}
		
		System.out.println("\t\tcenter: "+phys.getMassData().center);
		System.out.println("\t\tmass: "+phys.getMassData().mass+"kg");
		
		AtomicDouble result = new AtomicDouble();
		AtomicInteger raysHit = new AtomicInteger();
		for(int x=0;x<width*10;x++) {
			world.rayCast(
				(Fixture fixture, Vector2 point, Vector2 normal, float fraction) -> {
					raysHit.incrementAndGet();
					float angle = normal.angle(new Vector2(0,-1));
					result.addAndGet(Math.cos(normal.angleRad(new Vector2(0,-1))));
					return 0;
				},
				x/10f, -50,
				x/10f, 150
			);
		}
		result.set(result.get()/raysHit.get());
		dragCoefficients = Collections.singletonList((float)result.get());
		System.out.println("\t\tc_w = "+result.get());
		
		world.dispose();
	}
}
