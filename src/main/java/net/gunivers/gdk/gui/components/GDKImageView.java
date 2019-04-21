package net.gunivers.gdk.gui.components;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.gunivers.gdk.gui.model.GDKPlugin;
import net.gunivers.gdk.Main;
import net.gunivers.gdk.Util;
import static net.gunivers.gdk.Util.PATH;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class GDKImageView <G extends GDKPlugin> extends AnchorPane
{
	public final static String DEFAULT_TITLE = "<Title>";
	public final static Image DEFAULT_ICON = new Image(PATH.IMAGE + "no_icon.png");

	public final static HashMap<GDKPlugin, GDKImageView<? extends GDKPlugin>> instances = new HashMap<>();
	
	@FXML private Label label;
	@FXML private ImageView image;
	
	private final SimpleStringProperty title = new SimpleStringProperty(DEFAULT_TITLE);
	private final SimpleObjectProperty<Image> icon = new SimpleObjectProperty<>(DEFAULT_ICON);
	private final SimpleObjectProperty<G> plugin = new SimpleObjectProperty<G>();
	
	private Callback<G, Entry<String,Image>> factory = t -> Util.newEntry(t.getName().trim(), DEFAULT_ICON);
	
	{
		plugin.addListener((obs, old, value) ->
		{
			Entry<String,Image> back = factory.call(value);
			title.set(back.getKey() == null || back.getKey().isEmpty() ? DEFAULT_TITLE : back.getKey());
			icon.set(back.getValue() == null || back.getValue().isError() ? DEFAULT_ICON : back.getValue());
			
			instances.remove(old);
			instances.put(value, this);
		});
		
		this.setOnMouseClicked(event -> Main.getApp().getController().onPluginSelected(this.plugin.get(), event));
		
		instances.put(this.plugin.get(), this);
	}
	
	public GDKImageView()
	{
		FXMLLoader loader = new FXMLLoader(Main.class.getResource(PATH.FXML+"GDKImageView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try { loader.load(); } catch (IOException e) { e.printStackTrace(); }

		label.textProperty().bind(title);
		image.imageProperty().bind(icon);
	}
	public GDKImageView(G plugin)
	{
		this();
		this.plugin.set(plugin);
	}
	public GDKImageView(GDKImageView<G> base)
	{
		this(base.plugin.get());
		this.factory = base.factory;
	}
	public GDKImageView(GDKImageView<G> base, G plugin)
	{
		this(base);
		this.plugin.set(plugin);
	}
	
	public void setViewFactory(Callback<G, Entry<String,Image>> factory) { this.factory = factory; }
	public Callback<G, Entry<String, Image>> getViewFactory() { return factory; };
	
	public StringProperty titleProperty() { return title; }
	public ObjectProperty<Image> iconProperty() { return icon; }
	public ObjectProperty<G> pluginProperty() { return plugin; }
	
	public String getTitle() { return title.get(); }
	public Image getIcon() { return icon.get(); }
	public G getPlugin() { return plugin.get(); }
	
	public void setTitle(String title) { this.title.set(title); }
	public void setIcon(Image icon) { this.icon.set(icon); }
	public void setPlugin(G plugin) { this.plugin.set(plugin); }
}