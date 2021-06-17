package utils;

import entities.Owner;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {
    
    public static void populate() throws UnsupportedOperationException{

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        User user = new User("ChunkyMonkey", "Gulerødder4TW!");
        User admin = new User("Cthulhu", "Horror");
        User both = new User("Cathrine", "frkAwesome123");
        
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        if (admin.getUserPass().equals("Test") || user.getUserPass().equals("Test") || both.getUserPass().equals("Test")) {
            throw new UnsupportedOperationException("You have not changed the passwords");
        }
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
            em.getTransaction().commit();    
        } finally {
            em.close();
        }
        
    }

    public static void main(String[] args) throws Exception {
        populate();
        populateOwners();
    }

    public static void populateOwners() throws Exception {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        Owner owner1 = new Owner("Captain Jack Sparrow", "The Caribbean", "5318008");
        Owner owner2 = new Owner("Kaptajn Haddock", "Scotland Somewhere", "10000001");
        Owner owner3 = new Owner("Tin tin", "Tibet Somewhere", "10101010");
        Owner owner4 = new Owner("Skipper Skraek", "Popeye Village i Malta", "50503050");
        Owner owner5 = new Owner("Captain Morgan", "Mavebæltestedet 1", "10999999");
        Owner owner6 = new Owner("Harald Blåtand", "Vinkingevej 1010, 0001 Danmark", "89586858");
        Owner owner7 = new Owner("Ragnar Lodbrok", "Vinkingevej 1012, 0001 Danmark", "99999999");
        Owner owner8 = new Owner("Kaptajn Klo", "Fantasivej 12, 202020 Ønske Øen", "99910010");
        
        try {
            em.getTransaction().begin();
            em.persist(owner1);
            em.persist(owner2);
            em.persist(owner3);
            em.persist(owner4);
            em.persist(owner5);
            em.persist(owner6);
            em.persist(owner7);
            em.persist(owner8);
            em.getTransaction().commit();    
        } catch(Exception e){
            throw new Exception(e.getMessage());
            }finally {
            em.close();
        }
    }

}
