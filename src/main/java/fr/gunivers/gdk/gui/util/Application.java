package fr.gunivers.gdk.gui.util;

import javafx.stage.Stage;

public abstract class Application extends javafx.application.Application
{
	protected static Application app;
	protected static Stage stage;

	@Override
	public void start(Stage primaryStage)
	{	
		Application.app = this;
		Application.stage = primaryStage;
		
		initialize();
	}
	
	public abstract void initialize();
	
	@SuppressWarnings("unchecked")
	public static <A extends Application> A getApp() { return (A) app; }
	public static Stage getStage() { return stage; }
}
