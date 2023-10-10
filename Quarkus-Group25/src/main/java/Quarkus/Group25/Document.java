package Quarkus.Group25;

import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(database = "hospital-lab", collection = "Document")
public class Document extends PanacheMongoEntityBase {


    public ObjectId id;

    public BasicDBObject document;

    public String uploader;

    public String lab;

    public byte[] hash;

    public Document(){
        
    }
}