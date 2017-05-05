/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client;

import com.notehub.api.entity.User;
import java.rmi.registry.Registry;

/**
 *
 * @author triyono
 */
public class Conf {
    
    public static boolean isLogin = false;
    
    public static int UID = 0;
    
    public static int OnlineID = 0;
    
    public static User user = null;
    
    public static String serverip = "";
    
    public static int serverport = Registry.REGISTRY_PORT;
    
    /*public static NotesService ns;
    public static NoteChangesService nss;
    public static AuthService as;*/
    
    public static void setConf(boolean isLogin, int UID, int OnlineID, User user){
        Conf.isLogin = isLogin;
        Conf.UID = UID;
        Conf.OnlineID = OnlineID;
        Conf.user = user;
    }
    
}
