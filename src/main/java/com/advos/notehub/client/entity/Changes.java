/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.entity;

/**
 *
 * @author triyono
 */
public class Changes {
    private int id_changes;
    private int id_repo;
    private int version;
    private int row_updates_num;
    private String change_type;
    private String old;
    private String newChanges;
    private String file;
    private String updatedAt;

    /**
     * @return the id_changes
     */
    public int getId_changes() {
        return id_changes;
    }

    /**
     * @param id_changes the id_changes to set
     */
    public void setId_changes(int id_changes) {
        this.id_changes = id_changes;
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
     * @return the row_updates_num
     */
    public int getRow_updates_num() {
        return row_updates_num;
    }

    /**
     * @param row_updates_num the row_updates_num to set
     */
    public void setRow_updates_num(int row_updates_num) {
        this.row_updates_num = row_updates_num;
    }

    /**
     * @return the old
     */
    public String getOld() {
        return old;
    }

    /**
     * @param old the old to set
     */
    public void setOld(String old) {
        this.old = old;
    }

    /**
     * @return the newChanges
     */
    public String getNewChanges() {
        return newChanges;
    }

    /**
     * @param newChanges the newChanges to set
     */
    public void setNewChanges(String newChanges) {
        this.newChanges = newChanges;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return the change_type
     */
    public String getChange_type() {
        return change_type;
    }

    /**
     * @param change_type the change_type to set
     */
    public void setChange_type(String change_type) {
        this.change_type = change_type;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }
    
}
