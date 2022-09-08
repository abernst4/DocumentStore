package edu.yu.cs.com1320.project.impl; 
import edu.yu.cs.com1320.project.Stack; 

public class StackImpl<T> implements Stack<T>{
    private Node peek; 
    private class Node<T>{
        T t; 
        Node next; 
        private Node(T t){
            this.t = t; 
        }
    }
    private int counter; 
    public StackImpl(){}
    
    public void push(T element) { //ask him to check for null
         Node node = new Node(element); 
         counter++; 
         if(this.peek == null){
             this.peek = node; 
             return; 
         }
         node.next = peek; 
         this.peek = node; 
    }

   public T pop() {
       if(peek == null){
           return null; 
       }
        T temp = (T)peek.t; 
        counter = counter == 0? 0: --counter;
        peek = peek.next; 
        return temp; 
    }

    public T peek() {
        return this.peek == null? null:(T) this.peek.t; 
    }

    public int size() {
        return this.peek == null? 0: counter; 
    }
}