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
public class Repository {
    private int id_repo;
    private String name_repo;
    private String local_location;
    private String web_location;
    private int status;
    private String createdAt;
    
    public Repository(){
        // do nothing
    }
    
    public Repository(String name, String local, int status, String timestm){
        name_repo = name;
        local_location = local;
        this.status = status;
        createdAt = timestm;
    }

    /**
     * @return the id_repo
     */
    public int getId_repo() {
        return id_repo;
    }

    /**
     * @param id_repo the id_repo to set
     */
    public void setId_repo(int id_repo) {
        this.id_repo = id_repo;
    }

    /**
     * @return the name_repo
     */
    public String getName_repo() {
        return name_repo;
    }

    /**
     * @param name_repo the name_repo to set
     */
    public void setName_repo(String name_repo) {
        this.name_repo = name_repo;
    }

    /**
     * @return the local_location
     */
    public String getLocal_location() {
        return local_location;
    }

    /**
     * @param local_location the local_location to set
     */
    public void setLocal_location(String local_location) {
        this.local_location = local_location;
    }

    /**
     * @return the web_location
     */
    public String getWeb_location() {
        return web_location;
    }

    /**
     * @param web_location the web_location to set
     */
    public void setWeb_location(String web_location) {
        this.web_location = web_location;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
}
