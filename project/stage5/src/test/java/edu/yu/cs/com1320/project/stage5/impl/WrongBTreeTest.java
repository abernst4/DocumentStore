package edu.yu.cs.com1320.project.stage5.impl;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.WrongBTree;
import edu.yu.cs.com1320.project.stage5.impl.*;

public class WrongBTreeTest{
    @Test
    public void demoPut(){
        WrongBTree<Integer,String> st = new WrongBTree<>();
        st.put(1, "one");
        st.put(2, "two");
        st.put(3, "three");
        st.put(4, "four");
        st.put(5, "five");
        st.put(6, "six");
        st.put(7, "seven");
        st.put(8, "eight");
        st.put(9, "nine");
        st.put(10, "ten");
        st.put(11, "eleven");
        st.put(12, "twelve");
        st.put(13, "thirteen");
        st.put(14, "fourteen");
        st.put(15, "fifteen");
        st.put(16, "sixteen");
        st.put(17, "seventeen");
        st.put(18, "eighteen");
        st.put(19, "nineteen");
        st.put(20, "twenty");
        st.put(21, "twenty one");
        st.put(22, "twenty two");
        st.put(23, "twenty three");
        st.put(24, "twenty four");
        st.put(25, "twenty five");
        st.put(26, "twenty six");
        
        

        assertEquals("one", st.get(1));
        assertEquals("two", st.get(2));
        assertEquals("three", st.get(3));
        assertEquals("four", st.get(4));
        assertEquals("five", st.get(5));
        assertEquals("six", st.get(6));
        assertEquals("seven", st.get(7));
        assertEquals("eight", st.get(8));
        assertEquals("nine", st.get(9));
        assertEquals("ten", st.get(10));
        assertEquals("eleven", st.get(11));
        assertEquals("twelve",  st.get(12));
        assertEquals("thirteen",  st.get(13));
        assertEquals("fourteen",  st.get(14));
        assertEquals("fifteen",  st.get(15));
        assertEquals("sixteen",  st.get(16));
        assertEquals("seventeen",  st.get(17));
        assertEquals("eighteen",  st.get(18));
        assertEquals("nineteen",  st.get(19));
        assertEquals("twenty", st.get(20));
        assertEquals("twenty one", st.get(21));
        

    }
    @Test
    public void BTreeIMplTsest(){
        BTreeImpl<Integer, String> st = new BTreeImpl<>();
        st.put(1, "one");
        st.put(2, "two");
        st.put(3, "three");
        st.put(4, "four");
        st.put(5, "five");
        st.put(6, "six");
        st.put(7, "seven");
        st.put(8, "eight");
        st.put(9, "nine");
        st.put(10, "ten");
        st.put(11, "eleven");
        st.put(12, "twelve");
        st.put(13, "thirteen");
        st.put(14, "fourteen");
        st.put(15, "fifteen");
        st.put(16, "sixteen");
        st.put(17, "seventeen");
        st.put(18, "eighteen");
        st.put(19, "nineteen");
        st.put(20, "twenty");
        st.put(21, "twenty one");
        st.put(22, "twenty two");
        st.put(23, "twenty three");
        st.put(24, "twenty four");
        st.put(25, "twenty five");
        assertEquals(null,st.put(26, "twenty six"));
        
        

        assertEquals("one", st.get(1));
        assertEquals("two", st.get(2));
        assertEquals("three", st.get(3));
        assertEquals("four", st.get(4));
        assertEquals("five", st.get(5));
        assertEquals("six", st.get(6));
        assertEquals("seven", st.get(7));
        assertEquals("eight", st.get(8));
        assertEquals("nine", st.get(9));
        assertEquals("ten", st.get(10));
        assertEquals("eleven", st.get(11));
        assertEquals("twelve",  st.get(12));
        assertEquals("thirteen",  st.get(13));
        assertEquals("fourteen",  st.get(14));
        assertEquals("fifteen",  st.get(15));
        assertEquals("sixteen",  st.get(16));
        assertEquals("seventeen",  st.get(17));
        assertEquals("eighteen",  st.get(18));
        assertEquals("nineteen",  st.get(19));
        assertEquals("twenty", st.get(20));
        assertEquals("twenty one", st.get(21));
        
    }

    @Test
    public void BTreeTestReverse(){
        BTreeImpl<Integer,String> st = new BTreeImpl<>();
        assertEquals(null,st.put(26, "twenty six"));
        st.put(25, "twenty five");
        st.put(24, "twenty four");
        st.put(23, "twenty three");
        st.put(22, "twenty two");
        st.put(21, "twenty one");
        st.put(20, "twenty");
        st.put(19, "nineteen");
        st.put(18, "eighteen");
        st.put(17, "seventeen");
        st.put(16, "sixteen");
        st.put(1, "one");
        st.put(2, "two");
        st.put(3, "three");
        st.put(4, "four");
        st.put(5, "five");
        st.put(6, "six");
        st.put(7, "seven");
        st.put(8, "eight");
        st.put(9, "nine");
        st.put(10, "ten");
        st.put(11, "eleven");
        st.put(12, "twelve");
        st.put(13, "thirteen");
        st.put(14, "fourteen");
        st.put(15, "fifteen");

        assertEquals("one", st.get(1));
        assertEquals("two", st.get(2));
        assertEquals("three", st.get(3));
        assertEquals("four", st.get(4));
        assertEquals("five", st.get(5));
        assertEquals("six", st.get(6));
        assertEquals("seven", st.get(7));
        assertEquals("eight", st.get(8));
        assertEquals("nine", st.get(9));
        assertEquals("ten", st.get(10));
        assertEquals("eleven", st.get(11));
        assertEquals("twelve",  st.get(12));
        assertEquals("thirteen",  st.get(13));
        assertEquals("fourteen",  st.get(14));
        assertEquals("fifteen",  st.get(15));
        assertEquals("sixteen",  st.get(16));
        assertEquals("seventeen",  st.get(17));
        assertEquals("eighteen",  st.get(18));
        assertEquals("nineteen",  st.get(19));
        assertEquals("twenty", st.get(20));
        assertEquals("twenty one", st.get(21));
        assertEquals(null, st.get(30));
        assertEquals("one", st.put(1, "1"));

    }

    @Test
    public void replaceTest(){
        BTreeImpl<Integer, String> st = new BTreeImpl<>(); 
        st.put(1, "hi");
        assertEquals("hi", st.get(1));
        String a = st.put(1, "hello");
        assertEquals("hi", a);
        assertEquals("hello", st.get(1));
    }

    @Test
    public void testHeap(){
        MinHeapImpl<Integer> heap = new MinHeapImpl<>();
        heap.insert(1);
        heap.insert(4);
        heap.insert(2);
        heap.insert(5);
        heap.insert(6);
        heap.insert(-1);
        assertEquals(-1, heap.remove());
    }
}







