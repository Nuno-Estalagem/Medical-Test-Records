package Quarkus.Group25;

import cipher.CipherHandler;
import com.mongodb.BasicDBObject;
import domain.Server;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestHeader;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Key;

@Path("/upload")
@RolesAllowed({"admin","doctor"})
public class UploadDocuments {

    @Inject
    JsonWebToken token;


    @POST
    @Path("/document")
    @RolesAllowed({"admin", "doctor"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public byte[] uploadDocument(String request,@RestHeader("Timestamp") String timestamp) throws Exception{
        Server server = Server.getInstance();
        Key sessionKey= server.getSessionKey(token.getRawToken());
        OperationsWithSessionKey session= new OperationsWithSessionKey();
        try{
        JSONObject object = session.decipher(sessionKey,request,timestamp);


        try {
            Patient patient = Patient.findById(Integer.parseInt(object.getString("nif")));


            if (!patient.name.contentEquals(object.getString("name").toLowerCase())) {
                byte[] payload = "O Nome que introduziu nao condiz com o NIF do Paciente".getBytes();
                return session.cipher(payload, sessionKey);
            }
            BasicDBObject document = new BasicDBObject();
            BasicDBObject patientDetails = new BasicDBObject();
            patientDetails.put("nif", object.getString("nif"));
            patientDetails.put("name", object.getString("name"));
            BasicDBObject recordDetails = new BasicDBObject();
            recordDetails.put("theme", object.getString("theme"));
            recordDetails.put("title", object.getString("title"));
            recordDetails.put("observations", object.getString("observations"));
            document.put("patient details", patientDetails);
            document.put("Record details", recordDetails);
            Document documento = new Document();
            documento.document = document;
            documento.uploader = object.getString("username");
            documento.lab = "hospital-lab";
            documento.hash = CipherHandler.hashMessage(documento.toString().getBytes());
            documento.persist();
            patient.documentIds.add(documento.id);
            patient.update();


            byte[] payload = "Documento criado com sucesso".getBytes();
            ;
            return session.cipher(payload, sessionKey);
        }catch (Exception e){
            byte[] payload = "Potential Replay Attack".getBytes();;
            return session.cipher(payload,sessionKey);

        }
        } catch (NullPointerException e) {
            byte[] payload= "Não existe nenhum paciente associado a esse NIF".getBytes();
            return session.cipher(payload,sessionKey);


        }
    }
}