package edu.yu.cs.com1320.project.stage5.impl;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.yu.cs.com1320.project.stage5.Document;
//Need to ask piazza if I'm supposed to return an empty collection if if they ask for the map and I didn't use map
public class DocumentImpl implements Document{
    String txt;  
    byte[] bytes; 
    URI uri; 
    Map<String, Integer> map; 
    transient long timeUsed; 
    public DocumentImpl(URI uri, String txt, Map<String,Integer> wordCountMap){
        if(uri == null || txt == null){
            throw new IllegalArgumentException();
        }
        if(uri.toString().length()==0 || txt.length()==0){
            throw new IllegalArgumentException();
        }
        this.uri = uri; 
        this.txt = txt; 
        if(wordCountMap == null){
            this.map = new HashMap<>();
            makeMap();
        }else{
            map = wordCountMap;
        }
    }

    public DocumentImpl(URI uri, byte[] bytes){
        if(uri == null || bytes == null){
            throw new IllegalArgumentException();
        }
        if(uri.toString().length()== 0||bytes.length==0){
            throw new IllegalArgumentException();
        }
        this.uri = uri; 
        this.bytes = bytes; 
        this.map = new HashMap<>();
    }

    private void makeMap(){
        Arrays.stream(this.txt.split(" "))
                .map(this::prune)
                .filter(i -> !i.equals(""))
                .map(String::toUpperCase)
                .forEach(word -> this.map.merge(word, 1, Integer::sum));
    }

    
    @Override
    public int compareTo(Document o) {
       return this.getLastUseTime()>o.getLastUseTime()?1:o.getLastUseTime()>this.getLastUseTime()?-1:0;
    }

    @Override
    public String getDocumentTxt() {
       return  this.txt; 
    }

    @Override
    public byte[] getDocumentBinaryData() {
        return this.bytes;
    }

    @Override
    public URI getKey() {
        return this.uri;
    }

    @Override
    public int wordCount(String word) {
       return map.get(word.toUpperCase());
    }

    @Override
    public Set<String> getWords() {
      return Arrays.stream(txt.split(" "))
        .map(this::prune)
        .filter(x -> !x.equals(""))
        .map(String::toUpperCase)
        .collect(Collectors.toSet());
    }

    private String prune(String str){
        return str.replaceAll("[^a-zA-Z0-9]+", "");
    }

    @Override
    public long getLastUseTime() {
        return timeUsed;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds) {
        this.timeUsed=timeInNanoseconds;
    }

    @Override
    public Map<String, Integer> getWordMap() {
        return new HashMap<>(this.map);
    }

    @Override
    public void setWordMap(Map<String, Integer> wordMap) {
        this.map = new HashMap<>();
        if(wordMap==null){return; }
        for(String str: wordMap.keySet()){
            this.map.put(str.toUpperCase(), wordMap.get(str));
        }
    }

    @Override
    public int hashCode() {
        int result = this.uri.hashCode();
        result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.bytes);
        return result;
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true; 
        }
        if(null == obj){
            return false; 
        }
        if(this.getClass()!=obj.getClass()){
            return false; 
        }
        Document doc = (Document)obj; 
        return this.hashCode() == doc.hashCode();
    }
}