package io.github.manuelhegner.airships.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public interface Scene extends Disposable {
	void update(float delta);
	void render(float delta, SpriteBatch batch);
}
