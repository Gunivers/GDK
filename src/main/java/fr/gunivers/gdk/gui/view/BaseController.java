package fr.gunivers.gdk.gui.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map.Entry;

import com.jfoenix.controls.JFXMasonryPane;

import fr.gunivers.gdk.Main;
import fr.gunivers.gdk.gui.components.GDKImageView;
import fr.gunivers.gdk.gui.model.GDKPlugin;
import fr.gunivers.gdk.gui.util.Application;
import fr.gunivers.gdk.gui.util.Controller;
import fr.gunivers.gdk.gui.util.Util;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class BaseController extends Controller
{
	@FXML private JFXMasonryPane plugins;
	@FXML private JFXMasonryPane resources;

	@Override
	public void initialize()
	{
	}
	
	@Override
	public void refresh()
	{
		List<Node> children = plugins.getChildren();
		children.clear();
		
		for (GDKPlugin plugin : Main.plugins)
			children.add(new GDKImageView<GDKPlugin>(plugin));
	}
	
	@FXML
	public void menuFile_clickLoadPlugin()
	{
		FileChooser browser = new FileChooser();
		
		browser.setTitle("Select plugins to load");
		browser.setInitialDirectory(new File(System.getProperty("user.home")));
		browser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR", "*.jar"));
		
		List<File> files = browser.showOpenMultipleDialog(Application.getStage());
		
		if (files != null)
		{
			StringBuilder success = new StringBuilder("Successful loads:");
			StringBuilder fail = new StringBuilder("\nFailed loads:");
			
			for (File file : files)
			{
				Entry<GDKPlugin,String> result = Util.getPluginFromFile(file);
				
				if(result.getKey() != null)
				{
					success.append('\n'+ file.getName());
					Main.plugins.add(result.getKey());
				}
				else fail.append('\n'+ file.getName() +": "+ result.getValue());
			}
			
			refresh();
			Util.alert(AlertType.INFORMATION, "Load Result", "Loaded "+ files.size() +" plugin(s)", success +"\n"+ fail, false);
		}
	}
	
	@FXML
	public void menuFile_clickSave()
	{
		System.out.println("[DEBUG] Menu File::click(save)");
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Main.class.getResource("launcher.save").toURI()))))
		{
			oos.writeObject(Main.plugins);
			
			StringBuilder sb = new StringBuilder(); Main.plugins.forEach(p -> sb.append(p.getName() +'\n'));
			Util.alert(AlertType.INFORMATION, "Success", "Successfully savec", sb.toString(), false);
		} catch (IOException | URISyntaxException e)
		{
			e.printStackTrace();
			Util.alert(AlertType.ERROR, "Exception", "An exception occured whilst saving", e.getClass().getName(), true);
		}
	}

	@FXML
	public void menuFile_clickQuit()
	{
		Alert alert = Util.alert(AlertType.CONFIRMATION, "Are you sure ?", "Confirm Quitting", "", true);
		if (alert.getResult() == ButtonType.OK)
			Main.getStage().close();
	}

/*	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
	public void buttonRun_click()
	{
		System.out.println("[DEBUG] Button Run::click");
		
		GDKPlugin plugin = plugins.getSelectionModel().getSelectedItem();
		if (plugin == null) return;
		
		try
		{
			URLClassLoader loader = new URLClassLoader(new URL[] {plugin.getJarFile().toURI().toURL()}, Main.class.getClassLoader());
			Class main = Class.forName(plugin.getPath(), true, loader);
			main.getDeclaredMethod("main", String[].class).invoke(null, new Object[] {new String[0]});
		} catch (Exception e)
		{
			e.printStackTrace();
			Util.alert(AlertType.ERROR, "Exception", "An exception occured whilst running "+plugin.getName(), e.getClass().getName(), true);
		}
	}
*/
/*	
	@FXML
	public void buttonUnload_click()
	{
		System.out.println("[DEBUG] Button Unload::click");
		
		List<GDKPlugin> plugin = plugins.getSelectionModel().getSelectedItems();
		if (plugin == null || plugin.isEmpty()) return;
		
		StringBuilder sb = new StringBuilder();
		plugin.forEach(p -> sb.append(p.getName() +'\n'));
		
		Alert alert = Util.alert(AlertType.CONFIRMATION, "Are you sure ?", "Do you truly want to unload thess plugins ?", sb.toString(), true);
		
		if (alert.getResult() == ButtonType.OK)
		{
			Main.plugins.removeAll(plugin);
			this.refresh();
		}
	}
*/
}



