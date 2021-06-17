
package facades;

import dtos.OwnerDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import entities.Owner;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
public class OwnerFacade {
    
    private static OwnerFacade instance;
    private static EntityManagerFactory emf;

    public static OwnerFacade getOwnerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new OwnerFacade();
        }
        return instance;
    }
    
    public List getAll() throws Exception {
        List<OwnerDTO> ownerDTOs = new ArrayList();
        EntityManager em = emf.createEntityManager();
        TypedQuery<Owner> ownersQuery = em.createQuery("SELECT o FROM Owner o", Owner.class);
        List<Owner> owners = ownersQuery.getResultList();
        for (Owner owner : owners) {
            ownerDTOs.add(new OwnerDTO(owner));
        }
        if (ownerDTOs.isEmpty()) {
            throw new Exception("No owners in DB");
        }
        return ownerDTOs;
    }
    
    public Long getCount(){
        EntityManager em = emf.createEntityManager();
        try{
            Long countOwners = (Long)em.createQuery("SELECT COUNT(o) FROM Owner o").getSingleResult();
            return countOwners;
        }finally{  
            em.close();
        }
        
    }
    
}
