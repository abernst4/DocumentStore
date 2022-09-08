package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.MinHeap;

public class MinHeapImpl<V extends Comparable<V>> extends MinHeap<V>{

    public MinHeapImpl(){
        elements = (V[])new Comparable[2];
    }
    @Override
    public void reHeapify(V element) {
        this.downHeap(getArrayIndex(element));
        this.upHeap(getArrayIndex(element));
    }

    @Override
    protected int getArrayIndex(V element) {
       for(int i = 1; i<super.elements.length;i++){
           if(elements[i].equals(element)){
               return i; 
           }
       }
       return -1; 
    }

    @Override
    protected void doubleArraySize() {
        V[] temp = elements;
        elements = (V[])new Comparable[temp.length*2];
        for(int i = 1; i<temp.length; i++){
            elements[i] = temp[i]; 
        }
    }
}