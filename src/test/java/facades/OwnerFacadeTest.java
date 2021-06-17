
package facades;

import entities.Owner;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
public class OwnerFacadeTest {
    
     private static EntityManagerFactory emf;
    private static OwnerFacade facade;


    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = OwnerFacade.getOwnerFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {

        EntityManager emTD = emf.createEntityManager();
        EntityManager em = emf.createEntityManager();
         
        try{
            emTD.getTransaction().begin();
        //Delete existing users, roles, ect. to get a "fresh" database
        emTD.createQuery("delete from User").executeUpdate();
        emTD.createQuery("delete from Role").executeUpdate();
        emTD.createQuery("delete from Owner").executeUpdate();
        emTD.getTransaction().commit();
          }  finally{
            emTD.close();
        }


        User user = new User("ChunkyMonkey", "Gulerødder4TW!");
        User admin = new User("Cthulhu", "Horror");
        User both = new User("Cathrine", "frkAwesome123");
        
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        
        Owner owner1 = new Owner("Captain Jack Sparrow", "The Carribian", "5318008");
        Owner owner2 = new Owner("Kaptajn Haddock", "Scotland Somewhere", "10000001");
        Owner owner3 = new Owner("Tin tin", "Tibet Somewhere", "10101010");
        Owner owner4 = new Owner("Skipper Skraek", "Popeye Village i Malta", "50503050");
//        Owner owner5 = new Owner("Captain Morgan", "Mavebæltestedet 1", "10999999");
//        Owner owner6 = new Owner("Harald Blåtand", "Vinkingevej 1010, 0001 Danmark", "89586858");
//        Owner owner7 = new Owner("Ragnar", "Vinkingevej 1012, 0001 Danmark", "99999999");
//        Owner owner8 = new Owner("Kaptajn Klo", "Fantasivej 12, 202020 Ønske Øen", "99910010");

        try {
            em.getTransaction().begin();
            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.merge(user);
            em.merge(admin);
            em.merge(both);
            em.persist(owner1);
            em.persist(owner2);
            em.persist(owner3);
            em.persist(owner4);
//            em.persist(owner5);
//            em.persist(owner6);
//            em.persist(owner7);
//            em.persist(owner8);
            em.getTransaction().commit();    
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetAll() throws Exception {
        int expResult = 4;
        int result = facade.getAll().size();
        assertEquals(expResult, result);
    }
    
}
