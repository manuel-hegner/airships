package io.github.manuelhegner.airships.entities;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
}
