package edu.yu.cs.com1320.project.stage3.impl; 
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage3.*;
import java.net.URI;
import java.util.Set;
import java.util.List;
import java.util.*;
import java.util.stream.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.*;

public class DocumentStoreImpl implements DocumentStore{
   HashTableImpl<URI, Document> table;
   StackImpl<Undoable> stack; 
   TrieImpl<Document> trie;  
    public DocumentStoreImpl(){
        this.table = new HashTableImpl<>();
        this.stack = new StackImpl<>();
        this.trie = new TrieImpl<>();
    }

    private Function<Document, Integer> func = x -> x == null? 0: x.hashCode();
    private Comparator<Document> comp(String word){
        return (a,b) -> a.wordCount(word) > b.wordCount(word)? -1: a.wordCount(word) < b.wordCount(word)?1:0; 
    } 

    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException{
        if(uri == null){
            throw new IllegalArgumentException("input can't be null");
        }
        if(input == null){
            return removeDocument(uri);
        }
        if(format == null){
            throw new IllegalArgumentException("format can't be null");
        }
        byte[] bytes = input.readAllBytes();
        return putDocument(bytes, uri, format);
    }

    private int putDocument(byte[] bytes, URI uri, DocumentFormat format){ 
        Document doc = format == DocumentFormat.TXT? new DocumentImpl(uri, new String(bytes)): new DocumentImpl(uri, bytes);
        Document temp = this.table.put(uri, doc);
        if(format == DocumentFormat.TXT){
            putTxt(doc, temp, uri);
        }else{
            undoBin(temp, uri);
        }
        return func.apply(temp);
    }

    private void undoBin(Document temp, URI uri){ 
        Function<URI,Boolean> func = x -> {
            this.table.put(uri, temp);
            return true;
        };
        this.stack.push(new GenericCommand(uri, func));
    }

    private void putTxt(Document doc, Document temp, URI uri){
        doc.getWords().stream().forEach(x -> this.trie.put(x, doc));
        undoPutTXT(temp, uri, doc); 
        if(temp != null){
            temp.getWords().stream().forEach(s -> this.trie.delete(s, temp));
        }
    }

    private void undoPutTXT(Document old, URI uri, Document newd){
        Function<URI,Boolean> func = x -> {
            this.table.put(uri, old);
            newd.getWords().stream().forEach(y -> this.trie.delete(y, newd));
            if(old != null){
                old.getWords().stream().forEach(z -> this.trie.put(z, old));
            }
            return true; 
        };
        this.stack.push(new GenericCommand(uri, func));
    }
    
    private int removeDocument(URI uri){
        Document doc = this.getDocument(uri);
        if(doc == null){
            return 0; 
        }
        return doc.getDocumentTxt()== null? removeBdoc(doc, uri): removeTxtDoc(doc, uri);
    }

    private int removeBdoc(Document doc, URI uri){
        Document temp = this.table.put(uri, null);
        Function<URI, Boolean> fun = x -> {
            this.table.put(uri, doc);
            return true; 
        };
        this.stack.push(new GenericCommand(uri, fun));
        return func.apply(temp);
    }

    private int removeTxtDoc(Document doc, URI uri){
        doc.getWords().stream().forEach(x -> this.trie.delete(x, doc));
        Document temp = this.table.put(uri, null);
        this.stack.push(undoRemove(temp, uri));
        return func.apply(temp);
    }

    private GenericCommand undoRemove(Document doc, URI uri){
        Function<URI, Boolean> func = x ->{
            this.table.put(uri, doc);
            doc.getWords().stream().forEach( y -> this.trie.put(y, doc));
            return true; 
        }; 
       return new GenericCommand(uri, func);
    }

    public Document getDocument(URI uri){
        return this.table.get(uri); 
    }

    public boolean deleteDocument(URI uri){ 
        return this.removeDocument(uri) == 0? false:true; 
    }    

    public void undo() throws IllegalStateException{
        if(stack.size() == 0){
            throw new IllegalStateException("stack is empty");
        }
        Undoable com = stack.pop();
        com.undo(); 
    }

    public void undo(URI uri)throws IllegalStateException{ 
        if(this.stack.size() == 0){
            throw new IllegalStateException("stack is empty");
        }
        Undoable com = null; 
        boolean notFound = true; 
        StackImpl<Undoable> temp = new StackImpl<>();
        while(stack.size() != 0 && notFound){
            com = stack.pop();
            if(com instanceof CommandSet){
                CommandSet Com = (CommandSet)com; 
                notFound = checkCmSet(Com, temp, uri);
            }else{
                GenericCommand Com = (GenericCommand)com; 
                notFound = checkCm(Com, temp, uri);
            }
        }
        if(notFound){
            restack(temp);
            throw new IllegalStateException("not on stack");
        }   
    }
    
    private boolean checkCmSet(CommandSet com, StackImpl<Undoable> temp, URI uri){
        if(!com.containsTarget(uri)){
            temp.push(com);
            return true; 
        } 
        com.undo(uri);
        if(com.size() != 0){
            temp.push(com);
        }
        restack(temp);
        return false; 
    }

    private boolean checkCm(GenericCommand com, StackImpl<Undoable> temp, URI uri){
        if(com.getTarget() == uri){
            restack(temp);
            com.undo();
            return false; 
        }
        temp.push(com);
        return true; 
    }

    private void restack(StackImpl<Undoable> temp){
        while(temp.size() != 0){
            stack.push(temp.pop());
        }
    }

    public List<Document> search(String keyword){
        return this.trie.getAllSorted(keyword, comp(keyword.toUpperCase()));
    }

    public List<Document> searchByPrefix(String keywordPrefix){
        return this.trie.getAllWithPrefixSorted(keywordPrefix, compPre(keywordPrefix.toUpperCase()));
    }

    private Comparator<Document> compPre(String pre){
        return (a,b) -> {
            int x = 0; 
            int y = 0; 
            for(String s: a.getWords()){
                if(hasPre(s, pre.length(), pre)){
                    x += 1; 
                }
            }
            for(String s: b.getWords()){
                if(hasPre(s, pre.length(), pre)){
                    y+= 1; 
                }
            }
            return x > y? -1: x< y? 1: 0; 
        };
    }

    private boolean hasPre(String str, int i, String pre){
        try{
            return str.substring(0, i).equals(pre);  
        }catch(IndexOutOfBoundsException e){
            return false; 
        }
    }

    public Set<URI> deleteAll(String keyword){
        Set<Document> set = this.trie.deleteAll(keyword);
        deleteFromStore(set);
        makeUndoSet(set);
        return set.stream().map(x -> x.getKey()).collect(Collectors.toSet());
    }

    private void makeUndoSet(Set<Document> set){
        CommandSet<Undoable> cms = new CommandSet<>();
        for(Document doc: set){
            cms.addCommand(undoRemove(doc, doc.getKey()));
        }
        this.stack.push(cms);
    }

    private void deleteFromStore(Set<Document> set){
        for(Document doc: set){
            doc.getWords().stream().forEach(x -> this.trie.delete(x, doc));
            this.table.put(doc.getKey(), null);
        }
    }
    public Set<URI> deleteAllWithPrefix(String keywordPrefix){
        Set<Document> set = this.trie.deleteAllWithPrefix(keywordPrefix);
        deleteFromStore(set);
        makeUndoSet(set);
        return set.stream().map(x -> x.getKey()).collect(Collectors.toSet());
    }
} 
