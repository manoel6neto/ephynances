package br.com.ipsoftbrasil.web.agility.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "agility_scheduled_task")
@Entity
public class ScheduledTask implements BaseModel {

    private static final long serialVersionUID = 456565187932147L;

    private static final int WAITING = 0;
    private static final int EXECUTE_FAILL = 1;
    private static final int EXECUTE_SUCESS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "status")
    private Integer status;

    @NotNull
    @NotEmpty
    @Column(length = 250, nullable = false, name = "command")
    private String command;
    
    @NotNull
    @NotEmpty
    @Column(length = 100, nullable = false, name = "task")
    private String task;    

    @Column(length = 100, nullable = true, name = "so")
    private String so;
    
    @Lob @Basic(fetch= FetchType.EAGER)
    @Column(name = "scheduled_parameter_1", nullable = true)
    private String scheduledParameter1;
    
    @Lob @Basic(fetch= FetchType.EAGER)
    @Column(name = "scheduled_parameter_2", nullable = true)
    private String scheduledParameter2;
    
    @NotNull
    @Column(name = "scheduled_datetime", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date scheduledDatetime;

    @JoinTable(name = "schedule_task_machine", joinColumns = {
        @JoinColumn(name = "schedule_task_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "machine_id", referencedColumnName = "id")})
    @ManyToMany
    private List<InventoryMachine> machineList;
    
    @Override
    public Long getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getScheduledParameter1() {
        return scheduledParameter1;
    }

    public void setScheduledParameter1(String scheduledParameter1) {
        this.scheduledParameter1 = scheduledParameter1;
    }

    public String getScheduledParameter2() {
        return scheduledParameter2;
    }

    public void setScheduledParameter2(String scheduledParameter2) {
        this.scheduledParameter2 = scheduledParameter2;
    }

    public List<InventoryMachine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<InventoryMachine> machineList) {
        this.machineList = machineList;
    }

    public Date getScheduledDatetime() {
        return scheduledDatetime;
    }

    public void setScheduledDatetime(Date scheduledDatetime) {
        this.scheduledDatetime = scheduledDatetime;
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
        if (!(object instanceof ScheduledTask)) {
            return false;
        }
        ScheduledTask other = (ScheduledTask) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
