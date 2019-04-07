package fr.gunivers.gdk.gui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import fr.gunivers.gdk.Main;
import fr.gunivers.gdk.gui.model.GDKPlugin;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

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
	
	public static <P extends Pane, C extends Controller> C loadFXML(URL path, Consumer<P> action)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(path);
		
		C controller = null;
		
		try
		{
			action.accept(loader.load());
			controller = loader.getController();
			
			if (controller != null)
				controller.setMainApplication(Main.getApp());
			else
				System.out.println("WARNING: No controller found for '"+ path.toString() +'\'');
		} catch (IOException e) { e.printStackTrace(); }
		
		return controller;
	}
	
	public static Entry<GDKPlugin,String> getPluginFromFile(File file)
	{
		String line;
		
		try (ZipFile jar = new ZipFile(file))
		{
			ZipEntry plugin_txt = jar.getEntry("plugin.txt");
			
			if (plugin_txt == null) return Util.newEntry(null, "Could not find 'plugin.txt'");
			
			GDKPlugin plugin = new GDKPlugin();
			BufferedReader reader = new BufferedReader(new InputStreamReader(jar.getInputStream(plugin_txt)));
			
			loop: while((line = reader.readLine()) != null)
			{
				if (!line.matches(".+=.*")) continue;
				
				if (line.equalsIgnoreCase("description="))
				{
					StringBuilder desc = new StringBuilder();
					while((line = reader.readLine()) != null) desc.append('\n'+line);
					
					plugin.setDescription(desc.toString());
					break loop;
				}
				
				for (Link link : Link.values())
					if (line.split("=")[0].equalsIgnoreCase(link.key))
						link.action.accept(plugin, line.split("=")[1]);
			}
			reader.close();
			
/*			System.out.println("\n Plugin " + file.getName() +":\n"
							+  "  - Name: "+ plugin.getName()		+'\n'
							+  "  - Author: "+ plugin.getAuthor()	+'\n'
							+  "  - Version: "+ plugin.getVersion()	+'\n'
							+  "  - Main: "+ plugin.getPath()		+'\n'
							+  "  - Description: "+ plugin.getDescription());
*/			
			if (!plugin.getPath().matches("(\\w+.)*\\w+"))
				return Util.newEntry(null, "Invalid class path: " + plugin.getPath());
			
			plugin.setJarFile(file);
			return Util.newEntry(plugin, "");
		} catch (IOException e)
		{
			e.printStackTrace();
			return Util.newEntry(null, e.getClass().getSimpleName() +" occured");
		}
	}
	
	public static <K,V> Entry<K,V> newEntry(K key, V value)
	{
		return (new HashMap<K,V>() {
			private static final long serialVersionUID = -5357373460483833255L;
			{ put(key,value); }}
		).entrySet().iterator().next();
	}
}

enum Link
{
	NAME("name", (p,s) -> p.setName(s)),
	AUTHOR("author", (p,s) -> p.setAuthor(s)),
	VERSION("version", (p,s) -> p.setVersion(s)),
	MAIN("main", (p,s) -> p.setPath(s))
	;
	
	public final String key;
	public final BiConsumer<GDKPlugin, String> action;
	
	private Link(String key, BiConsumer<GDKPlugin, String> action)
	{
		this.key = key;
		this.action = action;
	}
}