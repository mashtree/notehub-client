package com.advos.notehub.client;

import com.advos.notehub.client.util.DateUtil;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


/**
 *
 * @author triyono
 */
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
            System.exit(0);
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
        System.out.println("Server setting");
        Scanner scanner = new Scanner(System.in);
        System.out.print("server id address : ");
        Conf.serverip = scanner.nextLine();
        System.out.print("server port : ");
        Conf.serverport = scanner.nextInt();
        //if(args.length>0){
            //Conf.serverip = ;
            //Conf.serverport = Integer.parseInt(args[1]);
        //}
        
        System.out.println(DateUtil.getTimeNow()+" client ip address : "+Inet4Address.getLocalHost().getHostAddress());
        
        launch(args);
        
    }

}
