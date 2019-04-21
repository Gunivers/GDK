package net.gunivers.gdk;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipInputStream;

import net.gunivers.gdk.gui.Controller;
import net.gunivers.gdk.gui.model.GDKPlugin;

/**
 * 
 * @author A~Z
 * @since Minecraft GDK 0.0.1
 */
public class Util
{
	public static Alert alert(AlertType type, String title, String header, String content, boolean wait)
	{
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		
		if (wait) alert.showAndWait(); else alert.show();
		return alert;
	}
	
	public static <P extends Parent, C extends Controller> C loadFXML(URL path, Consumer<P> action)
		{ return Util.loadFXML(path, null, action); }
	
	public static <P extends Parent, C extends Controller> C loadFXML(URL path, C defaultController, Consumer<P> action)
	{
		FXMLLoader loader = new FXMLLoader(path);
		C controller = null;
		
		try { action.accept(loader.load()); }
		catch (IOException e) { e.printStackTrace(); }
		
		controller = loader.getController();
			
		if (controller != null) controller.setMainApplication(Main.getApp());
		else loader.setController(controller = defaultController);
		
		return controller;
	}
	
	public static Entry<GDKPlugin,String> getPluginFromFile(File file)
	{
		Properties property = new Properties();
		GDKPlugin plugin = new GDKPlugin();
		
		try (ZipInputStream zip = new ZipInputStream(new FileInputStream(file.getCanonicalPath() + "/plugin.properties")))
		{
			property.load(zip);
			
			for (PluginProperty prop : PluginProperty.values())
				if (property.getProperty(prop.name().toLowerCase()) != null)
					prop.action.accept(plugin, property.getProperty(prop.name().toLowerCase()));
		} catch (Exception e)
		{
			e.printStackTrace();
			return Util.newEntry(null, e.getClass().getSimpleName());
		}
		
		return Util.newEntry(plugin, "");
	}
	
	@SuppressWarnings("serial")
	public static <K,V> Entry<K,V> newEntry(K key, V value)
	{
		return (new HashMap<K,V>() {{ put(key,value); }}).entrySet().iterator().next();
	}
	
	public static URL getResource(String path)
	{
		return Main.class.getResource(path);
	}
	
	/**
	 * A class that provides a path according to the type of resources you want to get.
	 * For instance, if you are trying to load an fxml, use:
	 * <code> Util.loadFxml(String path)</code>, where <code>path = PATH.FXML + "name of file.fxml"</code>
	 * <p>
	 * It contains for now three paths:
	 * <ul>
	 * <li>FXML: "/view/fxml/"</li>
	 * <li>IMAGE: "/view/img/"</li>
	 * <li>OTHERS: "/others/"</li>
	 * @author A~Z
	 * @since Minecraft GDK 0.0.1
	 */
	public static class PATH
	{
		public final static String FXML = "/view/fxml/";
		public final static String IMAGE = "/view/img/";
		public final static String CSS = "/view/css/";
		
		public final static String OTHERS = "/others/";
	}
}

enum PluginProperty
{
	NAME(		(p,s) -> p.setName(s)),
	AUTHOR(		(p,s) -> p.setAuthor(s)),
	LINK(		(p,s) -> p.setLink(s)),
	VERSION(	(p,s) -> p.setVersion(s)),
	MAIN(		(p,s) -> p.setPath(s)),
	DESCRIPTION((p,s) -> p.setDescription(s)),
	;
	
	public final BiConsumer<GDKPlugin, String> action;
	
	private PluginProperty(BiConsumer<GDKPlugin, String> action)
	{
		this.action = action;
	}
}