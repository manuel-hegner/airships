package io.github.manuelhegner.airships;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.utils.TimeUtils;
import com.google.common.util.concurrent.Uninterruptibles;

import io.github.manuelhegner.airships.render.Render;
import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.scenes.BattleScene;
import io.github.manuelhegner.airships.scenes.Scene;

public class UpdateLoop extends Thread {

	private final static long TARGET_UPDATE_RATE_NANOS = 1000_000_000L/60L;
	private Scene currentScene = new BattleScene();
	private final AtomicReference<Pair<Render, Render>> renderingPair = new AtomicReference<>(Pair.of(null, null));
	private final AtomicBoolean stopped = new AtomicBoolean(false);
	
	@Override
	public void run() {
		long lastNano = TimeUtils.nanoTime();
		while(!stopped.get()) {
			long nano = TimeUtils.nanoTime();
			float delta = (nano-lastNano)/1000_000_000f;
			Renderable r = currentScene.update(delta);
			Render render = new Render(r, r.collect(), nano);
			renderingPair.updateAndGet(p->Pair.of(p.getRight(), render));
			
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
		return renderingPair.get();
	}
}
