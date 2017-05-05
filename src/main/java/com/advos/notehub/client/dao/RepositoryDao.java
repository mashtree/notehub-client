/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.dao;

import com.advos.notehub.client.entity.Repository;
import com.advos.notehub.client.util.SQLiteConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author triyono
 */
public class RepositoryDao {
    
    Connection conn;
    private final int num = 1;
    public RepositoryDao(){
        try {
            conn = SQLiteConnection.connect();
        } catch (Exception ex) {
            Logger.getLogger(RepositoryDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public RepositoryDao(Connection c){
        conn = c;
    }
    /**
     * 
     * @param rep 
     */
    public void create(Repository rep){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "INSERT INTO repository(name_repo, local_location, web_location, status, created_time,id_on_server) "+
                " VALUES(?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rep.getName_repo());
            ps.setString(2, rep.getLocal_location());
            ps.setString(3, "");
            ps.setInt(4, rep.getStatus());
            ps.setString(5,rep.getCreatedAt());
            ps.setInt(6,rep.getId_on_server());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * 
     * @param rep 
     */
    public void update(Repository rep){
        String sql = "UPDATE repository SET name_repo= ?, local_location =?,web_location=?,status=?,id_on_server=? "+
                " where id_repo = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rep.getName_repo());
            ps.setString(2, rep.getLocal_location());
            ps.setString(3, rep.getWeb_location());
            ps.setInt(4, rep.getStatus());
            ps.setInt(5, rep.getId_on_server());
            ps.setInt(6, rep.getId_repo());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 
     * @return 
     */
    public ArrayList selectAll(){
        ArrayList<Repository> ar = new ArrayList<>();
        String sql = "SELECT id_repo, name_repo, local_location, web_location, status, created_time "+
                "FROM repository";
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                Repository rep = new Repository();
                rep.setId_repo(rs.getInt("id_repo"));
                rep.setName_repo(rs.getString("name_repo"));
                rep.setLocal_location(rs.getString("local_location"));
                rep.setWeb_location(rs.getString("web_location"));
                rep.setStatus(rs.getInt("status"));
                rep.setCreatedAt(rs.getString("created_time"));
                ar.add(rep);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return ar;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public Repository selectById(int id){
       Repository rep = null;
        String sql = "SELECT id_repo, name_repo, local_location, web_location, status, created_time "+
                "FROM repository where id_repo=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                rep = new Repository();
                rep.setId_repo(rs.getInt("id_repo"));
                rep.setName_repo(rs.getString("name_repo"));
                rep.setLocal_location(rs.getString("local_location"));
                rep.setWeb_location(rs.getString("web_location"));
                rep.setStatus(rs.getInt("status"));
                rep.setCreatedAt(rs.getString("created_time"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return rep;
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public Repository selectByName(String name){
       Repository rep = null;
        String sql = "SELECT id_repo, name_repo, local_location, web_location, status, created_time, id_on_server "+
                "FROM repository where name_repo=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                rep = new Repository();
                rep.setId_repo(rs.getInt("id_repo"));
                rep.setName_repo(rs.getString("name_repo"));
                rep.setLocal_location(rs.getString("local_location"));
                rep.setWeb_location(rs.getString("web_location"));
                rep.setStatus(rs.getInt("status"));
                rep.setId_on_server(rs.getInt("id_on_server"));
                rep.setCreatedAt(rs.getString("created_time"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return rep;
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public Repository selectByIdOnServer(int idOnServer){
        Repository rep = null;
        String sql = "SELECT id_repo, name_repo, local_location, web_location, status, created_time, id_on_server "+
                "FROM repository where id_on_server=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idOnServer);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                rep = new Repository();
                rep.setId_repo(rs.getInt("id_repo"));
                rep.setName_repo(rs.getString("name_repo"));
                rep.setLocal_location(rs.getString("local_location"));
                rep.setWeb_location(rs.getString("web_location"));
                rep.setStatus(rs.getInt("status"));
                rep.setId_on_server(rs.getInt("id_on_server"));
                rep.setCreatedAt(rs.getString("created_time"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return rep;
    }
    
    /**
     * 
     * @param rep 
     */
    public void delete(Repository rep){
        String sql = "DELETE FROM repository WHERE id_repo = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rep.getId_repo());
            ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
}
