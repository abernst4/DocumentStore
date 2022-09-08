package edu.yu.cs.com1320.project.stage3.impl; 
import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException; 
import java.util.*; 

public class DocumentImplTest{
    @Test
    public void getWordCont() throws URISyntaxException{
        URI uri = new URI("www.hello.org");
        String str = "The place in T4 76 76 76 76 france was the place in in * () !!";
        Document doc = new DocumentImpl(uri, str);
        assertEquals(3, doc.wordCount("in"));
        assertEquals(2, doc.wordCount("place"));
        assertEquals(2, doc.wordCount("the"));
        assertEquals(0, doc.wordCount("Paris"));
        assertEquals(0, doc.wordCount("*"));
        assertEquals(4, doc.wordCount("76"));
        assertEquals(1, doc.wordCount("t4"));
    }

    @Test
    public void WordSetTest() throws URISyntaxException{ 
        URI uri = new URI("www.hello.org");
        String str = "The pla(ce in T4 7!6 76 76 76 fra*nce was the place in in * () !!";
        Document doc = new DocumentImpl(uri, str);
        Set<String> a = doc.getWords(); 
        Set<String> temp = new HashSet<>(); 
        temp.add("THE");
        temp.add("PLACE");
        temp.add("IN");
        temp.add("T4");
        temp.add("76");
        temp.add("FRANCE");
        temp.add("WAS");
        assertEquals(temp, a);
        assertEquals(str, doc.getDocumentTxt());

    }

    @Test void fake() throws URISyntaxException{
        URI a = new URI("a");
        URI b = new URI("a");
        assertEquals(true, a.equals(b));
    }
}