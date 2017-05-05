/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author triyono
 */
public class SQLiteDatabase {
    
    Connection conn;
    
    public SQLiteDatabase(){
        try {
            conn = SQLiteConnection.connect();
        } catch (Exception ex) {
            Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SQLiteDatabase(Connection c){
        conn = c;
    }
    
    public void createTable(){
        
    }
    
    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */
    public void insert(String tableName, ArrayList ar){
        
    }
    
    public void update(){
        
    }
    
    public ArrayList select(String tableName){
        ArrayList<ArrayList> ar = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName;
        
        return ar; 
    }
    
    /*
    * 
    */
    public void delete(String tableName, String whereColumn, String operator, String whereValue) throws SQLException{
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "DELETE from "+tableName+" WHERE "+whereColumn+" "+operator+" "+whereValue;
        stmt.executeUpdate(sql);
        conn.commit();
    }
    
    /*
    *
    */
    public void delete(String tableName, String whereColumn, String whereValue) throws SQLException{
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "DELETE from "+tableName+" WHERE "+whereColumn+" = "+whereValue;
        stmt.executeUpdate(sql);
        conn.commit();
    }
    
    /*
    * 
    */
    public void delete(String tableName, int whereValue) throws SQLException{
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        String sql = "DELETE from "+tableName+" WHERE id = "+whereValue;
        stmt.executeUpdate(sql);
        conn.commit();
    }
    
}
