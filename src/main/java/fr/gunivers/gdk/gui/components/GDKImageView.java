package fr.gunivers.gdk.gui.components;

import java.io.IOException;
import java.util.Map.Entry;

import fr.gunivers.gdk.Main;
import fr.gunivers.gdk.Main.PATH;
import fr.gunivers.gdk.gui.model.GDKPlugin;
import fr.gunivers.gdk.gui.util.Util;

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

public class GDKImageView <T extends GDKPlugin> extends AnchorPane
{
	public final static String DEFAULT_TITLE = "<Title>";
	public final static Image DEFAULT_ICON = new Image(Main.getStream(PATH.IMAGE+"no_icon.png"));

	@FXML private Label label;
	@FXML private ImageView image;
	
	private final SimpleStringProperty title = new SimpleStringProperty();
	private final SimpleObjectProperty<Image> icon = new SimpleObjectProperty<>();
	private final SimpleObjectProperty<T> item = new SimpleObjectProperty<>();
	
	private Callback<T, Entry<String,Image>> factory = t -> Util.newEntry(t.getName(), DEFAULT_ICON);

	public final static GDKImageView<GDKPlugin> BASE = new GDKImageView<>();
	
	{
		label.textProperty().bind(title); title.set(DEFAULT_TITLE);
		image.imageProperty().bind(icon); icon.set(DEFAULT_ICON);
		
		item.addListener((obs, old, value) -> { Entry<String,Image> back = factory.call(value);
			title.set(back.getKey() == null || back.getKey().isEmpty() ? DEFAULT_TITLE : back.getKey());
			icon.set(back.getValue() == null || back.getValue().isError() ? DEFAULT_ICON : back.getValue());
		});
	}
	
	public GDKImageView()
	{
		System.out.println("Icon: "+ (DEFAULT_ICON.isError()));
	
		FXMLLoader loader = new FXMLLoader(GDKImageView.class.getResource("GDKImageView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try { loader.load(); } catch (IOException e) { throw new RuntimeException(e); }
	}
	public GDKImageView(T item)
	{
		this();
		this.item.set(item);
	}
	public GDKImageView(GDKImageView<T> base)
	{
		this(base.item.get());
		this.factory = base.factory;
	}
	public GDKImageView(GDKImageView<T> base, T item)
	{
		this(base);
		this.item.set(item);
	}
	
	public void setViewFactory(Callback<T, Entry<String,Image>> factory) { this.factory = factory; }
	public Callback<T, Entry<String, Image>> getViewFactory() { return factory; };
	
	public StringProperty titleProperty() { return title; }
	public ObjectProperty<Image> iconProperty() { return icon; }
	public ObjectProperty<T> itemProperty() { return item; }
	
	public String getTitle() { return title.get(); }
	public Image getIcon() { return icon.get(); }
	public T getItem() { return item.get(); }
	
	public void setTitle(String title) { this.title.set(title); }
	public void setIcon(Image icon) { this.icon.set(icon); }
	public void setItem(T item) { this.item.set(item); }
}