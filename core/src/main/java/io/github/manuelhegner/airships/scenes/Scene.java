package io.github.manuelhegner.airships.scenes;

import com.badlogic.gdx.utils.Disposable;

import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.util.LUID;
import lombok.Getter;

public abstract class Scene implements Disposable {
	@Getter
	protected final LUID luid = LUID.next();
	
	public abstract Renderable update(float delta);
	
}
