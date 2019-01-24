package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;

import lombok.Getter;

@Getter
public class ShipType extends ThingType<ShipType, Ship> {
	private final TextureRegion img;
	
	public ShipType() {
		img = new TextureRegion(new Texture(Gdx.files.local("assets/airship.png")));
		img.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}
	
	@Override
	public void dispose() {
		img.getTexture().dispose();
	}

	@Override
	protected BodyDef getBodyDef() {
		return null;
	}
}
