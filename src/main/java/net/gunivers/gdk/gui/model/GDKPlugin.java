package net.gunivers.gdk.gui.model;

import java.io.File;
import java.io.Serializable;

import javafx.scene.image.Image;

import static net.gunivers.gdk.gui.components.GDKImageView.DEFAULT_ICON;

public class GDKPlugin implements Serializable
{
	private static final long serialVersionUID = 1160705032547915636L;
	
	protected String name		 = "<Unknown>";
	protected String author		 = "<Unknown>";
	protected String link		 = "<No Link Provided>";
	protected String version	 = "<Unknown>";
	protected String description = "<No Description Provided>";
	
	protected String path =			"<No Path Provided>";
	protected File jar = new File(path);
	
	protected String header = "<No Header>";
	
	protected transient Image icon = DEFAULT_ICON;
	
	private String getHeader(String path)
	{
		String[] split = path.split(".");
		if (split.length >= 3) return split[0] +'.'+ split[1]  +'.'+ split[2];
		return "<No Header>";
	}
	
	public String getName() { return name; }
	public String getAuthor() { return author; }
	public String getVersion() { return version; }
	public String getDescription() { return description; }
	
	public String getPath() { return path; }
	public File getJarFile() { return jar; }
	public Image getIcon() { return icon; }
	
	public String getPathHeader() { return header; }
	
	public void setName(String name) { this.name = name; }
	public void setAuthor(String author) { this.author = author; }
	public void setLink(String link) { this.link = link; }
	public void setVersion(String version) { this.version = version; }
	public void setDescription(String description) { this.description = description; }
	
	public void setPath(String path) { this.path = path; this.header = this.getHeader(path); }
	public void setJarFile(File jar) { this.jar = jar; }
	public void setIcon(Image icon) { this.icon = icon; }
}
