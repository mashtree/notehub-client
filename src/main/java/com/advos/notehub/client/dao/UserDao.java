/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.dao;

import java.sql.Connection;
import com.advos.notehub.client.util.SQLiteConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.advos.notehub.client.entity.User;

/**
 *
 * @author aisyahumar
 */
public class UserDao {
    
    Connection conn;
    private final int num = 1;
    public UserDao(){
        conn = SQLiteConnection.connect();
    }
    
    /*
    * insert a new row into the user table
    * @param username
    * @param password
    * @param is_connect
    * @return void
    */
    public void create(User user){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String last_online = dateFormat.format(date);
        String sql = "INSERT INTO user(username, password, is_connect, last_online) "+
                " VALUES(?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, true);
            ps.setString(4, last_online);
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void update(User user){
        String sql = "UPDATE user SET username = ?, password =? "+
                " where username = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUsername());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public ArrayList selectAll(){
        ArrayList<User> ar = new ArrayList<>();
        String sql = "SELECT username, password, is_connect, last_online "+
                "FROM user";
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setIs_connect(rs.getBoolean("is_connect"));
                user.setLast_online(rs.getString("last_online"));
                ar.add(user);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return ar;
    }
    
    public void delete(User user){
        String sql = "DELETE FROM user WHERE username = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
}
