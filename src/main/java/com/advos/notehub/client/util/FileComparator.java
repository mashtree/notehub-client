/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author triyono
 */
public class FileComparator {

    private File original;

    private File revised;
    
    public FileComparator(){
        
    }
    
    public FileComparator(File original, File revised) {
        this.original = original;
        this.revised = revised;
    }

    public List<Chunk> getChangesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.CHANGE);
    }

    public List<Chunk> getInsertsFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.INSERT);
    }

    public List<Chunk> getDeletesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.DELETE);
    }
    
    public List<Chunk> getChangesFromOriginal(List<String> ori, List<String> revised) throws IOException {
        return getChunksByType(Delta.TYPE.CHANGE,ori, revised);
    }

    public List<Chunk> getInsertsFromOriginal(List<String> ori, List<String> revised) throws IOException {
        return getChunksByType(Delta.TYPE.INSERT, ori, revised);
    }

    public List<Chunk> getDeletesFromOriginal(List<String> ori, List<String> revised) throws IOException {
        return getChunksByType(Delta.TYPE.DELETE, ori, revised);
    }

    private List<Chunk> getChunksByType(Delta.TYPE type, List<String> ori, List<String> revised) throws IOException {
        final List<Chunk> listOfChanges = new ArrayList<Chunk>();
        final List<Delta> deltas = getDeltas(ori, revised);
        for (Delta delta : deltas) {
            if (delta.getType() == type) {
                listOfChanges.add(delta.getRevised());
            }
        }
        return listOfChanges;
    }
    
    private List<Chunk> getChunksByType(Delta.TYPE type) throws IOException {
        final List<Chunk> listOfChanges = new ArrayList<Chunk>();
        final List<Delta> deltas = getDeltas();
        for (Delta delta : deltas) {
            if (delta.getType() == type) {
                listOfChanges.add(delta.getRevised());
            }
        }
        return listOfChanges;
    }
    
    private List<Delta> getDeltas(List<String> ori, List<String> revised){
        final Patch patch = DiffUtils.diff(ori, revised);

        return patch.getDeltas();
    }

    private List<Delta> getDeltas() throws IOException {

        final List<String> originalFileLines = fileToLines(original);
        final List<String> revisedFileLines = fileToLines(revised);

        final Patch patch = DiffUtils.diff(originalFileLines, revisedFileLines);

        return patch.getDeltas();
    }

    private List<String> fileToLines(File file) throws IOException {
        final List<String> lines = new ArrayList<String>();
        String line;
        final BufferedReader in = new BufferedReader(new FileReader(file));
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }
    
}
