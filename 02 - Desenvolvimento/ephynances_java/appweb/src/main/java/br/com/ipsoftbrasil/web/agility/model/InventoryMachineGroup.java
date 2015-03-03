package br.com.ipsoftbrasil.web.agility.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "agility_inventory_machine_group")
@Entity
public class InventoryMachineGroup implements BaseModel {

    private static final long serialVersionUID = 456565187932147L;    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @NotEmpty    
    @Column(length = 100, unique = true, nullable = false, name = "name")
    private String name;                
    
    //References    
    @OneToMany(mappedBy = "group")
    private List<InventoryMachine> machines;
    
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InventoryMachine> getMachines() {
        return machines;
    }

    public void setMachines(List<InventoryMachine> machines) {
        this.machines = machines;
    }   
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return id.toString();
    }
    
    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof InventoryMachineGroup)) {
            return false;
        }
        InventoryMachineGroup other = (InventoryMachineGroup) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
