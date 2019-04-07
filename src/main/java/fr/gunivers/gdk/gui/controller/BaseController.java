package fr.gunivers.gdk.gui.controller;

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
import fr.gunivers.gdk.gui.util.Controller;
import fr.gunivers.gdk.gui.util.Util;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
		
		List<File> files = browser.showOpenMultipleDialog(Main.getStage());
		
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
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File
				( Main.class.getResource(Main.PATH.OTHERS + "launcher.save").toURI() ))))
		{
			oos.writeObject(Main.plugins);
			
			StringBuilder sb = new StringBuilder(); Main.plugins.forEach(p -> sb.append(p.getName() +'\n'));
			Util.alert(AlertType.INFORMATION, "Success", "Successfully saved", sb.toString(), false);
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
	
	public void onPluginSelected(GDKPlugin plugin, MouseEvent event)
	{
		final Stage stage = new Stage();
		
		PluginViewController controller = Util.loadFXML(Main.class.getResource(Main.PATH.FXML+"PluginView.fxml"), (p) ->
			stage.setScene(new Scene(p)) );
		
		if (controller != null)
			controller.setPlugin(plugin);
		
		stage.sizeToScene();
		stage.setResizable(false);
		stage.showAndWait();
	}
}

