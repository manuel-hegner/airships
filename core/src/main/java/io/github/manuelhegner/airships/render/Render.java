package io.github.manuelhegner.airships.render;

import java.util.Map;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.manuelhegner.airships.util.LUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Render {
	
	private final Renderable root;
	private final Map<LUID, Renderable> values;
	@Getter
	private final long nano;
	
	public void render(Render prevRender, float alpha, SpriteBatch batch) {
		root.render(prevRender, alpha, 1-alpha, batch);
	}

	public <T extends Renderable> T find(T successor) {
		return (T) values.getOrDefault(successor.getLuid(), successor);
	}
}
