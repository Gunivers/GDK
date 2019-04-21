package net.gunivers.gdk.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.gunivers.gdk.Main;
import net.gunivers.gdk.Util;
import net.gunivers.gdk.Util.PATH;
import net.gunivers.gdk.gui.Controller;
import net.gunivers.gdk.gui.model.GDKPlugin;

//fx:controller="net.gunivers.gdk.gui.controller.DebugController"

public class DebugController extends Controller
{
	public static final char INFO = '§';
	public static final char WARN = '!';
	public static final char ERR = '#';
	public static final char OTHER = '¤';
	
	public static DebugController CONTROLLER;
	public static boolean DEBUGGING = false;
	
	private static final Stage stage = new Stage(StageStyle.UNIFIED);
	
	private final StringBuilder builder = new StringBuilder();
	private final ArrayList<Text> lines = new ArrayList<>();
	
	@FXML private TextFlow text;
	@FXML private ScrollBar scroll;
	
	static {
		stage.setTitle("Debug Console");
	}
	
	public static DebugController run()
	{
		DEBUGGING = true;
		Util.loadFXML(DebugController.class.getResource(PATH.FXML + "Debug.fxml"), (p) -> { stage.setScene(new Scene(p)); });
		stage.show();
		
		return CONTROLLER;
	}
	
	@Override
	public void initialize()
	{
		CONTROLLER = this;
		
		text.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		text.setOnScroll(e -> scrollAction((int) - e.getDeltaY() / 3));
		
		System.setOut(new PrintStream(new DebugOutputStream(System.out, INFO)));
		System.setErr(new PrintStream(new DebugOutputStream(System.err, ERR)));
	}
	
	public void logInfo(String txt) { log(INFO + txt, true); }
	public void logWarning(String txt) { log(WARN + txt, true); }
	public void logError(String txt) { log(ERR + txt, true); }
	
	/**
	 * 
	 * @param txt
	 * @param alreadyFormated a boolean, set it <code>true</code> if the first char of txt either is: 
	 * <ul><li>INFO: §</li><li>WARN: !</li><li>ERR: #</li><li>OTHER: ¤</li></ul>
	 */
	public void log(String txt, boolean alreadyFormated)
	{
		if (DEBUGGING)
		{
			this.builder.append((!alreadyFormated ? OTHER : "") + txt +'\n');
			log();
		}
	}
	
	private void log()
	{
		Text text = new Text(this.getDate());
		String type = " "; Color color = Color.WHITE;
		
		switch (builder.charAt(0))
		{
			case INFO:
				type += "[INFO]\t";
				color = Color.AQUA;
				break;
			case WARN:
				type += "[WARN]\t";
				color = Color.YELLOW;
				break;
			case ERR:
				type += "[ERROR]\t";
				color = Color.RED;
				break;
		}
		
		text.setText(getDate() + type + builder.substring(1));
		text.setFill(color); text.setFont(Font.font("System"));
		
		for (GDKPlugin plugin : Main.plugins) if (builder.toString().contains(plugin.getPathHeader()))
			text.setFill(Color.PURPLE);
		
		if (builder.toString().contains("net.gunivers.gdk")) text.setFill(Color.PURPLE);
		
		this.text.getChildren().add(text);
		this.lines.add(text);
		builder.setLength(0);
	}

	public String getDate() { return "["+ new SimpleDateFormat("hh:mm:ss").format(new Date()) +"]"; }
	
	public void write(int b)
	{
		if (DEBUGGING)
		{
			builder.append((char) b);
			if ((char) b == '\n') log();
		}
	}
	
	/**
	 * This method is called whenever the ScrollBar of the debug menu is subject to an user's movement.
	 * It make the TextFlow shows only the lines from the index indicated by the ScrollBar value,
	 * hence this method may be used in order to refresh the TextFlow's 'affichage en anglais'
	 */
	@FXML
	public void onScrollAction()
	{
		this.text.getChildren().clear();
		int index = (int) scroll.getValue();
		
		if (index < lines.size())
			this.text.getChildren().addAll(lines.subList(index, lines.size()));
		else
		{
			this.text.getChildren().addAll(lines.subList(lines.size() -1, lines.size()));
			scroll.setValue(lines.size());
		}
	}

	@Deprecated
	public void scrollAction(int delta)
	{
		this.scroll.setValue(((int) scroll.getValue()) + delta > 0 ? ((int) scroll.getValue()) + delta : 0); 
		this.onScrollAction();
	}
}

class DebugOutputStream extends OutputStream
{
	private final OutputStream parent;
	private final char c;
	private boolean writing = false;
	
	public DebugOutputStream(OutputStream parent, char c)
	{
		this.parent = parent;
		this.c = c;
	}
	
	@Override
	public void write(int b) throws IOException
	{
		parent.write(b);
		if (writing)
		{
			DebugController.CONTROLLER.write(b);
			if (b == '\n') writing = false;
		} else
		{
			DebugController.CONTROLLER.write(c);
			DebugController.CONTROLLER.write(b);
			writing = b != '\n';
		}
	}
}