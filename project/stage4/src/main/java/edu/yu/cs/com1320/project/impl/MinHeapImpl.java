package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.MinHeap;
//import java.lang.reflect.Array; 
public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {
    
    public MinHeapImpl(){
        elements = (E[]) new Comparable[2];
    }

    @Override
    public void reHeapify(E element) {
        upHeap(this.getArrayIndex(element));
        downHeap(this.getArrayIndex(element));
    }

    @Override
    protected int getArrayIndex(E element) { 
        for(int i = 1; i < super.elements.length; i++){
            if(element == elements[i]){
                return i; 
            }
        }
        return -1; 
    }

    @Override
    protected void doubleArraySize() {  
       E[] temp = elements; 
       elements = (E[]) new Comparable[temp.length * 2];
       for(int i =1; i<temp.length; i++){
           elements[i] = temp[i];
       } 
    }  
}