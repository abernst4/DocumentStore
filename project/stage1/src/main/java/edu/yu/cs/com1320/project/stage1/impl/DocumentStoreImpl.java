package edu.yu.cs.com1320.project.stage1.impl; //need to check if this works
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl implements DocumentStore{
    HashTableImpl<URI, Document> hashTable; 

    public DocumentStoreImpl(){
        this.hashTable = new HashTableImpl<>();
    }

    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException { //is this proper syntax? 
        if(uri == null){
            throw new IllegalArgumentException("you can't have the uri be null");
        } 
        if(input == null){
            return removeDocument(uri); 
        }
        if(format == null){
            throw new IllegalArgumentException("fromat can't be null");
        }
        byte[] bytes = input.readAllBytes();
        return putDocument(uri, format, bytes);
    }

    private int putDocument(URI uri, DocumentFormat format, byte[] bytes){
        DocumentImpl tempDoc; 
        if(format == DocumentFormat.TXT){
          tempDoc = (DocumentImpl) this.hashTable.put(uri, new DocumentImpl(uri, new String(bytes)));
        }else{
          tempDoc = (DocumentImpl) this.hashTable.put(uri, new DocumentImpl(uri, bytes));
        }
        return valueAfterPut(tempDoc);
    }

    private int valueAfterPut(DocumentImpl doc){
        if(doc == null){
            return 0; 
        }
        return doc.hashCode(); 
    }

    private int removeDocument(URI uri){
        DocumentImpl doc = (DocumentImpl) this.hashTable.put(uri, null); 
        if(doc == null){
            return 0; 
        }
        return doc.hashCode(); 
    }

    public Document getDocument(URI uri){
        return (Document) this.hashTable.get(uri);
    }

    public boolean deleteDocument(URI uri){
        if(this.hashTable.put(uri, null) != null){
            return true; 
        }
        return false; 
    }
}