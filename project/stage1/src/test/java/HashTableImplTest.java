import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException; 

public class HashTableImplTest{
    @Test
    public void getWithReplace(){
        HashTableImpl<Integer, String> ht = new HashTableImpl<>(); 
        assertEquals(null, ht.get(1)); //or null if there is no such key in the table
        assertEquals(null, ht.put(1, "a")); //If the key was not already present, return null.
        assertEquals("a", ht.get(1)); // value that is stored in the HashTable for k
        assertEquals("a", ht.put(1, "b"));//if the key was already present in the HashTable, return the previous value stored for the key
        assertEquals("b", ht.get(1));
    }

    @Test
    public void getNodeAfterFirstOneWASReplaced(){
        HashTableImpl<Integer, String> ht = new HashTableImpl<>(); 
        ht.put(1, "a");
        ht.put(2, "f");
        assertEquals("f", ht.get(2));
        ht.put(1, "b");
        assertEquals("f", ht.get(2));
    }

    @Test
    public void deleteTest(){
        HashTableImpl<Integer, String> ht = new HashTableImpl<>(); 
        ht.put(1, "a");
        assertEquals("a", ht.put(1, null));
        assertEquals(null, ht.get(1));
        assertEquals(null, ht.put(4, null));
        assertEquals(null, ht.get(4));

        //now to do the same thing at the end of a linkedList
        HashTableImpl<Integer, String> ht1 = new HashTableImpl<>(); 
        ht1.put(1, "a");
        ht1.put(2, "f");
        assertEquals("f", ht1.put(2, null));
        assertEquals(null, ht1.get(2));
        assertEquals(null, ht1.put(2, "f"));
        assertEquals("f", ht1.get(2));
    }


}