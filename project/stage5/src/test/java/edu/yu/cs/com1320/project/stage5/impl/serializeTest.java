package edu.yu.cs.com1320.project.stage5.impl;
import edu.yu.cs.com1320.project.stage5.impl.*;
import jakarta.xml.bind.DatatypeConverter;
import edu.yu.cs.com1320.project.stage5.*;
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore.DocumentFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


public class serializeTest{
   
   
    @Test
    public void testStuff() throws URISyntaxException, IOException{
        URI uri = new URI("htpl://www.yu.edu/documents/doc1");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt,null);
        DocumentPersistenceManager man = new DocumentPersistenceManager(null);
        man.serialize(uri, doc);
        //Document doc1 = man.deserialize(uri);
        //assertEquals(doc, doc1);
    }

    @Test
    public void testTempDir() throws IOException, URISyntaxException{
        File baseDir = Files.createTempDirectory("stage4").toFile();
        //assertEquals(true, baseDir.exists());
        //assertEquals("hi", baseDir.getAbsolutePath());
        DocumentPersistenceManager dm = new DocumentPersistenceManager(baseDir); //stage49051124338438576670
        URI uri = new URI("http://edu.yu.cs/com1320/project/doc67");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt,null);
        dm.serialize(uri, doc);
        Document doc1 = dm.deserialize(uri);
        assertEquals(doc, doc1);

        Document doll = new DocumentImpl(uri, txt.getBytes());
        dm.serialize(uri, doll);
        Document doll2 =dm.deserialize(uri);
        assertEquals(doll, doll2);
    }
