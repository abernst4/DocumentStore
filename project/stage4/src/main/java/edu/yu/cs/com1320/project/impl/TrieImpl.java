package edu.yu.cs.com1320.project.impl; 
import edu.yu.cs.com1320.project.Trie;
import java.util.*;
import java.util.stream.*;

public class TrieImpl<Value> implements Trie<Value>{
    Node root; 
    private class Node<Value>{
        Set<Value> vals = new HashSet<>(); 
        Node[] links = new Node[36];
        private Set<Value> rmValS(){
            Set<Value> temp = new HashSet<>(this.vals);
            this.vals.clear();
            return temp; 
        }
        private boolean rmVal(Value val){
            return this.vals.remove(val);
        }
    }

    public TrieImpl(){}

    public void put(String key, Value val){
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
            return x;
        }
        char c = key.charAt(d);
        if(c<58){
            x.links[c-48] = put(x.links[c-48], key, val, ++d);
        }else{
            x.links[c-55] = put(x.links[c-55], key, val, ++d);
        }
        return x; 
    }

    public List<Value> getAllSorted(String key, Comparator com){
        List<Value> list = get(this.root, key.toUpperCase(), 0, new HashSet<Value>())
            .stream().collect(Collectors.toList());
        Collections.sort(list, com); 
        return list; 
    }

    private Set<Value> get(Node x, String key, int d, Set<Value> set){
        if(x == null){
            return set; 
        }
        if(d == key.length()){
            set.addAll(x.vals);
            return set; 
        }
        char c = key.charAt(d);
        if(c<58){
            get(x.links[c-48], key, ++d, set);
        }else{
            get(x.links[c-55], key, ++d, set);
        }
        return set; 
    }

    public List<Value> getAllWithPrefixSorted(String pre, Comparator<Value> com){
        List<Value> list = getPre(this.root, pre.toUpperCase(), 0, new HashSet<Value>())
            .stream().collect(Collectors.toList());
        Collections.sort(list, com);
        return list; 
    }

    private Set<Value> getPre(Node x, String key, int d, Set<Value> set){
        if(x == null){
            return set; 
        }
        if(d == key.length()){
            set.addAll(x.vals);
            for(int i = 0; i< 36; i++){ //need to see if this will work
                if(x.links[i] != null){
                    getPre(x.links[i], key, d, set);
                }
            }
            return set; 
        }
        char c = key.charAt(d);
        if(c<58){
            getPre(x.links[c-48], key, ++d, set);
        }else{
            getPre(x.links[c-55], key, ++d, set);
        }
        return set; 
    }

    public Set<Value> deleteAll(String key){
        Set<Value> temp = new HashSet<>(); 
        this.root = deleteAll(this.root, key.toUpperCase(), 0, temp);
        return temp; 
    }

    private Node deleteAll(Node x, String key, int d, Set<Value> set){
        if(x == null){
            return null; 
        }
        if(d == key.length()){
            set.addAll(x.rmValS());  
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
        for(int i = 0; i<36; ++i){ //need to see if this will work
            if(x.links[i] != null){
                return x; 
            }
        }
        return null; 
    }

    @Override
    public Set<Value> deleteAllWithPrefix(String prefix) {
        Set<Value> set = new HashSet<>(); 
        this.root = deletePre(this.root, prefix.toUpperCase(), 0, set);
        return set; 
    }

    private Node deletePre(Node x, String key, int d, Set<Value> set){
        if(x == null){
            return null; 
        }
        if(d == key.length()){
            set.addAll(x.rmValS());
            x.vals = null; 
            for(int i =0; i<36; ++i){
                if(x.links[i] != null){
                    x.links[i] = deletePre(x.links[i], key, d, set);
                }
            }
        }else{
            char c = key.charAt(d);
            if(c<58){
                x.links[c-48] = deletePre(x.links[c-48], key, ++d, set);
            }else{
                x.links[c-55] = deletePre(x.links[c-55], key, ++d, set);
            }
        }
        if(x.vals != null){
            return x; 
        }
        return null; 
    }

    @Override
    public Value delete(String key, Value val) {
        Set<Boolean> set = new HashSet<>(); 
        this.root = delete(this.root, key.toUpperCase(), val, 0, set);
        return set.contains(true)? val:null; 
    }

    private Node delete(Node x, String key, Value val, int d, Set<Boolean> set){
        if(x == null){
            return null; 
        }
        if(key.length()== d){
            set.add(x.rmVal(val));
        }else{
            char c = key.charAt(d);
            if(c<58){
                x.links[c-48] = delete(x.links[c-48], key, val, ++d, set);
            }else{
                x.links[c-55] = delete(x.links[c-55], key, val, ++d, set);
            }
        }
        if(x.vals.size() != 0){
            return x; 
        }
        for(int i=0; i<36; i++){
            if(x.links[i]!= null){
                return x; 
            }
        }
        return null; 
    }
}
