
package dtos;

import entities.Boat;
import entities.Harbour;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
public class HarbourDTO {
    private String name;
    private String address;
    private int capacity;
    private List<BoatDTO> boatDTOs;

    public HarbourDTO(Harbour harbour) {
        this.name = harbour.getName();
        this.address = harbour.getAddress();
        this.capacity = harbour.getCapacity();
        this.boatDTOs = new ArrayList<>();
        harbour.getBoats().forEach(boat-> boatDTOs.add(new BoatDTO(boat)));
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<BoatDTO> getBoatDTOs() {
        return boatDTOs;
    }
    
    
}