/*
    @Test
    public void DesOfTempDir() throws IOException, URISyntaxException{
        File baseDir = Files.createTempDirectory("stage4").toFile();
        DocumentStore store = new DocumentStoreImpl(baseDir);
        DocumentPersistenceManager dm = new DocumentPersistenceManager(baseDir); //stage49051124338438576670
        URI uri = new URI("http://edu.yu.cs/com1320/project/doc67");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt.getBytes());
        store.putDocument(new ByteArrayInputStream(txt.getBytes()),uri, DocumentFormat.BINARY);
        store.setMaxDocumentCount(0);
        assertEquals(doc, store.getDocument(uri));
    }
*/

    @Test
    public void testStackEmptyAfterOverflow() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("h://www.yu.edu/documents/doc1");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()),uri, DocumentFormat.TXT);
        store.setMaxDocumentBytes(0);
        assertEquals(true, store.deleteDocument(uri));
        //store.undo();THIS WORKS AND IT'S GESHMAK
        //store.undo(); THIS WORKS, THE FILE IS STILL IN THE STACK
    }

    @Test
    public void testReplaceFromDisk() throws IOException, URISyntaxException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("h://www.yu.edu/documents/doc1");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()),uri, DocumentFormat.TXT);
        URI uri2 = new URI("https://www.berny/berns");
        String t1 = "hello my friends hello"; 
        Document doc2 = new DocumentImpl(uri2, t1,null);
        int r2 = store.putDocument(new ByteArrayInputStream(t1.getBytes()), uri2, DocumentFormat.TXT);
        store.setMaxDocumentCount(1);
        Document doc3 = new DocumentImpl(uri, "the world says boo", null);
        //store.putDocument(new ByteArrayInputStream("the world says boo".getBytes()), uri, DocumentFormat.TXT);
    }

    @Test
    public void testDirPut() throws URISyntaxException, IOException{
        URI uri = new URI("h://www.yu.edu/documents/doc1");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt,null);
        DocumentPersistenceManager man = new DocumentPersistenceManager(new File("HashTableImpl"));
        man.serialize(uri, doc);
        Document doc1 = man.deserialize(uri);
        assertEquals(doc, doc1);
    }

    @Test 
    public void len() throws IOException{
        File f = new File(System.getProperty("user.dir"));
        int i = (f.listFiles().length);
        //assertEquals(3, i);
        //assertEquals("a", DatatypeConverter.printBase64Binary("hello".getBytes()));
        //assertEquals(9, new File("/").length());
    }

    
    @Test
    public void testBinarySerialize() throws IOException,URISyntaxException{
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        byte[] b = new String("Hello, this is a string").getBytes();
        Map<String, Integer> map = new HashMap<>();
        Document doc = new DocumentImpl(uri, b);
        doc.setWordMap(map);
        DocumentPersistenceManager man = new DocumentPersistenceManager(null);
        man.serialize(uri, doc);
        Document doc1 = man.deserialize(uri);
        assertEquals("Hello, this is a string", new String(doc1.getDocumentBinaryData()));
        assertEquals(doc, doc1); //[B@31fa1761 for both, so in other words, I WIN
    }

    @Test
    public void testdelete() throws URISyntaxException, IOException{
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        byte[] b = new String("Hello, this is a string").getBytes();
        Map<String, Integer> map = new HashMap<>();
        Document doc = new DocumentImpl(uri, b);
        doc.setWordMap(map);
        DocumentPersistenceManager man = new DocumentPersistenceManager(null);
        man.serialize(uri, doc);
        boolean be = man.delete(uri);
        assertEquals(true, be);
    }

    @Test
    public void testPut() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "hello my my my friends";
        Document doc = new DocumentImpl(uri, txt,null);
        assertEquals(null, store.getDocument(uri));
        int result = store.putDocument(new ByteArrayInputStream(txt.getBytes()),uri, DocumentFormat.TXT);
        assertEquals(0, result);
        assertEquals(doc, store.getDocument(uri));

        URI uri2 = new URI("https://www.berny/berns");
        String t1 = "hello my friends hello"; 
        Document doc2 = new DocumentImpl(uri2, t1,null);
        int r2 = store.putDocument(new ByteArrayInputStream(t1.getBytes()), uri2, DocumentFormat.TXT);
        assertEquals(0, r2);
        assertEquals(doc2, store.getDocument(uri2));

        URI uri3 = new URI("https://www.ber/fuchs");
        String t2 = "hello hello friends hello my my"; 
        Document doc3 = new DocumentImpl(uri3, t2,null);
        int r3 = store.putDocument(new ByteArrayInputStream(t2.getBytes()), uri3, DocumentFormat.TXT);
        assertEquals(0, r3);
        assertEquals(doc3, store.getDocument(uri3));

        List<Document> list1 = new ArrayList<>();
        list1.add(doc3);
        list1.add(doc2);
        list1.add(doc);
        assertEquals(list1, store.search("hello"));

        List<Document> list2 = new ArrayList<>();
        list2.add(doc);
        list2.add(doc3);
        list2.add(doc2);
        assertEquals(list2, store.search("my"));

    }

    @Test
    public void testSearchPre() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "alpha aLbert you most everybody";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()),uri, DocumentFormat.TXT);

        URI uri2 = new URI("https://www.berny/berns2");
        String t1 = "loves somebogy Al"; 
        Document doc2 = new DocumentImpl(uri2, t1,null);
        int r2 = store.putDocument(new ByteArrayInputStream(t1.getBytes()), uri2, DocumentFormat.TXT);

        URI uri3 = new URI("https://www.ber/fuchs3");
        String t2 = "All alb al most person in the world"; 
        Document doc3 = new DocumentImpl(uri3, t2,null);
        int r3 = store.putDocument(new ByteArrayInputStream(t2.getBytes()), uri3, DocumentFormat.TXT);

        List<Document> list1 = new ArrayList<>();
        list1.add(doc3);
        list1.add(doc);
        list1.add(doc2);
        assertEquals(list1, store.searchByPrefix("al"));
    }

    @Test
    public void testReplace() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "alpha aLbert you most everybody";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()),uri, DocumentFormat.TXT);
        assertEquals(doc, store.getDocument(uri));
        URI uri2 = new URI("https://www.berny/berns2");
        String t1 = "Yosef titl baum"; 
        Document doc2 = new DocumentImpl(uri, t1,null);
        int r2 = store.putDocument(new ByteArrayInputStream(t1.getBytes()), uri, DocumentFormat.TXT);
        assertEquals(doc.hashCode(), r2);
        assertEquals(doc2, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("alpha"));
        
    }

    @Test
    public void testURImethods() throws URISyntaxException{
        URI uri = new URI("h://www.yu.edu/documents/doc1");
        //assertEquals("a", uri.getRawQuery());
    }

    @Test
    public void testBtreePutAndGet() throws URISyntaxException{
        BTreeImpl<URI, Document> bt = new BTreeImpl<>();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "alpha aLbert you most everybody";
        Document doc = new DocumentImpl(uri, txt,null);
        Document doc2 = new DocumentImpl(new URI("hi/there"), txt,null);
        PersistenceManager pm = new DocumentPersistenceManager(null); 
        bt.setPersistenceManager(pm);
        assertEquals(null, bt.get(uri));
        bt.put(uri, doc);
        assertEquals(doc, bt.get(uri));
        assertEquals(doc, bt.put(uri, doc2));
        assertEquals(doc2, bt.put(uri, null));
        assertEquals(null, bt.get(uri));
    }

    @Test
    public void testBTreeDelete() throws Exception{
        BTreeImpl<URI, Document> bt = new BTreeImpl<>();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "alpha aLbert you most everybody";
        Document doc = new DocumentImpl(uri, txt,null);
        PersistenceManager pm = new DocumentPersistenceManager(null); 
        bt.setPersistenceManager(pm);
        bt.put(uri, doc);
        bt.moveToDisk(uri);
        assertEquals(doc, bt.get(uri));
        bt.put(uri, null);
        assertEquals(null, bt.get(uri));

        bt.put(uri, doc);
        bt.put(uri, null);
        assertEquals(null, bt.get(uri));
    }

    @Test
    public void testStorePut() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        int a =store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);
        assertEquals(a, 0);
        assertEquals(doc, store.getDocument(uri));
        List<Document> list = new ArrayList<>();
        list.add(doc);
        assertEquals(list, store.search("a"));
        assertEquals(list, store.search("yOU"));
        assertEquals(list, store.search("WelL"));
    }

    @Test
    public void testStoreDelete() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        int a =store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);
        assertEquals(a, 0);
        assertEquals(doc, store.getDocument(uri));
        assertEquals(3, store.getDocument(uri).wordCount("A"));
        assertEquals(3, store.getDocument(uri).wordCount("a"));
        boolean b = store.deleteDocument(uri);
        assertEquals(true, b);
        assertEquals(null, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("a"));
        assertEquals(new ArrayList<>(), store.search("You"));
        assertEquals(new ArrayList<>(), store.search("WeLL"));
    }

    @Test
    public void testStoreDelete2() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        int a =store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);
        assertEquals(a, 0);
        assertEquals(doc, store.getDocument(uri));
        assertEquals(3, store.getDocument(uri).wordCount("A"));
        assertEquals(3, store.getDocument(uri).wordCount("a"));
        int hash = store.putDocument(null, uri, DocumentFormat.TXT);
        assertEquals(doc.hashCode(), hash);
        assertEquals(null, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("a"));
        assertEquals(new ArrayList<>(), store.search("You"));
        assertEquals(new ArrayList<>(), store.search("WeLL"));
    }

    
    @Test
    public void testUndoStamPut() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);
        store.undo();
        assertEquals(null, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("a"));
        assertEquals(new ArrayList<>(), store.search("You"));
        assertEquals(new ArrayList<>(), store.search("WeLL"));
    }

    @Test
    public void testUndoStamDelete() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);
        store.deleteDocument(uri);
        assertEquals(null, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("a"));
        assertEquals(new ArrayList<>(), store.search("You"));
        assertEquals(new ArrayList<>(), store.search("WeLL"));
        store.undo();
        assertEquals(doc, store.getDocument(uri));
        List<Document> list = new ArrayList<>();
        list.add(doc);
        assertEquals(list, store.search("a"));
        assertEquals(list, store.search("yOU"));
        assertEquals(list, store.search("WelL"));
    }

    @Test
    public void testRootDir() throws IOException{
        String dirs = System.getProperty("user.dir");
        File file = new File(dirs); 
        while(file.getParent()!=null){
            file = file.getParentFile();
        }
        File[] one = new File[0];
        File[] files = file.listFiles();
        //assertEquals(one.length, files.length);
    }

    @Test //this is what im testing
    public void testSerializeInDocStore() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentCount(1);
        assertEquals(doc, store.getDocument(uri));

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "B b b dou dou hope";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);
        assertEquals(doc, store.getDocument(uri)); //this getDocument is causing me Bugs
        List<Document> list = new ArrayList<>();
        list.add(doc2);
        //assertEquals(list, store.search("a"));  //I think there should be serialization done here as well
        
        
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(true, store.deleteDocument(uri));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(list, store.search("b"));
        assertEquals(list, store.search("HOPE"));

        assertEquals(list, store.search("Dou"));

        //store.deleteDocument(uri);
        assertEquals(null, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("a"));
        //At this point the doc2 file still exists

        //assertEquals(doc2, store.getDocument(uri2));

        //and this point it doesn't :)!!!!
    }

    @Test
    public void testHeap(){
        MinHeapImpl<Integer> heap = new MinHeapImpl<>();
        heap.insert(1);
        heap.insert(1);
        assertEquals(1, heap.remove());
        assertEquals(1, heap.remove());
    }

    @Test
    public void limitDocSizeThenDeleteSerializedFile() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you you well";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentCount(1);
        assertEquals(doc, store.getDocument(uri));

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "B b b dou dou hope";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);
        assertEquals(doc2, store.getDocument(uri2));
        
        assertEquals(true, store.deleteDocument(uri));
        assertEquals(null, store.getDocument(uri));
        assertEquals(new ArrayList<>(), store.search("a"));

        List<Document> list = new ArrayList<>();
        list.add(doc2);
        assertEquals(list, store.search("b"));
        assertEquals(doc2, store.getDocument(uri2));
    }
 
    @Test
    public void limitDocSizeThenSearch() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a you b you well";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentCount(1);
        assertEquals(doc, store.getDocument(uri));

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "B b b a dou dou hope";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        
        List<Document> list = new ArrayList<>();
        list.add(doc);
        list.add(doc2);
        assertEquals(list, store.search("a"));
        
        //doc1 should be in a file
        //and IT IS!!!!!!

        list.clear();
        list.add(doc2);
        list.add(doc);
        assertEquals(list, store.search("b"));
        //doc2 should be in a file
        //AND IT IS!!!!!!!!!!!!
    } 

    @Test
    public void limitDocSizeThenPreSearch() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentCount(1);
        assertEquals(doc, store.getDocument(uri));

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "A art ballon Bel b";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        
        List<Document> list = new ArrayList<>();
        list.add(doc);
        list.add(doc2);
        assertEquals(list, store.searchByPrefix("a"));
        
        //doc1 should be in a file
        //and IT IS!!!!!!

        list.clear();
        list.add(doc2);
        list.add(doc);
        assertEquals(list, store.searchByPrefix("b"));
        //doc2 should be in a file
        //AND IT IS!!!!!!!!!!!!
    } 

    @Test
    public void testUndoURIPUT() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "A art ballon Bel b";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        URI uri3 = new URI("https://www.yu.edu/documents/doc3");
        String txt3 = "A art ballon well b";
        Document doc3 = new DocumentImpl(uri3, txt3,null);
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);

        store.undo(uri);
        assertEquals(doc3, store.getDocument(uri3));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri));
    }

    @Test
    public void testUnodURIDelete() throws IOException, URISyntaxException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "A art ballon Bel b";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        store.deleteDocument(uri);
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri));

        store.undo(uri);
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(doc, store.getDocument(uri));
    }

    @Test
    public void undoStamReplace() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        String txt2 = "A google Ball b ";
        Document doc2 = new DocumentImpl(uri, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri, DocumentFormat.TXT);

        assertEquals(doc2, store.getDocument(uri));
        store.undo();
        assertEquals(doc, store.getDocument(uri));
    }

    @Test
    public void undoReplaceURI() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        String txt2 = "A google Ball b ";
        Document doc2 = new DocumentImpl(uri, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri, DocumentFormat.TXT);

        String txt3 = "hello world";
        URI uri3 = new URI("http://www.hell/world/doc2");
        Document doc3 = new DocumentImpl(uri3, txt3,null);
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);

        assertEquals(doc2, store.getDocument(uri));
        store.undo(uri);
        assertEquals(doc, store.getDocument(uri));

    }
 
    @Test
    public void serializeDoclimZero() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentCount(0);
        //should have serialized doc
        //and there is!!!!
    }

    @Test
    public void UndoDeleteWithLimit() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "A art ballon Bel b";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        store.deleteDocument(uri2);
        assertEquals(doc, store.getDocument(uri));
        assertEquals(null, store.getDocument(uri2));
        store.setMaxDocumentCount(1);

        store.undo();
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc2, store.getDocument(uri2));
    }

    @Test
    public void deleteAllTestWUndo() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a a ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "A";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        String txt3 = "a A";
        URI uri3 = new URI("http://www.hell/world/doc3");
        Document doc3 = new DocumentImpl(uri3, txt3,null);
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);

        Set<URI> list = new HashSet<>();
        list.add(uri);
        list.add(uri3);
        list.add(uri2);
        assertEquals(list, store.deleteAll("a"));
        assertEquals(null, store.getDocument(uri));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri3));

        store.undo();
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

        List<Document> list2 = new ArrayList<>();
        list2.add(doc);
        list2.add(doc3);
        list2.add(doc2);
        assertEquals(list2, store.search("a"));
    }

    @Test
    public void deleteAllPRETestWUndo() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "Apple a alian ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        URI uri2 = new URI("https://www.yu.edu/documents/doc2");
        String txt2 = "Afew";
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        String txt3 = "albert Ashl";
        URI uri3 = new URI("http://www.hell/world/doc3");
        Document doc3 = new DocumentImpl(uri3, txt3,null);
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);
        
        Set<URI> list = new HashSet<>();
        list.add(uri);
        list.add(uri3);
        list.add(uri2);
        assertEquals(list, store.deleteAllWithPrefix("a"));
        
        assertEquals(null, store.getDocument(uri));
        assertEquals(null, store.getDocument(uri2));
        assertEquals(null, store.getDocument(uri3));
        
        store.undo();
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc2, store.getDocument(uri2));
        assertEquals(doc3, store.getDocument(uri3));

        List<Document> list2 = new ArrayList<>();
        list2.add(doc);
        list2.add(doc3);
        list2.add(doc2);
        assertEquals(list2, store.searchByPrefix("a"));
    }

    @Test
    public void searchWithMemoryLimit() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        String txt2 = "A a a google Ball b ";
        URI uri2 = new URI("hts://www.hi/your/doc2");
        Document doc2 = new DocumentImpl(uri2, txt2,null);
        store.putDocument(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentFormat.TXT);

        String txt3 = "hello a world";
        URI uri3 = new URI("http://www.hell/world/doc3");
        Document doc3 = new DocumentImpl(uri3, txt3,null);
        store.putDocument(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentFormat.TXT);

        List<Document> list = new ArrayList<>();
        list.add(doc2);
        list.add(doc);
        list.add(doc3);
        store.setMaxDocumentCount(2);
        assertEquals(list, store.search("a"));
        store.deleteDocument(uri2);
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc3, store.getDocument(uri3));
    }

    @Test
    public void testByteLim() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A a apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentBytes(txt.getBytes().length-15);
        store.deleteDocument(uri);
        assertEquals(null, store.getDocument(uri));
    }

   
    @Test
    public void testGson() throws URISyntaxException{
        byte[] bytes = "hellow world it;s teh gangser".getBytes();
        String encode = DatatypeConverter.printBase64Binary(bytes);
        Gson gson = new Gson();
        String json = gson.toJson(encode);
        //assertEquals("hi", json);

        Document doc = new DocumentImpl(new URI("hello/worold"), "the world is large".getBytes());
        Map<String, Integer> map = new HashMap<>();
        map.put("hi", 2);
        map.put("bo", 38);
        //doc.setWordMap(map);
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonSerializer<DocumentImpl> serializer = new JsonSerializer<DocumentImpl>() {  
        @Override
        public JsonElement serialize(DocumentImpl src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        if(src.getDocumentBinaryData()!=null){
            String encode = DatatypeConverter.printBase64Binary(src.getDocumentBinaryData());
            //assertEquals("hi", encode);
            obj.addProperty("byteString", encode);
            obj.addProperty("uri", src.getKey().toString());
            obj.addProperty("map", src.getWordMap().toString());
                return obj;
            }
                return new JsonPrimitive(src.toString());
            }
        }; // will implement in a second  
        gsonBuilder.registerTypeAdapter(DocumentImpl.class, serializer);
        Gson customGson = gsonBuilder.create();  
        String customJSON = customGson.toJson(doc);
        //assertEquals("hi", customJSON); 
        


        JsonDeserializer<Document> deserializer = new JsonDeserializer<Document>() {
            @Override
            public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();//well, need to look at my phone when we get back
            try {
                Document doc = null;
                if(jsonObject.get("byteString") == null){
                    doc = new DocumentImpl(new URI(jsonObject.get("uri").getAsString()), jsonObject.get("txt").getAsString(),null);
                }else{
                    byte[] bytes = DatatypeConverter.parseBase64Binary(jsonObject.get("byteString").getAsString());
                    doc = new DocumentImpl(new URI(jsonObject.get("uri").getAsString()), bytes);
                }
                //JsonObject obj = new JsonObject();
                //obj.add("map", jsonObject.get("map"));
                Type type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                //assertEquals("{}", jsonObject.get("map"));
                JsonElement what = jsonObject.get("map");
                Map<String, Integer> map = new Gson().fromJson(what.getAsString(), type);
                doc.setWordMap(map);
                return doc; 
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null; 
            }//catch(JsonSyntaxException e){}
            //return doc;
            } 
        }; 

        //GsonBuilder gsonBuilder2 = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DocumentImpl.class, deserializer);
       // gsonBuilder.registerTypeAdapter(HashMap.class, mapDe);
         customGson = gsonBuilder.create();  
        Document customObject = customGson.fromJson(customJSON, DocumentImpl.class);
        assertEquals(doc, customObject);
        assertEquals(new HashMap<>(), customObject.getWordMap());
        assertEquals(null, customObject.getDocumentTxt());
        assertEquals(Arrays.toString(doc.getDocumentBinaryData()), Arrays.toString(customObject.getDocumentBinaryData()));
        assertEquals(true, Arrays.equals(doc.getDocumentBinaryData(), customObject.getDocumentBinaryData()));
        JsonDeserializer<HashMap<String,Integer>> mapDe = new JsonDeserializer<HashMap<String,Integer>>() {

            @Override
            public HashMap<String, Integer> deserialize(JsonElement json, Type typeOfT,
                    JsonDeserializationContext context) throws JsonParseException {
                        JsonObject jsonO = json.getAsJsonObject();
                        Type type = new TypeToken<HashMap<String,Integer>>(){}.getType();
                        return gson.fromJson(jsonO.get("map"), type);
            }
            
        };
        //gsonBuilder2.registerTypeHierarchyAdapter(HashMap.class, mapDe);
        //Gson customGson3 = new GsonBuilder().registerTypeHierarchyAdapter(HashMap.class, mapDe).create(); 
        //Map<String, Integer> maps = customGson3.fromJson(customJSON, HashMap.class);
        //assertEquals(map, maps);
        //assertEquals(38, doc.wordCount("bo"));
        //assertEquals(38, customObject.wordCount("bo"));
    }

    @Test
    public void mapSer(){
        Gson gson = new Gson();
        Map<String, Integer> map = new HashMap<>();
        map.put("hi", 2);
        map.put("bo", 38);
        String ser = gson.toJson(map);
        Type type = new TypeToken<HashMap<String,Integer>>(){}.getType();
        Map<String,Integer> hi = gson.fromJson(ser, type);
        assertEquals(hi, map);
    }

    @Test
    public void ByteSir(){
        Gson gson = new Gson();
        byte[] bytes = "hellow there".getBytes();
        String Str = DatatypeConverter.printBase64Binary(bytes);
        String sir = gson.toJson(Str);
        String desir = gson.fromJson(sir, String.class);
        byte[] deB = DatatypeConverter.parseBase64Binary(desir);
        assertEquals(Str, desir);
        assertEquals(true, Arrays.equals(bytes, deB));

    }

    @Test
    public void TestGEt() throws URISyntaxException, IOException{
        DocumentStore store = new DocumentStoreImpl();
        URI uri = new URI("https://www.yu.edu/documents/doc1");
        String txt = "A apple aleph Ball b ";
        Document doc = new DocumentImpl(uri, txt,null);
        store.putDocument(new ByteArrayInputStream(txt.getBytes()), uri, DocumentFormat.TXT);

        store.setMaxDocumentCount(1);
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        assertEquals(doc, store.getDocument(uri));
        store.getDocument(uri);
        store.getDocument(uri);
        store.getDocument(uri);
        store.getDocument(uri);
    }

    
}