package edu.yu.cs.com1320.project.impl; 
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashSet;
import edu.yu.cs.com1320.project.Trie;

//I don't know why when I make this.vals = null in the rmVal method, getAllSortedFreeksOut about null pointer
public class TrieImpl<Value> implements Trie<Value>{
    Node root; 
    class Node<Value>{ 
        Set<Value> vals = new HashSet<>(); 
        Node[] links = new Node[36]; 
        private Set<Value> rmVal(){ 
            Set<Value> temp = new HashSet<>(this.vals);
            vals.clear(); //I WANT TO PLAY AROUND AND MAKE THIS NULL
            return temp; 
        }
        private boolean rmSingleVal(Value val){
            return this.vals.remove(val);
        }
    }
    public TrieImpl(){}

    public void put(String key, Value val) {
        if(val == null){
            deleteAll(key.toUpperCase());
        }else{
           this.root = put(this.root, key.toUpperCase(), val, 0);
        }
    }

    private Node put(Node x, String key, Value val, int d){
        if(x == null){
            x = new Node(); 
        }
        if(d == key.length()){
            x.vals.add(val); 
            return x;  //might be able to return this in one line
        }
        char c = key.charAt(d); 
        if(c < 58){
           x.links[c-48] = put(x.links[c-48], key, val, ++d); 
        }else{
           x.links[c-55] = put(x.links[c-55], key, val, ++d); 
        }
        return x; 
    }

    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        List<Value> list = get(this.root, key.toUpperCase(), 0, new HashSet<Value>())
            .stream().collect(Collectors.toList());
        Collections.sort(list, comparator);
        return list; 
    }

    private Set<Value> get(Node x, String key, int d, HashSet<Value> set){ 
        if(x == null){
            return set; 
        }
        if(d == key.length()){
            set.addAll(x.vals); 
            return set; //might be able to return this in one line
        }
        char c = key.charAt(d); 
        if(c < 58){
            get(x.links[c-48], key, ++d, set); 
        }else{
            get(x.links[c-55], key, ++d, set); 
        }
        return set; 
    }

    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        List<Value> list = getPref(this.root, prefix.toUpperCase(), 0, new HashSet<Value>())
            .stream().collect(Collectors.toList());
        Collections.sort(list, comparator);
        return list; 
    }

    private Set<Value> getPref(Node x, String pref, int d, HashSet<Value> set){
        if(x == null){
            return set; 
        }
        if(d == pref.length()){
            set.addAll(x.vals); 
            for(char c = 0; c < 36; ++c){
                if(x.links[c] != null){
                    getPref(x.links[c], pref, d, set);
                }
            }
            return set; 
        }
        char c = pref.charAt(d);
        if(c < 58){
            getPref(x.links[c-48], pref, ++d, set);
        }else{
            getPref(x.links[c-55], pref, ++d, set);
        }
        return set; 
    }

    public Set<Value> deleteAllWithPrefix(String prefix) {
        Set<Value> temp = new HashSet<>();
        this.root = deleteAllWithPrefix(this.root, prefix.toUpperCase(), 0, temp);
        return temp; 
    }

    private Node deleteAllWithPrefix(Node x, String prefix, int d, Set<Value> set){
        if(x == null){
            return null; 
        }
        if(d == prefix.length()){
            set.addAll(x.rmVal());
            x.vals = null; 
            for(char c= 0; c <36; ++c){
                if(x.links[c]!= null){
                  x.links[c] = deleteAllWithPrefix(x.links[c], prefix, d, set);
                }
            } 
        }else{
            char c = prefix.charAt(d); 
            if(c<58){
                x.links[c-48] = deleteAllWithPrefix(x.links[c-48], prefix, ++d, set);
            }else{
                x.links[c-55] = deleteAllWithPrefix(x.links[c-55], prefix, ++d, set);
            }
        }
        if(x.vals != null){ //THIS WOULD THEN BE (X != NULL)
            return x; 
        }
        return null; 
    }

    public Set<Value> deleteAll(String key) {
        Set<Value> temp = new HashSet<>();
        this.root = deleteAll(this.root, key.toUpperCase(), 0, temp);
        return temp; 
    }

    private Node deleteAll(Node x, String key, int d, Set<Value> set){
        if(x == null){
           return null; 
        }
        if(d == key.length()){
            set.addAll(x.rmVal()); 
        }else{
            char c = key.charAt(d);
            if(c<58){
                x.links[c-48] = deleteAll(x.links[c-48], key, ++d, set); 
            }else{
                x.links[c-55] = deleteAll(x.links[c-55], key, ++d, set);
            }
        }
        if(!x.vals.isEmpty()){ 
            return x; 
        }
        for(char c = 0; c< 36; ++c){
            if(x.links[c] != null){
                return x; 
            }
        }
        return null; 
    }

    public Value delete(String key, Value val) {
        Set<Boolean> set = new HashSet<>();  
        this.root = delete(this.root, key.toUpperCase(), 0, set, val);
        return set.contains(true)? val: null; 
    }

    private Node delete(Node x, String key, int d, Set<Boolean> set, Value val){
        if(x == null){
            return null; 
        }
        if(d == key.length()){
            set.add(x.rmSingleVal(val)); 
        }else{
            char c = key.charAt(d);
            if(c < 58){
                x.links[c-48] = delete(x.links[c-48], key, ++d, set, val);
            }else{
                x.links[c-55] = delete(x.links[c-55], key, ++d, set, val);
            }
        }
        if(x.vals.size() != 0){
            return x; 
        }
        for(char c = 0; c<36; ++c){
            if(x.links[c] != null){
                return x; 
            }
        }
        return null; 
    } 
}
