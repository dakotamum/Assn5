package cs2410.assn5.interact;

import cs2410.assn5.tools.DrawingPane;
import cs2410.assn5.tools.ToolPane;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import static java.lang.Math.abs;

public class Viewer extends Application
{
    DrawingPane drawPane;
    ToolPane toolPane;
    BorderPane rootPane;
    Scene someScene;
    double deltaX, deltaY;
    Rectangle rectangle;
    Ellipse ell;
    Path path;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage primaryStage)
    {
        drawPane = new DrawingPane();
        toolPane = new ToolPane();
        rootPane = new BorderPane();
        rootPane.setTop(toolPane);
        rootPane.setCenter(drawPane);
        someScene = new Scene(rootPane, 500, 500);
        primaryStage.setScene(someScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        drawPane.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                if (toolPane.rectBtnSelected())
                {
                    rectangle = new Rectangle();
                    drawPane.getChildren().add(rectangle);
                    rectangle.setFill(toolPane.getFillPickerValue());
                    rectangle.setStroke(toolPane.getStrokePickerValue());
                    rectangle.setStrokeWidth(toolPane.getStrokeSizeValue());
                    rectangle.setLayoutX(event.getX());
                    deltaX = rectangle.getLayoutX();
                    rectangle.setLayoutY(event.getY());
                    deltaY = rectangle.getLayoutY();
                }

                if (toolPane.ellBtnSelected())
                {
                    ell = new Ellipse();

                    ell.setFill(toolPane.getFillPickerValue());
                    ell.setStroke(toolPane.getStrokePickerValue());
                    ell.setStrokeWidth(toolPane.getStrokeSizeValue());
                    ell.setLayoutX(event.getX());
                    deltaX = ell.getLayoutX();
                    ell.setLayoutY(event.getY());
                    deltaY = ell.getLayoutY();
                    drawPane.getChildren().add(ell);
                }

                if (toolPane.freeBtnSelected())
                {
                    path = new Path();
                    drawPane.getChildren().add(path);
                    path.setStroke(toolPane.getStrokePickerValue());
                    path.setStrokeWidth(toolPane.getStrokeSizeValue());
                    path.getElements().add(new MoveTo(event.getX(), event.getY()));
                }

                if (toolPane.eraseBtnSelected())
                {
                    drawPane.getChildren().remove(event.getTarget());
                }
            }
        });

        drawPane.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                if (toolPane.rectBtnSelected())
                {

                    double dx = event.getX() - deltaX;
                    double dy = event.getY() - deltaY;


                    if (dx < 0)
                    {
                        rectangle.setTranslateX(dx);
                        rectangle.setWidth(-dx);
                    } else
                    {
                        rectangle.setTranslateX(0);
                        rectangle.setWidth(dx);
                    }

                    if (dy < 0)
                    {
                        rectangle.setTranslateY(dy);
                        rectangle.setHeight(-dy);
                    }
                    else
                    {
                        rectangle.setTranslateY(0);
                        rectangle.setHeight(dy);
                    }
                }

                if (toolPane.ellBtnSelected())
                {
                    double dx = event.getX() - deltaX;
                    double dy = event.getY() - deltaY;

                    if(dx < 0)
                    {
                        ell.setCenterX(dx/2);
                        ell.setRadiusX(-dx/2);
                    }
                    else
                    {
                        ell.setCenterX(dx/2);
                        ell.setRadiusX(dx/2);
                    }

                    if(dy < 0)
                    {
                        ell.setCenterY(dy/2);
                        ell.setRadiusY(-dy/2);
                    }
                    else
                    {
                        ell.setCenterY(dy/2);
                        ell.setRadiusY(dy/2);
                    }

                }

                if (toolPane.freeBtnSelected())
                {
                    path.getElements().add(new LineTo(event.getX(), event.getY()));
                }
            }
        });
    }
}