package io.github.manuelhegner.airships.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable<SELF extends Renderable<SELF>> {
	public void render(SELF precursor, float alpha, SpriteBatch batch);
}
