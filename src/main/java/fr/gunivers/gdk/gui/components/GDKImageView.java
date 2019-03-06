package fr.gunivers.gdk.gui.components;

import java.io.IOException;

import fr.gunivers.gdk.Main;
import fr.gunivers.gdk.Main.PATH;

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

public class GDKImageView extends AnchorPane
{
	@FXML private AnchorPane component;
	@FXML private Label label;
	@FXML private ImageView image;
	
	private SimpleStringProperty title = new SimpleStringProperty("<Title>");
	private SimpleObjectProperty<Image> icon = new SimpleObjectProperty<>(new Image(
			Main.class.getResource(PATH.RESOURCE+"img/no_icon.png").toExternalForm() ));
	
	{
		title.addListener((obs, old, value) -> label.setText(value));
		icon.addListener((obs, old, value) -> image.setImage(value));
	}
	
	public GDKImageView()
	{
		FXMLLoader loader = new FXMLLoader(GDKImageView.class.getResource("GDKImageView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		
		try { loader.load(); } catch (IOException e) { throw new RuntimeException(e); }
	}

	public StringProperty titleProperty() { return title; }
	public ObjectProperty<Image> iconProperty() { return icon; }
	
	public String getTitle() { return title.get(); }
	public Image getIcon() { return icon.get(); }
	
	public void setTitle(String title) { this.title.set(title); }
	public void setIcon(Image icon) { this.icon.set(icon); }
}