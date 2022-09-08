package edu.yu.cs.com1320.project.impl; 
import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T>{
    Node peek; 
    int counter; 
    class Node<T>{
        T value;
        Node next; 
        Node(T t){
            this.value = t; 
        }
    }
    public StackImpl(){}

    public void push(T t){
        Node node = new Node(t);
        node.next = peek; 
        peek = node; 
        counter++; 
    }

    public T pop(){
        if(peek == null){
           return null; 
        }
        T val = (T)peek.value; 
        peek = peek.next; 
        --counter; 
        return val; 
    }
    
    public T peek(){
        return peek == null? null: (T)peek.value; 
    }

    public int size(){
        return peek == null? 0: counter; 
    }  
}