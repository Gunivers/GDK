package fr.gunivers.gdk.gui.util;

import javafx.fxml.FXML;

public abstract class Controller
{
	protected Application app;
	
	public void setMainApplication(Application app)
	{
		this.app = app;
		this.refresh();
	}
	
	@FXML @Deprecated
	public abstract void initialize();
	public void refresh() {}
}
