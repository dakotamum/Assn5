package cs2410.assn5.tools;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class DrawingPane extends Pane
{
    private Circle circle;

    public DrawingPane()
    {
        Rectangle clip = new Rectangle(0, 0, 500, 500);
        this.setClip(clip);
        this.setStyle("-fx-background-color: white");
    }
}