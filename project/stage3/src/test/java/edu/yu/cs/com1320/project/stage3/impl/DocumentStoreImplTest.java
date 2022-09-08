package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.Utils;
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage3.*;
import edu.yu.cs.com1320.project.stage3.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage3.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException; 
import java.util.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

public class DocumentStoreImplTest {
    String txt1; 
    String txt2; 
    String txt3;
    URI uri1;  
    URI uri2; 
    URI uri3; 
    List<Document> list ;

    @BeforeEach
    public void init() throws Exception{
        this.txt1 = "today is the THe tHE day that 89 ?*";
        this.txt2 = "IS is THE day ho4 is 89 89 % ^ # f58";
        this.txt3 = "this is The is the txt3 ho4";
        this.uri1 = new URI("uri1");
        this.uri2 = new URI("uri2");
        this.uri3 = new URI("uri3");
        this.list = new ArrayList<>(); 
    }

    @Test
    public void getAllSortedTest() throws IOException{
        DocumentStore store = new DocumentStoreImpl(); 
        int result1 = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, DocumentFormat.TXT);
        int result2 = store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()), this.uri2, DocumentFormat.TXT);
        int result3 = store.putDocument(new ByteArrayInputStream(this.txt3.getBytes()), this.uri3, DocumentFormat.TXT);
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        Document doc3 = new DocumentImpl(uri3, txt3);
        list.add(doc1);
        list.add(doc3);
        list.add(doc2);
        assertEquals(list, store.search("tHe"));
        list.clear();
        list.add(doc2);
        list.add(doc3);
        list.add(doc1);
        assertEquals(list, store.search("is"));
        list.clear();
        assertEquals(list, store.search("3"));
        list.clear();
        list.add(doc2);
        list.add(doc1);
        assertEquals(list, store.search("89"));
        list.clear();
        list.add(doc1);
        list.add(doc2);
        assertEquals(list, store.search("day"));
        list.clear();
        list.add(doc3);
        assertEquals(list, store.search("txt3"));
        list.clear();
        assertEquals(list, store.search("ze"));
        list.clear();
        list.add(doc3);
        list.add(doc2);
        assertEquals(list, store.search("HO4"));

        //test normal undo put
        store.undo();
        list.clear();
        list.add(doc2);
        list.add(doc1);
        assertEquals(list, store.search("iS"));
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri3));

        //test specific undo()
        store.undo(uri1);
        list.clear();
        list.add(doc2);
        assertEquals(list, store.search("iS"));
        assertEquals(null, store.getDocument(uri1));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri3));


    }

    @Test
    public void testGetPrefix() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl(); 
        String a = "all a Alpha boo bp 34 hi";
        String b = "a hello bl hi help * 64";
        String c = "the abc he ap be beh blah hip";
        int result1 = store.putDocument(new ByteArrayInputStream(a.getBytes()), this.uri1, DocumentFormat.TXT);
        int result2 = store.putDocument(new ByteArrayInputStream(b.getBytes()), this.uri2, DocumentFormat.TXT);
        int result3 = store.putDocument(new ByteArrayInputStream(c.getBytes()), this.uri3, DocumentFormat.TXT);
        Document doc1 = new DocumentImpl(uri1, a);
        Document doc2 = new DocumentImpl(uri2, b);
        Document doc3 = new DocumentImpl(uri3, c);
        assertEquals(list, store.searchByPrefix("d"));
        list.add(doc1);
        list.add(doc3);
        list.add(doc2);
        assertEquals(list, store.searchByPrefix("a"));
        list.clear();
        list.add(doc3);
        list.add(doc1);
        list.add(doc2);
        assertEquals(list, store.searchByPrefix("b"));
        list.clear();
        list.add(doc2);
        list.add(doc3);
        list.add(doc1);
        assertEquals(list, store.searchByPrefix("h"));

    }

    @Test
    public void DeleteTest() throws IOException{
        DocumentStore store = new DocumentStoreImpl(); 
        Set<URI> set = new HashSet<>();
        String a = "Document1 one one two ";
        String b = "Document2 two help two";
        String c = "the abc one all";
        int result1 = store.putDocument(new ByteArrayInputStream(a.getBytes()), this.uri1, DocumentFormat.TXT);
        int result2 = store.putDocument(new ByteArrayInputStream(b.getBytes()), this.uri2, DocumentFormat.TXT);
        int result3 = store.putDocument(new ByteArrayInputStream(c.getBytes()), this.uri3, DocumentFormat.TXT);
        Document doc1 = new DocumentImpl(uri1, a);
        Document doc2 = new DocumentImpl(uri2, b);
        Document doc3 = new DocumentImpl(uri3, c);
        list.add(doc1);
        list.add(doc3);
        assertEquals(list, store.search("one"));
        list.clear();
        set.add(uri2);
        set.add(uri1);
        assertEquals(set, store.deleteAll("two"));
        list.clear();
        list.add(doc3);
        assertEquals(list, store.search("one"));
        assertEquals(null, store.getDocument(uri1));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

        //test undo()
        store.undo();
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));
        list.clear();
        list.add(doc1);
        list.add(doc3);
        assertEquals(list, store.search("one"));
    }

    @Test
    public void DeletePrefixTest() throws IOException{
        DocumentStore store = new DocumentStoreImpl(); 
        Set<URI> set = new HashSet<>();
        String a = "all a ab";
        String b = "Document apple at";
        String c = "the bc one ll";
        int result1 = store.putDocument(new ByteArrayInputStream(a.getBytes()), this.uri1, DocumentFormat.TXT);
        int result2 = store.putDocument(new ByteArrayInputStream(b.getBytes()), this.uri2, DocumentFormat.TXT);
        int result3 = store.putDocument(new ByteArrayInputStream(c.getBytes()), this.uri3, DocumentFormat.TXT);
        Document doc1 = new DocumentImpl(uri1, a);
        Document doc2 = new DocumentImpl(uri2, b);
        Document doc3 = new DocumentImpl(uri3, c);
        set.add(uri1);
        set.add(uri2);
        assertEquals(set, store.deleteAllWithPrefix("a"));
        assertEquals(null, store.getDocument(uri1));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));
        list.add(doc3);
        assertEquals(list, store.search("the"));
        assertEquals(list, store.search("bc"));
        assertEquals(list, store.search("one"));
        assertEquals(list, store.search("ll"));
        list.clear();
        assertEquals(list, store.search("all"));
        assertEquals(list, store.search("a"));
        assertEquals(list, store.search("ab"));
        assertEquals(list, store.search("Document"));
        assertEquals(list, store.search("apple"));
        assertEquals(list, store.search("at"));
        
        //test undo() == check, undo for uri1 == good, 
        store.undo(uri1);
        store.undo(uri2);
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

    }

    @Test
    public void putBin() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl(); 
        Set<URI> set = new HashSet<>();
        String a = "all a ab"; 
        int result1 = store.putDocument(new ByteArrayInputStream(a.getBytes()), this.uri1, DocumentFormat.BINARY);
        Document doc1 = new DocumentImpl(uri1, a.getBytes());
        assertEquals(doc1, store.getDocument(uri1));
        store.undo();
        assertEquals(null, store.getDocument(uri1));
    }     


    @Test
    public void replace() throws IOException{
        DocumentStore store = new DocumentStoreImpl(); 
        Set<URI> set = new HashSet<>();
        String a = "This is is Doc1";
        String b = "This is this Doc2";
        Document doc1 = new DocumentImpl(uri1, a);
        Document doc2 = new DocumentImpl(uri1, b);
        int result1 = store.putDocument(new ByteArrayInputStream(a.getBytes()), this.uri1, DocumentFormat.TXT);
        assertEquals(doc1, store.getDocument(uri1));
        list.add(doc1);
        assertEquals(list, store.search("ThIS"));
        assertEquals(list, store.search("DOC1"));
        assertEquals(list, store.search("IS"));
        list.clear();
        int result2 = store.putDocument(new ByteArrayInputStream(b.getBytes()), this.uri1, DocumentFormat.TXT);
        assertEquals(doc2, store.getDocument(uri1));
        list.add(doc2);
        assertEquals(list, store.search("is"));
        assertEquals(list, store.search("DOC2"));
        list.clear();
        assertEquals(list, store.search("Doc1"));
        assertEquals(list, store.search("DOC1"));

        //time to test undo()
        store.undo();
        assertEquals(list, store.search("DOC2"));
        list.add(doc1);
        assertEquals(list, store.search("DOC1"));
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(list, store.search("IS"));

        store.undo();
        list.clear();
        assertEquals(null, store.getDocument(uri1));
        assertEquals(list, store.search("DOC1"));
    }
}
