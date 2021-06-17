
package dtos;

import entities.Owner;

/**
 *
 * @author Anne Cathrine Høyer Christensen
 */
public class OwnerDTO {
    
    private String name; 
    private String address;
    private String phone;

    public OwnerDTO(Owner owner) {
        this.name = owner.getName();
        this.address = owner.getAddress();
        this.phone = owner.getPhone();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
   
    
}
