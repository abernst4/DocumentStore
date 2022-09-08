package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage4.impl.*;
import edu.yu.cs.com1320.project.stage4.*;
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.impl.*;
import java.util.*;
import java.util.stream.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.*;

public class DocumentStoreImpl implements DocumentStore {
    HashTableImpl<URI, Document> table;
    StackImpl<Undoable> stack;
    TrieImpl<Document> trie;
    MinHeapImpl<Document> heap;
    int DocSize = 0;
    int docs = 0;
    int ByteSize = 0;
    int bytes = 0;
    boolean docLim = false;
    boolean byteLim = false;

    public DocumentStoreImpl() {
        this.table = new HashTableImpl<>();
        this.stack = new StackImpl<>();
        this.trie = new TrieImpl<>();
        this.heap = new MinHeapImpl<>();
    }

    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        if (input == null) {
            return removeDoc(uri);
        }
        if (format == null) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = input.readAllBytes();
        return putTableAndSome(bytes, uri, format);
    }

    private int putTableAndSome(byte[] bytes, URI uri, DocumentFormat format) {
        Document newDoc = format == DocumentFormat.TXT ? new DocumentImpl(uri, new String(bytes))
                : new DocumentImpl(uri, bytes);
        clearHouse(newDoc.getDocumentTxt() == null ? newDoc.getDocumentBinaryData().length
                : newDoc.getDocumentTxt().getBytes().length, 1);
        Document oldDoc = this.table.put(uri, newDoc);
        if (format == DocumentFormat.TXT) {
            putTxt(newDoc, oldDoc, uri);
        } else {
            putBin(newDoc, oldDoc, uri);
        }
        return func.apply(oldDoc);
    }

    private void clearHouse(int len, int doc) {
        while ((byteLim && bytes + len > ByteSize) || (docLim && docs + doc > DocSize)) {
            Document dok = this.heap.remove();
            this.table.put(dok.getKey(), null);
            dok.getWords().stream().forEach(x -> this.trie.delete(x, dok));
            clearUndo(dok);
            this.bytes -= dok.getDocumentTxt() == null ? dok.getDocumentBinaryData().length
                    : dok.getDocumentTxt().getBytes().length;
            --this.docs;
        }
    }

    private void clearUndo(Document doc) {
        StackImpl<Undoable> temp = new StackImpl<>();
        while (this.stack.size() != 0) {
            Undoable un = stack.pop();
            if (un instanceof GenericCommand) {
                GenericCommand gn = (GenericCommand) un;
                if (!gn.getTarget().equals(doc.getKey())) {
                    temp.push(gn);
                }
            } else {
                temp.push(un);
            }
        }
        while (temp.size() != 0) {
            this.stack.push(temp.pop());
        }
    }

    private void putTxt(Document nD, Document oD, URI uri) {
        putTrie(nD, oD, uri);
        putHeap(nD, oD, uri);
        this.stack.push(undoPutDoc(nD, oD, uri));
    }

    private void putTrie(Document nD, Document oD, URI uri) {
        if (oD != null) {
            oD.getWords().stream().forEach(x -> this.trie.delete(x, oD));
        }
        if (nD != null) {
            nD.getWords().stream().forEach(x -> this.trie.put(x, nD));
        }
    }

    private void putHeap(Document nD, Document oD, URI uri) {
        if (oD != null) {
            oD.setLastUseTime(-1);
            this.heap.reHeapify(oD);
            this.heap.remove();
            --this.docs;
            this.bytes -= oD.getDocumentTxt() == null ? oD.getDocumentBinaryData().length
                    : oD.getDocumentTxt().getBytes().length;
        }
        if (nD != null) {
            nD.setLastUseTime(System.nanoTime());
            this.heap.insert(nD);
            ++this.docs;
            bytes += nD.getDocumentTxt() == null ? nD.getDocumentBinaryData().length
                    : nD.getDocumentTxt().getBytes().length;
        }
    }

    private GenericCommand undoPutDoc(Document nD, Document oD, URI uri) {
        int len = oD == null ? 0
                : oD.getDocumentTxt() == null ? oD.getDocumentBinaryData().length
                        : oD.getDocumentTxt().getBytes().length;
        int num = oD == null ? 0 : 1;
        Function<URI, Boolean> fun = x -> {
            clearHouse(len, num);
            this.table.put(uri, oD);
            putTrie(oD, nD, uri);
            putHeap(oD, nD, uri);
            return true;
        };
        return new GenericCommand(uri, fun);
    }

    Function<Document, Integer> func = x -> x == null ? 0 : x.hashCode();

    private void putBin(Document nD, Document oD, URI uri) {
        putHeap(nD, oD, uri);
        this.stack.push(undoPutDoc(nD, oD, uri));
    }

    @Override
    public Document getDocument(URI uri) {
        Document doc = this.table.get(uri);
        if (doc != null) {
            doc.setLastUseTime(System.nanoTime());
            this.heap.reHeapify(doc);
        }
        return doc;
    }

    @Override
    public boolean deleteDocument(URI uri) {
        return removeDoc(uri) == 0 ? false : true;
    }

    private int removeDoc(URI uri) {
        Document doc = this.table.put(uri, null);
        if (doc != null) {
            doc.getWords().stream().forEach(x -> this.trie.delete(x, doc));
            doc.setLastUseTime(-1);
            this.heap.reHeapify(doc);
            this.heap.remove();
            --this.docs;
            this.bytes -= doc.getDocumentTxt() == null ? doc.getDocumentBinaryData().length
                    : doc.getDocumentTxt().getBytes().length;
        }
        this.stack.push(undoPutDoc(null, doc, uri));
        return func.apply(doc);
    }

    public void undo() throws IllegalStateException {
        if (this.stack.size() == 0) {
            throw new IllegalStateException("stack empty");
        }
        Undoable com = stack.pop();
        com.undo();
    }

    public void undo(URI uri) throws IllegalStateException {
        if (this.stack.size() == 0) {
            throw new IllegalStateException("Stack empty");
        }
        boolean NotFound = true;
        Undoable com;
        StackImpl<Undoable> temp = new StackImpl<>();
        while (this.stack.size() != 0 && NotFound) {
            com = this.stack.pop();
            if (com instanceof GenericCommand) {
                GenericCommand gc = (GenericCommand) com;
                NotFound = checkCom(gc, uri, temp);
            } else {
                CommandSet cs = (CommandSet) com;
                NotFound = checkComSet(cs, uri, temp);
            }
        }
        if (NotFound) {
            restack(temp);
            throw new IllegalStateException("Uri not here");
        }
    }

    private boolean checkCom(GenericCommand com, URI uri, StackImpl<Undoable> temp) {
        if (com.getTarget().equals(uri)) {
            com.undo();
            restack(temp);
            return false;
        }
        temp.push(com);
        return true;
    }

    private boolean checkComSet(CommandSet cs, URI uri, StackImpl<Undoable> temp) {
        if (!cs.containsTarget(uri)) {
            temp.push(cs);
            return true;
        }
        cs.undo(uri);
        if (cs.size() != 0) {
            temp.push(cs);
        }
        restack(temp);
        return false;
    }

    private void restack(StackImpl<Undoable> temp) {
        while (temp.size() != 0) {
            this.stack.push(temp.pop());
        }
    }

    public List<Document> search(String key) {
        List<Document> list = this.trie.getAllSorted(key, srchCom(key.toUpperCase()));
        list.stream().forEach(i -> {
            i.setLastUseTime(System.nanoTime());
            this.heap.reHeapify(i);
        });
        return list;
    }

    private Comparator<Document> srchCom(String key) {
        return (a, b) -> a.wordCount(key) > b.wordCount(key) ? -1 : a.wordCount(key) < b.wordCount(key) ? 1 : 0;
    }

    public List<Document> searchByPrefix(String pre) {
        List<Document> list = this.trie.getAllWithPrefixSorted(pre, srchPreCom(pre.toUpperCase()));
        list.stream().forEach(i -> {
            i.setLastUseTime(System.nanoTime());
            this.heap.reHeapify(i);
        });
        return list;
    }

    private Comparator<Document> srchPreCom(String p) {
        return (a, b) -> {
            int x = 0;
            int y = 0;
            for (String s : a.getWords()) {
                if (hasPre(s, p.length(), p)) {
                    x += 1;
                }
            }
            for (String s : b.getWords()) {
                if (hasPre(s, p.length(), p)) {
                    y += 1;
                }
            }
            return x > y ? -1 : x < y ? 1 : 0;
        };
    }

    private boolean hasPre(String s, int i, String p) {
        try {
            return s.substring(0, i).equals(p);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Set<URI> deleteAll(String keyword) {
        Set<Document> set = this.trie.deleteAll(keyword);
        deleteFromStore(set);
        mkUndoSet(set);
        return set.stream().map(x -> x.getKey()).collect(Collectors.toSet());
    }

    private void deleteFromStore(Set<Document> set) {
        for (Document doc : set) {
            this.table.put(doc.getKey(), null);
            doc.getWords().stream().forEach(x -> this.trie.delete(x, doc));
            doc.setLastUseTime(-1);
            this.heap.reHeapify(doc);
            this.heap.remove();
            --this.docs;
            this.bytes -= doc.getDocumentTxt() == null ? doc.getDocumentBinaryData().length
                    : doc.getDocumentTxt().getBytes().length;
        }
    }

    private void mkUndoSet(Set<Document> set) {
        CommandSet cm = new CommandSet<>();
        for (Document doc : set) {
            cm.addCommand(undoPutDoc(null, doc, doc.getKey()));
        }
        this.stack.push(cm);
    }

    public Set<URI> deleteAllWithPrefix(String prefix) {
        Set<Document> set = this.trie.deleteAllWithPrefix(prefix);
        deleteFromStore(set);
        mkUndoSet(set);
        return set.stream().map(x -> x.getKey()).collect(Collectors.toSet());
    }

    public void setMaxDocumentCount(int limit) {
        this.DocSize = limit;
        this.docLim = true;
        cleanHouse();
    }

    public void setMaxDocumentBytes(int limit) {
        this.ByteSize = limit;
        this.byteLim = true;
        cleanHouse();
    }

    private void cleanHouse() {
        while ((docs > DocSize && this.docLim) || (bytes > ByteSize && this.byteLim)) {
            Document doc = this.heap.remove();
            this.table.put(doc.getKey(), null);
            doc.getWords().stream().forEach(x -> this.trie.delete(x, doc));
            clearUndo(doc);
            --this.docs;
            this.bytes -= doc.getDocumentTxt() == null ? doc.getDocumentBinaryData().length
                    : doc.getDocumentTxt().getBytes().length;
        }
    }
}