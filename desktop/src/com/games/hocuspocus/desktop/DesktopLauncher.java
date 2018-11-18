package com.games.hocuspocus.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.games.hocuspocus.HocusPocus;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = HocusPocus.WIDTH;
		config.height = HocusPocus.HEIGHT;
		config.title = HocusPocus.TITLE;
		new LwjglApplication(new HocusPocus(), config);
	}
}
