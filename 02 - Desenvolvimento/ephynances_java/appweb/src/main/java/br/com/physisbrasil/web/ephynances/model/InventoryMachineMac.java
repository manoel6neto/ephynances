package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "ephynances_inventory_machine_mac", uniqueConstraints = @UniqueConstraint(columnNames = {"mac"}))
@Entity
public class InventoryMachineMac implements BaseModel {

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
    @Column(name = "mac", nullable = false, length = 50, unique = true)
    private String mac;
    
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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
        if (!(object instanceof InventoryMachineMac)) {
            return false;
        }
        InventoryMachineMac other = (InventoryMachineMac) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
