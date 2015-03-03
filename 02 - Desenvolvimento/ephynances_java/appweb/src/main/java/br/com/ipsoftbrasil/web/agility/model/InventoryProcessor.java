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
@Table(name = "agility_inventory_processor")
@Entity
public class InventoryProcessor implements BaseModel {

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
    @Column(length = 250, nullable = false, name = "model")
    private String model;
    
    @NotNull
    @NotEmpty
    @Column(length = 100, nullable = false, name = "manufacturer")
    private String manufacturer;
    
    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "clock")
    private float clock;
    
    @NotNull
    @DefaultValue(value = "1")
    @Column(nullable = false, name = "cores")
    private Integer cores;
    
    @NotNull
    @NotEmpty
    @DefaultValue(value = "x86")
    @Column(length = 100, nullable = false, name = "architecture")
    private String architecture;
    
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public Integer getCores() {
        return cores;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
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
        if (!(object instanceof InventoryProcessor)) {
            return false;
        }
        InventoryProcessor other = (InventoryProcessor) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
