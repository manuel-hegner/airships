package io.github.manuelhegner.airships.render;

import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Render implements Renderable<Render> {
	
	private final Renderable<?> root;
	private final Map<UUID, Renderable<?>> values;
	@Getter
	private final long nano;
	
	public void render(Render precursor, float alpha, SpriteBatch batch) {
		
	}
}
