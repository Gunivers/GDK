package fr.gunivers.gdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import fr.gunivers.gdk.gui.controller.BaseController;
import fr.gunivers.gdk.gui.model.GDKPlugin;
import fr.gunivers.gdk.gui.util.Util;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends javafx.application.Application
{
	public final static String TITLE = "GDK Plugins Launcher";
	public final static ArrayList<GDKPlugin> plugins = new ArrayList<>();
	
	private static Main app;
	private static Stage stage;
	private BaseController controller;
	
	public static void main(String... args) { launch(args); }

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage)
	{	
		Main.app = (Main) this;
		Main.stage = primaryStage;
		
		controller = Util.loadFXML(Main.class.getResource(PATH.FXML+"Base.fxml"), (BorderPane p) -> { stage.setScene(new Scene(p)); });
		
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
	}

	public static Main getApp() { return app; }
	public static Stage getStage() { return stage; }
	public BaseController getController() { return controller; }

	public static InputStream getStream(String path) { return Main.class.getClassLoader().getResourceAsStream(path); }
	
	public static class PATH
	{
		public final static String FXML = "/fxml/";
		public final static String IMAGE = "/img/";
		public final static String OTHERS = "/others/";
	}
}
