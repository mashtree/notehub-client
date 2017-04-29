/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.entity;

/**
 *
 * @author aisyahumar
 */
public class User {
    private String username;
    private String password;
    private boolean is_connect;
    private String last_online;
    private int id_online; //unique id from server
    private int id_user; //id on server

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

    /**
     * @return the is_connect
     */
    public boolean isIs_connect() {
        return is_connect;
    }

    /**
     * @param is_connect the is_connect to set
     */
    public void setIs_connect(boolean is_connect) {
        this.is_connect = is_connect;
    }

    /**
     * @return the last_connect
     */
    public String getLast_online() {
        return last_online;
    }

    /**
     * @param last_connect the last_connect to set
     */
    public void setLast_online(String last_connect) {
        this.last_online = last_connect;
    }

    /**
     * @return the id_online
     */
    public int getId_online() {
        return id_online;
    }

    /**
     * @param id_online the id_online to set
     */
    public void setId_online(int id_online) {
        this.id_online = id_online;
    }

    /**
     * @return the id_user
     */
    public int getId_user() {
        return id_user;
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
    
    public void setAttributeFromUserAPI(com.notehub.api.entity.User user, boolean isConnect){
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setLast_online(user.getLastConnect());
        this.setIs_connect(isConnect);
        this.setId_user(user.getIdUser());
        this.setId_online(user.getIdOnline());
    }
    
}
