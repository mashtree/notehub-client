package com.advos.notehub.client.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.advos.notehub.client.gui.ShapeType;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import static javafx.application.Application.launch;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author aisyahumar
 */
public class CanvasController implements Initializable {
    
    @FXML Pane pane;
    @FXML Canvas canvas;
    
    @FXML Button btPencil;
    @FXML Button btErase;
    @FXML Button btOval;
    @FXML Button btRectangle;
    @FXML Button btLine;
    @FXML Button btTextBox;
    @FXML Button btSave;
    
    @FXML ColorPicker cpColor;
    @FXML ColorPicker cpStroke;
    @FXML ComboBox cbStroke;
    
    private Color color;
    private Color stroke;
    private int lineWidth;
    private ShapeType shapeType;
    private double x1;
    private double y1;
    MouseGestures mg;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mg = new MouseGestures();
        color = Color.TRANSPARENT;
        stroke = Color.BLACK;
        lineWidth = 5;
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        initDraw(gc);
        btPencil.setOnAction(e->freeDraw(canvas, gc));
        btErase.setOnAction(e->eraseShape(canvas, gc));
        btOval.setOnMousePressed(e->handleEvent(e,btOval,ShapeType.OVAL));
        btRectangle.setOnMousePressed(e->handleEvent(e,btRectangle,ShapeType.RECTANGLE));
        btLine.setOnMousePressed(e->handleEvent(e,btLine,ShapeType.LINE));
        cpColor.setValue(stroke);
        cpColor.setOnAction(e->setColor("color"));
        cpStroke.setValue(stroke);
        cpStroke.setOnAction(e->setColor("stroke"));
        
    }
    
    private void setColor(String param){
        if(param.equalsIgnoreCase("color")){
            color = cpColor.getValue();
        }else{
            stroke = cpStroke.getValue();
        }
    }
    
    static class TextNode{
        int x;
        int y;
        
        int fontSize;
        String fontType;
        int fontStyle; //bold 1, italic 2, underline 3, normal 0
        
        public TextNode(int x, int y, int fontS, String fontT, int fontstyle){
            this.x = x;
            this.y = y;
            fontSize = fontS;
            fontType = fontT;
            fontStyle = fontstyle;
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    EventHandler<MouseEvent> ehCircle = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent ev) {
            createCircle(ev);
            //sp.removeEventFilter(MouseEvent.MOUSE_PRESSED, this);
        }

    };
    
    EventHandler<MouseEvent> ehRect = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent ev) {
            createRectangle(ev);
            
        }

    };
    
    EventHandler<MouseEvent> ehLine = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent ev) {
            createLine(ev);
            
        }

    };
    
    //private void removeEventHandler(Pane o, EventHandler eh){
    private void removeEventHandler(Pane o){
        //if(eh==ehCircle){
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehRect);
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehLine);
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehCircle);
        //}
        /*
        if(eh==ehRect){
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehCircle);
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehLine);
        }
        
        if(eh==ehLine){
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehRect);
            o.removeEventFilter(MouseEvent.MOUSE_PRESSED, ehCircle);
        }*/
    }
    
    private void handleEvent(Event e, Button b, ShapeType st){
        System.out.println(b.getText()+"-"+st);
        shapeType = st;
        if(st == ShapeType.OVAL){
             
            //sp.setOnMousePressed(ev->createCircle(ev));
            ///removeEventHandler(pane);
            pane.addEventFilter(MouseEvent.MOUSE_PRESSED, ehCircle);
            
        }
        
        if(st == ShapeType.RECTANGLE){
             
            //sp.setOnMousePressed(ev->createRectangle(ev));
            
            pane.addEventFilter(MouseEvent.MOUSE_PRESSED, ehRect);
            //removeEventHandler(pane);
        }
        
        if(st == ShapeType.LINE){
             
            
            pane.addEventFilter(MouseEvent.MOUSE_PRESSED, ehLine);
            //removeEventHandler(pane);
            /*Line line = new Line(50,50,400,400);
            line.setStrokeWidth(10);
            sp.getChildren().add(line);
            mg.makeLineDraggable(line);*/
        }
        
        if(st == ShapeType.TEXT){
            
        } 
    }
    
    private void createCircle(MouseEvent e){
        setInitialCoordinate(e);
        
        final Circle c  = new Circle(e.getX(),e.getY(),5,color);
        //c.setStroke(Color.BLACK);
        //c.setFill(Color.WHITE);
       // c.setCenterX(x1);
        //c.setCenterY(y1);
       // c.setFill(color);
        c.setStroke(stroke);
        pane.getChildren().add(c);
        

        c.setOnMouseDragged(ev->{
            //the coordinate is not located in the proper place, it is far away from the mouse clicked location
            double x2 = ev.getSceneX();
            double y2 = ev.getSceneX();
            c.setCenterX(x1);
            c.setCenterY(y1);
            c.setRadius(Math.abs(x2-x1));
            
        });
        
        c.setOnMouseReleased(ev->{
            x1 = 0.0;
            y1 = 0.0;
            mg.makeCircleDraggable(c);
            removeEventHandler(pane);
        });
    }
    
    private void createRectangle(MouseEvent e){
        setInitialCoordinate(e);
        final Rectangle rect = new Rectangle();
        rect.setStroke(Color.RED);
        rect.setFill(Color.LIGHTGRAY);
        rect.setWidth(5);
        rect.setHeight(5);
        rect.setFill(color);
        rect.setStroke(stroke);
        pane.getChildren().add(rect);
        rect.setOnMouseDragged(ev->{
            double x2 = ev.getSceneX();
            double y2 = ev.getSceneY();
            rect.setWidth(Math.abs(x2-x1));
            rect.setHeight(Math.abs(y2-y1));
        });
        rect.setOnMouseReleased(ev->{
            mg.makeRectDraggable(rect);
            removeEventHandler(pane);
        });
        
    }
    
    private void createLine(MouseEvent e){
        setInitialCoordinate(e);
        final Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x1+5);
        line.setEndY(y1+5);
        line.setFill(color);
        line.setStroke(stroke);
        line.setStrokeWidth(lineWidth);
        pane.getChildren().add(line);
        line.setOnMouseDragged(ev->{
            double x2 = ev.getSceneX();
            double y2 = ev.getSceneY();
            line.setEndX(x2);
            line.setEndY(y2);
        });
        
        line.setOnMouseReleased(ev->{
            mg.makeLineDraggable(line);
            removeEventHandler(pane);
        });
    }
    
    private void setInitialCoordinate(MouseEvent e){
        x1 = e.getX();
        y1 = e.getY();
        System.out.println(x1+"-"+y1);
    }
    
    private void freeDraw(Canvas canvas, GraphicsContext gc){
        canvas.setOnMousePressed(e->{
            gc.setStroke(Color.RED);
            gc.setLineWidth(lineWidth);
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });
        canvas.setOnMouseDragged(e->{
            gc.setStroke(Color.PURPLE);
            gc.setLineWidth(lineWidth);
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
        
    }
    
    private void eraseShape(Canvas canvas, GraphicsContext gc){
        canvas.setOnMouseDragged(e->{
           gc.clearRect(e.getX() - 2, e.getY() - 2, 20, 20);
        });
    }
    
    private void initDraw(GraphicsContext gc){
        gc.setLineWidth(5);
        gc.fill();
        gc.setFill(Color.RED);
        gc.setStroke(color);
        gc.setLineWidth(1);
    }
    
    /**
     * class mousegesture
     */
    public static class MouseGestures {

        double orgSceneX, orgSceneY;
        double orgTranslateX, orgTranslateY;

        public void makeCircleDraggable(Node node) {
            node.setOnMousePressed(circleOnMousePressedEventHandler);
            node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        }
        
        public void makeRectDraggable(Node node) {
            node.setOnMousePressed(rectOnMousePressedEventHandler);
            node.setOnMouseDragged(rectOnMouseDraggedEventHandler);
        }
        
        public void makeLineDraggable(Node node) {
            node.setOnMousePressed(lineOnMousePressedEventHandler);
            node.setOnMouseDragged(lineOnMouseDraggedEventHandler);
        }
        
        EventHandler<MouseEvent> lineOnMousePressedEventHandler = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Line)(t.getSource())).getTranslateX();
            orgTranslateY = ((Line)(t.getSource())).getTranslateY();
        }
    };
        
        EventHandler<MouseEvent> lineOnMouseDraggedEventHandler = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            ((Line)(t.getSource())).setTranslateX(newTranslateX);
            ((Line)(t.getSource())).setTranslateY(newTranslateY);
        }
    };
        
        EventHandler<MouseEvent> rectOnMousePressedEventHandler = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Rectangle)(t.getSource())).getTranslateX();
            orgTranslateY = ((Rectangle)(t.getSource())).getTranslateY();
        }
    };
        
        EventHandler<MouseEvent> rectOnMouseDraggedEventHandler = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            ((Rectangle)(t.getSource())).setTranslateX(newTranslateX);
            ((Rectangle)(t.getSource())).setTranslateY(newTranslateY);
        }
    };

        EventHandler<MouseEvent> circleOnMousePressedEventHandler = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
        }
    };
     
    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = 
        new EventHandler<MouseEvent>() {
 
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;
             
            ((Circle)(t.getSource())).setTranslateX(newTranslateX);
            ((Circle)(t.getSource())).setTranslateY(newTranslateY);
        }
    };

    }
    
}
