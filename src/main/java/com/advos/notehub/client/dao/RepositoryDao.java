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

/**
 *
 * @author aisyahumar
 */
public class RepositoryDao {
    
    Connection conn;
    private final int num = 1;
    public RepositoryDao(){
        conn = SQLiteConnection.connect();
    }
    
    public void create(Repository rep){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "INSERT INTO repository(name_repo, local_location, web_location, status, created_time) "+
                " VALUES(?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rep.getName_repo());
            ps.setString(2, rep.getLocal_location());
            ps.setString(3, "");
            ps.setInt(4, rep.getStatus());
            ps.setString(5,rep.getCreatedAt());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void update(Repository rep){
        String sql = "UPDATE repository SET name_repo= ?, local_location =?,web_location=?,status=? "+
                " where id_repo = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rep.getName_repo());
            ps.setString(2, rep.getLocal_location());
            ps.setString(3, rep.getWeb_location());
            ps.setInt(4, rep.getStatus());
            ps.setInt(5, rep.getId_repo());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
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
