package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Stack;

public class StackImpl<V> implements Stack<V>{
    Node peek; 
    int count; 
    private class Node{
        V v; 
        Node next; 
        Node(V v){
            this.v = v; 
        }
    }
    public StackImpl(){}
    @Override
    public void push(V element) {
        Node node = new Node(element);
        node.next = peek; 
        peek = node;
        ++count;
    }
    @Override
    public V pop() {
        if(peek == null){return null;}
        V val = peek.v; 
        peek = peek.next; 
        --count; 
        return (V)val; 
    }
    @Override
    public V peek() {
        return peek == null?null:(V)peek.v;
    }
    @Override
    public int size() {
        return peek == null?0:count;
    }  
}