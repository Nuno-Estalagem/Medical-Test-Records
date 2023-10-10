package Quarkus.Group25;

import domain.Server;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.jboss.resteasy.reactive.RestHeader;
import org.json.JSONObject;

@Path("/view")
@RolesAllowed({"admin","nurse","ca","porter","psa"})
public class viewPatientsRequest {
    @Inject
    JsonWebToken token;



    @GET
    @Path("/patients")
    @RolesAllowed({"admin","nurse","ca","porter","psa","doctor"})
    @Produces(MediaType.APPLICATION_JSON)
    public byte[] viewPatients(String request,@RestHeader("Timestamp") String timestamp) throws Exception{
        System.out.println(timestamp+"here");
        Server server = Server.getInstance();
        List<Patient> allPersons = Patient.listAll();
            JSONObject body= new JSONObject();
            for (Patient patient : allPersons) {
                JSONObject object=new JSONObject();
                object.put("age",patient.age);
                object.put("nif",patient.nif);
                object.put("name", patient.name);
                body.put(String.valueOf(patient.nif),object);
            }
            OperationsWithSessionKey operations= new OperationsWithSessionKey();
            if(operations.compareStamps(timestamp,server.getSessionKey(token.getRawToken()))){
                return operations.cipher(body.toString().getBytes(), server.getSessionKey(token.getRawToken()));
            }
            else
                return operations.cipher("ReplayAttack".getBytes(), server.getSessionKey(token.getRawToken()));



    }
}
