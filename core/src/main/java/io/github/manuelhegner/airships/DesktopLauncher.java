package io.github.manuelhegner.airships;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		config.foregroundFPS=240;
		config.vSyncEnabled=false;
		new LwjglApplication(new AirshipsGame(), config);
	}
}
