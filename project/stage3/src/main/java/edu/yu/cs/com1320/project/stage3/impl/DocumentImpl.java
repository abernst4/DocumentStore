package edu.yu.cs.com1320.project.stage3.impl;
import edu.yu.cs.com1320.project.stage3.Document;
import java.net.URI; 
import java.util.*;
import java.util.stream.*; 

public class DocumentImpl implements Document{
    String txt;  
    byte[] bytes; 
    URI uri; 
    Map<String, Integer> map;
    public DocumentImpl(URI uri, String txt){
        if(uri == null || txt == null){
            throw new IllegalArgumentException("can't be null");
        }
        if(uri.toString().length() == 0 || txt.equals("")){
            throw new IllegalArgumentException("can't have no length");
        }
        this.uri = uri; 
        this.txt = txt;  
        map = new HashMap<>();
        makeMap();
    }

    private void makeMap(){
        Arrays.stream(this.txt.split(" "))
            .map(this::prune)
            .filter(i -> !i.equals(""))
            .map(String::toUpperCase)
            .forEach(word-> map.merge(word, 1, Integer::sum)); 
    }

    public DocumentImpl(URI uri, byte[] bytes){
        if(uri == null || bytes == null){
            throw new IllegalArgumentException("can't be null");
        }
        if(uri.toString().length() == 0 || bytes.length == 0){
            throw new IllegalArgumentException("can't have no length");
        }
        this.uri = uri; 
        this.bytes = bytes; 
    }

    public String getDocumentTxt() {
        return this.txt; 
    }

    public byte[] getDocumentBinaryData() {
        return this.bytes; 
    }

    public URI getKey() {
        return this.uri; 
    }

    public int wordCount(String word) { 
        if(this.bytes != null){ return 0; }
        return map.get(word.toUpperCase()) == null? 0: map.get(word.toUpperCase());
    }

    public Set<String> getWords() { 
        if(bytes != null){ return new HashSet<>(); }
        return Arrays.stream(this.txt.split(" "))
        .map(this::prune)
        .filter(i -> !i.equals(""))
        .map(String::toUpperCase) 
        .collect(Collectors.toSet());  
    }  
    
    public int hashCode() { 
        int result = this.uri.hashCode();
        result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.bytes);
        return result;
    }

    private String prune(String str){
        return str.replaceAll("[^a-zA-Z0-9]+", ""); 
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true; 
        }
        if(obj == null){
            return false; 
        }
        if(this.getClass()!= obj.getClass()){
            return false; 
        }
        DocumentImpl doc = (DocumentImpl)obj;
        return this.hashCode() == obj.hashCode(); 
    }
}