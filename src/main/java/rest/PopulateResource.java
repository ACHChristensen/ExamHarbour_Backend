package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Anne Cathrine Høyer Christesen
 */
@Path("populate")
public class PopulateResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("users")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String populateTestUsersNAdmin() {
        try {
            utils.SetupTestUsers.populate();
            return "SUCCES for population!";
        } catch (Exception e) {
            return "FAILED to populate! \n"+ e.getMessage();
        }
        
    }
    
    @Path("owners")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String populateTestOwners() {
        try {
            utils.SetupTestUsers.populateOwners();
            return "SUCCES for population!";
        } catch (Exception e) {
            return "FAILED to populate! \n"+ e.getMessage();
        }
        
    }

}
