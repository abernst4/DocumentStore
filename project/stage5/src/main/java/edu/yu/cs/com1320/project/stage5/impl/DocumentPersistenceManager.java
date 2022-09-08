
package edu.yu.cs.com1320.project.stage5.impl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import jakarta.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileWriter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.Gson;

public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    File baseDir;
    public DocumentPersistenceManager(File baseDir){
        this.baseDir= baseDir; 
    }
    
    @Override
    public void serialize(URI uri, Document doc) throws IOException {
        Gson gson = doc.getDocumentBinaryData() != null?
        new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new mySerial()).create():
        new GsonBuilder().registerTypeAdapter(Document.class, new mySerial()).create();
        String str = gson.toJson(doc);
        String Bdir = this.baseDir == null?System.getProperty("user.dir"):this.baseDir.getAbsolutePath();//need to confirm this
        String path = makePath(uri);
        File newDir = new File(Bdir+path);
        newDir.getParentFile().mkdirs();
        int marker = getDir(path);
        String dir = Bdir + path.substring(0,marker);
        String fileName = path.substring(marker) + ".json";
        File file = new File(dir, fileName); 
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(str);
        writer.close();
    }

    private String makePath(URI uri){
        return uri.getSchemeSpecificPart();
    }

    private int getDir(String path){
        for(int i = path.length()-1; i > 0; i--){
            if(path.charAt(i) == '/'){
                return i; 
            }
        }
        return -1;
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new myDeserial()).create();
        String dir = this.baseDir==null?System.getProperty("user.dir"):this.baseDir.getAbsolutePath();//ask judah about this
        String path = makePath(uri);
        File file = new File(dir+path+".json");
        String json = stringFromFile(file);
        Document doc = gson.fromJson(json, Document.class);
        deleteFile(file);
        return doc; 
    }
    
    private void deleteFile(File file){
        File dir = file.getParentFile();
        file.delete();
        if(dir.isDirectory() && dir.listFiles().length == 0){
            deleteFile(dir);
        }
    }

    private String stringFromFile(File file) throws FileNotFoundException{
        Scanner scan = new Scanner(file);
        return scan.nextLine();
    }

    @Override
    public boolean delete(URI uri) throws IOException {
        String base = this.baseDir==null?System.getProperty("user.dir"):this.baseDir.getAbsolutePath();
        String path = makePath(uri);
        File file = new File(base+path+".json");
        File par = file.getParentFile();
        boolean b = file.delete();
        ridDir(par);
        return b; 
    }

    private void ridDir(File dir) throws IOException {
        if(dir.listFiles() != null && dir.listFiles().length == 0){
            File par = dir.getParentFile(); 
            dir.delete(); 
            ridDir(par);
        }
    }

    private class mySerial implements JsonSerializer<Document>{
        @Override
        public JsonElement serialize(Document arg0, Type arg1, JsonSerializationContext arg2) {
            if(arg0.getDocumentBinaryData()==null){
                return new JsonPrimitive(arg0.toString());
            }
            JsonObject obj = new JsonObject();
            String encode = DatatypeConverter.printBase64Binary(arg0.getDocumentBinaryData());
            obj.addProperty("byteString", encode);
            obj.addProperty("uri", arg0.getKey().toString());
            obj.addProperty("map", arg0.getWordMap().toString());
            return obj; 
        }
    }

    private class myDeserial implements JsonDeserializer<Document>{
        @Override
        public Document deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            JsonObject jO = arg0.getAsJsonObject();//well, need to look at my phone when we get back
            Type type = new TypeToken<HashMap<String,Integer>>(){}.getType();
            try {
                if(jO.get("byteString")==null){
                    Map<String, Integer> map = new Gson().fromJson(jO.get("map").toString(), type);
                    return new DocumentImpl(new URI(jO.get("uri").getAsString()),jO.get("txt").getAsString(), map);
                }
                byte[] bytes = DatatypeConverter.parseBase64Binary(jO.get("byteString").getAsString());
                return new DocumentImpl(new URI(jO.get("uri").getAsString()), bytes);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null; 
            }
        }
    }
}

