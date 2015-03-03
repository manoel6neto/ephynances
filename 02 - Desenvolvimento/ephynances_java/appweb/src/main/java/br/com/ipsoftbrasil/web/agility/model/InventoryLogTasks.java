package br.com.ipsoftbrasil.web.agility.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "agility_inventory_log_tasks")
@Entity
public class InventoryLogTasks implements BaseModel {

    private static final long serialVersionUID = 456565187932147L;    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob @Basic(fetch= FetchType.EAGER)
    @Column(nullable = false, name = "log")
    @NotNull
    @NotEmpty
    private String log;
    
    @NotNull
    @Column(name = "insert_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date insertDate;
    
    @JoinColumn(name = "machine_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private InventoryMachine machine;
    
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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
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
        if (!(object instanceof InventoryLogTasks)) {
            return false;
        }
        InventoryLogTasks other = (InventoryLogTasks) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
