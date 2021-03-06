package rest;

import entities.User;
import entities.Role;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
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

//@Disabled
public class LoginEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

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
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
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

        User user = new User("ChunkyMonkey", "Guler??dder4TW!");
        User admin = new User("Cthulhu", "Horror");
        User both = new User("Cathrine", "frkAwesome123");
        
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

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
    
    @AfterEach
    public void tearDown(){
       
        
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
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
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }


    @Test
    public void testRestForAdmin() {
        login("Cthulhu", "Horror");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("info/admin").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to (admin) User: Cthulhu"));
    }

    @Test
    public void testRestForUser() {
        login("ChunkyMonkey", "Guler??dder4TW!");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("info/user").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to User: ChunkyMonkey"));
    }

    @Test
    public void testAutorizedUserCannotAccesAdminPage() {
        login("ChunkyMonkey", "Guler??dder4TW!");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("info/admin").then() //Call Admin endpoint as user
                .statusCode(401);
    }

    @Test
    public void testAutorizedAdminCannotAccesUserPage() {
        login("Cthulhu", "Horror");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("info/user").then() //Call User endpoint as Admin
                .statusCode(401);
    }

    @Test
    public void testRestForMultiRole1() {
        login("Cathrine", "frkAwesome123");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("info/admin").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to (admin) User: Cathrine"));
    }

    @Test
    public void testRestForMultiRole2() {
        login("Cathrine", "frkAwesome123");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("info/user").then()
                .statusCode(200)
                .body("msg", equalTo("Hello to User: Cathrine"));
    }

    @Test
    public void userNotAuthenticated() {
        logOut();
        given()
                .contentType("application/json")
                .when()
                .get("info/user").then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

    @Test
    public void adminNotAuthenticated() {
        logOut();
        given()
                .contentType("application/json")
                .when()
                .get("info/user").then()
                .statusCode(403)
                .body("code", equalTo(403))
                .body("message", equalTo("Not authenticated - do login"));
    }

}
