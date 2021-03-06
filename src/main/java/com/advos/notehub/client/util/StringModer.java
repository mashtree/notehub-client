/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author triyono
 */
public class StringModer {
    
    /**
     * removing hmtl tags on String
     * @param htmlText
     * @return 
     */
    public String stripHTMLTags(String htmlText) {
        
        String text = br2nl(htmlText);
        Pattern pattern = Pattern.compile("<[^>]*>");
        //Pattern pattern = Pattern.compile("/\\<body[^>]*\\>([^]*)\\<\\/body/m");
        //Pattern pattern = Pattern.compile("/\\<body[^>]*\\>([^]*)\\<\\/body/\\");
        Matcher matcher = pattern.matcher(text);
        final StringBuffer sb = new StringBuffer(text.length());
        while(matcher.find()) {
            matcher.appendReplacement(sb, " ");
        }
        matcher.appendTail(sb);
        return sb.toString().trim();

    }
    
    private String br2nl(String html) {
        Document document = Jsoup.parse(html);
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        return document.text().replace("\\n", "\n");
    }

    private String nl2br(String text) {
        return text.replace("\n\n", "<p>").replace("\n", "<br>");
    }
    
    /**
     * removing hmtl tags on String
     * @param htmlText
     * @return 
     */
    public String addNewLineHTMLTags(String htmlText) {

        Pattern pattern = Pattern.compile("<br>");
        //Pattern pattern = Pattern.compile("/\\<body[^>]*\\>([^]*)\\<\\/body/m");
        Matcher matcher = pattern.matcher(htmlText);
        final StringBuffer sb = new StringBuffer(htmlText.length());
        while(matcher.find()) {
            matcher.appendReplacement(sb, "\n");
        }
        matcher.appendTail(sb);
        return sb.toString().trim();

    }
    
    /**
     * 
     * @param s
     * @return 
     */
    public ArrayList<String> strToArrayList(String s){
        ArrayList<String> a = new ArrayList<>();
        Scanner scanner = new Scanner(s);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          a.add(line);
        }
        scanner.close();
        return a;
    }
}
