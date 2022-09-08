package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<K,V> implements HashTable<K,V>{
    private class Node<K,V>{
        K k; 
        V v; 
        Node next; 
        Node(K k, V v){
            this.k = k; 
            this.v = v; 
        }  
    }
    Node[] nodes; 
    int count; 
   public HashTableImpl(){
        nodes = new Node[5];
    }

    public V put(K k, V v){
        return v == null? deleteValue(k,v): addValue(k,v);
    }

    private V deleteValue(K k, V v){
        if(k == null){ 
            return null; 
        }
        Node current = nodes[makeIndex(k)];
        if(current == null){
            return null; 
        }
        if(!isDifferentKey((K)current.k, k)){
            V val = (V)current.v; 
            nodes[makeIndex(k)] = current.next; 
            reduceCount();
            return val; 
        }
        while(current.next != null && isDifferentKey((K)current.next.k, k)){
            current = current.next; 
        }
        if(current.next == null){
            return null; 
        }
        V val = (V) current.next.v; 
        current.next = current.next.next; 
        reduceCount();
        return val; 
    }

    private V addValue(K k, V v){
        if(k == null){  
             return null; 
        }
        Node current = nodes[makeIndex(k)]; 
        if(current == null){
            nodes[makeIndex(k)] = new Node(k,v);
            raiseCount();
            return null; 
        }
        if(!isDifferentKey((K)current.k, k)){
            Node node = new Node(k, v); 
            node.next = current.next; 
            nodes[makeIndex(k)] = node; 
            return (V)current.v; 
        }
        while(current.next != null && isDifferentKey((K)current.next.k, k)){
            current = current.next; 
        }
        if(current.next == null){
            current.next = new Node(k,v);
            raiseCount();
            return null; 
        }
        V val = (V) current.next.v; 
        Node node = new Node(k, v);
        node.next = current.next.next; 
        current.next = node; 
        return val; 
    }

    private void raiseCount(){
        ++count; 
        if(count >= .75 * nodes.length){
            rehash();
        }
    }

   private int makeIndex(K k){
       return (k.hashCode() & 0x7fffffff) % this.nodes.length; 
   }

    private void reduceCount(){
        count = count == 0? 0: --count; 
    }

    private boolean isDifferentKey(K k1, K k2){
        return !k1.equals(k2);
    }

    private void rehash(){
        Node[] temp = nodes; 
        nodes = new Node[2 * temp.length];
        count = 0; 
        for(int i = 0; i < temp.length; ++i){
            Node current = temp[i];
            while(current != null){
                this.put((K)current.k, (V)current.v); 
                current = current.next; 
            }
        }
    }

    public V get(K k){
        Node current = nodes[makeIndex(k)];
        if(current == null){
            return null; 
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

// for(Node i = head; i != null; i = i.next){
//   
//}