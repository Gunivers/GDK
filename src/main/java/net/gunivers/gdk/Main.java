package net.gunivers.gdk;

import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static net.gunivers.gdk.Util.PATH;
import net.gunivers.gdk.gui.controller.BaseController;
import net.gunivers.gdk.gui.controller.DebugController;
import net.gunivers.gdk.gui.model.GDKPlugin;

/**
 * 
 * @author A~Z
 * @since Minecraft GDK 0.0.0
 */
public class Main extends javafx.application.Application
{
	public final static String TITLE = "GDK Plugins Launcher";
	public final static ArrayList<GDKPlugin> plugins = new ArrayList<>();
	public static DebugController DEBUG = new DebugController();
	
	private static Main app;
	private static Stage stage;
	private BaseController controller;
	
	public static void main(String... args) { launch(args); }

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * This methods, other than loading Base.fxml, will try to load the saved plugins.
	 * If this is launched for the first time, the file won't exist thus sending an error message to the user, the first save will correct it.
	 */
	public void start(Stage primaryStage)
	{
		if (this.getParameters().getRaw().contains("-debug"));
			DEBUG = DebugController.run();
		
		Main.app = this;
		Main.stage = primaryStage;
		
		controller = Util.loadFXML(Util.getResource(PATH.FXML + "Base.fxml"), (BorderPane p) -> { stage.setScene(new Scene(p)); });
		
		stage.show();
		stage.setTitle(TITLE);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(
				Util.getResource(PATH.OTHERS + "launcher.save").toURI()))))
		{
			ArrayList<GDKPlugin> plugins = (ArrayList<GDKPlugin>) ois.readObject();
			if (plugins != null) Main.plugins.addAll(plugins);
		} catch (Exception e)
		{
			e.printStackTrace();
			Util.alert(AlertType.ERROR, "Error", "Could not retrieve the loaded plugins, using defaults", e.getClass().getSimpleName(), true);
		}
		
		controller.refresh();
		
		System.out.println("> Output tests:");
		System.out.println(" -- System output");
		System.out.println("System:out");
		System.err.println("System:err");
		
		DEBUG.logInfo(" -- Debug output");
		DEBUG.logInfo("Debug:info");
		DEBUG.logWarning("Debug:warn");
		DEBUG.logError("Debug:err");
		
		DEBUG.logInfo(" -- Debug log output");
		DEBUG.log("Debug:log [alreadyFormated=false]", false);
		DEBUG.log("Debug:log [alreadyFormated=true]", true);
		DEBUG.log(DebugController.INFO + "Debug:log [alreadyFormated=false]", false);
		DEBUG.log(DebugController.INFO + "Debug:log [alreadyFormated=true]", true);
		DEBUG.log(DebugController.WARN + "Debug:log [alreadyFormated=false]", false);
		DEBUG.log(DebugController.WARN + "Debug:log [alreadyFormated=true]", true);
		DEBUG.log(DebugController.ERR + "Debug:log [alreadyFormated=false]", false);
		DEBUG.log(DebugController.ERR + "Debug:log [alreadyFormated=true]", true);
		DEBUG.log(DebugController.OTHER + "Debug:log [alreadyFormated=false]", false);
		DEBUG.log(DebugController.OTHER + "Debug:log [alreadyFormated=true]", true);
	}

	public static Main getApp() { return app; }
	public static Stage getStage() { return stage; }
	public BaseController getController() { return controller; }
}
