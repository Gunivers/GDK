package fr.gunivers.gdk.gui.model;

import java.io.File;
import java.io.Serializable;

public class GDKPlugin implements Serializable
{
	private static final long serialVersionUID = 1160705032547915636L;
	
	protected String name = "<GDK Plugin>";
	protected String author = "<Unknown>";
	protected String version = "<Unknown>";
	protected String description = "<No Description Provided>";
	
	protected String path = "";
	protected File jar = new File("<No Path Provided>");
	
	public GDKPlugin() {}
	public GDKPlugin(String name, String author, String version, String description)
	{
		this.name = name;
		this.author = author;
		this.version = version;
		this.description = description;
	}
	public GDKPlugin(String name, String author, String version, String description, String path, File jar)
	{
		this(name, author, version, description);
		this.path = path;
		this.jar = jar;
	}
	
	public String getName() { return name; }
	public String getAuthor() { return author; }
	public String getVersion() { return version; }
	public String getDescription() { return description; }
	
	public String getPath() { return path; }
	public File getJarFile() { return jar; }
	
	public void setName(String name) { this.name = name; }
	public void setAuthor(String author) { this.author = author; }
	public void setVersion(String version) { this.version = version; }
	public void setDescription(String description) { this.description = description; }
	
	public void setPath(String path) { this.path = path; }
	public void setJarFile(File jar) { this.jar = jar; }
}
