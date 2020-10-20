package me.nroffler.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.nroffler.Screens.LeitScreen;

//Hauptklasse
public class DarkWorld extends Game {

	//Hier werden nur Objekte hinzugefügt, die sehr viel Arbeitsspeicher und Grafikspeicher brauchen und überall verwendet werden
	public AssetManager assetManager;
	public SpriteBatch batch;

	private Music musik;

	@Override
	public void create () {
		assetManager = new AssetManager();
		batch = new SpriteBatch();

		System.out.println("-- Starte Spiel --");

		//Musik
		musik = Gdx.audio.newMusic(Gdx.files.internal("dark_music.mp3"));
		musik.setLooping(true);
		musik.setVolume(0.1f);
		musik.play();

		//Die Oberklasse DarkWorld übergibt die Aufgabe das Bild/den Screen zu rendern an eine Untrtklasse, die Screen implementiert
		setScreen(new LeitScreen(this, 0));
	}

	@Override
	public void render () {
		//Die render Methode des jeweiligen Screens wird durch die Oberklasse (super) aufgerufen
		super.render();
	}
	
	@Override
	public void dispose () {
		//Sobald das Spiel geschlossen wird Grafikspeicher freigeben
		assetManager.dispose();
		batch.dispose();
	}
}
