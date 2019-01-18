package io.github.manuelhegner.airships.render;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.manuelhegner.airships.util.LUID;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class Renderable {
	
	@Getter
	protected
	final LUID luid;
	
	public abstract void render(Render prevRender, float a, float b, SpriteBatch batch);

	public Map<LUID, Renderable> collect() {
		return collect(10);
	}
	
	public Map<LUID, Renderable> collect(int lastSize) {
		Map<LUID, Renderable> m = new HashMap<>(lastSize);
		collect(m);
		return m;
	}

	public void collect(Map<LUID, Renderable> m) {
		m.put(getLuid(), this);
	}
}
