package io.github.manuelhegner.airships.render;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.manuelhegner.airships.util.LUID;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GroupRenderable extends Renderable {

	@Singular
	private final List<Renderable> items;
	
	@Override
	public void render(Render prevRender, float a, float b, SpriteBatch batch) {
		for(Renderable r:items)
			r.render(prevRender, a, b, batch);
	}
	
	@Override
	public void collect(Map<LUID, Renderable> m) {
		for(Renderable r:items)
			r.collect(m);
	}

}
