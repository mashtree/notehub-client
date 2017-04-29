package com.advos.notehub.client.controller;

import com.advos.notehub.client.Conf;
import com.advos.notehub.client.dao.ChangesDao;
import com.advos.notehub.client.dao.RepositoryDao;
import com.advos.notehub.client.dao.UserDao;
import com.advos.notehub.client.entity.Changes;
import com.advos.notehub.client.entity.Note;
import com.advos.notehub.client.entity.Repository;
import com.advos.notehub.client.gui.CanvasText;
import com.advos.notehub.client.gui.Login;
import com.advos.notehub.client.entity.User;
import com.advos.notehub.client.util.ConfigurationManager;
import com.advos.notehub.client.util.FileComparator;
import com.advos.notehub.client.util.FileModer;
import com.advos.notehub.client.util.SQLiteConnection;
import com.advos.notehub.client.util.StringModer;
import com.advos.notehub.client.util.sandsoft.CustomHTMLEditor;
import com.notehub.api.service.AuthService;
import com.notehub.api.service.NoteChangesService;
import com.notehub.api.service.NotesService;
import com.notehub.api.service.UsersService;
import difflib.Chunk;
import difflib.Delta;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

public class FXMLController1 implements Initializable {
    
    @FXML private Button btAddNote;
    @FXML private Button btSave;
    @FXML private Button btLogout;
    @FXML private Button btLogin;
    //@FXML private TextFlow tfDisplayNote;
    @FXML private Tab tabNotesName;
    @FXML private TextArea txtNote;
    @FXML private ListView<String> lvNotes;
    @FXML private VBox vbNoteName;
    @FXML private VBox vbCanvas;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private Label lbWelcome;
    @FXML private MenuItem miSynch;
    @FXML private MenuItem miDelNote;
    
    private String ipAddress;
    private String pathFile;
    private Connection conn;
    private final Map<String,Note> notes = new HashMap<>();
    private String openedNote;
    private Map<String, Object> services = new HashMap<>();
    private Registry client;
    private Map<String, Object> serverService = new HashMap<>();
    private UsersService us;
    private NotesService ns;
    private NoteChangesService nss;
    private AuthService as;
    private RepositoryDao repdao;
    
    private CustomHTMLEditor che;
    private CanvasText ct;
    
    private Map<String,String> ulogin = new HashMap<>(); //key username, value password 
    
