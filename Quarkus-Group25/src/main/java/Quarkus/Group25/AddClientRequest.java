package Quarkus.Group25;


import domain.Server;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestHeader;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Key;
import java.util.*;

@Path("/add")
@RolesAllowed({"admin","wardClerk"})
public class AddClientRequest {

    @Inject
    JsonWebToken token;


    @POST
    @Path("/client")
    @RolesAllowed({"admin","wardClerk"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public byte[] addClient(String request,@RestHeader("Timestamp") String timestamp) throws Exception{
        Server server = Server.getInstance();
        Key sessionKey= server.getSessionKey(token.getRawToken());
        OperationsWithSessionKey session= new OperationsWithSessionKey();
        JSONObject object = session.decipher(sessionKey,request,timestamp);
        try {

            DecodeRequest decode = new DecodeRequest();
            List<String> values = decode.decode(request);



            Patient patient = new Patient();
            patient.nif = Integer.parseInt(object.getString("nif"));
            patient.name = object.getString("name");
            patient.age = Integer.parseInt(object.getString("age"));
            patient.doctors = new ArrayList<>();
            patient.documentIds = new ArrayList<>();
            patient.foreignDocumentsIds = new ArrayList<>();
            patient.persist();

            byte[] payload = "Paciente adicionado com sucesso".getBytes();;
            return session.cipher(payload,sessionKey);


        } catch (Exception e){
            byte[] payload= "Os valores inseridos nao sao validos".getBytes();
            return session.cipher(payload,sessionKey);
        }
    }
}
