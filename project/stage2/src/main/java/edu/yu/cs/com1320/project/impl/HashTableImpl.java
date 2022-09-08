package edu.yu.cs.com1320.project.impl; 
import edu.yu.cs.com1320.project.HashTable;
//how to optimize this: try moving the checkCounter to be in each method && make append the last thing in addValue after/appart of repace
public class HashTableImpl<Key, Value> implements HashTable<Key, Value>{
    private class Node<Key, Value>{
        Node next; 
        Key k; 
        Value v; 
        private Node(Key k, Value v){
            this.k = k; 
            this.v = v; 
        }   
    }
    private int counter; 
    Node[] nodes; 
    public HashTableImpl(){
        this.nodes = new Node[5]; 
    }

    private Value addValue(Key k, Value v){
        Node newNode = new Node(k,v); 
        checkCounter(k,v); 
        if(firstSlotEmpty(newNode, makeIndex(k))){
            return null; 
        }
        if(isAppendable(newNode, k)){
            return null; 
        }
        return (Value) replace(newNode, nodes[makeIndex(k)]); 
    }

    private Value deleteValue(Key k, Value v){
        checkCounter(k, v);
        if(nodes[makeIndex(k)] == null){
            return null;
        }
        Node current = nodes[makeIndex(k)];
        if(!isDifferentKey(k, (Key)current.k)){
            Value val = (Value) current.v; 
            nodes[makeIndex(k)] = current.next; 
            return val; 
        }
        while(current.next != null && isDifferentKey(k, (Key)current.next.k)){
            current = current.next; 
        }
        if(current.next == null){
            return null;
        }
        Value val = (Value) current.next.v;
        current.next = current.next.next; 
        return val; 
    }

    public Value get(Key k) {
        Node current = nodes[makeIndex(k)];
        if(current == null){
            return null; 
        }
        while(isDifferentKey(k, (Key)current.k)){
            current = current.next;
            if(current == null){
                return null; 
            }
        }
        return (Value) current.v; 
    }

    public Value put(Key k, Value v) {
        return v == null? deleteValue(k, v): addValue(k, v); 
    }

    private void checkCounter(Key k, Value v){
        if(v == null){
            if(containsKey(k)){
                counter--; 
            }
            return; 
        }
        if(containsKey(k)){
            return; 
        }
        counter++; 
        if(this.counter > nodes.length * .75){
            rehash(); 
        }
    }
    
    private void rehash(){
       Node[] temp = nodes; 
       nodes = new Node[temp.length * 2];
       for(int i = 0; i < temp.length; i++){
           Node current = temp[i]; 
           while(current != null){
               this.put((Key)current.k, (Value)current.v);
               current = current.next; 
           }
       }
    }

    private boolean isDifferentKey(Key k1, Key k2){
        return !k1.equals(k2);
    }

    private int makeIndex(Key k){
        return (k.hashCode() & 0x7fffffff) % this.nodes.length; 
    }

    private boolean containsKey(Key k){
        Node current = nodes[makeIndex(k)];
        if(current == null){
            return false; 
        }
        while(current.next != null && isDifferentKey(k, (Key)current.k)){
            current = current.next; 
        }
        return !isDifferentKey(k, (Key)current.k);
    }

    private boolean firstSlotEmpty(Node newNode, int i){
        if(nodes[i] != null){
            return false; 
        }
        nodes[i] = newNode; 
        return true; 
    }

    private boolean isAppendable(Node newNode, Key k){
        Node current = nodes[makeIndex(k)];
        while(current.next != null && isDifferentKey((Key)current.k, (Key)newNode.k)){
            current = current.next; 
        }
        if(current.next == null && isDifferentKey((Key)current.k, (Key)newNode.k)){
            append(newNode, k);
            return true; 
        }
        return false; 
    }

    private void append(Node newNode, Key k){
        Node current = nodes[makeIndex(k)];
        while(current.next != null){
            current = current.next; 
        }
        current.next = newNode; 
    }

    private Value replace(Node newNode, Node current){
        if(!isDifferentKey((Key)newNode.k, (Key)current.k)){
            newNode.next = current.next; 
            nodes[makeIndex((Key)newNode.k)] = newNode; 
            return (Value) current.v; 
        }
        while(isDifferentKey((Key)newNode.k, (Key)current.next.k)){
            current = current.next; 
        }
        Value temp = (Value) current.next.v; 
        newNode.next = current.next.next; 
        current.next = newNode; 
        return temp;  
    } 
}
