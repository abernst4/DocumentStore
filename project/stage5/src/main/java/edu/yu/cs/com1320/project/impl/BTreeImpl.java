package edu.yu.cs.com1320.project.impl; 
import java.io.IOException;
import java.net.URI;

import edu.yu.cs.com1320.project.BTree;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
//I'm still not done because I NEED TO USE DEAL WITH REMOVING FROM DISK AND WRITING TO DISK

//IF SOMETHING IS WRITTEN TO DISK, DO DELETE EVERYTHING FROM EVERY DATASTRUCTURE
public class BTreeImpl<K extends Comparable<K>,V> implements BTree{
    private Node root;
    private int height; 
    private PersistenceManager<URI,Document> pm;  
    private class Node{
        private int entryCount; 
        private Entry[] entries = new Entry[4]; //why in his WrongBtree did he not have to cast (E[])???
        private Node(int k){ //if I change my mind and want the max to be six, I'll have to change all of the 2's to 3's
            this.entryCount = k; 
        }
    }
    private class Entry<K>{ //need to try overriding the comparable method: 
        Comparable key; 
        Object val; 
        Node child; 
        private Entry(Comparable k, Object v, Node x){
            this.key = k; 
            this.val = v; 
            this.child = x; 
        }
    }

    public BTreeImpl(){
        this.root = new Node(0);
    }

    private boolean less(Comparable k1, Comparable k2){
        return k1.compareTo(k2) < 0;  
    }

    private boolean isEqual(Comparable k1, Comparable k2){
        return k1.compareTo(k2) == 0; 
    }
    //I need to check if ent Not(null) and if ent.val == uri, because then I have to change the val to the document
    //and delete the file and directories from disk
    //Once I have the value returned and it's not in documentStore then I have to put it back into 
    //btree, However, I'm thinking about putting it back into the Btree from DocumentStore
    @Override
    public V get(Comparable k) {
        if(k == null){throw new IllegalArgumentException();}
        Entry ent = get(this.root, k, this.height);
        if(ent!= null && ent.val instanceof URI){
            try {
                Document doc = pm.deserialize((URI)ent.val);
                this.put(k, doc);
                return (V)doc; 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(ent != null){
            return (V)ent.val; 
        }
        return null; 
    }

    private Entry get(Node x, Comparable k, int height){
        Entry[] ents = x.entries; 
        if(height == 0){
            for(int j = 0; j<x.entryCount; j++){
                if(isEqual(k, ents[j].key)){
                    return ents[j];
                }
            }
            return null; 
        }else{
            for(int j= 0; j<x.entryCount; j++){
                if(j+1 == x.entryCount || less(k, ents[j+1].key)){
                    return get(ents[j].child, k, height-1);
                }
            }
            return null; 
        }
    }
    @Override
    public V put(Comparable key, Object v) {
        if(key == null){throw new IllegalArgumentException();}
        Entry exists = get(this.root, key, this.height);
        if(exists != null){ 
            Object obj = exists.val; 
            exists.val = v; 
            return (V)obj; 
        }
        Node newNode = put(this.root, (K)key, (V)v, this.height); 
        if(newNode == null){ return null; }
        Node newRoot = new Node(2);
        newRoot.entries[0] = new Entry(this.root.entries[0].key, null, this.root);
        newRoot.entries[1] = new Entry(newNode.entries[0].key, null, newNode);
        this.root = newRoot; 
        this.height++; 
        return null; 
    }

    private Node put(Node x, K k, V v, int height){ 
        int j; 
        Entry newEntry = new Entry(k, v, null);
        if(height == 0){
            for(j = 0; j<x.entryCount; j++){
                if(less(k, x.entries[j].key)){
                    break; 
                }
            }
        }else{
           for(j = 0; j<x.entryCount; j++){
               if(j+1 == x.entryCount || less(k,x.entries[j+1].key)){
                   Node newNode = put(x.entries[j++].child, k, v, height-1);
                   if(newNode == null){return null; }
                   newEntry.key = newNode.entries[0].key; 
                   newEntry.val = null; 
                   newEntry.child = newNode; 
                   break; 
               }
           } 
        }
        for(int i = x.entryCount; i>j; i--){
            x.entries[i] = x.entries[i-1];
        }
        x.entries[j] = newEntry;
        x.entryCount++;
        if(x.entryCount<4){ 
            return null; 
        }else{
            return split(x, height);
        }
    }

    private Node split(Node x, int height){ //need to look at his code where he Null's out the top of x.entries
        Node newNode = new Node(2);
        x.entryCount = 2; 
        for(int j = 0; j<2; j++){
            newNode.entries[j] = x.entries[2+j];
            x.entries[2+j] = null; //need to look at his code for nulling top of x. 
        }
        return newNode; 
    }
    @Override
    public void moveToDisk(Comparable k) throws Exception { 
        V doc = this.put(k, k);
        this.pm.serialize((URI)k, (Document)doc);
        //WHEN WE MAKE OUR UNIQUE PERSISTENCE MANAGER, WHERE IS IT SUPPOSED TO BE IN OUR FILE SYSTEM? 
    }
    @Override
    public void setPersistenceManager(PersistenceManager pm) {
        this.pm = pm; 
    }
}