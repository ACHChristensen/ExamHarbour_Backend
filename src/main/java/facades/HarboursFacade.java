
package facades;

import dtos.HarbourDTO;
import dtos.OwnerDTO;
import entities.Harbour;
import entities.Owner;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
public class HarboursFacade {
    private static HarboursFacade instance;
    private static EntityManagerFactory emf;

    public static HarboursFacade getHarboursFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HarboursFacade();
        }
        return instance;
    }

    public List<HarbourDTO> getAll() throws Exception {
        List<HarbourDTO> harbourDTOs = new ArrayList();
        EntityManager em = emf.createEntityManager();
        TypedQuery<Harbour> ownersQuery = em.createQuery("SELECT h FROM Harbour h", Harbour.class);
        List<Harbour> harbours = ownersQuery.getResultList();
        for (Harbour harbour : harbours) {
            harbourDTOs.add(new HarbourDTO(harbour));
        }
        if (harbourDTOs.isEmpty()) {
            throw new Exception("No harbours in DB - have Cthulhu destroyed them all?");
        }
        return harbourDTOs;
    }
}
