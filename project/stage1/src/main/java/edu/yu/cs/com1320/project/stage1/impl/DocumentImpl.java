package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.Document;
import java.net.URI;
import java.util.Arrays;

public class DocumentImpl implements Document {
    String DocumentTxt;
    byte[] binaryData;
    URI uri;
    // what does it mean for the URI to be empty?

    public DocumentImpl(URI uri, String txt) {
        if (uri == null || txt == null) {
            throw new IllegalArgumentException("arguements can't be null");
        }
        if (uri.toString().length() == 0 || txt.equals("")) {
            throw new IllegalArgumentException("arguements must have length > 0");
        }
        this.uri = uri;
        this.DocumentTxt = txt;
    }

    public DocumentImpl(URI uri, byte[] binaryData) {
        if (uri == null || binaryData == null) {
            throw new IllegalArgumentException("Arguements can't be null");
        }
        if(uri.toString().length() == 0 || binaryData.length == 0){
            throw new IllegalArgumentException("arguements must have length > 0");
        }
        this.uri = uri;
        this.binaryData = binaryData;
    }

    @Override
    public int hashCode() {
        int result = this.uri.hashCode();
        result = 31 * result + (this.DocumentTxt != null ? this.DocumentTxt.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.binaryData);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        DocumentImpl doc = (DocumentImpl) obj;
        return this.hashCode() == doc.hashCode();
    }

    public String getDocumentTxt() {
        return this.DocumentTxt;
    }

    public byte[] getDocumentBinaryData() {
        return this.binaryData;
    }

    public URI getKey() {
        return this.uri;
    }
}
