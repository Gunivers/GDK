package fr.gunivers.gdk.gui.util;

import fr.gunivers.gdk.Main;
import javafx.fxml.FXML;

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
