package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import lombok.Getter;

@Getter
public abstract class ThingType<SELF extends ThingType<SELF, THING>, THING extends Thing<THING, SELF>> implements Disposable {

	protected final float width = 96;
	protected final float height = 96;
	protected final float centerX = 48;
	protected final float centerY = 40;
	
	@Override
	public void dispose() {}
}
