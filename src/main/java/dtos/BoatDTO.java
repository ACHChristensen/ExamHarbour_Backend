package dtos;

import entities.Boat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
class BoatDTO {

    private String name;
    private String brand;
    private int make;
    private String image;
    private HarbourDTO harbourDTO;

    public BoatDTO(Boat boat){
        this.name = boat.getName();
        this.brand = boat.getBrand();
        this.make = boat.getMake();
        this.image = boat.getImage();
        this.harbourDTO = new HarbourDTO(boat.getHarbour());
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

    public HarbourDTO getHarbourDTO() {
        return harbourDTO;
    }

    
    
}
