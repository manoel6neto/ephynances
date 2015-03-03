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
import javax.ws.rs.DefaultValue;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "ephynances_inventory_alert")
@Entity
public class InventoryAlert implements BaseModel {

    private static final long serialVersionUID = 456565187932147L;    
    public static final int ACTION_ADD_COMPONENT = 1;
    public static final int ACTION_REMOVE_COMPONENT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @NotEmpty    
    @Column(length = 250, nullable = false, name = "component")
    private String component; 
    
    @NotNull    
    @Column(nullable = false, name = "action_status")
    private Integer actionStatus;
    
    @NotNull
    @DefaultValue(value = "false")
    @Column(nullable = false, name = "send_email")
    private boolean sendEmail;
    
    @NotNull
    @DefaultValue(value = "false")
    @Column(nullable = false, name = "status")
    private boolean status;
    
    @NotNull
    @Column(name = "insert_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date insertDate;
    
    //References
    @OneToOne(optional = false)
    @NotNull   
    @JoinColumn(name = "machine_id", referencedColumnName = "id", nullable = false)
    private InventoryMachine machine;   
    
    @Override
    public Long getId() {
        return id;
    }    

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Integer actionStatus) {
        this.actionStatus = actionStatus;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public InventoryMachine getMachine() {
        return machine;
    }

    public void setMachine(InventoryMachine machine) {
        this.machine = machine;
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
        if (!(object instanceof InventoryAlert)) {
            return false;
        }
        InventoryAlert other = (InventoryAlert) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
