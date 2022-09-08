package edu.yu.cs.com1320.project.impl; 
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.Trie;
import org.junit.jupiter.api.Test; 
import java.util.*; 
import static org.junit.jupiter.api.Assertions.assertEquals; 


public class TrieImplTest{
    @Test
    public void simplePutTest(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        trie.put("a", 3); 
        List<Integer> s = new ArrayList<>(); 
        s.add(3); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        assertEquals(s, trie.getAllSorted("a", com));
        assertEquals(s, trie.getAllSorted("A", com));
    }

    @Test
    public void testLongPut(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0;
        trie.put("abcdef", 36);
        List<Integer> s = new ArrayList<>(); 
        assertEquals(s, trie.getAllSorted("a", com));
        assertEquals(s, trie.getAllSorted("ab", com));
        assertEquals(s, trie.getAllSorted("abc", com));
        assertEquals(s, trie.getAllSorted("abcd", com));
        assertEquals(s, trie.getAllSorted("abcde", com));
        s.add(36);
        assertEquals(s, trie.getAllSorted("abcdef", com));
    }
/*
    @Test
    public void testHISTrie(){
        TooSimpleTrie<Integer> tri = new TooSimpleTrie<>();
        tri.put("a",3);
        List<Integer> s = new ArrayList<>(); 
        s.add(3); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        assertEquals(s, tri.getAllSorted("a"));
    }
*/
    @Test
    public void testPref(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        trie.put("a", 3); 
        trie.put("ab", 6);
        trie.put("ab3", 8);
        List<Integer> s = new ArrayList<>(); 
        s.add(8); 
        s.add(6);
        s.add(3);
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; //this is my comparator for documentStore
        assertEquals(s, trie.getAllWithPrefixSorted("a", com));
        assertEquals(s, trie.getAllWithPrefixSorted("A", com));
    }

    @Test
    public void numberPutTest(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        trie.put("3", 3); 
        List<Integer> s = new ArrayList<>(); 
        s.add(3); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        assertEquals(s, trie.getAllSorted("3", com));
    }

    @Test
    public void testGetAllSorted(){
        TrieImpl<Integer> trie = new TrieImpl<>();
        trie.put("a", 5);
        trie.put("a", 7);
        trie.put("a", 3);
        List<Integer> s = new ArrayList<>(); 
        s.add(7); 
        s.add(5); 
        s.add(3); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        assertEquals(s, trie.getAllSorted("a", com));
    }

    @Test
    public void testDeleteAllForONE(){
        TrieImpl<Integer> trie = new TrieImpl<>();
        trie.put("a", 5);
        trie.put("a", 7);
        trie.put("a", 3);
        List<Integer> s = new ArrayList<>(); 
        s.add(7); 
        s.add(5); 
        s.add(3); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        assertEquals(s, trie.getAllSorted("a", com));
        trie.put("ab", 5);
        trie.put("ab", 7);
        trie.put("ab", 3);
        Set<Integer> set = new HashSet<>();
        set.add(7);
        set.add(5);
        set.add(3);
        assertEquals(set, trie.deleteAll("a"));
        assertEquals(s, trie.getAllSorted("ab", com));
        List<Integer> temp = new ArrayList<>();
        assertEquals(temp, trie.getAllSorted("a", com));
    }

    @Test
    public void testDeleteAllForTwo(){
        TrieImpl<Integer> trie = new TrieImpl<>();
        trie.put("0", 5);
        trie.put("0", 7);
        trie.put("0", 3);
        List<Integer> s = new ArrayList<>(); 
        s.add(7); 
        s.add(5); 
        s.add(3); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        assertEquals(s, trie.getAllSorted("0", com));
        trie.put("0z", 5);
        trie.put("0z", 7);
        trie.put("0z", 3);
        Set<Integer> set = new HashSet<>();
        set.add(7);
        set.add(5);
        set.add(3);
        assertEquals(set, trie.deleteAll("0z"));
        assertEquals(s, trie.getAllSorted("0", com));
        List<Integer> temp = new ArrayList<>();
        assertEquals(temp, trie.getAllSorted("0z", com));
    }

    @Test
    public void testDeleteAllFORTHREEVALUES(){
        TrieImpl<Integer> trie = new TrieImpl<>();
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("0", 5);
        trie.put("0", 7);
        trie.put("0z", 9);
        trie.put("0Z", 2);
        trie.put("0z5", 1);
        trie.put("0Z5", 28);
        Set<Integer> set = new HashSet<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        assertEquals(list1, trie.getAllSorted("a", com));
        assertEquals(set, trie.deleteAll("a"));
        set.add(9);
        set.add(2);
        assertEquals(set, trie.deleteAll("0z"));
        list1.add(7);
        list1.add(5);
        list2.add(28);
        list2.add(1);
        assertEquals(list1, trie.getAllSorted("0", com));
        assertEquals(list2, trie.getAllSorted("0z5", com));
        set.clear();
        set.add(7);
        set.add(5);
        assertEquals(set, trie.deleteAll("0"));
        list1.clear();
        assertEquals(list1, trie.getAllSorted("0", com));
        assertEquals(list1, trie.getAllSorted("0z", com));
        assertEquals(list2, trie.getAllSorted("0z5", com));        
    }

