package edu.yu.cs.com1320.project.stage2.impl; 
import edu.yu.cs.com1320.project.stage2.Document;
import java.net.URI; 
import java.util.Arrays; 

public class DocumentImpl implements Document{
    String txt; 
    byte[] bytes; 
    URI uri; 

    public DocumentImpl(URI uri, String txt){
        if(uri == null || txt == null){
            throw new IllegalArgumentException("arguements can't be null");
        }
        if(uri.toString().length() == 0 || txt.equals("")){
            throw new IllegalArgumentException("arguements can't be length 0");
        }
        this.uri = uri;
        this.txt = txt; 
    }

    public DocumentImpl(URI uri, byte[] binaryData){
        if (uri == null || binaryData == null) {
            throw new IllegalArgumentException("Arguements can't be null");
        }
        if(uri.toString().length() == 0 || binaryData.length == 0){
            throw new IllegalArgumentException("arguements must have length > 0");
        }
        this.uri = uri;
        this.bytes = binaryData;
    }

    public int hashCode() {
        int result = this.uri.hashCode();
        result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.bytes);
        return result;
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true; 
        }
        if(obj == null){
            return false; 
        }
        if(this.getClass() != obj.getClass()){
            return false; 
        }
        DocumentImpl doc = (DocumentImpl) obj; 
        return this.hashCode() == doc.hashCode(); 
    }

    public String getDocumentTxt() {
        return this.txt;
    }

    public byte[] getDocumentBinaryData() {
        return this.bytes;
    }

    public URI getKey() {
        return this.uri;
    }
}