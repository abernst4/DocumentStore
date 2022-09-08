
package edu.yu.cs.com1320.project.stage5.impl; 
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage5.*;
import edu.yu.cs.com1320.project.stage5.impl.*;
import java.util.*; 
import java.util.stream.*;
import java.util.function.Function;

/*This is the stuff that I need to fix because of piazza:
2)when I kick things out to disk, I need to make sure that I keep the URI command objects in the stack
3)I need to place a delete from disk back onto disk. 
*/

public class DocumentStoreImpl implements DocumentStore{
    BTreeImpl<URI, Document> bt; 
    StackImpl<Undoable> stack; 
    TrieImpl<URI> trie; 
    MinHeapImpl<Nod> heap; 
    int DocSize = 0; 
    int ByteSize = 0; 
    int docs = 0; 
    int bytes = 0; 
    boolean DocLim = false; 
    boolean ByteLim = false; 
    public DocumentStoreImpl(){
        this.bt = new BTreeImpl<>(); 
        this.stack = new StackImpl<>();
        this.trie = new TrieImpl<>(); 
        this.heap = new MinHeapImpl<>(); 
        this.bt.setPersistenceManager(new DocumentPersistenceManager(null));
    }

    public DocumentStoreImpl(File baseDir){
        this.bt = new BTreeImpl<>(); 
        this.stack = new StackImpl<>();
        this.trie = new TrieImpl<>(); 
        this.heap = new MinHeapImpl<>(); 
        this.bt.setPersistenceManager(new DocumentPersistenceManager(baseDir));
    }

    private abstract class Nod implements Comparable<Nod>{
        protected abstract URI getUri();
    }
    private class Node extends Nod{ //WILL YOU TAKE OFF POINTS FOR PUBLIC OVERRIDE METHODS IN THIS INNER CLASS? 
        private URI uri; 
        private Node(URI uri){
            this.uri = uri; 
        }
        
        @Override
        public int compareTo(Nod o) {
            Document a = bt.get(this.getUri());
            Document b = bt.get(o.getUri());
            return a.getLastUseTime()>b.getLastUseTime()?1:b.getLastUseTime()>a.getLastUseTime()?-1:0; //NOT SURE THAT THIS IS THE CORRECT THING
        }
        
        @Override
        protected URI getUri() {
            return this.uri; 
        }

        @Override
        public boolean equals(Object o){
            if(this == o){
                return true; 
            }
            if(o == null){
                return false; 
            }
            if(this.getClass()!= o.getClass()){
                return false; 
            }
            Node nod = (Node)o; 
            return this.getUri().equals(nod.getUri());
        }
    }
    
    public int putDocument(InputStream input, URI uri, DocumentStore.DocumentFormat format) throws IOException{
        if(uri == null){
            throw new IllegalArgumentException(); 
        }
        if(input == null){
            return removeDoc(uri);
        }
        if(format == null){
            throw new IllegalArgumentException(); 
        }
        byte[] bytes = input.readAllBytes(); 
        return putTableAndSome(bytes, uri, format);
    }

