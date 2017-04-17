/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author aisyahumar
 */
public class SQLiteConnection {

    private static Connection c = null;

    public static Connection connect() throws Exception {
        if (c == null) {
            File f = new File("src/main/resources/database/notehub-desktop.sqlite3");
            String url = "jdbc:sqlite:"+f.getAbsolutePath();
            c = DriverManager.getConnection(url);
        }
        return c;
    }


public static Connection cconnect(){
        Connection conn = null;
        try{
            File f = new File("src/main/resources/database/notehub-desktop.sqlite3");
            String url = "jdbc:sqlite:"+f.getAbsolutePath();
            conn = DriverManager.getConnection(url);
            System.out.println("Connection has been established");
            return conn;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        /*finally{
            try{
                if(conn!=null){
                    conn.close();
                }
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }*/
        return null;
    }
    
}
