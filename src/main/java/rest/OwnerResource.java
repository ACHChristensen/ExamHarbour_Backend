
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.OwnerDTO;
import errorhandling.ExceptionDTO;
import facades.OwnerFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
@Path("owners")
public class OwnerResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final OwnerFacade FACADE = OwnerFacade.getOwnerFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllOwners() {
        List<OwnerDTO> ownerDtos;
        try {
            ownerDtos = FACADE.getAll();
            return GSON.toJson(ownerDtos);
        } catch (Exception ex) {
         return GSON.toJson(new ExceptionDTO(404, ex.getMessage()));
        }
    }
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount() {
        Long count = FACADE.getCount();
        return "{\"count\":"+count+"}";
    }
}
