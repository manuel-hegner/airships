package io.github.manuelhegner.airships;

import org.apache.commons.lang3.tuple.Pair;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

import io.github.manuelhegner.airships.render.Render;
import io.github.manuelhegner.airships.render.Renderable;
import io.github.manuelhegner.airships.scenes.BattleScene;
import io.github.manuelhegner.airships.scenes.Scene;
import io.github.manuelhegner.airships.util.FrameRate;

public class AirshipsGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private FrameRate fps;
	private UpdateLoop updateLoop;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		fps = new FrameRate();
		updateLoop = new UpdateLoop();
		updateLoop.start();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		Pair<Render, Render> p = updateLoop.getRenderingPair();
		if(p != null) {
			p.getRight().render(
				p.getLeft(), 
				1f + 
					(TimeUtils.nanoTime()-p.getRight().getNano()) /
					(float)(p.getRight().getNano()-p.getLeft().getNano()), 
				batch);
		}
		batch.end();
		
		fps.update();
		fps.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		fps.dispose();
		updateLoop.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}
}
