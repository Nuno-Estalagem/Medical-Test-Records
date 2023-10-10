package Quarkus.Group25;

import com.mongodb.BasicDBObject;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@MongoEntity(database = "partner-lab", collection = "Document")
public class PartnerLabDocument extends PanacheMongoEntityBase {

    public ObjectId id;

    public BasicDBObject document;

    public String uploader;

    public String lab;

    public byte[] hash;

    
}
