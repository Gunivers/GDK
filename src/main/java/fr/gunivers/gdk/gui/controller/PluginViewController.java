package fr.gunivers.gdk.gui.controller;

import java.net.URL;
import java.net.URLClassLoader;

import fr.gunivers.gdk.Main;
import fr.gunivers.gdk.gui.components.GDKImageView;
import fr.gunivers.gdk.gui.model.GDKPlugin;
import fr.gunivers.gdk.gui.util.Controller;
import fr.gunivers.gdk.gui.util.Util;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class PluginViewController extends Controller
{	
	@FXML private VBox container;
	@FXML private TextFlow infos;
	@FXML private TextFlow description;
	
	private GDKPlugin plugin = new GDKPlugin();
	private GDKImageView<GDKPlugin> img = new GDKImageView<>(plugin);
	
	public PluginViewController() {}
	
	@Override
	public void initialize()
	{
		plugin.setAuthor("Loading...");
		plugin.setDescription("Loading...");
		plugin.setName("Loading...");
		plugin.setPath("Loading...");
		plugin.setVersion("Loading...");
		
		infos.setTextAlignment(TextAlignment.LEFT);
		description.setTextAlignment(TextAlignment.JUSTIFY);
		
		img.setOnMouseClicked(null);
	}
	
	@Override
	public void refresh()
	{
		this.setDescText();
		this.setInfoText();
		this.img.setPlugin(plugin);
		
		this.container.getChildren().clear();
		this.container.getChildren().add(img);
		this.container.getChildren().add(infos);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@FXML
	public void buttonRun_click()
	{
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
	
	@FXML
	public void buttonUnload_click()
	{
		Alert alert = Util.alert(AlertType.CONFIRMATION, "Are you sure ?", "Do you truly want to unload this plugin?", plugin.getName(), true);
		
		if (alert.getResult() == ButtonType.OK)
		{
			Main.plugins.remove(plugin);
			Main.getApp().getController().refresh();
		}
	}

	private void setInfoText()
	{
		Text name = new Text(" - " + plugin.getName() +" v"+ plugin.getVersion() +'\n');
		Text author = new Text(" by " + plugin.getAuthor() +'\n');
		Text main = new Text("Main: " + plugin.getPath() +'\n');
		
		this.infos.getChildren().clear();
		this.infos.getChildren().addAll(name, author, main);
	}
	
	private void setDescText()
	{
		Text desc = new Text("TEST: "+plugin.getDescription());
		desc.setFill(Color.GRAY);
		
		this.description.getChildren().clear();
		this.description.getChildren().add(desc);
	}
	
	public GDKPlugin getPlugin() { return plugin; }
	public void setPlugin(GDKPlugin plugin) { this.plugin = plugin; this.refresh(); }
}