    private int putTableAndSome(byte[] bytes, URI uri, DocumentFormat format) throws IOException{
        Document newDoc = format==DocumentFormat.TXT? new DocumentImpl(uri, new String(bytes), null):
            new DocumentImpl(uri, bytes);
        try {
            clearHouse(len(newDoc), 1, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document oldDoc = this.bt.put(uri, newDoc);
        if(format == DocumentFormat.TXT){
            putTxt(newDoc, oldDoc, uri);
        }else{
            putBin(newDoc, oldDoc, uri);
        }
        return func.apply(oldDoc);
    }

    private int len(Document doc){
        return doc.getDocumentTxt()==null?doc.getDocumentBinaryData().length:doc.getDocumentTxt().getBytes().length;
    }

    private void clearHouse(int len, int doc, URI ur) throws Exception{
        if(!InStore(ur)){
            while((ByteLim && bytes+len>ByteSize) || (DocLim && docs +doc>DocSize)){
                Nod nod = this.heap.remove();
                URI uri = nod.getUri();
                Document dok = this.bt.get(uri); 
                this.bt.moveToDisk(uri);
                this.bytes-= len(dok); 
                --this.docs;  
            }    
        }      
    }


    Function<Document, Integer> func = x -> x==null?0:x.hashCode();

    private void putTxt(Document nd, Document od, URI uri){
        putTrie(nd, od, uri);
        putHeap(nd, od, uri);
        this.stack.push(undoPutDoc(nd, od, uri));
    }

    private void putTrie(Document nd, Document od, URI uri){
        if(od != null){
            od.getWords().stream().forEach(x -> this.trie.delete(x, uri));
        }
        if(nd != null){
            nd.getWords().stream().forEach(x -> this.trie.put(x, uri));
        }
    }

    private void putHeap(Document nd, Document od, URI uri){
        if(od!=null){
            od.setLastUseTime(-1);
            this.heap.reHeapify(new Node(uri));
            this.heap.remove();
            --this.docs; 
            this.bytes-= len(od);
        }
        if(nd!=null){
            nd.setLastUseTime(System.nanoTime());
            this.heap.insert(new Node(uri));
            ++this.docs; 
            this.bytes+= len(nd);
        }
    }
    
    private GenericCommand undoPutDoc(Document nd, Document od, URI uri){ //i was getting errors either way; had to use if else
        int len = od == null? 0: len(od);
        int num = od == null? 0:1; 
        Function<URI, Boolean> fun = x -> {
            try {
                clearHouse(len, num, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(nd==null){
                this.bt.put(uri, od); 
                putHeap(od, nd, uri);
            }else{
                putHeap(od, nd, uri);
                this.bt.put(uri, od);
            }
            // I had an error when bt was here; it's because compareTo uses bt
            putTrie(od, nd, uri);
            return true; 
        };
        return new GenericCommand<URI>(uri, fun);
    }

    private void putBin(Document nd, Document od, URI uri){
        putHeap(nd, od, uri);
        this.stack.push(undoPutDoc(nd, od, uri));
    }
    
    @Override
    public Document getDocument(URI uri){
        Document doc = this.bt.get(uri);
        if(doc == null){
            return null; 
        }
        try {
            if(!InStore(uri)){
                clearHouse(len(doc), 1, uri);
                this.heap.insert(new Node(uri));
                ++this.docs;
                this.bytes+=len(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc.setLastUseTime(System.nanoTime());
        this.heap.reHeapify(new Node(uri));
        return doc; 
    }
    
    private boolean InStore(URI uri){
        try{
            this.heap.reHeapify(new Node(uri));
            return true;
        }catch(ArrayIndexOutOfBoundsException e){
            return false; 
        }catch(NullPointerException e){
            return false; 
        }
    }

    @Override
    public boolean deleteDocument(URI uri) {
        return removeDoc(uri)==0? false:true; 
    }

    private int removeDoc(URI uri){
        if(!InStore(uri)){
            return func.apply(undoFromDisk(uri));
        }
        Document doc = this.getDocument(uri);
        if(doc!=null){
            doc.getWords().stream().forEach(x->this.trie.delete(x, uri));
            doc.setLastUseTime(-1);
            this.heap.reHeapify(new Node(uri));
            this.heap.remove();
            --this.docs;
            this.bytes -= len(doc);
            this.bt.put(uri, null);
        }
        this.stack.push(undoPutDoc(null, doc, uri));
        return func.apply(doc);
    }

    private Document undoFromDisk(URI uri){
        Document doc = this.bt.get(uri);
        //I have to remove from trie and bt and then make an undo and then return doc;
        doc.getWords().stream().forEach(x->this.trie.delete(x, uri));
        this.bt.put(uri, null);
        Function<URI,Boolean> fun = x ->{
            bt.put(x, doc);
            try {
                bt.moveToDisk(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            doc.getWords().stream().forEach(y->this.trie.put(y,x));
            return true; 
        };
        this.stack.push(new GenericCommand(uri, fun));
        return doc; 
    }

    @Override
    public void undo() throws IllegalStateException {
        if(this.stack.size() == 0){
            throw new IllegalStateException("stack empty");
        }
        Undoable com = this.stack.pop();
        com.undo();  
    }

    @Override
    public void undo(URI uri) throws IllegalStateException {
        if(this.stack.size()==0){
            throw new IllegalStateException();
        }
        boolean NotFound = true; 
        StackImpl<Undoable> temp = new StackImpl<>();
        while(this.stack.size()!=0 && NotFound){
            Undoable cm = this.stack.pop();
            if(cm instanceof GenericCommand){
                GenericCommand gc = (GenericCommand)cm; 
                NotFound = checkGenCom(gc, uri, temp);
            }else{
                CommandSet cs = (CommandSet)cm;
                NotFound = checkCmSet(cs, uri, temp);
            }
        }
        if(NotFound){
            restack(temp);
            throw new IllegalStateException("command not found");
        }
    }

    private boolean checkGenCom(GenericCommand gc, URI uri, StackImpl<Undoable> temp){
        if(gc.getTarget().equals(uri)){
            gc.undo();
            restack(temp);
            return false; 
        }
        temp.push(gc);
        return true; 
    }

    private void restack(StackImpl<Undoable> temp){
        while(temp.size() != 0){
            this.stack.push(temp.pop());
        }
    }

    private boolean checkCmSet(CommandSet cs, URI uri, StackImpl<Undoable> temp){
        if(!cs.containsTarget(uri)){
            temp.push(cs);
            return true; 
        }
        cs.undo(uri);
        if(cs.size()!=0){
            temp.push(cs);
        }
        restack(temp);
        return false; 
    }

    @Override
    public List<Document> search(String key) { //this doesn't yet take serializeing into account
        List<URI> ulist = this.trie.getAllSorted(key, Com(key.toUpperCase()));
        List<Document> docList = ulist.stream().map(this::getDocument).collect(Collectors.toList());
        return docList; 
    }

    private Comparator<URI> Com(String key){
        return (a,b) -> this.bt.get(a).wordCount(key)>this.bt.get(b).wordCount(key)?-1
            :this.bt.get(b).wordCount(key)>this.bt.get(a).wordCount(key)?1:0; 
    }

    @Override
    public List<Document> searchByPrefix(String key) {
        List<URI> ulist = this.trie.getAllWithPrefixSorted(key, preCom(key.toUpperCase()));
        List<Document> docList = ulist.stream().map(this::getDocument).collect(Collectors.toList()); 
        return docList; 
    }

    private Comparator<URI> preCom(String key){
        return (a,b)->{
            Document doc1 = this.bt.get(a);
            Document doc2 = this.bt.get(b);
            int x =0; 
            int y =0; 
            for(String s: doc1.getWords()){
                if(hasPre(s, key.length(), key)){
                    x+=1; 
                }
            }
            for(String s: doc2.getWords()){
                if(hasPre(s, key.length(), key)){
                    y+=1; 
                }
            }
            return x>y?-1: y>x?1:0; 
        };
    }

    private boolean hasPre(String s, int i, String key){
        try{
            return s.substring(0, i).equals(key);
        }catch(IndexOutOfBoundsException e){
            return false; 
        }
    }

    @Override
    public Set<URI> deleteAll(String key) {
        Set<URI> ulist = this.trie.deleteAll(key);
        mkeUndoSet(ulist);
        deleteFromStore(ulist);
        return ulist; 
    }

    private void deleteFromStore(Set<URI> set){
        for(URI uri: set){
            Document doc = this.bt.get(uri); //might want to change this to this.getDocument(uri)
            doc.setLastUseTime(-1);
            this.heap.reHeapify(new Node(uri));
            this.heap.remove();
            this.bt.put(uri, null);
            --this.docs;
            this.bytes -= len(doc);
            //doc.getWords().stream().forEach(x -> this.trie.delete(x, uri));
        }
    }

    private void mkeUndoSet(Set<URI> set){
        CommandSet cm = new CommandSet<>(); 
        for(URI uri: set){
            cm.addCommand(undoPutDoc(null, this.bt.get(uri), uri));//NOT SURE IF THIS.BT.GET WILL WORK
        }
        this.stack.push(cm);
    }

    @Override
    public Set<URI> deleteAllWithPrefix(String key) {
        Set<URI> set = this.trie.deleteAllWithPrefix(key);
        mkeUndoSet(set);
        deleteFromStore(set);
        return set; 
    }

    @Override
    public void setMaxDocumentCount(int limit) {
        this.DocSize= limit; 
        this.DocLim=true; 
        clearhouse();
    }

    @Override
    public void setMaxDocumentBytes(int limit) {
        this.ByteSize=limit;
        this.ByteLim=true;
        clearhouse();
    }

    private void clearhouse(){
        while((this.docs>this.DocSize&&this.DocLim) || (this.bytes>this.ByteSize&&this.ByteLim)){
            Nod node = this.heap.remove();
            URI uri = node.getUri();
            Document doc = this.bt.get(uri);
            try {
                this.bt.moveToDisk(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            --this.docs; 
            this.bytes -= len(doc);
        }
    }
}

/*
NEED TO ASK IF AFTER DELETING DOC THAT IS SERIALZED, BY DOING UNDO, I SHOULD BRING IT BACK TO MEMORY
OR SERILAZE IT WHEN RETURNING

NEED TO ASK IF THE CUSTOM BASEDIR IS SUPPOSED TO BE CREATED AFTER THE ROOT DIRECTORY 

because of memory management, i'm thinking about switching getBtree to getDocument in the 
comparator, undoPut, and the undoSet


IM STILL UNSURE WHERE TO MAKE THE FILE WHEN PASSED IN A CUSTOM DIR
ALSO, NOT SURE IF TO THROW EXCEPTIONS IF DOC.LEN > BYTEMAX
*/