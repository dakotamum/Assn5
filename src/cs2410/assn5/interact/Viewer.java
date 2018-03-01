package cs2410.assn5.interact;

import cs2410.assn5.tools.DrawingPane;
import cs2410.assn5.tools.ToolPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

/**
 * Viewer class is the application that interacts with the user
 * @author Dakota Mumford
 * @version 1.0
 */
public class Viewer extends Application
{
    private DrawingPane drawPane;
    private ToolPane toolPane;
    private double deltaX, deltaY;
    private Rectangle rectangle;
    private Ellipse ell;
    private Path path;
    private Shape selectedShape;

    /**
     * main method launches the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the drawing application and sets the primary stage
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        drawPane = new DrawingPane();
        toolPane = new ToolPane();
        BorderPane rootPane = new BorderPane();
        rootPane.setTop(toolPane);
        rootPane.setCenter(drawPane);
        Scene someScene = new Scene(rootPane, 500, 500);
        primaryStage.setScene(someScene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Super Amazing Drawing Application");
        primaryStage.show();

        /**
         * Event handler for when the mouse is pressed on the drawing pane
         */
        drawPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // adds a rectangle of characteristics selected on the tool pane
                if (toolPane.rectBtnSelected()) {
                    rectangle = new Rectangle();
                    rectangle.setFill(toolPane.getFillPickerValue());
                    rectangle.setStroke(toolPane.getStrokePickerValue());
                    rectangle.setStrokeWidth(toolPane.getStrokeSizeValue());
                    rectangle.setLayoutX(event.getX());
                    deltaX = rectangle.getLayoutX();
                    rectangle.setLayoutY(event.getY());
                    deltaY = rectangle.getLayoutY();
                    drawPane.getChildren().add(rectangle);
                }

                // adds an ellipse of characteristics selected on the tool pane
                if (toolPane.ellBtnSelected()) {
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

                // adds a path of characteristics selected on the tool pane
                if (toolPane.freeBtnSelected()) {
                    path = new Path();
                    path.setStroke(toolPane.getStrokePickerValue());
                    path.setStrokeWidth(toolPane.getStrokeSizeValue());
                    path.getElements().add(new MoveTo(event.getX(), event.getY()));
                    drawPane.getChildren().add(path);
                }
            }
        });

        /**
         * Event handler for when the mouse is dragged on the drawing pane
         */
        drawPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // run calculations to position rectangle accordingly
                if (toolPane.rectBtnSelected()) {
                    double dx = event.getX() - deltaX;
                    double dy = event.getY() - deltaY;

                    if (dx < 0) {
                        rectangle.setTranslateX(dx);
                        rectangle.setWidth(-dx);
                    }
                    else {
                        rectangle.setTranslateX(0);
                        rectangle.setWidth(dx);
                    }

                    if (dy < 0) {
                        rectangle.setTranslateY(dy);
                        rectangle.setHeight(-dy);
                    }
                    else {
                        rectangle.setTranslateY(0);
                        rectangle.setHeight(dy);
                    }
                    setShapeHandler(rectangle);

                    // set this rectangle to selected shape
                    selectedShape = rectangle;
                    toolPane.setFillPickerAction(pickerAction);
                    toolPane.setStrokePickerAction(strokeAction);
                    toolPane.setStrokeSizeAction(strokeWidthAction);
                }

                // run calculations to position ellipse accordingly
                if (toolPane.ellBtnSelected()) {
                    double dx = event.getX() - deltaX;
                    double dy = event.getY() - deltaY;

                    if(dx < 0) {
                        ell.setCenterX(dx/2);
                        ell.setRadiusX(-dx/2);
                    }
                    else {
                        ell.setCenterX(dx/2);
                        ell.setRadiusX(dx/2);
                    }

                    if(dy < 0) {
                        ell.setCenterY(dy/2);
                        ell.setRadiusY(-dy/2);
                    }
                    else {
                        ell.setCenterY(dy/2);
                        ell.setRadiusY(dy/2);
                    }
                    setShapeHandler(ell);

                    // set this rectangle to selected shape
                    selectedShape = ell;
                    toolPane.setFillPickerAction(pickerAction);
                    toolPane.setStrokePickerAction(strokeAction);
                    toolPane.setStrokeSizeAction(strokeWidthAction);
                }

                // run calculations to position path accordingly
                if (toolPane.freeBtnSelected()) {
                    path.getElements().add(new LineTo(event.getX(), event.getY()));
                    setShapeHandler(path);
                    // set this path to selected shape
                    selectedShape = path;
                    toolPane.setStrokePickerAction(strokeAction);
                    toolPane.setStrokeSizeAction(strokeWidthAction);
                }
            }
        });
    }

    /**
     * Event handler to be used for selecting shapes in edit mode as well as erasing shapes
     * @param shape
     */
    private void setShapeHandler(Shape shape) {
        shape.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(toolPane.editBtnSelected()) {
                    selectedShape = shape;                                       // set shape to selected
                    deltaX = event.getX();
                    deltaY = event.getY();

                    drawPane.getChildren().remove(shape);                        // bring shape to front of pane
                    drawPane.getChildren().add(shape);

                    toolPane.setFillPickerValue((Color)shape.getFill());         // set tool pane options to those of selected shape
                    toolPane.setStrokePickerValue((Color)shape.getStroke());
                    toolPane.setStrokeSizeValue((int)shape.getStrokeWidth());

                    toolPane.setFillPickerAction(pickerAction);                  // set action event handlers
                    toolPane.setStrokePickerAction(strokeAction);
                    toolPane.setStrokeSizeAction(strokeWidthAction);
                }
                else if(toolPane.eraseBtnSelected()) {                           // remove shape if erase button is pressed
                    drawPane.getChildren().remove(shape);
                }
            }
        });

        /**
         * Event handler to be used to drag shapes in edit mode
         */
        shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(toolPane.editBtnSelected()) {
                    shape.setTranslateX(shape.getTranslateX() + event.getX() - deltaX);     // calculate new position of shape
                    shape.setTranslateY(shape.getTranslateY() + event.getY() - deltaY);
                }
            }
        });
    }

    /**
     * Event handler to be used for setting a selected shape to a new fill
     */
    private EventHandler<ActionEvent> pickerAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (toolPane.editBtnSelected()) {
                if (selectedShape instanceof Rectangle || selectedShape instanceof Ellipse) {
                    selectedShape.setFill(toolPane.getFillPickerValue());
                }
            }
        }
    };

    /**
     * Event handler to be used for setting a selected shape to a new stroke color
     */
    private EventHandler<ActionEvent> strokeAction = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event) {
            if (toolPane.editBtnSelected()) {
                selectedShape.setStroke(toolPane.getStrokePickerValue());
            }
        }
    };

    /**
     * Event handler to be used for setting a selected shape to a new stroke width
     */
    private EventHandler<ActionEvent> strokeWidthAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if(toolPane.editBtnSelected()) {
                selectedShape.setStrokeWidth(toolPane.getStrokeSizeValue());
            }
        }
    };
}