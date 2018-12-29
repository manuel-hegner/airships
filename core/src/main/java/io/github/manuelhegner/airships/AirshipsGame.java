package io.github.manuelhegner.airships;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.manuelhegner.airships.scenes.BattleScene;
import io.github.manuelhegner.airships.scenes.Scene;

public class AirshipsGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Scene currentScene;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		currentScene = new BattleScene();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		//TODO input
		float delta = Gdx.graphics.getDeltaTime();
		currentScene.update(delta);
		
				
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		currentScene.render(delta, batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		currentScene.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}
}
