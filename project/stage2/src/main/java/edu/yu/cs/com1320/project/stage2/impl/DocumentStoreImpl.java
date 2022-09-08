package edu.yu.cs.com1320.project.stage2.impl; 
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.Stack;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore{
    HashTableImpl<URI, Document> hashtable; 
    StackImpl<Command> stack; 
    public DocumentStoreImpl(){
        this.hashtable = new HashTableImpl<>();
        this.stack = new StackImpl<>();
    }

    public int putDocument(InputStream input, URI uri, DocumentFormat format) throws IOException{
        if(uri == null){
            throw new IllegalArgumentException("uri can't be null");
        }
        if(input == null){
           return removeDocument(uri);
        }
        if(format == null){
            throw new IllegalArgumentException("format can't be null");
        }
        byte[] bytes = input.readAllBytes();
        return putDocument(uri, format, bytes);
    }

    private int putDocument(URI uri, DocumentFormat format, byte[] bytes){
        Document doc = format == DocumentFormat.TXT? new DocumentImpl(uri, new String(bytes)): new DocumentImpl(uri,bytes);
        Document temp = this.hashtable.put(uri, doc);
        undoPut(temp, uri); //changing from doc to temp
        return hCodeOrZero.apply(temp);
    }

    private void undoPut(Document doc, URI uri){
        Function<URI, Boolean> func = x -> {
            this.hashtable.put(uri, doc);
            return true; 
        };
        stack.push(new Command(uri, func));
    }

    Function<Document, Integer> hCodeOrZero = x -> x == null? 0: x.hashCode();

    private int removeDocument(URI uri){ 
        //need to ask if I have to create a command for deleting null; whether there is supposed to be somehting on the stack
        Document doc = (DocumentImpl) this.hashtable.put(uri, null);
       // if(doc != null){//need to take null into account
        undoDelete(doc,uri);//NEED TO MAKE THIS ENTIRE CLASS ABLE TO WITHSTAND DELETING FROM KEY THAT DOESN'T EXIST
       // }
        return hCodeOrZero.apply(doc);
    }

    private void undoDelete(Document doc, URI uri){
        Function<URI,Boolean> func = x -> {
            this.hashtable.put(uri, doc); 
            return true; 
        };
        stack.push(new Command(uri, func));
    }

    public Document get(URI uri){
        return (Document) this.hashtable.get(uri);
    }

    public boolean deleteDocument(URI uri){
        return this.removeDocument(uri) != 0? true: false; //need to check this again. 
    }

    public Document getDocument(URI uri) {
        return (Document) this.hashtable.get(uri);
    }

    public void undo() throws IllegalStateException {
        if(this.stack.size() == 0){
            throw new IllegalStateException("stack is empty");
        }
        Command command = this.stack.pop();
        command.undo(); 
    }

    public void undo(URI uri) throws IllegalStateException {
        if(this.stack.size() == 0){
            throw new IllegalStateException("stack is empty");
        }
        Stack<Command> tempStack = new StackImpl<>(); 
        boolean notFound = true; 
        Command command= null; 
        /*if the first thing is the uri then great, if not then we add the command to teh tempStack
        //and pop the next thing off of the stack, make it a command and see whether they have the same
        //uri or not; if we don't find the uri, then we throw a illegalStateException. regardless,
        i have to add everything back up to the stack. the two edge cases are the first and last commands*/
        while(this.stack.size() != 0 && notFound){
            command = this.stack.pop();
            if(command.getUri().equals(uri)){
              reStack(tempStack);
              notFound = false;   
            }else{
                tempStack.push(command);
            }
        }
        if(notFound){
            reStack(tempStack);//not sure if I need to redo this
            throw new IllegalStateException("we don't have this URI on the stack");
        }
        command.undo();
    } 

    private void reStack(Stack<Command> temp){
        while(temp.size() != 0){
            this.stack.push(temp.pop());
        }
    }
}