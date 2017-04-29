/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.dao;

import com.advos.notehub.client.entity.Changes;
import com.advos.notehub.client.entity.Repository;
import com.advos.notehub.client.util.SQLiteConnection;
import com.notehub.api.entity.NoteChange;
import com.notehub.api.entity.NoteChangesMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aisyahumar
 */
public class ChangesDao {

    Connection conn;
    List<Changes> changes = new ArrayList<>();

    public enum CHANGE {
        CHANGE, DELETE, INSERT
    }

    public ChangesDao() {
        try {
            conn = SQLiteConnection.connect();
        } catch (Exception ex) {
            Logger.getLogger(ChangesDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param ch
     * @return
     */
    public ChangesDao setChangesAll(List<Changes> ch) {
        this.changes = ch;
        return this;
    }

    public void addChange(Changes change) {
        changes.add(change);
    }

    /**
     *
     */
    public void insertAll() {
        if (!changes.isEmpty()) {
            Iterator it = changes.iterator();
            while (it.hasNext()) {
                create((Changes) it.next());
            }
        }
    }

    /**
     *
     * @param changes
     */
    public void insertAll(List<NoteChangesMap> changes, int id_repo) {
        for (int i = 0; i < changes.size(); i++) {
            Map<Integer, NoteChange> ncm = changes.get(i).getNoteChangesMap();
            for (int x : ncm.keySet()) {
                if (changes.get(i).getVersion() > 0) {
                    com.notehub.api.entity.NoteChange nc = ncm.get(x);
                    Changes ch = new Changes();
                    ch.setId_repo(id_repo);
                    ch.setVersion(changes.get(i).getVersion());

                    ch.setRow_updates_num(nc.getRowChange());
                    ch.setChange_type(nc.getChangeType());
                    ch.setOld(nc.getOld());
                    ch.setNewChanges(nc.getNewChanges());
                    ch.setFile("");
                    ch.setUpdatedAt(nc.getCreatedAt());
                    addChange(ch);
                }
            }
        }
        insertAll();
    }

    public void create(Changes ch) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "INSERT INTO changes(id_repo,version,row_updates_num, change_type, old, new, file, updatedAt) "
                + " VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ch.getId_repo());
            ps.setInt(2, ch.getVersion());
            ps.setInt(3, ch.getRow_updates_num());
            ps.setString(4, ch.getChange_type());
            ps.setString(5, ch.getOld());
            ps.setString(6, ch.getNewChanges());
            ps.setString(7, ch.getFile());
            ps.setString(8, time);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Changes ch) {
        String sql = "UPDATE repository SET id_repo=?,version=?,row_updates_num=?, change_type=?, old=?, new=?, file=?, updatedAt=? "
                + " where id_changes = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ch.getId_repo());
            ps.setInt(2, ch.getVersion());
            ps.setInt(3, ch.getRow_updates_num());
            ps.setString(4, ch.getChange_type());
            ps.setString(5, ch.getOld());
            ps.setString(6, ch.getNewChanges());
            ps.setString(7, ch.getFile());
            ps.setString(8, ch.getUpdatedAt());
            ps.setInt(9, ch.getId_changes());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList selectAll(Repository rep) {
        ArrayList<Changes> ar = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM changes Where id_repo=? ORDER BY version DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rep.getId_repo());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("version") > 0) {
                    Changes ch = new Changes();
                    ch.setId_changes(rs.getInt("id_changes"));
                    ch.setVersion(rs.getInt("version"));
                    ch.setId_repo(rs.getInt("id_repo"));
                    ch.setRow_updates_num(rs.getInt("row_updates_num"));
                    ch.setChange_type("change_type");
                    ch.setOld(rs.getString("old"));
                    ch.setNewChanges(rs.getString("new"));
                    ch.setFile(rs.getString("file"));
                    ch.setUpdatedAt(rs.getString("updatedAt"));
                    ar.add(ch);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ar;
    }

    public Changes selectById(int id) {
        Changes ch = null;
        String sql = "SELECT id_changes, id_repo,version, row_updates_num, change_type, old, new, file, updatedAt "
                + "FROM changes Where id_changes=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ch = new Changes();
                ch.setId_changes(rs.getInt("id_changes"));
                ch.setId_repo(rs.getInt("id_repo"));
                ch.setVersion(rs.getInt("version"));
                ch.setRow_updates_num(rs.getInt("row_updates_num"));
                ch.setChange_type("change_type");
                ch.setOld(rs.getString("old"));
                ch.setNewChanges(rs.getString("new"));
                ch.setFile(rs.getString("file"));
                ch.setUpdatedAt(rs.getString("updatedAt"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ch;
    }

    public void delete(Changes ch) {
        String sql = "DELETE FROM changes WHERE id_changes = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ch.getId_changes());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteByRepository(Repository rep) {
        String sql = "DELETE FROM changes WHERE id_repo = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rep.getId_repo());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void inputChanges(Changes ch) {
        changes.add(ch);
    }

    public int getLastVersion(Repository rep) {
        int version = 0;
        String sql = "SELECT MAX(version) as version FROM changes WHERE id_repo=" + rep.getId_repo();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                version = rs.getInt("version");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return version;
    }
}
