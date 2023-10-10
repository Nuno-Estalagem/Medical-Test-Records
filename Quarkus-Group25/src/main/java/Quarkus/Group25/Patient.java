package Quarkus.Group25;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Locale;

@MongoEntity(database = "hospital-lab", collection = "Patient")
public class Patient extends PanacheMongoEntityBase {

    @BsonId
    public int nif;

    public String name;

    public int age;

    public List<String> doctors;

    public List<ObjectId> documentIds;

    public List<ObjectId> foreignDocumentsIds;

    public Patient(){
        
    }
    public void setName(String name){
        this.name=name.toLowerCase();
    }

}
