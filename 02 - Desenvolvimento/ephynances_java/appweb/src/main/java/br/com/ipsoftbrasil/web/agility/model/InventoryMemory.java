package br.com.ipsoftbrasil.web.agility.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "agility_inventory_memory")
@Entity
public class InventoryMemory implements BaseModel {

    private static final long serialVersionUID = 456565187932147L;    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "machine_id", referencedColumnName = "id", nullable = false)
    private InventoryMachine machine;
                
    @NotNull
    @NotEmpty
    @DefaultValue(value = "DDR-3")
    @Column(length = 100, nullable = false, name = "tecnology")
    private String tecnology;
    
    @NotNull
    @NotEmpty
    @Column(length = 100, nullable = false, name = "manufacturer")
    private String manufacturer;
    
    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "clock")
    private float clock;
    
    @NotNull  
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "size_memory")
    private float sizeMemory;       
    
    @Override
    public Long getId() {
        return id;
    } 

    public InventoryMachine getMachine() {
        return machine;
    }

    public void setMachine(InventoryMachine machine) {
        this.machine = machine;
    }

    public String getTecnology() {
        return tecnology;
    }

    public void setTecnology(String tecnology) {
        this.tecnology = tecnology;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public float getClock() {
        return clock;
    }

    public void setClock(float clock) {
        this.clock = clock;
    }

    public float getSizeMemory() {
        return sizeMemory;
    }

    public void setSizeMemory(float sizeMemory) {
        this.sizeMemory = sizeMemory;
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
        if (!(object instanceof InventoryMemory)) {
            return false;
        }
        InventoryMemory other = (InventoryMemory) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
