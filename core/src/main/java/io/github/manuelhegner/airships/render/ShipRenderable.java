package io.github.manuelhegner.airships.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.manuelhegner.airships.entities.ShipType;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ShipRenderable extends Renderable {
	private final ShipType type;
	private final Vector2 translate;
	private final float rotate;
	
	
	@Override
	public void render(Render prevRender, float a, float b, SpriteBatch batch) {
		ShipRenderable p = prevRender.find(this);
		batch.draw(
			type.getImg(),
			a*translate.x + b*p.translate.x,
			a*translate.y + b*p.translate.y,
			type.getCenterX(),
			type.getCenterY(),
			type.getWidth(),
			type.getHeight(),
			1,//a*scale + b*p.scale,
			1,//a*scale + b*p.scale,
			a*rotate + b*p.rotate
		);
	}
}
