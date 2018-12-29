package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Getter;

@Getter
public class ShipType extends ThingType<ShipType, Ship> {
	private final TextureRegion img = new TextureRegion(new Texture(Gdx.files.internal("airship.png")));

	@Override
	public void render(float delta, SpriteBatch batch, Ship ship) {
		batch.draw(img, ship.getPosition().x, ship.getPosition().y, centerX, centerY, width, height, 1, 1, ship.getDirection().angle()-90);
	}
	
	@Override
	public void dispose() {
		img.getTexture().dispose();
	}
}
