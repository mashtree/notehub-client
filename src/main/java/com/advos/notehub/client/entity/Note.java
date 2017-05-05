/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author triyono
 */
public class Note {
    
    private String fileName;
    private boolean isOpen;
    private String content;
    private String location;
    private ArrayList<Map<Integer, String>> change; //changes, 1 for old version, 2 for new version
    
    public Note(){}
    
    /**
     * 
     * @param fname filename
     * @param isopen is open true
     * @param cont content
     * @param loc local location
     */
    public Note(String fname, boolean isopen, String cont, String loc){
        fileName = fname;
        isOpen = isopen;
        content = cont;
        location = loc;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the isOpen
     */
    public boolean isIsOpen() {
        return isOpen;
    }

    /**
     * @param isOpen the isOpen to set
     */
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the change
     */
    public ArrayList<Map<Integer, String>> getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(ArrayList<Map<Integer, String>> change) {
        this.change = change;
    }

    
}
