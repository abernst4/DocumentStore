package edu.yu.cs.com1320.project.stage4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import edu.yu.cs.com1320.project.stage4.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage4.impl.DocumentStoreImpl;

public class DocumentStoreImplTest{
    String txt1;
    String txt2;
    URI uri1;
    URI uri2;
    List<Document> list; 
    @BeforeEach
    public void init() throws Exception{
         this.txt1 = "this is doc1 a";
         this.txt2 = "this is doc2 b";
         this.uri1 = new URI("a");
         this.uri2 = new URI("b");
         this.list = new ArrayList<>();
    }

    @Test
    public void putTestWithGet() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        assertEquals(0, result);
        Document doc = new DocumentImpl(uri1, txt1);
        assertEquals(doc, store.getDocument(uri1));
        list.add(doc);
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));
        store.undo();
        list.clear();
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(null, store.getDocument(uri1));
    }
    @Test
    public void putSwitchTest() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        int result2 =store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentFormat.TXT);
        assertEquals(0, result2);
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        assertEquals(doc2, store.getDocument(uri2));
        list.add(doc2);
        assertEquals(list, store.search("doc2"));
        assertEquals(list, store.search("B"));
        assertEquals(new ArrayList<>(), store.search("Ir"));
        store.undo();
        list.clear();
        list.add(doc1);
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(doc1, store.getDocument(uri1));
    }

    @Test
    public void limitTestDocSize() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        store.setMaxDocumentCount(1);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentFormat.TXT);
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri1));
        list.add(doc2);
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(list, store.search("B"));
        assertEquals(list, store.search("DoC2"));
        list.clear();
        assertEquals(list, store.search("Doc1"));
        assertEquals(list, store.search("a"));
        store.undo();
        assertEquals(null, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri1));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(list, store.search("B"));
        assertEquals(list, store.search("DoC2"));
        assertEquals(list, store.search("Doc1"));
        assertEquals(list, store.search("a"));
    }

    @Test
    public void testByteLimitImmed() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        store.setMaxDocumentBytes(15);
        assertEquals(doc1, store.getDocument(uri1));
        list.add(doc1);
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));

        store.setMaxDocumentBytes(14);
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));

        store.setMaxDocumentBytes(13);
        assertEquals(null, store.getDocument(uri1));
        list.clear();
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));
       
    }

   // @Test
    public void testSizeLimitImmed() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        store.setMaxDocumentCount(2);
        assertEquals(doc1, store.getDocument(uri1));
        list.add(doc1);
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));

        store.setMaxDocumentCount(1);
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));

        store.setMaxDocumentCount(0);
        assertEquals(null, store.getDocument(uri1));
        list.clear();
        assertEquals(list, store.search("doc1"));
        assertEquals(list, store.search("This"));
        assertEquals(list, store.search("IS"));
        assertEquals(new ArrayList<>(), store.search("Ir"));
       
    }

    @Test
    public void testReplaceSameURIWithLimit() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri1, txt2);
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        store.setMaxDocumentCount(1);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentFormat.TXT);
        assertEquals(doc2, store.getDocument(uri1));
        list.add(doc2);
        assertEquals(list, store.search("b"));
        assertEquals(new ArrayList<>(), store.search("a"));

        list.clear();
        store.undo();
        assertEquals(null, store.getDocument(uri1));
        assertEquals(list, store.search("b"));
        assertEquals(list, store.search("a"));
    }

    @Test
    public void replaceBin() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1.getBytes());
        Document doc2 = new DocumentImpl(uri1, txt2.getBytes());
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.BINARY);
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(list, store.search("a"));


        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentFormat.BINARY);
        assertEquals(doc2, store.getDocument(uri1));
        assertEquals(list, store.search("a"));

        store.undo();
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(list, store.search("a"));
    }

    @Test
    public void testDelete() throws IOException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        assertEquals(doc1, store.getDocument(uri1));
        list.add(doc1);
        assertEquals(list, store.search("A"));
        assertEquals(list, store.searchByPrefix("D"));

        store.deleteDocument(uri1);
        list.clear();
        assertEquals(null, store.getDocument(uri1));
        assertEquals(list, store.search("A"));
        assertEquals(list, store.searchByPrefix("D"));

        int result1 =store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentFormat.TXT);
        list.add(doc2);
        assertEquals(list, store.search("B"));
        assertEquals(list, store.searchByPrefix("D"));
        assertEquals(doc2, store.getDocument(uri2));

        store.setMaxDocumentCount(1);
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(list, store.search("B"));
        assertEquals(list, store.searchByPrefix("D"));

        store.undo(uri1);
        list.clear();
        assertEquals(list, store.search("B"));
        assertEquals(null, store.getDocument(uri2));

        list.add(doc1);
        assertEquals(list, store.search("A"));
        assertEquals(list, store.searchByPrefix("D"));
        assertEquals(doc1, store.getDocument(uri1));
    }


    @Test
    public void testDeleteAll() throws IOException, URISyntaxException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        Document doc3 = new DocumentImpl(new URI("c"), "Doc3 is the way");
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentFormat.TXT);
        String txt3 = "Doc3 is the way";
        URI uri3 = new URI("c");
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);
        store.deleteAll("This");
        store.setMaxDocumentCount(2);
        assertEquals(null, store.getDocument(uri1));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

        store.undo();
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri3));

    }

    @Test
    public void testDeletePre() throws IOException, URISyntaxException{
        DocumentStoreImpl store = new DocumentStoreImpl();
        Document doc1 = new DocumentImpl(uri1, txt1);
        Document doc2 = new DocumentImpl(uri2, txt2);
        Document doc3 = new DocumentImpl(new URI("c"), "Doc3 is loe way");
        int result =store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentFormat.TXT);
        store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri2, DocumentFormat.TXT);
        String txt3 = "Doc3 is loe way";
        URI uri3 = new URI("c");
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);
        assertEquals(doc3, store.getDocument(uri3));
        Set<URI> set = new HashSet<>();
        set.add(uri1);
        set.add(uri2);
        assertEquals(set, store.deleteAllWithPrefix("Th"));
        
        assertEquals(null, store.getDocument(uri1));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

        store.setMaxDocumentCount(2);
        assertEquals(null, store.getDocument(uri1));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

        store.undo();
        assertEquals(doc1, store.getDocument(uri1));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri3));
    }
}