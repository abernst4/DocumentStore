import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.impl.DocumentImpl; 
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.DocumentStore.DocumentFormat;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException; 

public class Stage1Test {
    @Test
    public void test1() throws URISyntaxException {
       String string = "https://www.geeksforgeeks.org/";
        URI uri = new URI(string);
        Document doc = new DocumentImpl( uri, "hello"); 
        assertEquals(doc.getDocumentTxt(), "hello", "well, it was worth a shot");
    }

    @Test
    public void test2() throws URISyntaxException {
       String string = "https://www.geeksforgeeks.org/";
     // final  
      URI uri = new URI(string);
        Document doc = new DocumentImpl( uri, "hello"); 
        assertEquals(doc.getKey(), uri, "well, it was worth a shot");
    }

    @Test
    public void test3() throws URISyntaxException {
       String string = "https://www.geeksforgeeks.org/";
        URI uri = new URI(string);
       assertThrows(IllegalArgumentException.class,()->{
        Document doc = new DocumentImpl( uri, "");
        });
    }

    @Test 
    public void DocumentImplputDoc() throws URISyntaxException, IOException {
        //tests whether a simple put returns 0 and whether a delete there returns the hashCdoe of the
        //deleted object
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore doc = new DocumentStoreImpl(); 
        assertEquals(doc.putDocument(i, uri, DocumentFormat.TXT), 0);
        assertEquals(doc.getDocument(uri).hashCode(), doc.putDocument(null, uri, DocumentFormat.TXT));
    }

    @Test
    public void DocStoreImplTest()throws URISyntaxException, IOException{
        //I want to test getting a uri not there returning null
        //Tested URI getting the Strin doc that I want
        //tested a different URI ojbect with the same content and returns the same object. 
        //
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world");
        assertEquals(docStore.putDocument(i, uri, DocumentFormat.TXT), 0);
        assertEquals(doc, docStore.getDocument(uri));
        URI ur2 = new URI("hello.org");
        assertEquals(null, docStore.getDocument(ur2));
        URI ur3 = new URI("hi.org");
        assertEquals(doc, docStore.getDocument(ur3));
        assertEquals(true, docStore.deleteDocument(uri));
        assertEquals(null, docStore.getDocument(uri));
        assertEquals(false, docStore.deleteDocument(ur3));
    }

    @Test
    public void documentStorePutDoc() throws URISyntaxException, IOException{
        //there should 4 different options: replace and get 0 and hashcode and null 0 and hashcode
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world");
        assertEquals(0, docStore.putDocument(i, uri, DocumentFormat.TXT));
        i = new ByteArrayInputStream("hello planet earth".getBytes());
        assertEquals(doc.hashCode(), docStore.putDocument(i, uri, DocumentFormat.TXT));

    }

    @Test
    public void documentStorePutDocwithNull() throws URISyntaxException, IOException{
        //there should 4 different options: replace and get 0 and hashcode and null 0 and hashcode
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world");
        assertEquals(0, docStore.putDocument(i, uri, DocumentFormat.TXT));
        i = new ByteArrayInputStream("hello planet earth".getBytes());
        assertEquals(doc.hashCode(), docStore.putDocument(null, uri, DocumentFormat.TXT));
        assertEquals(0, docStore.putDocument(null, uri, DocumentFormat.TXT));
    }

    @Test
    public void TestBrudersDelete() throws URISyntaxException, IOException{
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world");
        assertEquals(0, docStore.putDocument(i, uri, DocumentFormat.TXT));
        assertEquals(doc.hashCode(), docStore.putDocument(null, uri, null));
    }

    @Test
    public void TestDocumentArguements() throws URISyntaxException{
        URI uri = new URI("yo");
        String str = "hello world"; 
        Document doc = new DocumentImpl(uri, str);
        assertEquals(uri, doc.getKey());
        assertEquals(str, doc.getDocumentTxt());
    }    
    //NOW TEST ABOVE IN BINARY

    @Test 
    public void DocumentImplputDocBinary() throws URISyntaxException, IOException {
        //tests whether a simple put returns 0 and whether a delete there returns the hashCdoe of the
        //deleted object
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore doc = new DocumentStoreImpl(); 
        assertEquals(doc.putDocument(i, uri, DocumentFormat.BINARY), 0);
        assertEquals(doc.getDocument(uri).hashCode(), doc.putDocument(null, uri, DocumentFormat.BINARY));
    }

    @Test
    public void DocStoreImplTestB()throws URISyntaxException, IOException{
        //I want to test getting a uri not there returning null
        //Tested URI getting the Strin doc that I want
        //tested a different URI ojbect with the same content and returns the same object. 
        //
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world".getBytes());
        assertEquals(docStore.putDocument(i, uri, DocumentFormat.BINARY), 0);
        assertEquals(doc, docStore.getDocument(uri));
        URI ur2 = new URI("hello.org");
        assertEquals(null, docStore.getDocument(ur2));
        URI ur3 = new URI("hi.org");
        assertEquals(doc, docStore.getDocument(ur3));
        assertEquals(true, docStore.deleteDocument(uri));
        assertEquals(null, docStore.getDocument(uri));
        assertEquals(false, docStore.deleteDocument(ur3));
    }

    @Test
    public void documentStorePutDocB() throws URISyntaxException, IOException{
        //there should 4 different options: replace and get 0 and hashcode and null 0 and hashcode
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world".getBytes());
        assertEquals(0, docStore.putDocument(i, uri, DocumentFormat.BINARY));
        i = new ByteArrayInputStream("hello planet earth".getBytes());
        assertEquals(doc.hashCode(), docStore.putDocument(i, uri, DocumentFormat.BINARY));

    }

    @Test
    public void documentStorePutDocwithNullB() throws URISyntaxException, IOException{
        //there should 4 different options: replace and get 0 and hashcode and null 0 and hashcode
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world".getBytes());
        assertEquals(0, docStore.putDocument(i, uri, DocumentFormat.BINARY));
        i = new ByteArrayInputStream("hello planet earth".getBytes());
        assertEquals(doc.hashCode(), docStore.putDocument(null, uri, DocumentFormat.BINARY));
        assertEquals(0, docStore.putDocument(null, uri, DocumentFormat.BINARY));
    }

    @Test
    public void TestBrudersDeleteB() throws URISyntaxException, IOException{
        InputStream i = new ByteArrayInputStream("hello world".getBytes());
        URI uri = new URI("hi.org"); 
        DocumentStore docStore = new DocumentStoreImpl(); 
        Document doc = new DocumentImpl(uri, "hello world".getBytes());
        assertEquals(0, docStore.putDocument(i, uri, DocumentFormat.BINARY));
        assertEquals(doc.hashCode(), docStore.putDocument(null, uri, null));
    }

    @Test
    public void TestNearlyAllExceptions() throws URISyntaxException{
        String string = "https://www.geeksforgeeks.org/";
        byte[] b = new byte[0];
         URI uri = new URI(string);
         URI blank = new URI("");
         assertThrows(IllegalArgumentException.class,()->{
            Document doc = new DocumentImpl( uri, "");
            });
        assertThrows(IllegalArgumentException.class, ()->{
            DocumentImpl doc1 = new DocumentImpl(uri, (String)null); //how am I supposed to test this? 
            });
        assertThrows(IllegalArgumentException.class, ()->{
            Document doc2 = new DocumentImpl(blank, string);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Document doc3 = new DocumentImpl(null, string);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Document doc4 = new DocumentImpl(uri, (byte[])null);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            Document doc5 = new DocumentImpl(uri, b);
        });

    }

}
