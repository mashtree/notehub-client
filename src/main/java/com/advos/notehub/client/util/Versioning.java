/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.client.util;

import java.util.Map;

/**
 *
 * @author aisyahumar
 * why not do this:
read each line of the old file
there's no 2
store the lines in a set
do the same for each line in the new file
every line from the old file set that's missing in the new file set was removed
every line from the new file set that's missing in the old file set was added
* https://github.com/dnaumenko/java-diff-utils
 */
public class Versioning {
    
    private String oldVersion;    
    private String newVersion;
    private Map<Integer, Change> change;
    
    public Versioning(){
        
    }
    
    private enum Status{
        CONSTANT, INSERTION, MODIFICATION, DELETION
    }
    
    private class Change{
        private int row;
        private Status status;
        private String content;
        
        
    }
    
}