    @Test
    public void testDeletePref(){
        TrieImpl<Integer> trie = new TrieImpl<>();
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("0", 5);
        trie.put("0", 7);
        trie.put("0z", 9);
        trie.put("0Z", 2);
        trie.put("0z5", 1);
        trie.put("0Z5", 28);
        Set<Integer> set = new HashSet<>();
        set.add(28);
        set.add(1);
        set.add(2);
        set.add(9);
        assertEquals(set, trie.deleteAllWithPrefix("0z"));
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(7);
        list1.add(5);
        assertEquals(list1, trie.getAllSorted("0", com));
        assertEquals(list2, trie.getAllSorted("0z", com));
        assertEquals(list2, trie.getAllSorted("0Z5", com));
        set.clear();
        set.add(5);
        set.add(7);
        assertEquals(set, trie.deleteAllWithPrefix("0"));
        assertEquals(list2, trie.getAllSorted("0", com));
    }
    
    @Test
    public void testDeletePref2(){
        TrieImpl<Integer> trie = new TrieImpl<>();
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("0", 5);
        trie.put("0", 7);
        trie.put("0z", 9);
        trie.put("0Z", 2);
        trie.put("0z5", 1);
        trie.put("0Z5", 28);
        trie.put("Z5", 8);
        trie.put("Z5", 28);
        List<Integer> list1 = new ArrayList<>();
        list1.add(28);
        list1.add(9);
        list1.add(7);
        list1.add(5);
        list1.add(2);
        list1.add(1);
        assertEquals(list1, trie.getAllWithPrefixSorted("0", com));
        list1.clear();
        list1.add(28);
        list1.add(8);
        assertEquals(list1, trie.getAllWithPrefixSorted("z", com));
        assertEquals(new ArrayList<Integer>(), trie.getAllSorted("z", com));
        Set<Integer> set = new HashSet<>();
        set.add(28);
        set.add(1);
        set.add(2);
        set.add(9);
        set.add(7);
        set.add(5);
        assertEquals(set, trie.deleteAllWithPrefix("0"));
        assertEquals(new ArrayList<Integer>(), trie.getAllSorted("0", com));
        assertEquals(new ArrayList<Integer>(), trie.getAllSorted("0z", com));
        assertEquals(new ArrayList<Integer>(), trie.getAllSorted("0z5", com));
    }



    @Test
    public void testDeleteSingleValue(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("0", 5);
        trie.put("0", 7);
        assertEquals(7, trie.delete("0", 7));
        List<Integer> list1 = new ArrayList<>();
        list1.add(5);
        assertEquals(list1, trie.getAllSorted("0", com));
        assertEquals(null, trie.delete("z", 7));
        assertEquals(5, trie.delete("0", 5));
    }

    @Test
    public void testSingleDeleteNothingThere(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        assertEquals(null, trie.delete("a", 7));
    }

    @Test
    public void testSDwithDifferentValues(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("z", 5);
        trie.put("z0", 7);
        trie.put("z0a", 78);
        List<Integer> list1 = new ArrayList<>();
        list1.add(78);
        list1.add(7);
        list1.add(5);
        assertEquals(list1, trie.getAllWithPrefixSorted("z", com));
        assertEquals(5, trie.delete("z", 5));
        list1.remove(2);
        assertEquals(list1, trie.getAllWithPrefixSorted("z", com));
        assertEquals(7, trie.delete("z0", 7));
        list1.remove(1);
        assertEquals(list1, trie.getAllWithPrefixSorted("z", com));
        assertEquals(78, trie.delete("z0a", 78));
        list1.remove(0);
        assertEquals(list1, trie.getAllWithPrefixSorted("z", com));
    }

    @Test
    public void testSameValueDelete(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("y", 5);
        trie.put("x", 5);
        trie.put("yc", 5);
        List<Integer> list1 = new ArrayList<>();
        list1.add(5);
        assertEquals(5, trie.delete("y", 5));
        assertEquals(list1, trie.getAllSorted("x", com));
        assertEquals(list1, trie.getAllSorted("yc", com));
    }

    @Test
    public void testDeleteback(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        trie.put("y", 5);
        trie.put("yc", 5); 
        List<Integer> list1 = new ArrayList<>();
        list1.add(5);
        assertEquals(list1, trie.getAllWithPrefixSorted("y", com));
        Set<Integer> set = new HashSet<>();
        set.add(5);
        assertEquals(set, trie.deleteAll("y"));
        assertEquals(list1, trie.getAllSorted("yc", com));
    }

    @Test
    public void testAllKeyPut(){
        TrieImpl<Integer> trie = new TrieImpl<>(); 
        Comparator<Integer> com = (a,b) -> a > b? -1: a < b? 1: 0; 
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        for(int i = 0; i < 11; ++i){
            for(char c = 'a'; c <= 'z'; ++c){
                String str = Integer.toString(i) + Character.toString(c);
                trie.put(str, 1);
            }
        }
        for(int i = 0; i < 11; ++i){
            for(char c = 'a'; c <= 'z'; ++c){
                String str = Integer.toString(i) + Character.toString(c);
                assertEquals(list1, trie.getAllSorted(str, com));
            }
        }

        for(int i = 0; i < 11; ++i){
            for(char c = 'a'; c <= 'z'; ++c){
                    String str = Integer.toString(i) + Character.toString(c);
                    trie.put(str, 5);
            }
        }
        list1.clear();
        list1.add(5);
        list1.add(1);
        for(int i = 0; i < 11; ++i){
            for(char c = 'a'; c <= 'z'; ++c){
                String str = Integer.toString(i) + Character.toString(c);
                assertEquals(list1, trie.getAllSorted(str, com));
            }
        }

    }
    
}