package fr.gunivers.gdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import fr.gunivers.gdk.gui.model.GDKPlugin;
import fr.gunivers.gdk.gui.util.Application;
import fr.gunivers.gdk.gui.util.Util;
import fr.gunivers.gdk.gui.view.BaseController;

import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class Main extends Application
{
	public final static String TITLE = "GDK Plugins Launcher";
	
	public final static ArrayList<GDKPlugin> plugins = new ArrayList<>();
	
	private BaseController controller;
	
	public static void main(String... args) { launch(args); }
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize()
	{	
		controller = Util.loadFXML(Main.class.getResource("gui/view/Base.fxml"), (BorderPane p) -> { stage.setScene(new Scene(p)); });
		
		stage.show();
		stage.setTitle(TITLE);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(Main.class.getResource("launcher.save").toURI()))))
		{
			ArrayList<GDKPlugin> plugins = (ArrayList<GDKPlugin>) ois.readObject();
			if (plugins != null) Main.plugins.addAll(plugins);
		} catch (Exception e)
		{
			e.printStackTrace();
			Util.alert(AlertType.ERROR, "Error", "Could not retrieve the loaded plugins, using defaults", e.getClass().getSimpleName(), true);
		}
		
		controller.refresh();
		
		System.out.println(new File(Main.class.getClassLoader().getResource("../default/").getFile() +"img/no_icon.png").exists());
	}
	
	public BaseController getController() { return controller; }
	
	public static InputStream getStream(String path) { return Main.class.getClassLoader().getResourceAsStream(path); }
	
	public static class PATH
	{
		public final static String RESOURCE = "../default/";
		public final static String IMAGE = RESOURCE+"img/";
	}
}
