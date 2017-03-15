package com.advos.notehub.client.controller;

import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class FXMLController implements Initializable {
    
    @FXML private Button btAddNote;
    @FXML private TextFlow tfDisplayNote;
    @FXML private HTMLEditor heNote;
    @FXML private Tab tabNotesName;
    @FXML private TextArea txtNote;
    
    private String pathFile;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //btAddNote = new Button();
        //heNote = new HTMLEditor();
        //tabNotesName.setText("hello Anda!");
    }

    @FXML
    public void textingNote(){
        //String notes = heNote.getHtmlText();
        String notes = txtNote.getText();
        
        System.out.println(notes.endsWith("\n"));
        if(notes.endsWith("\n")){
            Text textNote;
            tfDisplayNote.getChildren().clear();
            tfDisplayNote.getChildren().add(new Text(stripHTMLTags(notes)));
        }
        
    }
    
    private String stripHTMLTags(String htmlText) {

        Pattern pattern = Pattern.compile("<[^>]*>");
        //Pattern pattern = Pattern.compile("/\\<body[^>]*\\>([^]*)\\<\\/body/m");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer sb = new StringBuffer(htmlText.length());
        while(matcher.find()) {
            matcher.appendReplacement(sb, " ");
        }
        matcher.appendTail(sb);
        return sb.toString().trim();

    }

    
    @FXML
    public void addNote(){
        Stage primaryStage = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choosing a directory as your note's name");
        File defaultDirectory = new File("c:/");
        dc.setInitialDirectory(defaultDirectory);
        File selectedDirectory = dc.showDialog(primaryStage);
        tabNotesName.setText(selectedDirectory.getName());
        pathFile = selectedDirectory.getAbsolutePath();
        //System.out.println(pathFile+" "+selectedDirectory.getName());
    }


    
}
