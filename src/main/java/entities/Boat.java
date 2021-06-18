
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
@Entity
public class Boat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String brand;
    private int make;
    private String image;
    @ManyToOne
    private Harbour harbour;

    public Boat(String name, String brand, int make, String image, Harbour harbour) {
        this.name = name;
        this.brand = brand;
        this.make = make;
        this.image = image;
        this.harbour = harbour;
    }
    

    public Boat() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public int getMake() {
        return make;
    }

    public String getImage() {
        return image;
    }

    public Harbour getHarbour() {
        return harbour;
    }

    

}
