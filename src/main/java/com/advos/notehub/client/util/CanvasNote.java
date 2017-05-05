/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import com.advos.notehub.client.util.sandsoft.CustomHTMLEditor;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

/**
 *
 * @author aisyahumar
 */
public class CanvasNote {
    
    private Color color = Color.BLACK;
    private int lineWidth = 5;
    
    
    public void freeDraw(Canvas canvas, GraphicsContext gc){
        canvas.setOnMousePressed(e->{
            gc.setStroke(getColor());
            gc.setLineWidth(getLineWidth());
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });
        canvas.setOnMouseDragged(e->{
            gc.setStroke(getColor());
            gc.setLineWidth(getLineWidth());
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });
        
    }
    
    public void eraseShape(Canvas canvas, GraphicsContext gc){
        canvas.setOnMouseDragged(e->{
           gc.clearRect(e.getX() - 2, e.getY() - 2, 20, 20);
        });
    }
    
    public void initDraw(GraphicsContext gc){
        gc.setLineWidth(getLineWidth());
        gc.fill();
        gc.setFill(getColor());
        gc.setStroke(getColor());
        gc.setLineWidth(1);
    }
    
    public void savePicture(Canvas canvas, CustomHTMLEditor che){
        File img = takeSnapShot(canvas);
        che.importDataFile(img);
        img.delete();
        
    }
    
    private File takeSnapShot(Canvas canvas){
        File output = null;
        try {
                SnapshotParameters parameters = new SnapshotParameters();
                WritableImage wi = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                WritableImage snapshot = canvas.snapshot(parameters, wi);

                output = new File("snapshot" + new Date().getTime() + ".png");
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
            } catch (IOException ex) {
                //Logger.getLogger(TakeSnapShoot.class.getName()).log(Level.SEVERE, null, ex);
            }
        return output;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return the lineWidth
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * @param lineWidth the lineWidth to set
     */
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }
    
}
