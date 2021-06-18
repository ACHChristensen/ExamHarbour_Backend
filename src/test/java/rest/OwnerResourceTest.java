package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Owner;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import utils.EMF_Creator;
import javax.persistence.TypedQuery;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
public class OwnerResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void tearDownClass() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @AfterEach
    public void tearDown() {

    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        EntityManager emTD = emf.createEntityManager();
        try {
            emTD.getTransaction().begin();
            //Delete existing users, roles, ect. to get a "fresh" database
            emTD.createQuery("DELETE FROM User").executeUpdate();
            emTD.createQuery("DELETE FROM Role").executeUpdate();
            emTD.createQuery("DELETE FROM Owner").executeUpdate();
            emTD.getTransaction().commit();
        } finally {
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
            System.out.println("Succes");
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String username, String password) {
        //Boolean passwordCrypted = BCrypt.checkpw(password,BCrypt.gensalt());
        // if(passwordCrypted==true){
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
        //}else throw new Exception("Not identical passwords");
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/owners/count").then().statusCode(200);
    }

    @Test
    public void testCount() throws Exception {
        login("ChunkyMonkey", "Gulerødder4TW!");
        given()
                .contentType("application/json")
                .get("owners/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(4));
    }

}
