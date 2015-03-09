package br.com.physisbrasil.web.ephynances.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Malvadeza
 */
@Table(name = "ephynances_inventory_machine")
@Entity
@CascadeOnDelete
public class InventoryMachine implements BaseModel {

    public static final String LINUX = "Linux";
    public static final String WINDOWS = "Windows";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(length = 100, nullable = false, name = "hostname")
    private String hostName;

    @NotNull
    @NotEmpty
    @Column(length = 100, nullable = false, name = "os")
    private String os;

    @NotNull
    @Column(length = 250, nullable = true, name = "os_version")
    private String osVersion;

    @NotNull
    @DefaultValue(value = "false")
    @Column(nullable = false, name = "status")
    private boolean status;
    
    @NotNull
    @DefaultValue(value = "false")
    @Column(nullable = false, name = "vnc_status")
    private boolean vncStatus;    

    //References    
    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryNetwork> machineNetworks;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryNetworkHistory> machineNetworksHistory;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryMemory> machineMemories;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryMemoryHistory> machineMemoriesHistory;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryGPU> machineGpus;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryMachineMac> listMacs;

    @JoinTable(name = "schedule_task_machine", joinColumns = {
        @JoinColumn(name = "machine_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "schedule_task_id", referencedColumnName = "id")})
    @ManyToMany
    private List<ScheduledTask> scheduleTaskList;
    
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @DefaultValue("1")
    private InventoryMachineGroup group;

    @Override
    public Long getId() {
        return id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isVncStatus() {
        return vncStatus;
    }

    public void setVncStatus(boolean vncStatus) {
        this.vncStatus = vncStatus;
    }

    //Get & set for references
    public List<InventoryNetwork> getMachineNetworks() {
        return machineNetworks;
    }

    public void setMachineNetworks(List<InventoryNetwork> machineNetworks) {
        this.machineNetworks = machineNetworks;
    }

    public List<InventoryMemory> getMachineMemories() {
        return machineMemories;
    }

    public void setMachineMemories(List<InventoryMemory> machineMemories) {
        this.machineMemories = machineMemories;
    }

    public List<InventoryGPU> getMachineGpus() {
        return machineGpus;
    }

    public void setMachineGpus(List<InventoryGPU> machineGpus) {
        this.machineGpus = machineGpus;
    }

    public List<InventoryMachineMac> getListMacs() {
        return listMacs;
    }

    public void setListMacs(List<InventoryMachineMac> listMacs) {
        this.listMacs = listMacs;
    }

    public List<InventoryNetworkHistory> getMachineNetworksHistory() {
        return machineNetworksHistory;
    }

    public void setMachineNetworksHistory(List<InventoryNetworkHistory> machineNetworksHistory) {
        this.machineNetworksHistory = machineNetworksHistory;
    }

    public List<InventoryMemoryHistory> getMachineMemoriesHistory() {
        return machineMemoriesHistory;
    }

    public void setMachineMemoriesHistory(List<InventoryMemoryHistory> machineMemoriesHistory) {
        this.machineMemoriesHistory = machineMemoriesHistory;
    }


    public InventoryMachineGroup getGroup() {
        return group;
    }

    public void setGroup(InventoryMachineGroup group) {
        this.group = group;
    }

    public List<ScheduledTask> getScheduleTaskList() {
        return scheduleTaskList;
    }

    public void setScheduleTaskList(List<ScheduledTask> scheduleTaskList) {
        this.scheduleTaskList = scheduleTaskList;
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
        if (!(object instanceof InventoryMachine)) {
            return false;
        }
        InventoryMachine other = (InventoryMachine) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