    public void addService(String nameService, Object o){
        services.put(nameService, o);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try{
            client = LocateRegistry.getRegistry("75.102.66.84s"
                    + "", Registry.REGISTRY_PORT);
            us = (UsersService) client.lookup("UsersServiceServer");
            
            ns = (NotesService) client.lookup("NoteServiceServer");
            serverService.put("NoteServiceServer", ns);
            
            nss = (NoteChangesService) client.lookup("NoteChangeServiceServer");
            serverService.put("NoteChangeServiceServer", nss);
            
            as = (AuthService) client.lookup("AuthServiceServer");
            com.notehub.api.entity.Note note = new com.notehub.api.entity.Note();
            note.setNoteTitle("note test");
            note.setDescription("blablabla");
            note.setOwner(1);
            ns.insertNote(note);
            
            ipAddress = Inet4Address.getLocalHost().getHostAddress();
        }catch(RemoteException re){
            System.out.println("Remote Exception : "+re);
        }catch(NotBoundException nbe){
            System.out.println("Not Bound Exception : "+nbe);
        }catch(UnknownHostException uhe){
            System.out.println("Unknown Host Exception : "+uhe);
        }
        
        btLogout.setVisible(false);
        
        try {
            conn = SQLiteConnection.connect();
        } catch (Exception ex) {
            Logger.getLogger(FXMLController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        repdao = new RepositoryDao(conn);
        listViewNotes(repdao);
        
        lvNotes.setOnMouseClicked((MouseEvent e) -> {
            if(e.getButton()==MouseButton.PRIMARY){
                try {
                    openNote(e);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        che = new CustomHTMLEditor();
        ct = new CanvasText();
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/canvas.fxml"));
        VBox canvas = null;
        try{
            canvas = fxmlloader.load();
            System.out.println(canvas.getClass().getName());
            vbCanvas.getChildren().add(canvas);
        }catch(IOException e){
            System.out.println("IOException : "+e.getMessage());
        }
        
        vbNoteName.getChildren().add(che);
        
        /**
         * listview context menu
         */
        miDelNote.setOnAction(e->{
            String note = lvNotes.getSelectionModel().getSelectedItem();
            deleteNote(note);
        });
        
        miSynch.setOnAction(e->{
            String note = lvNotes.getSelectionModel().getSelectedItem();
            try {
                noteSynchronizing(note, Conf.OnlineID);
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLController1.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //lvNotes.setContextMenu(contextMenu);
        
    }
    /**
     * 
     */
    @FXML
    public void textingNote(){
        
        String notes = txtNote.getText();
        
        if(notes.endsWith("\n")){
            Text textNote;
            //tfDisplayNote.getChildren().clear();
            //tfDisplayNote.getChildren().add(new Text(stripHTMLTags(notes)));
        }
        
    }

    /**
    * add note to repository and open it
    * @param
    * @return
    */
    @FXML
    public void addNote() throws IOException{
        StringModer sm = new StringModer();
        ConfigurationManager cm = new ConfigurationManager();
        Stage stage = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choosing a directory as your note's name");
        File defaultDirectory = new File("c:/");
        dc.setInitialDirectory(defaultDirectory);
        File selectedDirectory = dc.showDialog(stage);
        String str = "<html dir=\"ltr\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body contenteditable=\"true\">\n" +
                "</body>\n" +
                "</html>";
        if(openedNote != null){
            
            String txtnote = txtNote.getText();
            // find the changes, record to the object
            List<String> original = this.getOriginalContent(pathFile+"/"+openedNote+".not");
            Map<Integer, HashMap<ChangesDao.CHANGE,List>> changes = getNoteChanges((ArrayList<String>) original,sm.strToArrayList(txtnote));
            saveNote(); // save the opened note
            // update to database
            // close the opened note
            che.setHtmlText(str);//txtNote.setText("");
            // open the new note
        }else{
            if(notes.containsKey(openedNote))
                  notes.get(openedNote).setIsOpen(false);
        }
        openedNote = selectedDirectory.getName();
        tabNotesName.setText(openedNote);
        pathFile = selectedDirectory.getAbsolutePath();
        cm.createFileConf(selectedDirectory, pathFile);
        
        //---------- DATE
        DateFormat dateFormat = new SimpleDateFormat("Y-m-d HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        //----------- ./DATE
        
        //add repository to database
        Repository rep = new Repository(openedNote,pathFile,1,dateFormat.format(date));
        repdao.create(rep);
        listViewNotes(repdao);
        
        if(!notes.containsKey(selectedDirectory.getName()))
            notes.put(openedNote,new Note(selectedDirectory.getName(),true, "", pathFile));   
        
    }
    
    @FXML
    private void saveNote() throws IOException{
        //String t = notecontent;
        //String t = txtNote.getText();
        StringModer sm = new StringModer();
        String t = sm.addNewLineHTMLTags(che.getHtmlText());
        String onlyText = sm.stripHTMLTags(t);
        String pathName = pathFile+"/"+openedNote+".htm";
        ArrayList<String> revised = sm.strToArrayList(t);
        String pathNameOnlyText = pathFile+"/"+openedNote+".not";
        ArrayList<String> revisedOnlyText = sm.strToArrayList(onlyText);
        FileModer fm = new FileModer();
        ArrayList<String> original = fm.readFile(pathName);
        //Map<Integer, HashMap<ChangesDao.CHANGE,List>> changes = getNoteChanges(original, revised);
        //save changes to database
        //ChangesDao cd = new ChangesDao();
        RepositoryDao rd = new RepositoryDao();
        //Repository id_repo = rd.selectByName(openedNote);
        //get old string per row and give row number as a key
        Map<Integer, String> mapOri = new HashMap<>();
        for(int i=0;i<original.size();i++){
            mapOri.put(i, original.get(i));
        }
        insertChange(getNoteChanges(original, revised), rd.selectByName(openedNote), mapOri, new ChangesDao());
        //save note's changes to database
        /*for(Integer i:changes.keySet()){
            for(ChangesDao.CHANGE x:changes.get(i).keySet()){
                System.out.println(i);
                String type = null;
                if(x == ChangesDao.CHANGE.CHANGE) type = "change";
                if(x == ChangesDao.CHANGE.DELETE) type = "delete";
                if(x == ChangesDao.CHANGE.INSERT) type = "insert";
                List<String> changeItem = changes.get(i).get(x);
                for(String chstr:changeItem){
                    Changes ch = new Changes();
                    ch.setId_repo(id_repo.getId_repo());
                    ch.setRow_updates_num(i);
                    ch.setChange_type(type);
                    ch.setOld(mapOri.get(i));
                    ch.setNewChanges(chstr);
                    ch.setFile("");
                    cd.addChange(ch);
                }
            }
        }
        cd.insertAll();*/
        //---- end of save note's changes
        fm.writeFile(revised, pathName);
        fm.writeFile(revisedOnlyText, pathNameOnlyText);
    }
    
    @FXML
    private void openNote(MouseEvent e) throws IOException{
       
        FileModer fm = new FileModer();
        String noteName = lvNotes.getSelectionModel().getSelectedItem();
        RepositoryDao rd = new RepositoryDao(conn);
        Repository rep = rd.selectByName(noteName);
        String local = rep.getLocal_location();
        String noteTxtFile = local+"/"+noteName+".htm";
        
        String content = fm.readFileToString(noteTxtFile);
        System.out.println(content);
        if(openedNote!=null){
            saveNote();
        }
        
        pathFile = local;
        openedNote = noteName;
        tabNotesName.setText(openedNote);
        //txtNote.setText(content);
        che.setHtmlText(content);
        System.out.println(che.getHtmlText());
        //tfDisplayNote.getChildren().clear();
        //tfDisplayNote.getChildren().add(new Text(stripHTMLTags(content)));
        
    }
    
    private void deleteNote(String note){
        //confirmation alert,
        //deleted note
        String confirm = "Do you want to delete note "+note+"?\n "
                + "In order to delete the note on the server, you should delete it on the web application.";
        Alert  alert = new Alert(Alert.AlertType.CONFIRMATION, confirm, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            System.out.println("yes");
            RepositoryDao repDao = new RepositoryDao();
            Repository rep = repDao.selectByName(note);
            ChangesDao cd = new ChangesDao();
            String dir = rep.getLocal_location();
            try{
                FileUtils.deleteDirectory(new File(dir));
            }catch(IOException ioe){
                System.out.println("IOException : "+ioe.getMessage());
            }
            
            repDao.delete(rep);
            cd.deleteByRepository(rep);
        }
    }
    
    private void noteSynchronizing(String noteName, int OID) throws RemoteException{
        // check is login
        // check on server, whether the note is belong to the user or the user is as contributor, -> it is not necessary 
        // check whether it is first commit or make a change 
        // if it is first commit, then send the html text
        // if it make a change, then send the html text and the changes
        // from server, return the data, consists of changes every submission that the user has not pull it
        // every submission, server will notify every contirbutor and user for the changes were happened
        if(Conf.user!=null){
            boolean isLogin = as.isLogin(Conf.user);
            int isOnServer = ns.isAvailable(Conf.UID, noteName);
            if(isLogin && isOnServer>0){
                
                Repository rep = repdao.selectByName(noteName);
                
                com.notehub.api.entity.Note note = new com.notehub.api.entity.Note();
                //upload them
                note.setOwner(Conf.UID);
                note.setNoteTitle(rep.getName_repo());
                note.setDescription("");
                note.setCreatedAt(rep.getCreatedAt());
                //server update new note, verify and validate first
                note = ns.insertNote(note);
            }else{
                //server create new note, update table user to note as well
            }
            System.out.println(noteName+" "+OID+" "+isLogin);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "please, login to server!", ButtonType.CLOSE);
            alert.show();
            System.out.println(noteName+" "+OID+" belum login");
        }
        
    }
    
    /**
     * display Notes to listView
     * @param repdao 
     */
    private void listViewNotes(RepositoryDao repdao){
        lvNotes.getItems().removeAll();
        ArrayList<Repository> list = repdao.selectAll();
        for(Repository rep:list){
            System.out.println(rep.getName_repo());
            lvNotes.getItems().add(rep.getName_repo());
        }
    }
    
    /**
     * get note's changes 
     * @param original
     * @param revised
     * @return row# change, type of change, 
     */
    private Map<Integer, HashMap<ChangesDao.CHANGE,List>> getNoteChanges(ArrayList<String> original, ArrayList<String> revised ) throws IOException{
        Map<Integer, HashMap<ChangesDao.CHANGE,List>> am = new TreeMap<>();
        FileComparator fc = new FileComparator();
        List<Chunk> changes = fc.getChangesFromOriginal(original, revised); //problems
        List<Chunk> deletes = fc.getDeletesFromOriginal(original, revised);
        List<Chunk> inserts = fc.getInsertsFromOriginal(original, revised);
        sortChunk(am, changes, ChangesDao.CHANGE.CHANGE);
        sortChunk(am, deletes, ChangesDao.CHANGE.DELETE);
        sortChunk(am, inserts, ChangesDao.CHANGE.INSERT);
      
        for(Chunk x:changes){
            //am.put(x.getPosition(), x.getLines());
            //System.out.println(x.getPosition()+"---"+x.getLines());
        }
        return am;
    }
    
    private void sortChunk(Map<Integer, HashMap<ChangesDao.CHANGE,List>> ret, List<Chunk> chunks, ChangesDao.CHANGE change){
        for(Chunk x:chunks){
            if(ret.containsKey(x.getPosition())){
                if(ret.get(x).containsKey(change)){
                    ret.get(x.getPosition()).get(change).add(x.getLines());
                }else{
                    ret.get(x.getPosition()).put(change, x.getLines()); 
                }
                
            }else{
                Map<ChangesDao.CHANGE, List> bucky = new HashMap<>();
                bucky.put(change, x.getLines());
                ret.put(x.getPosition(), (HashMap<ChangesDao.CHANGE, List>) bucky);
            }
        }
    }
    
    
    
    /**
     * 
     * @param pathName
     * @return
     * @throws IOException 
     */
    private List<String> getOriginalContent(String pathName) throws IOException{
        FileModer fm = new FileModer();
        ArrayList<String> original = fm.readFile(pathName);
        return original;
    }
    
    /**
     * 
     * @param changes
     * @param id_repo
     * @param mapOri
     * @param cd 
     */
    private void insertChange(Map<Integer, HashMap<ChangesDao.CHANGE,List>> changes, Repository id_repo, Map<Integer, String> mapOri, ChangesDao cd){
        for(Integer i:changes.keySet()){
            for(ChangesDao.CHANGE x:changes.get(i).keySet()){
                System.out.println(i);
                String type = null;
                if(x == ChangesDao.CHANGE.CHANGE) type = "change";
                if(x == ChangesDao.CHANGE.DELETE) type = "delete";
                if(x == ChangesDao.CHANGE.INSERT) type = "insert";
                List<String> changeItem = changes.get(i).get(x);
                for(String chstr:changeItem){
                    Changes ch = new Changes();
                    ch.setId_repo(id_repo.getId_repo());
                    ch.setRow_updates_num(i);
                    ch.setChange_type(type);
                    ch.setOld(mapOri.get(i));
                    ch.setNewChanges(chstr);
                    ch.setFile("");
                    cd.addChange(ch);
                }
            }
        }
        cd.insertAll();
    }
    
    public void login(){
        //System.out.println(txtUsername.getText()+" "+txtPassword.getText());
        doLogin(txtUsername.getText(),txtPassword.getText());
    }
    
    /**
     * 
     * @param username
     * @param password 
     */
    public void doLogin(String username, String password){
        com.notehub.api.entity.User user = new com.notehub.api.entity.User();
        user.setUsername(username);
        user.setPassword(password);
        user.setIpAddress(ipAddress);
   
        try{
            user = as.login(user); //login to server
        }catch(RemoteException re){
            System.out.println("Remote Exception : "+re.getMessage());
        }
        //update user
        UserDao userdao = new UserDao();
        User userl = new User();
        userl.setUsername(user.getUsername());
        userl.setPassword(user.getPassword());
        userl.setLast_online(user.getLastConnect());
        userl.setIs_connect(true);
        userl.setId_user(user.getIdUser());
        userl.setId_online(user.getIdOnline());
        if(userdao.isExistUser(userl)==1){
            System.out.println("update "+userdao.isExistUser(userl));
            userdao.update(userl);
        }else{
            System.out.println("create");
            userdao.create(userl);
        }
        Conf.isLogin = true;
        Conf.UID = user.getIdUser();
        Conf.OnlineID = user.getIdOnline();
        Conf.user = user;
        
        lbWelcome.setText("Hi, "+user.getUsername()+"!");
        System.out.println(user.getLastConnect()+"-"+user.getIdOnline());
        btLogout.setVisible(true);
        btLogin.setVisible(false);
        txtUsername.setVisible(false);
        txtPassword.setVisible(false);
    }
    
    public void logout(){
        com.notehub.api.entity.User user = null;
        try{
            user = as.logout(Conf.user);
        }catch(RemoteException re){
            System.out.println("Remote Exception : "+re.getMessage());
        }
        
        if(user.getIdOnline()<1){
            btLogout.setVisible(false);
            btLogin.setVisible(true);
            txtUsername.setVisible(true);
            txtPassword.setVisible(true);
            lbWelcome.setText("");
            Conf.OnlineID = 0;
            Conf.UID = 0;
            Conf.user = null;
            
            UserDao userdao = new UserDao();
            User userl = new User();
            userl.setUsername(user.getUsername());
            userl.setPassword(user.getPassword());
            userl.setLast_online(user.getLastConnect());
            userl.setIs_connect(false);
            userl.setId_user(user.getIdUser());
            userl.setId_online(user.getIdOnline());
            userdao.update(userl);
        }
        
    }
    
}
