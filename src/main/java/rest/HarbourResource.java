
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HarbourDTO;
import dtos.OwnerDTO;
import errorhandling.ExceptionDTO;
import facades.HarboursFacade;
import facades.OwnerFacade;
import java.util.List;
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
@Path("habours")
public class HarbourResource {
    

    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final HarboursFacade FACADE = HarboursFacade.getHarboursFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllHabours() {
        List<HarbourDTO> habourDTOs;
        try {
            habourDTOs = FACADE.getAll();
            return GSON.toJson(habourDTOs);
        } catch (Exception ex) {
         return GSON.toJson(new ExceptionDTO(404, ex.getMessage()));
        }
    }
}
