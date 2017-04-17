/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.canvas;

import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author aisyahumar
 */
public class CanvasNote extends VBox{
    
    private StackPane root;
    public CanvasNote(){
        root = new StackPane();
    }
    
    public Map<Integer, Canvas> layers = new HashMap<>();
    
    public void erase(Canvas canvas){
        
       final GraphicsContext gc = canvas.getGraphicsContext2D();
       // Clear away portions as the user drags the mouse
       canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
       new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent e) {
               gc.clearRect(e.getX() - 2, e.getY() - 2, 5, 5);
           }
       });
       
    }
 
    public void clearAll(final Canvas canvas){
    // Fill the Canvas with a Blue rectnagle when the user double-clicks
       canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, 
        new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {            
                if (t.getClickCount() >1) {
                    reset(canvas, Color.BLUE);
                }  
            }
        });
    }
    
    private void reset(Canvas canvas, Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    
    private void createLayers(int width, int height){
        
        // Layers 1&2 are the same size
        Canvas layer = new Canvas(300,250);
        GraphicsContext gc = layer.getGraphicsContext2D();
        // Obtain Graphics Contexts
        gc = layer.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillOval(50,50,20,20);
        int index = layers.size()+1;
        layers.put(index, layer);
        putLayerOnTop(index);
    }
    
    private void putLayerOnTop(int index){
        Canvas tmp = layers.get(1);
        layers.put(1, layers.get(index));
        layers.put(index, tmp);
        
    }
       

    private void addLayers(Canvas layer, Group root){
        // Add Layers
        //temporary
        BorderPane borderPane = new BorderPane();
        ChoiceBox cb = new ChoiceBox();
        borderPane.setTop(cb);        
        Pane pane = new Pane();
        pane.getChildren().add(layer);
       
        layer.toFront();
        borderPane.setCenter(pane);    
        root.getChildren().add(borderPane);
    }

    private void handleLayers(Canvas layer){
        final GraphicsContext gc = layer.getGraphicsContext2D();
        // Handler for Layer 1
        layer.addEventHandler(MouseEvent.MOUSE_PRESSED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {          
                    gc.fillOval(e.getX(),e.getY(),20,20);
                }
            });

        // Handler for Layer 2
        /*layer2.addEventHandler(MouseEvent.MOUSE_PRESSED, 
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    gc2.fillOval(e.getX(),e.getY(),20,20);
                }
            });
        */
    }

    
}
