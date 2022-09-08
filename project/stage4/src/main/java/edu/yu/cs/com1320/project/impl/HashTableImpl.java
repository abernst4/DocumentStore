package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<K,V> implements HashTable<K,V> {
    private class Node<K,V>{
        K k; 
        V v; 
        Node next; 
        private Node(K k, V v){
            this.k = k; 
            this.v = v; 
        }
    }
    Node[] nodes; 
    int count; 
    public HashTableImpl(){
        this.nodes = new Node[5]; 
    }

    public V put(K k, V v){
        return v == null? deleteVal(k):addValue(k,v);
    }

    private V deleteVal(K k){
        if(k == null){ return null; }
        Node current = nodes[makeIndex(k)];
        if(current == null){
            return null; 
        }
        if(!isDifferentKey(k, (K)current.k)){
            V val = (V)current.v; 
            nodes[makeIndex(k)] = current.next; 
            reduceCount();
            return val; 
        }
        while(current.next != null && isDifferentKey((K)current.next.k, k)){
            current = current.next; 
        }
        if(current.next == null){ return null; }
        V val = (V)current.next.v; 
        current.next = current.next.next; 
        reduceCount();
        return val; 
    }

    private int makeIndex(K k){
        return (k.hashCode() & 0x7fffffff) % this.nodes.length; 
    }

    private void reduceCount(){
        this.count = count == 0? 0: --count; 
    }

    private boolean isDifferentKey(K k1, K k2){
        return !k1.equals(k2);
    }

    private V addValue(K k, V v){
        Node current = nodes[makeIndex(k)];
        if(current == null){
            nodes[makeIndex(k)] = new Node(k,v); 
            raiseCount();
            return null; 
        }
        if(!isDifferentKey((K)current.k, k)){
            Node newNode = new Node(k,v);
            newNode.next= current.next; 
            nodes[makeIndex(k)] = newNode; 
            return (V) current.v; 
        }
        while(current.next != null && isDifferentKey((K)current.next.k, k)){
            current = current.next; 
        }
        if(current.next == null){
            current.next = new Node(k,v);
            raiseCount();
            return null; 
        }
        V val = (V)current.next.v; 
        Node newNode = new Node(k, v);
        newNode.next = current.next.next; 
        current.next = newNode;
        return val; 
    }

    private void raiseCount(){
        ++this.count; 
        if(count >= .75 * this.nodes.length){
            rehash();
        }
    }

    private void rehash(){
        Node[] temp = nodes; 
        nodes = new Node[2 * temp.length];
        count = 0; 
        for(int i = 0; i < temp.length; i++){
            Node current = temp[i];
            while(current != null){
                put((K)current.k, (V)current.v);
                current = current.next; 
            }
        }
    }

    public V get(K k){
        Node current = nodes[makeIndex(k)];
        if(current == null){ return null; }
        if(!isDifferentKey((K)current.k, k)){
            return (V) current.v; 
        }
        while(current.next != null && isDifferentKey((K)current.k, k)){
            current = current.next; 
        }
        if(!isDifferentKey((K)current.k, k)){
            return (V)current.v; 
        }
        return null; 
    }
}