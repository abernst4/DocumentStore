package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;

import org.junit.jupiter.api.Test; 
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException; 
public class MinHeapImplTest{
    @Test
    public void testPut(){
        MinHeapImpl<O> heap = new MinHeapImpl<O>();
        heap.insert(new O(4));
        heap.insert(new O(1));
        heap.insert(new O(8));
        heap.insert(new O(3));
        heap.insert(new O(2));
        assertEquals(1, heap.remove().i);
        assertEquals(2, heap.remove().i);
        assertEquals(3, heap.remove().i);
        assertEquals(4, heap.remove().i);
        assertEquals(8, heap.remove().i);
    }

    @Test
    public void reHeapify(){
        MinHeapImpl<O> heap = new MinHeapImpl<O>();
        O a = new O(4); 
        O b = new O(1); 
        O c = new O(8); 
        O d = new O(3); 
        O e = new O(2); 
        heap.insert(a);
        heap.insert(b);
        heap.insert(c);
        heap.insert(d);
        heap.insert(e);
        b.i = 16;
        heap.reHeapify(b);
        assertEquals(2, heap.remove().i);
        assertEquals(3, heap.remove().i);
        assertEquals(4, heap.remove().i);
        assertEquals(8, heap.remove().i);
        assertEquals(16, heap.remove().i);
       
    }

    @Test
    public void rehapDoc() throws URISyntaxException{
        MinHeapImpl<Document> heap = new MinHeapImpl<>();
        Document doc1 = new DocumentImpl(new URI("doc1"), "doc1");
        doc1.setLastUseTime(System.nanoTime());
        Document doc2 = new DocumentImpl(new URI("doc2"), "doc2");
        doc2.setLastUseTime(System.nanoTime());
        Document doc3 = new DocumentImpl(new URI("doc3"), "doc3");
        doc3.setLastUseTime(System.nanoTime());
        Document doc4 = new DocumentImpl(new URI("doc4"), "doc4");
        doc4.setLastUseTime(System.nanoTime());
        Document doc5 = new DocumentImpl(new URI("doc5"), "doc5");
        doc5.setLastUseTime(System.nanoTime());
        Document doc6 = new DocumentImpl(new URI("doc6"), "doc6");
        doc6.setLastUseTime(System.nanoTime());
        heap.insert(doc1);
        heap.insert(doc4);
        heap.insert(doc2);
        heap.insert(doc6);
        heap.insert(doc3);
        heap.insert(doc5);
        assertEquals("doc1", heap.remove().getDocumentTxt());
        assertEquals("doc2", heap.remove().getDocumentTxt());
        assertEquals("doc3", heap.remove().getDocumentTxt());
        assertEquals("doc4", heap.remove().getDocumentTxt());
        assertEquals("doc5", heap.remove().getDocumentTxt());
        assertEquals("doc6", heap.remove().getDocumentTxt());
    }

    @Test
    public void rehapDoc2() throws URISyntaxException{
        MinHeapImpl<Document> heap = new MinHeapImpl<>();
        Document doc1 = new DocumentImpl(new URI("doc1"), "doc1");
        doc1.setLastUseTime(System.nanoTime());
        Document doc2 = new DocumentImpl(new URI("doc2"), "doc2");
        doc2.setLastUseTime(System.nanoTime());
        Document doc3 = new DocumentImpl(new URI("doc3"), "doc3");
        doc3.setLastUseTime(System.nanoTime());
        Document doc4 = new DocumentImpl(new URI("doc4"), "doc4");
        doc4.setLastUseTime(System.nanoTime());
        Document doc5 = new DocumentImpl(new URI("doc5"), "doc5");
        doc5.setLastUseTime(System.nanoTime());
        Document doc6 = new DocumentImpl(new URI("doc6"), "doc6");
        doc6.setLastUseTime(System.nanoTime());
        heap.insert(doc1);
        heap.insert(doc4);
        heap.insert(doc2);
        heap.insert(doc6);
        heap.insert(doc3);
        heap.insert(doc5);
        doc2.setLastUseTime(System.nanoTime());
        reHeapify();
        doc4.setLastUseTime(System.nanoTime());
        reHeapify();
        assertEquals("doc1", heap.remove().getDocumentTxt());
        assertEquals("doc3", heap.remove().getDocumentTxt());
        assertEquals("doc5", heap.remove().getDocumentTxt());
        assertEquals("doc6", heap.remove().getDocumentTxt());
        assertEquals("doc2", heap.remove().getDocumentTxt());
        assertEquals("doc4", heap.remove().getDocumentTxt());
    }

}

interface Comp extends Comparable<O>{
    int get();
}

class O implements Comp{
    int i; 
    O(int i){
        this.i = i; 
    }
    public int get(){
        return this.i; 
    }
    @Override
    public int compareTo(O o) {
        O n = (O)o; 
        return this.i > n.i? 1: this.i<n.i? -1:0; 
    }
}