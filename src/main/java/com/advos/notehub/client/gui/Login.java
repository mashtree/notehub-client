/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.gui;

import com.advos.notehub.client.controller.FXMLController;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author aisyahumar
 */
public class Login extends VBox{
    
    private Label lUser;
    private Label lPass;
    private Label lTopDesc;
    private TextField txtUser;
    private TextField txtPass;
    private Button btSubmit;
    
    private String username = "";
    private String password = "";
    
    public Login(Map<String,String> ulogin){
        this.setHeight(300);
        this.setWidth(300);
        this.setPadding(Insets.EMPTY);
        lUser = new Label("username");
        lPass = new Label("password");
        lTopDesc = new Label("LOGIN");
        txtUser = new TextField();
        txtPass = new TextField();
        btSubmit = new Button("OK");
        
        btSubmit.setOnAction(e->submit(ulogin));
        
        getChildren().addAll(lTopDesc, lUser, txtUser, lPass, txtPass, btSubmit);
    }
    
    public void submit(Map<String,String> ulogin){
        setUsername(txtUser.getText());
        setPassword(txtPass.getText());
        ulogin.put(username, password);
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    
}
