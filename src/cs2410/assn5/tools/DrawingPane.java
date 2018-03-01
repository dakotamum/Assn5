package cs2410.assn5.tools;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * DrawingPane is a simple class that provides formatting for a drawing pane
 * @author Dakota Mumford
 * @version 1.0
 */
public class DrawingPane extends Pane
{
    public DrawingPane()
    {
        // sets a clip for the drawing pane
        Rectangle clip = new Rectangle(5, 0, 500, 470);
        this.setClip(clip);
        this.setStyle("-fx-background-color: white");
    }
}