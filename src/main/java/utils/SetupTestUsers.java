package utils;

import entities.Boat;
import entities.Harbour;
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
    public static void populateHarboursAndBoats() throws UnsupportedOperationException{

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        Harbour harbour1 = new Harbour("Rungsted Havn", "Rungsted", 200);
        Harbour harbour2 = new Harbour("Den Hemmelige Havn", "Narnia", 10);
        Harbour harbour3 = new Harbour("Yokohama", "Tokyobugten", 30000);
        
        Boat boat1 = new Boat("The Black Perl", "Gammeldags piratskib", 200, ":D", harbour2);
        Boat boat2 = new Boat("Yvonne", "Ubåd", 7, "xD", harbour3);
        Boat boat3 = new Boat("Freja", "Vinkingeskib", 100, ":P", harbour1);
        Boat boat4 = new Boat("Den rygende kvinde", "Dampskib", 1, "^^", harbour1);
        Boat boat5 = new Boat("Den vågende kvinde", "Dampskib", 21, ":)", harbour3);
        Boat boat6 = new Boat("Gerda", "Dampskib", 3, "xP", harbour3);
        Boat boat7 = new Boat("[Ukendt]", "ubåd", 65, "-_-", harbour3);
        
        try {
            em.getTransaction().begin();
            em.persist(harbour1);
            em.persist(harbour2);
            em.persist(harbour3);
            em.persist(boat1);
            em.persist(boat2);
            em.persist(boat3);
            em.persist(boat4);
            em.persist(boat5);
            em.persist(boat6);
            em.persist(boat7);
            em.getTransaction().commit();    
        } finally {
            em.close();
        }
        
    }

    public static void main(String[] args) throws Exception {
        populate();
        populateOwners();
        populateHarboursAndBoats();
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
