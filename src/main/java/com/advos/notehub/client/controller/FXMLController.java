package com.advos.notehub.client.controller;

import com.advos.notehub.client.Conf;
import com.advos.notehub.client.dao.ChangesDao;
import com.advos.notehub.client.dao.RepositoryDao;
import com.advos.notehub.client.dao.UserDao;
import com.advos.notehub.client.entity.Changes;
import com.advos.notehub.client.entity.Note;
import com.advos.notehub.client.entity.Repository;
import com.advos.notehub.client.gui.CanvasText;
import com.advos.notehub.client.entity.User;
import com.advos.notehub.client.util.ConfigurationManager;
import com.advos.notehub.client.util.DateUtil;
import com.advos.notehub.client.util.FileComparator;
import com.advos.notehub.client.util.FileModer;
import com.advos.notehub.client.util.SQLiteConnection;
import com.advos.notehub.client.util.StringModer;
import com.advos.notehub.client.util.sandsoft.CustomHTMLEditor;
import com.notehub.api.entity.NoteChange;
import com.notehub.api.entity.NoteChangesMap;
import com.notehub.api.service.AuthService;
import com.notehub.api.service.NoteChangesService;
import com.notehub.api.service.NotesService;
import com.notehub.api.service.UsersService;
import difflib.Chunk;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

public class FXMLController implements Initializable {

    @FXML
    private Button btAddNote;
    @FXML
    private Button btSave;
    @FXML
    private Button btLogout;
    @FXML
    private Button btLogin;
    //@FXML private TextFlow tfDisplayNote;
    @FXML
    private Tab tabNotesName;
    @FXML
    private ListView<String> lvNotes;
    @FXML
    private VBox vbNoteName;
    @FXML
    private VBox vbCanvas;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lbWelcome;
    @FXML
    private MenuItem miSynch;
    @FXML
    private MenuItem miDelNote;
    @FXML
    private TitledPane tpFileInfo;
    @FXML
    private TitledPane tpVersion;
    @FXML
    private Tab tpChanges;

    private String ipAddress;
    private String pathFile;
    private Connection conn;
    private final Map<String, Note> notes = new HashMap<>();
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
    //private CanvasText ct;
    private Accordion accHistory;

    private Map<String, String> ulogin = new HashMap<>(); //key username, value password 

