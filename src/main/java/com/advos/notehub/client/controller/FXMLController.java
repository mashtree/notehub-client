package com.advos.notehub.client.controller;

import com.advos.notehub.client.dao.RepositoryDao;
import com.advos.notehub.client.entity.Note;
import com.advos.notehub.client.entity.Repository;
import com.advos.notehub.client.util.FileModer;
import com.advos.notehub.client.util.SQLiteConnection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    @FXML private Button btSave;
    @FXML private TextFlow tfDisplayNote;
    @FXML private HTMLEditor heNote;
    @FXML private Tab tabNotesName;
    @FXML private TextArea txtNote;
    @FXML private ListView lvNotes;
    
    private String pathFile;
    private Connection conn;
    private final Map<String,Note> notes = new HashMap<>();
    private String openedNote;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //btAddNote = new Button();
        //heNote = new HTMLEditor();
        //tabNotesName.setText("hello Anda!");
        conn = SQLiteConnection.connect();
        RepositoryDao repdao = new RepositoryDao(conn);
        listViewNotes(repdao);
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
    
    /**
     * removing hmtl tags on String
     * @param htmlText
     * @return 
     */
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

    /**
    * add note to repository and open it
    * @param
    * @return
    */
    @FXML
    public void addNote() throws IOException{
        
        Stage stage = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choosing a directory as your note's name");
        File defaultDirectory = new File("c:/");
        dc.setInitialDirectory(defaultDirectory);
        File selectedDirectory = dc.showDialog(stage);
        if(openedNote != null){
            
            String txtnote = txtNote.getText();
            // find the changes, record to the object
            getNoteChanges(txtnote);
            saveNote(txtnote); // save the opened note
            // update to database
            // close the opened note
            txtNote.setText("");
            // open the new note
        }else{
            Note note = notes.get(openedNote);
            note.setIsOpen(false);
        }
        openedNote = selectedDirectory.getName();
        tabNotesName.setText(openedNote);
        pathFile = selectedDirectory.getAbsolutePath();
        createFileConf(selectedDirectory);
        
        
        
        //---------- DATE
        DateFormat dateFormat = new SimpleDateFormat("Y-m-d HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        //----------- ./DATE
        
        //add repository to database
        Repository rep = new Repository(openedNote,pathFile,1,dateFormat.format(date));
        RepositoryDao repdao = new RepositoryDao(conn);
        repdao.create(rep);
        
        if(!notes.containsKey(selectedDirectory.getName()))
            notes.put(openedNote,new Note(selectedDirectory.getName(),true, "", pathFile));
        
        
        
        
    }
    
    /**
     * create Note's directory and version marker
     * @param selectedDirectory
     * @throws IOException 
     */
    private void createFileConf(File selectedDirectory) throws IOException{
        FileModer fm = new FileModer();
        //System.out.println(pathFile+" "+selectedDirectory.getName());
        String fileName = selectedDirectory.getName()+".txt";
        //System.out.println(pathFile+"/"+fileName);
        fm.writeFile("", pathFile+"/"+fileName);
        
        String confFile = "notehub";
        fm.createDirectory(pathFile+"/.conf");
        fm.createDirectory(pathFile+"/images");
        fm.writeFile("", pathFile+"/.conf/"+confFile);
        //System.out.println(pathFile+"/.conf/"+confFile);
    }
    
    /**
     * display Notes to listView
     * @param repdao 
     */
    private void listViewNotes(RepositoryDao repdao){
        ArrayList<Repository> list = repdao.selectAll();
        for(Repository rep:list){
            System.out.println(rep.getName_repo());
            lvNotes.getItems().add(rep.getName_repo());
        }
    }
    
    private void saveNote(String notecontent) throws IOException{
        String t = notecontent;
        ArrayList<String> s = strToArrayList(t);
        FileModer fm = new FileModer();
        fm.writeFile(s, pathFile+"/"+openedNote+".txt");
    }
    
    private ArrayList<Map<Integer, String>> getNoteChanges(String notecontent){
        ArrayList<Map<Integer, String>> am = new ArrayList<>();
        /**
         * this is the main feature
         */
        return am;
    }
    
    private ArrayList<String> strToArrayList(String s){
        ArrayList<String> a = new ArrayList<>();
        Scanner scanner = new Scanner(s);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          a.add(line);
        }
        scanner.close();
        return a;
    }
    
}
