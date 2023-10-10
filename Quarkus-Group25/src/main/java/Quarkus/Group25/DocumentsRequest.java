package Quarkus.Group25;

import com.mongodb.BasicDBObject;
import domain.Server;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestHeader;
import org.json.JSONObject;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Key;
import java.util.List;


@Path("/post")
@RolesAllowed({"doctor","admin","psa","ca","porter","nurse"})
public class DocumentsRequest {

    @Inject
    JsonWebToken token;

    @POST
    @Path("/documents")
    @RolesAllowed({"doctor","admin","psa","ca","porter","nurse"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public byte[] documents(String request,@RestHeader("Timestamp") String timestamp) throws Exception{

        Server server = Server.getInstance();
        Key sessionKey= server.getSessionKey(token.getRawToken());
        OperationsWithSessionKey session= new OperationsWithSessionKey();
        try {
            JSONObject object = session.decipher(sessionKey, request, timestamp);

            System.out.println(object);

            String nif = object.getString("nif");
            Patient patient = Patient.findById(Integer.parseInt(nif));
            List<ObjectId> documentos = patient.documentIds;
            List<ObjectId> plDocuments = patient.foreignDocumentsIds;

            JSONObject docArray = new JSONObject();

            for (ObjectId id : documentos) {

                Document doc = Document.findById(id);
                BasicDBObject content = doc.document;

                String patientDetails = content.getString("patient details");
                JSONObject pDetails = new JSONObject(patientDetails);
                String name = pDetails.get("name").toString();
                String recordDetails = content.getString("Record details");
                JSONObject rDetails = new JSONObject(recordDetails);
                String theme = rDetails.get("theme").toString();
                String title = rDetails.get("title").toString();
                String observations = rDetails.get("observations").toString();
                String lab = doc.lab;
                String uploader = doc.uploader;

                JSONObject document = new JSONObject();
                document.put("name", name);
                document.put("theme", theme);
                document.put("title", title);
                document.put("observations", observations);
                document.put("uploader", uploader);
                document.put("id", id.toString());
                document.put("lab", lab);
                docArray.put(String.valueOf(doc.id), document);


            }
            for (ObjectId id : plDocuments) {

                PartnerLabDocument doc = PartnerLabDocument.findById(id);
                BasicDBObject content = doc.document;

                String patientDetails = content.getString("patient details");
                JSONObject pDetails = new JSONObject(patientDetails);
                String name = pDetails.get("name").toString();
                String recordDetails = content.getString("Record details");
                JSONObject rDetails = new JSONObject(recordDetails);
                String theme = rDetails.get("theme").toString();
                String title = rDetails.get("title").toString();
                String observations = rDetails.get("observations").toString();
                String lab = doc.lab;
                String uploader = doc.uploader;

                JSONObject document = new JSONObject();
                document.put("name", name);
                document.put("theme", theme);
                document.put("title", title);
                document.put("observations", observations);
                document.put("uploader", uploader);
                document.put("id", id.toString());
                document.put("lab", lab);
                docArray.put(String.valueOf(doc.id), document);

            }
            return session.cipher(docArray.toString().getBytes(), server.getSessionKey(token.getRawToken()));
        }catch (Exception e){
            byte[] payload = "Potential Replay Attack".getBytes();;
            return session.cipher(payload,sessionKey);

        }
        }
}

