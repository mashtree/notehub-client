package com.advos.notehub.client;

import com.advos.notehub.client.controller.FXMLController;
import com.advos.notehub.client.event.ServiceLocator;
import com.notehub.api.entity.User;
import com.advos.notehub.client.util.SQLiteConnection;
import com.notehub.api.service.NotesService;
import com.notehub.api.service.UsersService;
import com.notehub.api.service.NoteChangesService;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


public class MainApp extends Application {
    Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
        Parent root = fxmlloader.load();
        stage = primaryStage;
        stage.setOnCloseRequest(e->{
            e.consume();
            try {
                closeProgram();
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
        //SQLiteConnection.connect();
    }
    
    private void closeProgram() throws IOException{
        if(Conf.user!=null){
            Alert alertLogin = new Alert(Alert.AlertType.INFORMATION, "Please logout from server before close your application!", ButtonType.OK);
            alertLogin.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Sure you want to exit ? \nSave all your works!", ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            stage.close();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        //System.setSecurityManager(new RMISecurityManager());
        
        System.out.println(Inet4Address.getLocalHost().getHostAddress());
        
        launch(args);
        
    }

}
