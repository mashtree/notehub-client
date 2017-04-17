/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author aisyahumar
 */
public class FileModer {
    
    private String fileName;
    private String fullPathName;
    private String pathName;
    
    /**
     * 
     * @param input
     * @param fileTo
     * @throws IOException 
     */
    public void writeFile(String input, String fileTo) throws IOException{
        FileWriter writer = new FileWriter(fileTo, false);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(input);
        
        bufferedWriter.close();
    }
    
    /**
     * 
     * @param input
     * @param fileTo
     * @throws IOException 
     */
    public void writeFile(ArrayList<String> input, String fileTo) throws IOException{
        FileWriter writer = new FileWriter(fileTo, false);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        int rows = 0;
        for(String s:input){
            bufferedWriter.write(s);
            rows++;
            if(rows<input.size()) bufferedWriter.newLine();
        }
        
        bufferedWriter.close();
    }
    
    /**
     * 
     * @param filePath
     * @return ArrayList<String> file's content
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public ArrayList<String> readFile(String filePath) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> result = new ArrayList<>();
        String line = br.readLine();
        while(line!=null){
            result.add(line);
            line = br.readLine();
        }
        fr.close();
        return result;
    }
    
    /**
     * 
     * @param filePath
     * @return String file's content
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String readFileToString(String filePath) throws FileNotFoundException, IOException{
        String result = "";
        StringBuilder sb = new StringBuilder();
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while(line!=null){
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        result = sb.toString();
        fr.close();
        return result;
    }
    
    /**
     * 
     * @param pathName 
     */
    public void createDirectory(String pathName){
        File f = new File(pathName);
        f.mkdir();
    }
    
}
