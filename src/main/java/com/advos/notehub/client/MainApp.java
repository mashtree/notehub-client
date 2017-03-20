package com.advos.notehub.client;

import com.notehub.api.entity.User;
import com.advos.notehub.client.util.SQLiteConnection;
import com.notehub.api.service.NotesService;
import com.notehub.api.service.UsersService;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
        SQLiteConnection.connect();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {
        
        //Registry client = LocateRegistry.getRegistry("10.242.132.53", Registry.REGISTRY_PORT);
        //UsersService nss = (UsersService) client.lookup("UsersServiceServer");
        //User user = new User();
        //User x = nss.insertUser(user);
        //System.out.println(x.getIdUser()+" "+x.getUsername());
        launch(args);
        
    }

}
