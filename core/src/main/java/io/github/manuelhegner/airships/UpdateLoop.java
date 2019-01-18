package io.github.manuelhegner.airships;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.google.common.util.concurrent.Uninterruptibles;

import io.github.manuelhegner.airships.entities.ShipType;
import io.github.manuelhegner.airships.render.Render;
import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.scenes.BattleScene;
import io.github.manuelhegner.airships.scenes.Scene;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class UpdateLoop extends Thread {

	private final static long TARGET_UPDATE_RATE_NANOS = 1000_000_000L/60L;
	private Scene currentScene = new BattleScene();
	private final AtomicReference<Pair<Render, Render>> renderingPair = new AtomicReference<>();
	private final AtomicBoolean stopped = new AtomicBoolean(false);
	private long start = TimeUtils.nanoTime();

	@Override
	public void run() {
		long lastNano = TimeUtils.nanoTime();
		while(stopped.get()) {
			long nano = TimeUtils.nanoTime();
			float delta = (nano-lastNano)/1000_000_000f;
			currentScene.update(delta);
			
			if(delta < TARGET_UPDATE_RATE_NANOS)
				Uninterruptibles.sleepUninterruptibly(TARGET_UPDATE_RATE_NANOS - nano + lastNano, TimeUnit.NANOSECONDS);
			lastNano = nano;
		}
	}
	
	public void dispose() {
		this.stopped.set(true);
		Uninterruptibles.joinUninterruptibly(this);
		currentScene.dispose();
	}

	public Pair<Render, Render> getRenderingPair() {
		ShipType type = new ShipType();
		
		return Pair.of(
			new Render(new R(
				type,
				new Affine2().idt()	
			), null, start), 
			new Render(new R(
				type,
				new Affine2().idt().setToTranslation(10000, 0)
			), null, start + 10_000_000_000L)
		);
	}

	@RequiredArgsConstructor
	private static class R implements Renderable<R> {

		private final ShipType type;
		private final Affine2 transform;
		
		@Override
		public void render(R precursor, float alpha, SpriteBatch batch) {
			batch.draw(type.getImg(), type.getWidth(), type.getHeight(), translate(alpha, precursor.transform,transform));
		}

		private Affine2 translate(float alpha, Affine2 m1, Affine2 m2) {
			Vector2 v11 = new Vector2(1, 0);
			Vector2 v12 = new Vector2(0, 1);
			Vector2 v21 = new Vector2(1, 0);
			Vector2 v22 = new Vector2(0, 1);
			
			m1.applyTo(v11);
			m1.applyTo(v12);
			m2.applyTo(v21);
			m2.applyTo(v22);
			
			Vector2 a = v11.cpy().scl(alpha).mulAdd(v21, 1f-alpha).setLength(v11.len()*alpha + v21.len()*(1f-alpha));
			Vector2 b = v12.cpy().scl(alpha).mulAdd(v22, 1f-alpha).setLength(v12.len()*alpha + v22.len()*(1f-alpha));
			
			Affine2 result = new Affine2();
			
		}
		
	}
}
