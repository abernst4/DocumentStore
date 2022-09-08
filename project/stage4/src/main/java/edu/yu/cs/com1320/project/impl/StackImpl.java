package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Stack;

public class StackImpl<V> implements Stack<V>{
    Node peek; 
    private class Node<V>{
        V v; 
        Node next; 
        private Node(V v){
            this.v = v; 
        }
    }
    int count; 
    public StackImpl(){}

    public void push(V v){
        Node node = new Node(v);
        node.next = peek; 
        peek = node; 
        ++count; 
    }

    public V pop(){
        if(peek == null){return null; }
        V v = (V)peek.v; 
        peek = peek.next; 
        count = count == 0? 0: --count; 
        return v; 
    }

    public V peek(){
        return peek == null? null: (V)peek.v;  
    }

    public int size(){
        return peek == null? 0: count; 
    }
}