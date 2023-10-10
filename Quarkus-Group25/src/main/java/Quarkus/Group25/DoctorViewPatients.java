package Quarkus.Group25;

import domain.Server;
import io.quarkus.mongodb.panache.PanacheQuery;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestHeader;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Key;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Path("/doctor")
@RolesAllowed({"doctor"})
public class DoctorViewPatients {

        @Inject
        JsonWebToken token;



    @POST
    @Path("/assign")
    @RolesAllowed({"doctor"})
    @Consumes(MediaType.APPLICATION_JSON)
    public byte[] assignPatients(String request,@RestHeader("Timestamp") String timestamp) throws Exception{
        Server server = Server.getInstance();
        Key sessionKey= server.getSessionKey(token.getRawToken());
        OperationsWithSessionKey session= new OperationsWithSessionKey();

        JSONObject object = session.decipher(sessionKey,request,timestamp);

        Patient patient = Patient.findById(Integer.parseInt(object.getString("nif")));
        if (!patient.doctors.contains(object.getString("doctor"))) {
            patient.doctors.add(object.getString("doctor"));
            patient.update();
            byte[] payload= "Paciente adicionado a si com sucesso".getBytes();
            return session.cipher(payload,sessionKey);
        }
        byte[] payload= "Este paciente já estava associado a sí".getBytes();
        return session.cipher(payload,sessionKey);
    }



    @POST
    @Path("/assignedPatients")
    @RolesAllowed({"doctor"})
    @Produces(MediaType.APPLICATION_JSON)
    public byte[] viewAssignedPatients(String request,@RestHeader("Timestamp") String timestamp) throws Exception{

            Server server = Server.getInstance();
            Key sessionKey = server.getSessionKey(token.getRawToken());
            OperationsWithSessionKey session = new OperationsWithSessionKey();
        try {
            JSONObject objeto = session.decipher(sessionKey, request, timestamp);

            String username = objeto.getString("username");
            System.out.println(username + " here");
            PanacheQuery<Patient> doctorsPatients = Patient.find("doctors", username);
            List<Patient> patients = doctorsPatients.list();

            JSONObject body = new JSONObject();
            for (Patient patient : patients) {
                JSONObject object = new JSONObject();
                object.put("age", patient.age);
                object.put("nif", patient.nif);
                object.put("name", patient.name);
                body.put(String.valueOf(patient.nif), object);
            }

            return session.cipher(body.toString().getBytes(), server.getSessionKey(token.getRawToken()));
        }catch (Exception e){
                byte[] payload = "Potential Replay Attack".getBytes();;
                return session.cipher(payload,sessionKey);

        }
        }

}
