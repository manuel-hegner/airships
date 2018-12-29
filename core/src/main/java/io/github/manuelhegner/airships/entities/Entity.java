package io.github.manuelhegner.airships.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public abstract class Entity implements Disposable {
	public abstract void update(float delta);
	public abstract void render(float delta, SpriteBatch batch);
}
