package me.nroffler.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import me.nroffler.main.DarkWorld;
import me.nroffler.main.Statics;

public class DesktopLauncher {

	public static void main (String[] arg) {

		JTextField level = new JTextField();
		JTextField debug = new JTextField();

		Object[] message = {"Fenster wurde auf "+Statics.BREITE +" * "+Statics.HOEHE +" Pixel gesezt\nBildwiederholrate (FPS) wurde auf "+Statics.BILDER_PRO_SEKUNDE +" gesezt\n\n level (0-12)", level, "Legt das Startlevel fest\n\n\n" ,"Debug (0|1)", debug,"Wenn 1, werden Hitboxen mit gezeichnet\n\n"};

		Object[] buttons = {"Save and Launch", "Launch"};

		JOptionPane pane = new JOptionPane( message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null);
		pane.setOptions(buttons);
		pane.createDialog(null, "Desktop Launcher").setVisible(true);

		System.out.println(pane.getValue());

		if (pane.getValue().equals("Save and Launch")){

			Statics.level = Integer.parseInt(level.getText());
			Statics.ueberschreiben = true;

			if (Integer.parseInt(debug.getText())==1) {
				Statics.debug = true;
				System.out.println("debug on");
			}
		} else if (pane.getValue().equals("Launch")){
			Statics.debug = false;
		}


		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Statics.BREITE;
		config.height = Statics.HOEHE;
		config.foregroundFPS = Statics.BILDER_PRO_SEKUNDE;
		config.backgroundFPS = Statics.BILDER_PRO_SEKUNDE;
		config.title = "Dark World";

		new LwjglApplication(new DarkWorld(), config);
	}
}
