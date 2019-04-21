package net.gunivers.gdk.gui;

import javafx.fxml.FXML;
import net.gunivers.gdk.Main;

public abstract class Controller
{
	protected Main app;
	
	public void setMainApplication(Main app)
	{
		this.app = app;
		this.refresh();
	}
	
	@FXML @Deprecated
	public abstract void initialize();
	public void refresh() {}
}
