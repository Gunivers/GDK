package fr.gunivers.gdk.gui.util;

import javafx.stage.Stage;

@SuppressWarnings("unchecked")
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
	
	public static <A extends Application> A getApp() { return (A) app; }
	public static <A extends Application> A getApp(Class<A> clazz) { return (A) app; }
	public static Stage getStage() { return stage; }
}
