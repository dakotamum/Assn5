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

public class Viewer extends Application
{
    private DrawingPane drawPane;
    private ToolPane toolPane;
    private double deltaX, deltaY;
    private Rectangle rectangle;
    private Ellipse ell;
    private Path path;
    private Shape selectedShape;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        drawPane = new DrawingPane();
        toolPane = new ToolPane();
        BorderPane rootPane = new BorderPane();
        rootPane.setTop(toolPane);
        rootPane.setCenter(drawPane);
        Scene someScene = new Scene(rootPane, 500, 500);
        primaryStage.setScene(someScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        drawPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
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

                if (toolPane.freeBtnSelected()) {
                    path = new Path();
                    path.setStroke(toolPane.getStrokePickerValue());
                    path.setStrokeWidth(toolPane.getStrokeSizeValue());
                    path.getElements().add(new MoveTo(event.getX(), event.getY()));
                    drawPane.getChildren().add(path);
                }
            }
        });

        drawPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
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
                }

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
                }

                if (toolPane.freeBtnSelected()) {
                    path.getElements().add(new LineTo(event.getX(), event.getY()));
                    setShapeHandler(path);
                }
            }
        });
    }

    private void setShapeHandler(Shape shape) {

        shape.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(toolPane.editBtnSelected()) {
                    selectedShape = shape;
                    deltaX = event.getX();
                    deltaY = event.getY();
                    drawPane.getChildren().remove(shape);
                    drawPane.getChildren().add(shape);
                    toolPane.setFillPickerValue((Color)shape.getFill());
                    toolPane.setStrokePickerValue((Color)shape.getStroke());
                    toolPane.setStrokeSizeValue((int)shape.getStrokeWidth());

                    toolPane.setFillPickerAction(pickerAction);
                    toolPane.setStrokePickerAction(strokeAction);
                    toolPane.setStrokeSizeAction(strokeWidthAction);
                }
                else if(toolPane.eraseBtnSelected()) {
                    drawPane.getChildren().remove(shape);
                }
            }
        });

        shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(toolPane.editBtnSelected()) {
                    shape.setTranslateX(shape.getTranslateX() + event.getX() - deltaX);
                    shape.setTranslateY(shape.getTranslateY() + event.getY() - deltaY);
                }
            }
        });
    }

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

    private EventHandler<ActionEvent> strokeAction = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent event) {
            if (toolPane.editBtnSelected()) {
                selectedShape.setStroke(toolPane.getStrokePickerValue());
            }
        }
    };

    private EventHandler<ActionEvent> strokeWidthAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if(toolPane.editBtnSelected()) {
                selectedShape.setStrokeWidth(toolPane.getStrokeSizeValue());
            }
        }
    };
}