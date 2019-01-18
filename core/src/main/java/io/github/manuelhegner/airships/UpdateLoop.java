package io.github.manuelhegner.airships;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.utils.TimeUtils;
import com.google.common.util.concurrent.Uninterruptibles;

import io.github.manuelhegner.airships.render.Render;
import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.scenes.BattleScene;
import io.github.manuelhegner.airships.scenes.Scene;
import lombok.Setter;

public class UpdateLoop extends Thread {

	private final static long TARGET_UPDATE_RATE = 1000_000_000L/60L;
	private Scene currentScene = new BattleScene();
	private final AtomicReference<Pair<Render, Render>> renderingPair = new AtomicReference<>();
	private final AtomicBoolean stopped = new AtomicBoolean(false);

	@Override
	public void run() {
		long lastNano = TimeUtils.nanoTime();
		while(stopped.get()) {
			long nano = TimeUtils.nanoTime();
			float delta = (nano-lastNano)/1000_000_000f;
			currentScene.update(delta);
			
			
		}
	}
	
	public void dispose() {
		this.stopped.set(true);
		Uninterruptibles.joinUninterruptibly(this);
		currentScene.dispose();
	}

	public Pair<Render, Render> getRenderingPair() {
		// TODO Auto-generated method stub
		return null;
	}

}