    public void addService(String nameService, Object o) {
        services.put(nameService, o);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String ip = Conf.serverip.equalsIgnoreCase("") ? "192.168.130.1" : Conf.serverip;
        int port = Conf.serverport;

        try {
            client = LocateRegistry.getRegistry(ip
                    + "", port);
            us = (UsersService) client.lookup("UsersServiceServer");

            ns = (NotesService) client.lookup("NoteServiceServer");
            serverService.put("NoteServiceServer", ns);

            nss = (NoteChangesService) client.lookup("NoteChangeServiceServer");
            serverService.put("NoteChangeServiceServer", nss);

            as = (AuthService) client.lookup("AuthServiceServer");

            ipAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (RemoteException re) {
            System.out.println("Remote Exception : " + re);
        } catch (NotBoundException nbe) {
            System.out.println("Not Bound Exception : " + nbe);
        } catch (UnknownHostException uhe) {
            System.out.println("Unknown Host Exception : " + uhe);
        }

        accHistory = new Accordion();

        btLogout.setVisible(false);

        try {
            conn = SQLiteConnection.connect();
        } catch (Exception ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        repdao = new RepositoryDao(conn);
        listViewNotes(repdao);

        lvNotes.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                try {
                    openNote();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        che = new CustomHTMLEditor();
        //ct = new CanvasText();
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/canvas.fxml"));
        VBox canvas = null;
        try {
            canvas = fxmlloader.load();
            System.out.println(canvas.getClass().getName());
            vbCanvas.getChildren().add(canvas);
        } catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }

        vbNoteName.getChildren().add(che);

        /**
         * listview context menu
         */
        miDelNote.setOnAction(e -> {
            String note = lvNotes.getSelectionModel().getSelectedItem();
            deleteNote(note);
        });

        miSynch.setOnAction(e -> {
            String note = lvNotes.getSelectionModel().getSelectedItem();
            try {
                //openNote();
                noteSynchronizing(note, Conf.OnlineID);
            } catch (RemoteException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tpVersion.setVisible(false);
        tpFileInfo.setVisible(false);

    }

    /**
     * add note to repository and open it
     *
     * @param
     * @return
     */
    @FXML
    public void addNote() {
        StringModer sm = new StringModer();
        ConfigurationManager cm = new ConfigurationManager();
        Stage stage = new Stage();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choosing a directory as your note's name");
        File defaultDirectory = new File("c:/");
        dc.setInitialDirectory(defaultDirectory);
        try {
            File selectedDirectory = dc.showDialog(stage);
            String str = "<html dir=\"ltr\">\n"
                    + "<head>\n"
                    + "</head>\n"
                    + "<body contenteditable=\"true\">\n"
                    + "</body>\n"
                    + "</html>";
            if (openedNote != null) {
                String txtnote = che.getHtmlText();
                //String txtnote = txtNote.getText();
                // find the changes, record to the object
                List<String> original = this.getOriginalContent(pathFile + "/" + openedNote + ".txt");
                Map<Integer, HashMap<ChangesDao.CHANGE, List>> changes = getNoteChanges((ArrayList<String>) original, sm.strToArrayList(txtnote));
                saveNote(); // save the opened note
                // update to database
                // close the opened note
                che.setHtmlText(str);//txtNote.setText("");
                // open the new note
            } else {
                if (notes.containsKey(openedNote)) {
                    notes.get(openedNote).setIsOpen(false);
                }
            }
            
            openedNote = selectedDirectory.getName();
            tabNotesName.setText(openedNote);
            pathFile = selectedDirectory.getAbsolutePath();
            
            
            cm.createFileConf(selectedDirectory, pathFile); //create configuration file
            
            

            //---------- DATE
            DateFormat dateFormat = new SimpleDateFormat("Y-m-d HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date)+" creating note : "+openedNote);
            //----------- ./DATE

            //add repository to database
            Repository rep = new Repository(openedNote, pathFile, 1, dateFormat.format(date));
            repdao.create(rep);
            listViewNotes(repdao);

            if (!notes.containsKey(selectedDirectory.getName())) {
                notes.put(openedNote, new Note(selectedDirectory.getName(), true, "", pathFile));
            }
        } catch (IOException ioe) {
            System.out.println("IOException : "+ioe.getMessage());
        }catch(RuntimeException re){
            System.out.println("RuntimeException : "+re.getMessage());
        }

    }

    @FXML
    private void saveNote() throws IOException {
        StringModer sm = new StringModer();
        String t = sm.addNewLineHTMLTags(che.getHtmlText());
        String onlyText = sm.stripHTMLTags(t);
        String pathName = pathFile + "/" + openedNote + ".htm";
        ArrayList<String> revised = sm.strToArrayList(t);
        String pathNameOnlyText = pathFile + "/" + openedNote + ".txt";
        ArrayList<String> revisedOnlyText = sm.strToArrayList(onlyText);
        FileModer fm = new FileModer();
        //ArrayList<String> original = fm.readFile(pathName);
        //save changes to database
        //RepositoryDao rd = new RepositoryDao();
        //get old string per row and give row number as a key
        //Map<Integer, String> mapOri = new HashMap<>();
        //for(int i=0;i<original.size();i++){
        //    mapOri.put(i, original.get(i));
        //}
        //save note's changes to database
        //insertChange(getNoteChanges(original, revised), rd.selectByName(openedNote), mapOri, new ChangesDao());

        //---- end of save note's changes
        fm.writeFile(revised, pathName);
        fm.writeFile(revisedOnlyText, pathNameOnlyText);
        System.out.println(DateUtil.getTimeNow()+" saving note : "+openedNote);
    }

    @FXML
    private void openNote() throws IOException {

        FileModer fm = new FileModer();
        String noteName = lvNotes.getSelectionModel().getSelectedItem();
        RepositoryDao rd = new RepositoryDao(conn);
        Repository rep = rd.selectByName(noteName);
        String local = rep.getLocal_location();
        String noteTxtFile = local + "/" + noteName + ".htm";

        String content = fm.readFileToString(noteTxtFile);
        //System.out.println(content);
        if (openedNote != null) {
            saveNote();
        }

        pathFile = local;
        openedNote = noteName;
        tabNotesName.setText(openedNote);
        //txtNote.setText(content);
        che.setHtmlText(content);
        displayHistory();
        System.out.println(DateUtil.getTimeNow()+" open note : "+openedNote);

    }

    private void displayHistory() {
        Repository rep = repdao.selectByName(openedNote);
        accHistory.getPanes().clear();
        ChangesDao cd = new ChangesDao();
        List<Changes> changes = cd.selectAll(rep);
        StringModer sm = new StringModer();
        int version = 0;
        String versionLabel = "";
        for (int i = 0; i < changes.size(); i++) {
            if (i == 0 || version != changes.get(i).getVersion()) {
                version = changes.get(i).getVersion();
                versionLabel = "Changes : " + changes.get(i).getVersion();
                TitledPane tp = new TitledPane();
                tp.setText(versionLabel);
                accHistory.getPanes().add(tp);
                ListView lv = new ListView();
                for (int j = 0; j < changes.size(); j++) {
                    if (changes.get(j).getVersion() == version) {

                        String old = sm.stripHTMLTags(changes.get(j).getOld());
                        String newV = sm.stripHTMLTags(changes.get(j).getNewChanges());
                        lv.getItems().add(changes.get(j).getChange_type().toUpperCase() + " at row : " + changes.get(j).getRow_updates_num());
                        lv.getItems().add("old : " + old);
                        lv.getItems().add("new : " + newV);
                    }

                }
                tp.setContent(lv);
            }

        }
        tpChanges.setContent(accHistory);
    }

    private void deleteNote(String note) {
        //confirmation alert,
        //deleted note
        String confirm = "Do you want to delete note " + note + "?\n "
                + "In order to delete the note on the server, you should delete it on the web application.";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, confirm, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            System.out.println("yes");
            RepositoryDao repDao = new RepositoryDao();
            Repository rep = repDao.selectByName(note);
            ChangesDao cd = new ChangesDao();
            
            try {
                String dir = rep.getLocal_location();
                FileUtils.deleteDirectory(new File(dir));
            } catch (IOException ioe) {
                System.out.println("IOException : " + ioe.getMessage());
            }catch(NullPointerException npe){
                System.out.println("IOException : " + npe.getMessage());
            }

            repDao.delete(rep);
            cd.deleteByRepository(rep);
            System.out.println(DateUtil.getTimeNow()+" delete note : "+openedNote+" completed");
        }
        //listViewNotes(repdao);
        this.lvNotes.getItems().remove(note);
    }

    private void noteSynchronizing(String noteName, int OID) throws RemoteException {
        
        // check is login v 
        // check whether it is first commit or make a change 
        // if it is first commit, then send the html text
        // if it make a change, then send the html text and the changes
        // from server, return the data, consists of changes every submission that the user has not pull it
        // every submission, server will notify every contirbutor and user for the changes were happened
        System.out.println(DateUtil.getTimeNow()+" checking login state ...");
        if (Conf.user != null) { //if login
            System.out.println(DateUtil.getTimeNow()+" preparing for synchronizing : "+noteName);
            
            boolean isLogin = as.isLogin(Conf.user);
            int isOnServer = ns.isAvailable(Conf.UID, noteName);
            //System.out.println(isLogin + " " + isOnServer);
            Repository rep = repdao.selectByName(noteName);
            com.notehub.api.entity.Note note = new com.notehub.api.entity.Note();
            //upload them
            note.setOwner(Conf.UID);
            note.setNoteTitle(rep.getName_repo());
            note.setDescription("");
            note.setContent(che.getHtmlText());
            note.setCreatedAt(rep.getCreatedAt());
            note.setIdNote(rep.getId_on_server());
            ChangesDao cd = new ChangesDao();
            StringModer sm = new StringModer();
            //String text = sm.addNewLineHTMLTags(che.getHtmlText());
            String text = sm.stripHTMLTags(che.getHtmlText());
            ArrayList<String> arrayNote = sm.strToArrayList(text);
            NoteChangesMap ncm = new NoteChangesMap();
            int lastVersionOnLocal = 0;
            System.out.println(DateUtil.getTimeNow()+" checking note's data on server ...");
            if (isLogin && isOnServer > 0) { //the note is already in the server
                System.out.println(DateUtil.getTimeNow()+" user is login and the note is on server database");
                //note changes
                //check and compare the update version between local and server
                System.out.println(DateUtil.getTimeNow()+" comparing version id between local data and server data ...");
                int lastVersionOnServer = nss.getLastVersion(note, Conf.UID); //update api
                lastVersionOnLocal = cd.getLastVersion(rep);
                //System.out.println(note.getIdNote() + "--" + lastVersionOnServer + "<- ->" + lastVersionOnLocal);
                if (lastVersionOnServer > lastVersionOnLocal) { //if there is/are difference version between local and server
                    //if server's is larger, ask whether just take the update or overwrite the server's data
                    System.out.println("server database has newer version");
                    String msg = "server database has newer version!\ndo you want to overwrite the data?\n"
                            + "be carefull, it will be erase many works on the note";
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.APPLY, ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.APPLY) {

                        //if overwrite, update the server's data, 
                        //      return all of the update between local data and the server, 
                        //      then update the local database
                        //1 write to server
                        System.out.println(DateUtil.getTimeNow()+" updating the server data ...");
                        ns.updateNote(note); //update the last note to server
                        System.out.println(DateUtil.getTimeNow()+" calculating note's changes ...");
                        ncm = checkingChanges(sm, text, ncm, lastVersionOnServer, note);
                        System.out.println(DateUtil.getTimeNow()+" updating server note's changes ...");
                        nss.insertNoteChange(ncm); //insert to server's note changes
                    }
                } else if ((lastVersionOnServer == lastVersionOnLocal) && lastVersionOnServer>0 && lastVersionOnLocal>0) {
                    //klo sama, update di server
                    //update notes
                    System.out.println(DateUtil.getTimeNow()+" local version is the same with server data's version");
                    System.out.println(DateUtil.getTimeNow()+" updating the server data ...");
                    ns.updateNote(note);
                    //update note changes
                    //System.out.println(text);
                    System.out.println(DateUtil.getTimeNow()+" calculating note's changes ...");
                    ncm = checkingChanges(sm, text, ncm, lastVersionOnServer, note);
                    System.out.println(ncm.getNoteChangesMap().size());
                    System.out.println(DateUtil.getTimeNow()+" updating server note's changes ...");
                    nss.insertNoteChange(ncm); //insert to server's note changes
                }

            } else {  //the note is not in the server
                System.out.println(DateUtil.getTimeNow()+" user is login and the note is not on server database");
                //server create new note, update table user to note as well
                note = ns.insertNote(note);
                //send the note change objectsss
                System.out.println(DateUtil.getTimeNow()+" note online id : " + note.getIdNote());
                rep.setId_on_server(note.getIdNote());
                repdao.update(rep);
                ncm.setVersion(1);
                for (int i = 0; i < arrayNote.size(); i++) {
                    NoteChange nc = new NoteChange();
                    nc.setIdNote(note.getIdNote());
                    nc.setIdUser(Conf.UID);
                    nc.setOld("");
                    nc.setChangeType("insert");
                    nc.setRowChange(1);
                    nc.setNewChanges(arrayNote.get(i));
                    nc.setVersion(ncm.getVersion());
                    ncm.setNoteChange(i + 1, nc);
                }

                nss.insertNoteChange(ncm);

            }
            System.out.println(DateUtil.getTimeNow()+" retrieve changes from server : "+ncm.getNoteChangesMap().size());
            List<NoteChangesMap> serverSynch = nss.getNoteChangesMap(note, note.getIdNote(), lastVersionOnLocal + 1);
            System.out.println(DateUtil.getTimeNow()+" # of changes : " + serverSynch.size());
            if(serverSynch.size()>0){
                cd.insertAll(serverSynch, rep.getId_repo());
                //flush data note di server ke file fisik
                note = ns.getNote(note.getIdNote());
                updateLocalFile(note);
            }
            System.out.println(DateUtil.getTimeNow()+" Synchronizing done!");
        } else { //not login
            Alert alert = new Alert(Alert.AlertType.WARNING, "please, login to server!", ButtonType.CLOSE);
            alert.show();
            return;
        }

    }

    private void updateLocalFile(com.notehub.api.entity.Note note) {
        StringModer sm = new StringModer();
        String t = note.getContent();
        String onlyText = sm.stripHTMLTags(t);
        String pathName = pathFile + "/" + openedNote + ".htm";
        ArrayList<String> revised = sm.strToArrayList(t);
        String pathNameOnlyText = pathFile + "/" + openedNote + ".txt";
        ArrayList<String> revisedOnlyText = sm.strToArrayList(onlyText);
        FileModer fm = new FileModer();
        try {
            //---- end of save note's changes
            fm.writeFile(revised, pathName);
            fm.writeFile(revisedOnlyText, pathNameOnlyText);
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(DateUtil.getTimeNow()+" updating local file : "+openedNote);
    }

    private NoteChangesMap checkingChanges(StringModer sm, String text, NoteChangesMap ncm, int lastVersionOnServer, com.notehub.api.entity.Note note) {
        String pathName = pathFile + "/" + openedNote + ".htm";
        ArrayList<String> revised = sm.strToArrayList(text);
        //String pathNameOnlyText = pathFile+"/"+openedNote+".not";
        //ArrayList<String> revisedOnlyText = sm.strToArrayList(onlyText);
        Map<Integer, HashMap<ChangesDao.CHANGE, List>> noteChanges = new HashMap<>();
        ArrayList<String> original = new ArrayList<>();
        Map<Integer, String> mapOri = new HashMap<>();
        FileModer fm = new FileModer();
        try {
            original = fm.readFile(pathName);

            for (int i = 0; i < original.size(); i++) {
                mapOri.put(i, original.get(i));
            }
            noteChanges = getNoteChanges(original, revised);
            //save note's changes to database
            insertChange(noteChanges, repdao.selectByName(openedNote), mapOri, new ChangesDao());
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        ncm.setVersion(lastVersionOnServer + 1); //set last server version ++

        for (Integer i : noteChanges.keySet()) {
            for (ChangesDao.CHANGE x : noteChanges.get(i).keySet()) {
                String type = null;
                if (x == ChangesDao.CHANGE.CHANGE) {
                    type = "change";
                }
                if (x == ChangesDao.CHANGE.DELETE) {
                    type = "delete";
                }
                if (x == ChangesDao.CHANGE.INSERT) {
                    type = "insert";
                }
                List<String> changeItem = noteChanges.get(i).get(x);
                for (String chstr : changeItem) {

                    NoteChange nc = new NoteChange();
                    nc.setIdNote(note.getIdNote());
                    nc.setIdUser(Conf.UID);
                    nc.setChangeType(type);
                    nc.setOld(mapOri.get(i));
                    nc.setRowChange(i);
                    nc.setNewChanges(chstr);
                    nc.setVersion(ncm.getVersion());
                    ncm.setNoteChange(i, nc);
                }
            }
        }
        return ncm;
    }

    /**
     * display Notes to listView
     *
     * @param repdao
     */
    private void listViewNotes(RepositoryDao repdao) {
        List<String> movingItems = new ArrayList<>(lvNotes.getSelectionModel().getSelectedItems());
        lvNotes.getItems().removeAll(movingItems);
        ArrayList<Repository> list = repdao.selectAll();
        for (Repository rep : list) {
            System.out.println(rep.getName_repo());
            lvNotes.getItems().add(rep.getName_repo());
        }
    }

    /**
     * get note's changes
     *
     * @param original
     * @param revised
     * @return row# change, type of change,
     */
    private Map<Integer, HashMap<ChangesDao.CHANGE, List>> getNoteChanges(ArrayList<String> original, ArrayList<String> revised) throws IOException {
        Map<Integer, HashMap<ChangesDao.CHANGE, List>> am = new TreeMap<>();
        FileComparator fc = new FileComparator();
        List<Chunk> changes = fc.getChangesFromOriginal(original, revised); //problems
        List<Chunk> deletes = fc.getDeletesFromOriginal(original, revised);
        List<Chunk> inserts = fc.getInsertsFromOriginal(original, revised);
        sortChunk(am, changes, ChangesDao.CHANGE.CHANGE);
        sortChunk(am, deletes, ChangesDao.CHANGE.DELETE);
        sortChunk(am, inserts, ChangesDao.CHANGE.INSERT);

        for (Chunk x : changes) {
            //am.put(x.getPosition(), x.getLines());
            //System.out.println(x.getPosition()+"---"+x.getLines());
        }
        return am;
    }

    /**
     *
     * @param ret
     * @param chunks
     * @param change
     */
    private void sortChunk(Map<Integer, HashMap<ChangesDao.CHANGE, List>> ret, List<Chunk> chunks, ChangesDao.CHANGE change) {
        for (Chunk x : chunks) {
            if (ret.containsKey(x.getPosition())) {
                if (ret.get(x).containsKey(change)) {
                    ret.get(x.getPosition()).get(change).add(x.getLines());
                } else {
                    ret.get(x.getPosition()).put(change, x.getLines());
                }

            } else {
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
    private List<String> getOriginalContent(String pathName) throws IOException {
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
    private void insertChange(Map<Integer, HashMap<ChangesDao.CHANGE, List>> changes, Repository id_repo, Map<Integer, String> mapOri, ChangesDao cd) {
        for (Integer i : changes.keySet()) {
            for (ChangesDao.CHANGE x : changes.get(i).keySet()) {
                System.out.println(i);
                String type = null;
                if (x == ChangesDao.CHANGE.CHANGE) {
                    type = "change";
                }
                if (x == ChangesDao.CHANGE.DELETE) {
                    type = "delete";
                }
                if (x == ChangesDao.CHANGE.INSERT) {
                    type = "insert";
                }
                List<String> changeItem = changes.get(i).get(x);
                for (String chstr : changeItem) {
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

    public void login() {
        //System.out.println(txtUsername.getText()+" "+txtPassword.getText());
        doLogin(txtUsername.getText(), txtPassword.getText());
    }

    /**
     *
     * @param username
     * @param password
     */
    public void doLogin(String username, String password) {
        com.notehub.api.entity.User user = new com.notehub.api.entity.User();
        user.setUsername(username);
        user.setPassword(password);
        user.setIpAddress(ipAddress);

        try {
            user = as.login(user); //login to server
            if (user.getIdOnline() < 1 && user.getIdUser() < 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "login failed!", ButtonType.CLOSE);
                alert.show();
                return;
            }
        } catch (RemoteException re) {
            System.out.println("Remote Exception : " + re.getMessage());
        }
        //update local user
        UserDao userdao = new UserDao();
        User userl = new User();

        userl.setAttributeFromUserAPI(user, true);
        if (userdao.isExistUser(userl) == 1) {
            System.out.println("update " + userdao.isExistUser(userl));
            userdao.update(userl);
        } else {
            System.out.println("create");
            userdao.create(userl);
        }

        Conf.setConf(true, user.getIdUser(), user.getIdOnline(), user);

        setLoginButton("Hi, " + user.getUsername() + "!", false);
        System.out.println(user.getLastConnect() + "-" + user.getIdOnline());

    }

    /**
     *
     */
    public void logout() {
        com.notehub.api.entity.User user = null;
        try {
            user = as.logout(Conf.user);
        } catch (RemoteException re) {
            System.out.println("Remote Exception : " + re.getMessage());
        }

        if (user.getIdOnline() < 1) {
            setLoginButton("", true);
            Conf.OnlineID = 0;
            Conf.UID = 0;
            Conf.user = null;

            UserDao userdao = new UserDao();
            User userl = new User();

            userl.setAttributeFromUserAPI(user, false);
            userdao.update(userl);
        }

    }

    /**
     *
     * @param welcome
     * @param isNotLogin
     */
    private void setLoginButton(String welcome, boolean isNotLogin) {
        btLogout.setVisible(!isNotLogin);
        btLogin.setVisible(isNotLogin);
        txtUsername.setVisible(isNotLogin);
        txtPassword.setVisible(isNotLogin);
        lbWelcome.setText(welcome);
    }

}
