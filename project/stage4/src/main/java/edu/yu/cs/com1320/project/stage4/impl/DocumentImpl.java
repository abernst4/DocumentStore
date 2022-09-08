package edu.yu.cs.com1320.project.stage4.impl;
import java.net.URI;
import java.util.*;
import java.util.stream.*;
import java.net.URI;

import edu.yu.cs.com1320.project.stage4.Document; //need to ask if 

public class DocumentImpl implements Document{
    String txt; 
    byte[] bytes; 
    URI uri; 
    Map<String,Integer> map; 
    long nanoTime; 
    public DocumentImpl(URI uri, String str){
        if(uri == null || str == null){
            throw new IllegalArgumentException("can't be null");
        }
        if(uri.toString().length()== 0 || str.length()== 0){
            throw new IllegalArgumentException("can't have length 0");
        }
        this.uri = uri; 
        this.txt = str; 
        map = new HashMap<>(); 
        makeMap();
    }

    private void makeMap(){
        Arrays.stream(this.txt.split(" ")) 
            .map(this::prune)
            .filter(i -> !i.equals(""))
            .map(String::toUpperCase)
            .forEach(word -> map.merge(word, 1, Integer::sum));
    }

    private String prune(String str){
        return str.replaceAll("[^a-zA-Z0-9]+", "");
    }

    public DocumentImpl(URI uri, byte[] bytes){
        if(uri == null || bytes == null){
            throw new IllegalArgumentException("can't be null");
        }
        if(uri.toString().length()==0 || bytes.length == 0){
            throw new IllegalArgumentException("can't be length zero");
        }
        this.uri = uri; 
        this.bytes = bytes; 
    }

    @Override
    public int compareTo(Document o) {
        return this.getLastUseTime() > o.getLastUseTime()? 1: this.getLastUseTime()< o.getLastUseTime()? -1: 0; 
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
        if(this.bytes != null){return 0; }
        return map.get(word.toUpperCase()) == null? 0: map.get(word.toUpperCase());
    }

    public Set<String> getWords() { //might need to chagne this depending on three
        if(bytes != null){ return new HashSet<>(); }
        return Arrays.stream(this.txt.split(" ")) 
            .map(this::prune)
            .filter(i -> !i.equals(""))
            .map(String::toUpperCase)
            .collect(Collectors.toSet());
    }

    public long getLastUseTime() {
        return this.nanoTime; 
    }

    public void setLastUseTime(long timeInNanoseconds) {
        this.nanoTime = timeInNanoseconds; 
    }

    public int hashCode() {
        int result = this.uri.hashCode();
        result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.bytes);
        return result;
    }
     
    public boolean equals(Object o){
        if(this == o){
            return true; 
        }
        if(null == o){
            return false; 
        }
        if(this.getClass() != o.getClass()){
            return false; 
        }
        Document doc = (Document)o; 
        return this.hashCode() == doc.hashCode();
    } 
    
}