package br.com.physisbrasil.web.ephynances.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "ephynances_inventory_audio_history")
@Entity
public class InventoryAudioHistory implements BaseModel {

    private static final long serialVersionUID = 456565187932147L; 
    public static final int ACTION_ADD_COMPONENT = 1;
    public static final int ACTION_REMOVE_COMPONENT = 2;

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
    @Column(nullable = false, name = "action_status")
    private Integer actionStatus;
    
    @NotNull
    @Column(name = "insert_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date insertDate;
    
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

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }        

    public Integer getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Integer actionStatus) {
        this.actionStatus = actionStatus;
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
        if (!(object instanceof InventoryAudioHistory)) {
            return false;
        }
        InventoryAudioHistory other = (InventoryAudioHistory) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
