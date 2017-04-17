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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aisyahumar
 */
public class UserDao {
    
    Connection conn;
    private final int num = 1;
    public UserDao(){
        if(conn==null) try {
            conn = SQLiteConnection.connect();
        } catch (Exception ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String sql = "INSERT INTO user(username, password, is_connect, idOnline, idUser, last_online) "+
                " VALUES(?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isIs_connect());
            ps.setInt(4, user.getId_online());
            ps.setInt(5, user.getId_user());
            ps.setString(6, last_online);
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void update(User user){
        String sql = "UPDATE user SET username = ?, password =?, is_connect=?, idOnline=?, idUser=?, last_online=? "+
                " WHERE username = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.isIs_connect());
            ps.setInt(4, user.getId_online());
            ps.setInt(5, user.getId_user());
            ps.setString(6, user.getLast_online());
            ps.setString(7, user.getUsername());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public ArrayList selectAll(){
        ArrayList<User> ar = new ArrayList<>();
        String sql = "SELECT * FROM user";
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
    
    public int isExistUser(User user){
        ArrayList<User> ar = new ArrayList<>();
        String sql = "SELECT COUNT(username) as isExist FROM user WHERE username='"+user.getUsername()+"'";
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                return (int) rs.getInt("isExist");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
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
