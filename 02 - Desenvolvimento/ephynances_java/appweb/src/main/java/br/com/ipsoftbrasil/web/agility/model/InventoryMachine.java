package br.com.ipsoftbrasil.web.agility.model;

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
@Table(name = "agility_inventory_machine")
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
    private List<InventoryProcessor> machineProcessors;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryProcessorHistory> machineProcessorsHistory;

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
    private List<InventoryGPUHistory> machineGpusHistory;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryDisk> machineDisks;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryDiskHistory> machineDisksHistory;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryAudio> machineAudios;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryAudioHistory> machineAudiosHistory;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryMotherboard> machineMotherboard;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryMotherboardHistory> machineMotherboardsHistory;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryMachineMac> listMacs;

    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryAlert> listAlerts;

    @JoinTable(name = "schedule_task_machine", joinColumns = {
        @JoinColumn(name = "machine_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "schedule_task_id", referencedColumnName = "id")})
    @ManyToMany
    private List<ScheduledTask> scheduleTaskList;
    
    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventorySoftware> machineSoftwares;
    
    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryLogTestes> logsTestes;
    
    @OneToMany(mappedBy = "machine", orphanRemoval = true)
    private List<InventoryLogTasks> logsTasks;  
    
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
    public List<InventoryProcessor> getMachineProcessors() {
        return machineProcessors;
    }

    public void setMachineProcessors(List<InventoryProcessor> machineProcessors) {
        this.machineProcessors = machineProcessors;
    }

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

    public List<InventoryDisk> getMachineDisks() {
        return machineDisks;
    }

    public void setMachineDisks(List<InventoryDisk> machineDisks) {
        this.machineDisks = machineDisks;
    }

    public List<InventoryAudio> getMachineAudios() {
        return machineAudios;
    }

    public void setMachineAudios(List<InventoryAudio> machineAudios) {
        this.machineAudios = machineAudios;
    }

    public List<InventoryMotherboard> getMachineMotherboard() {
        return machineMotherboard;
    }

    public void setMachineMotherboard(List<InventoryMotherboard> machineMotherboard) {
        this.machineMotherboard = machineMotherboard;
    }

    public List<InventoryMachineMac> getListMacs() {
        return listMacs;
    }

    public void setListMacs(List<InventoryMachineMac> listMacs) {
        this.listMacs = listMacs;
    }

    public List<InventoryProcessorHistory> getMachineProcessorsHistory() {
        return machineProcessorsHistory;
    }

    public void setMachineProcessorsHistory(List<InventoryProcessorHistory> machineProcessorsHistory) {
        this.machineProcessorsHistory = machineProcessorsHistory;
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

    public List<InventoryGPUHistory> getMachineGpusHistory() {
        return machineGpusHistory;
    }

    public void setMachineGpusHistory(List<InventoryGPUHistory> machineGpusHistory) {
        this.machineGpusHistory = machineGpusHistory;
    }

    public List<InventoryDiskHistory> getMachineDisksHistory() {
        return machineDisksHistory;
    }

    public void setMachineDisksHistory(List<InventoryDiskHistory> machineDisksHistory) {
        this.machineDisksHistory = machineDisksHistory;
    }

    public List<InventoryAudioHistory> getMachineAudiosHistory() {
        return machineAudiosHistory;
    }

    public void setMachineAudiosHistory(List<InventoryAudioHistory> machineAudiosHistory) {
        this.machineAudiosHistory = machineAudiosHistory;
    }

    public List<InventoryMotherboardHistory> getMachineMotherboardsHistory() {
        return machineMotherboardsHistory;
    }

    public void setMachineMotherboardsHistory(List<InventoryMotherboardHistory> machineMotherboardsHistory) {
        this.machineMotherboardsHistory = machineMotherboardsHistory;
    }

    public List<InventoryAlert> getListAlerts() {
        return listAlerts;
    }

    public void setListAlerts(List<InventoryAlert> listAlerts) {
        this.listAlerts = listAlerts;
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

    public List<InventorySoftware> getMachineSoftwares() {
        return machineSoftwares;
    }

    public void setMachineSoftwares(List<InventorySoftware> machineSoftwares) {
        this.machineSoftwares = machineSoftwares;
    }

    public List<InventoryLogTestes> getLogsTestes() {
        return logsTestes;
    }

    public void setLogsTestes(List<InventoryLogTestes> logsTestes) {
        this.logsTestes = logsTestes;
    }

    public List<InventoryLogTasks> getLogsTasks() {
        return logsTasks;
    }

    public void setLogsTasks(List<InventoryLogTasks> logsTasks) {
        this.logsTasks = logsTasks;
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
