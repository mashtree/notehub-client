/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client;

import com.advos.notehub.client.dao.ChangesDao;
import com.advos.notehub.client.dao.RepositoryDao;
import com.advos.notehub.client.entity.Repository;
import com.advos.notehub.client.util.DateUtil;
import com.advos.notehub.client.util.FileModer;
import com.advos.notehub.client.util.StringModer;
import com.notehub.api.entity.Note;
import com.notehub.api.entity.NoteChangesMap;
import com.notehub.api.service.MessageClient;
import com.notehub.api.service.MessageServerService;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author triyono
 */
public class MessageClientImpl extends UnicastRemoteObject implements MessageClient {

    private MessageServerService messageServer;
    private int UID;
    private final RepositoryDao repDao = new RepositoryDao();

    /**
     * register
     *
     * @param ID
     * @param ms
     * @throws RemoteException
     */
    public MessageClientImpl(int ID, MessageServerService ms) throws RemoteException {
        UID = ID;
        messageServer = ms;
        messageServer.registerUser(UID, this);
        System.out.println(DateUtil.getTimeNow() + " creating message client");
    }

    @Override
    public void retrieveVersionMessage(String string, int noteID) throws RemoteException {

    }

    @Override
    public void retrieveNoteChanges(Note note, List<NoteChangesMap> list) throws RemoteException {
        //save the note to repository data, note has id on server
        System.out.println(DateUtil.getTimeNow()+" retrieve note's changes data from server");
        Repository rep = repDao.selectByIdOnServer(note.getIdNote());
        if (rep.getId_on_server() > 1) { //if the note exist on local database
            StringModer sm = new StringModer();
            String t = sm.addNewLineHTMLTags(note.getContent());
            String onlyText = sm.stripHTMLTags(t);
            String pathName = rep.getLocal_location() + "/" + rep.getName_repo() + ".htm";
            ArrayList<String> revised = sm.strToArrayList(t);
            String pathNameOnlyText = rep.getLocal_location() + "/" + rep.getName_repo() + ".txt";
            ArrayList<String> revisedOnlyText = sm.strToArrayList(onlyText);
            FileModer fm = new FileModer();
            try {
                //---- flush to physical file
                fm.writeFile(revised, pathName);
                fm.writeFile(revisedOnlyText, pathNameOnlyText);
            } catch (IOException ex) {
                Logger.getLogger(MessageClientImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            //update the note in htmleditor if it is opened
            //update the note's changes
            if (list.size() > 0) {
                ChangesDao cd = new ChangesDao();
                cd.insertAll(list, rep.getId_repo());
            }
        }

    }

}
