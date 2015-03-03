package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.InventoryAlertBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryAudioBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryDiskBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryGPUBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryLogTasksBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryMachineBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryMachineGroupBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryMemoryBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryMotherboardBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryNetworkBean;
import br.com.physisbrasil.web.ephynances.ejb.InventoryProcessorBean;
import br.com.physisbrasil.web.ephynances.ejb.UploadDriveBean;
import br.com.physisbrasil.web.ephynances.ejb.UploadImageBean;
import br.com.physisbrasil.web.ephynances.ejb.UploadSoftwareBean;
import br.com.physisbrasil.web.ephynances.model.InventoryAlert;
import br.com.physisbrasil.web.ephynances.model.InventoryAudio;
import br.com.physisbrasil.web.ephynances.model.InventoryAudioHistory;
import br.com.physisbrasil.web.ephynances.model.InventoryGPU;
import br.com.physisbrasil.web.ephynances.model.InventoryMotherboard;
import br.com.physisbrasil.web.ephynances.model.InventoryProcessor;
import br.com.physisbrasil.web.ephynances.model.InventoryMemory;
import br.com.physisbrasil.web.ephynances.model.InventoryDisk;
import br.com.physisbrasil.web.ephynances.model.InventoryDiskHistory;
import br.com.physisbrasil.web.ephynances.model.InventoryGPUHistory;
import br.com.physisbrasil.web.ephynances.model.InventoryLogTasks;
import br.com.physisbrasil.web.ephynances.model.InventoryMachine;
import br.com.physisbrasil.web.ephynances.model.InventoryMachineGroup;
import br.com.physisbrasil.web.ephynances.model.InventoryMemoryHistory;
import br.com.physisbrasil.web.ephynances.model.InventoryMotherboardHistory;
import br.com.physisbrasil.web.ephynances.model.InventoryNetwork;
import br.com.physisbrasil.web.ephynances.model.InventoryNetworkHistory;
import br.com.physisbrasil.web.ephynances.model.InventoryProcessorHistory;
import br.com.physisbrasil.web.ephynances.model.InventorySoftware;
import br.com.physisbrasil.web.ephynances.model.UploadDrive;
import br.com.physisbrasil.web.ephynances.model.UploadImage;
import br.com.physisbrasil.web.ephynances.model.UploadSoftware;
import br.com.physisbrasil.web.ephynances.util.Comunicacao;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.PaginationHelper;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Malvadeza
 */
@ManagedBean
@ViewScoped
public class InventoryMachineController extends BaseController {

    @EJB
    private InventoryMachineBean inventoryMachineBean;
    private PaginationHelper<InventoryMachine> pagination;
    private InventoryMachine inventoryMachine;
    private List<InventoryMachine> listMachinesSelectSO;
    private List<InventoryMachine> selectedMachines;
    private List<InventoryMachine> selectedMachinesTestHardware;
    private List<InventoryMachine> selectedMachinesInstallDrive;
    private List<InventoryMachine> selectedMachinesInstallSoftware;
    private List<InventoryMachine> selectedMachinesInstallImage;
    private InventoryMachineGroup inventoryMachineGroup;
    private List<InventoryMachine> selectedMachinesBackup;
    private List<InventoryMachine> listMachinesWindows;
    private List<InventoryMachine> selectedMachinesAdd;
    private List<InventoryMachine> selectedMachinesRemove;
    private List<String> selectedTests;
    private String tabID;
    private String selectSO;

    @EJB
    private UploadDriveBean uploadDriveBean;
    private UploadDrive uploadDrive;
    private List<UploadDrive> listDrives;

    @EJB
    private UploadSoftwareBean uploadSoftwareBean;
    private UploadSoftware uploadSoftware;
    private List<UploadSoftware> listSoftwares;
    private List<UploadSoftware> listSoftwaresSelectSO;

    @EJB
    private UploadImageBean uploadImageBean;
    private UploadImage uploadImage;
    private List<UploadImage> listImages;
    private List<UploadImage> listImagesSelectSO;

    @EJB
    private InventoryMachineGroupBean inventoryMachineGroupBean;

    @EJB
    private InventoryAlertBean inventoryAlertBean;

    @EJB
    private InventoryMotherboardBean inventoryMotherboardBean;

    @EJB
    private InventoryProcessorBean inventoryProcessorBean;

    @EJB
    private InventoryMemoryBean inventoryMemoryBean;

    @EJB
    private InventoryDiskBean inventoryDiskBean;

    @EJB
    private InventoryGPUBean inventoryGPUBean;

    @EJB
    private InventoryAudioBean inventoryAudioBean;

    @EJB
    private InventoryNetworkBean inventoryNetworkBean;

    @EJB
    private InventoryLogTasksBean inventoryLogTasksBean;

    private String globalFilter;

    @PostConstruct
    public void init() {
        InventoryMachine requestInventoryMachine = (InventoryMachine) getFlash("inventoryMachine");
        if (requestInventoryMachine != null) {
            inventoryMachineBean.clearCache();
            inventoryMachine = requestInventoryMachine;
            tabID = "0";
            tabID = (String) JsfUtil.getSessionAttribute("tabID");
        } else {
            tabID = "0";
            inventoryMachine = new InventoryMachine();
        }

        InventoryMachineGroup requestInventoryMachineGroup = (InventoryMachineGroup) getFlash("inventoryMachineGroup");
        if (requestInventoryMachineGroup != null) {
            inventoryMachineGroup = requestInventoryMachineGroup;
        } else {
            inventoryMachineGroup = new InventoryMachineGroup();
        }

        selectedTests = new ArrayList<String>();
        selectSO = "";
        listMachinesSelectSO = new ArrayList<InventoryMachine>();
        selectedMachines = new ArrayList<InventoryMachine>();
        selectedMachinesTestHardware = new ArrayList<InventoryMachine>();
        selectedMachinesInstallDrive = new ArrayList<InventoryMachine>();
        selectedMachinesInstallSoftware = new ArrayList<InventoryMachine>();
        selectedMachinesInstallImage = new ArrayList<InventoryMachine>();
        selectedMachinesBackup = new ArrayList<InventoryMachine>();
        selectedMachinesAdd = new ArrayList<InventoryMachine>();
        selectedMachinesRemove = new ArrayList<InventoryMachine>();
        listSoftwaresSelectSO = new ArrayList<UploadSoftware>();
        listImagesSelectSO = new ArrayList<UploadImage>();
        listMachinesWindows = new ArrayList<InventoryMachine>();

        if (uploadDrive == null) {
            uploadDrive = new UploadDrive();
        }

        if (listDrives == null) {
            listDrives = new ArrayList<UploadDrive>();
            listDrives = uploadDriveBean.findAll();
        }

        if (uploadSoftware == null) {
            uploadSoftware = new UploadSoftware();
        }

        if (listSoftwares == null) {
            listSoftwares = new ArrayList<UploadSoftware>();
            listSoftwares = uploadSoftwareBean.findAll();
        }

        if (uploadImage == null) {
            uploadImage = new UploadImage();
        }

        if (listImages == null) {
            listImages = new ArrayList<UploadImage>();
            listImages = uploadImageBean.findAll();
        }

        loadConfigUIOS();

        putFlash("inventoryMachine", null);
        JsfUtil.setSessionAttribute("tabID", "0");
    }

    public void loadConfigUIOS() {
        listMachinesSelectSO.clear();
        listMachinesWindows.clear();
        if (selectSO.equalsIgnoreCase("")) {
            listMachinesSelectSO = inventoryMachineBean.findAll();
        } else {
            for (InventoryMachine inventoryMachine : inventoryMachineBean.findAll()) {
                if (inventoryMachine.getOs().contains("Windows")) {
                    listMachinesWindows.add(inventoryMachine);
                    if (selectSO.equalsIgnoreCase("Windows")) {
                        listMachinesSelectSO.add(inventoryMachine);
                    }
                }

                if (!inventoryMachine.getOs().contains("Windows") && selectSO.equalsIgnoreCase("Linux")) {
                    listMachinesSelectSO.add(inventoryMachine);
                }
            }
        }

        listSoftwaresSelectSO.clear();
        for (UploadSoftware uploadSoftware : uploadSoftwareBean.findAll()) {
            if (uploadSoftware.getOs().contains("Windows") && selectSO.equalsIgnoreCase("Windows")) {
                listSoftwaresSelectSO.add(uploadSoftware);
            }

            if (!uploadSoftware.getOs().contains("Windows") && selectSO.equalsIgnoreCase("Linux")) {
                listSoftwaresSelectSO.add(uploadSoftware);
            }
        }

        listImagesSelectSO.clear();
        for (UploadImage uploadImage : uploadImageBean.findAll()) {
            if (uploadImage.getOs().contains("Windows") && selectSO.equalsIgnoreCase("Windows")) {
                listImagesSelectSO.add(uploadImage);
            }

            if (!uploadImage.getOs().contains("Windows") && selectSO.equalsIgnoreCase("Linux")) {
                listImagesSelectSO.add(uploadImage);
            }
        }
    }

    public String listHardware(InventoryMachine inventoryMachine) {
        putFlash("inventoryMachine", inventoryMachine);
        JsfUtil.setSessionAttribute("tabID", "0");
        return "/inventoryMachine/list";
    }

    public String listSoftware(InventoryMachine inventoryMachine) {
        putFlash("inventoryMachine", inventoryMachine);
        JsfUtil.setSessionAttribute("tabID", "1");
        return "/inventoryMachine/list";
    }

    public String listLogTasks(InventoryMachine inventoryMachine) {
        putFlash("inventoryMachine", inventoryMachine);
        JsfUtil.setSessionAttribute("tabID", "2");
        return "/inventoryMachine/list";
    }

    public String listLogTests(InventoryMachine inventoryMachine) {
        putFlash("inventoryMachine", inventoryMachine);
        JsfUtil.setSessionAttribute("tabID", "3");
        return "/inventoryMachine/list";
    }

    public String vnc(InventoryMachine inventoryMachine) {
        putFlash("inventoryMachine", inventoryMachine);
        JsfUtil.setSessionAttribute("tabID", "4");
        return "/inventoryMachine/list";
    }

    public String addSoftware() {
        return "/uploadSoftware/create";
    }

    public String addDrive() {
        return "/uploadDrive/create";
    }

    public String addImage() {
        return "/uploadImage/create";
    }

    public String manage(InventoryMachineGroup inventoryMachineGroup) {
        try {
            if (inventoryMachineGroup.getId() != null && inventoryMachineGroup.getId() > 0) {
                this.inventoryMachineGroup = inventoryMachineGroupBean.find(inventoryMachineGroup.getId());
                putFlash("inventoryMachineGroup", this.inventoryMachineGroup);
                putFlash("inventoryMachine", this.inventoryMachine);
            } else {
                JsfUtil.addErrorMessage("Objeto não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "manage";
    }

    public String listIps(InventoryMachine inventoryMachine) {
        String ips = "";

        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
            ips += " [" + inventoryNetwork.getIpv4() + "]";
        }

        return ips;
    }

    public String getListMotherboard() {
        String listMotherboard = "";

        for (InventoryMotherboard inventoryMotherboard : inventoryMachine.getMachineMotherboard()) {
            listMotherboard += " Fabricante: " + inventoryMotherboard.getManufacturer();
            listMotherboard += " Modelo: " + inventoryMotherboard.getModel();
            listMotherboard += " Versão: " + inventoryMotherboard.getVersion() + "  |  ";
        }

        return listMotherboard;
    }

    public String getListDefaultMotherboard() {
        try {
            String listDefaultMotherboard = "";
            Date defaultDate = inventoryMachine.getMachineMotherboardsHistory().get(0).getInsertDate();

            for (InventoryMotherboardHistory inventoryMotherboardHistory : inventoryMachine.getMachineMotherboardsHistory()) {
                if (inventoryMotherboardHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultMotherboard += " Fabricante: " + inventoryMotherboardHistory.getManufacturer();
                    listDefaultMotherboard += " Modelo: " + inventoryMotherboardHistory.getModel();
                    listDefaultMotherboard += " Versão: " + inventoryMotherboardHistory.getVersion() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultMotherboard;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesMotherboard() {
        try {
            String listChangesMotherboard = "";
            Date defaultDate = inventoryMachine.getMachineMotherboardsHistory().get(0).getInsertDate();

            for (InventoryMotherboardHistory inventoryMotherboardHistory : inventoryMachine.getMachineMotherboardsHistory()) {
                if (!inventoryMotherboardHistory.getInsertDate().equals(defaultDate)) {
                    listChangesMotherboard += " Fabricante: " + inventoryMotherboardHistory.getManufacturer();
                    listChangesMotherboard += " Modelo: " + inventoryMotherboardHistory.getModel();
                    listChangesMotherboard += " Versão: " + inventoryMotherboardHistory.getVersion() + "  |  ";
                }
            }

            return listChangesMotherboard;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListProcessors() {
        String listProcessors = "";

        for (InventoryProcessor inventoryProcessors : inventoryMachine.getMachineProcessors()) {
            listProcessors += " Fabricante: " + inventoryProcessors.getManufacturer();
            listProcessors += " Modelo: " + inventoryProcessors.getModel();
            listProcessors += " Clock: " + String.valueOf(inventoryProcessors.getClock());
            listProcessors += " Núcleos: " + String.valueOf(inventoryProcessors.getCores());
            listProcessors += " Arquitetura: " + inventoryProcessors.getArchitecture() + "  |  ";
        }

        return listProcessors;
    }

    public String getListDefaultProcessors() {
        try {
            String listDefaultProcessors = "";
            Date defaultDate = inventoryMachine.getMachineProcessorsHistory().get(0).getInsertDate();

            for (InventoryProcessorHistory inventoryProcessorHistory : inventoryMachine.getMachineProcessorsHistory()) {
                if (inventoryProcessorHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultProcessors += " Fabricante: " + inventoryProcessorHistory.getManufacturer();
                    listDefaultProcessors += " Modelo: " + inventoryProcessorHistory.getModel();
                    listDefaultProcessors += " Clock: " + String.valueOf(inventoryProcessorHistory.getClock());
                    listDefaultProcessors += " Núcleos: " + String.valueOf(inventoryProcessorHistory.getCores());
                    listDefaultProcessors += " Arquitetura: " + inventoryProcessorHistory.getArchitecture() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultProcessors;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesProcessors() {
        try {
            String listChangesProcessors = "";
            Date defaultDate = inventoryMachine.getMachineProcessorsHistory().get(0).getInsertDate();

            for (InventoryProcessorHistory inventoryProcessorHistory : inventoryMachine.getMachineProcessorsHistory()) {
                if (!inventoryProcessorHistory.getInsertDate().equals(defaultDate)) {
                    listChangesProcessors += " Fabricante: " + inventoryProcessorHistory.getManufacturer();
                    listChangesProcessors += " Modelo: " + inventoryProcessorHistory.getModel();
                    listChangesProcessors += " Clock: " + String.valueOf(inventoryProcessorHistory.getClock());
                    listChangesProcessors += " Núcleos: " + String.valueOf(inventoryProcessorHistory.getCores());
                    listChangesProcessors += " Arquitetura: " + inventoryProcessorHistory.getArchitecture() + "  |  ";
                }
            }

            return listChangesProcessors;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListMemories() {
        String listMemories = "";

        for (InventoryMemory inventoryMemory : inventoryMachine.getMachineMemories()) {
            listMemories += " Fabricante: " + inventoryMemory.getManufacturer();
            listMemories += " Clock: " + String.valueOf(inventoryMemory.getClock());
            listMemories += " Tamanho: " + String.valueOf(inventoryMemory.getSizeMemory());
            listMemories += " Tecnologia: " + inventoryMemory.getTecnology() + "  |  ";
        }

        return listMemories;
    }

    public String getListDefaultMemories() {
        try {
            String listDefaultMemories = "";
            Date defaultDate = inventoryMachine.getMachineMemoriesHistory().get(0).getInsertDate();

            for (InventoryMemoryHistory inventoryMemoryHistory : inventoryMachine.getMachineMemoriesHistory()) {
                if (inventoryMemoryHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultMemories += " Fabricante: " + inventoryMemoryHistory.getManufacturer();
                    listDefaultMemories += " Clock: " + String.valueOf(inventoryMemoryHistory.getClock());
                    listDefaultMemories += " Tamanho: " + String.valueOf(inventoryMemoryHistory.getSizeMemory());
                    listDefaultMemories += " Tecnologia: " + inventoryMemoryHistory.getTecnology() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultMemories;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesMemories() {
        try {
            String listChangesMemories = "";
            Date defaultDate = inventoryMachine.getMachineMemoriesHistory().get(0).getInsertDate();

            for (InventoryMemoryHistory inventoryMemoryHistory : inventoryMachine.getMachineMemoriesHistory()) {
                if (!inventoryMemoryHistory.getInsertDate().equals(defaultDate)) {
                    listChangesMemories += " Fabricante: " + inventoryMemoryHistory.getManufacturer();
                    listChangesMemories += " Clock: " + String.valueOf(inventoryMemoryHistory.getClock());
                    listChangesMemories += " Tamanho: " + String.valueOf(inventoryMemoryHistory.getSizeMemory());
                    listChangesMemories += " Tecnologia: " + inventoryMemoryHistory.getTecnology() + "  |  ";
                }
            }

            return listChangesMemories;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListDisks() {
        String listDisks = "";

        for (InventoryDisk inventoryDisk : inventoryMachine.getMachineDisks()) {
            listDisks += " Fabricante: " + inventoryDisk.getManufacturer();
            listDisks += " Modelo: " + inventoryDisk.getModel();
            listDisks += " Tamanho: " + String.valueOf(inventoryDisk.getSizeDisk());
            listDisks += " Serial: " + inventoryDisk.getSerial() + "  |  ";
        }

        return listDisks;
    }

    public String getListDefaultDisks() {
        try {
            String listDefaultDisks = "";
            Date defaultDate = inventoryMachine.getMachineDisksHistory().get(0).getInsertDate();

            for (InventoryDiskHistory inventoryDiskHistory : inventoryMachine.getMachineDisksHistory()) {
                if (inventoryDiskHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultDisks += " Fabricante: " + inventoryDiskHistory.getManufacturer();
                    listDefaultDisks += " Modelo: " + inventoryDiskHistory.getModel();
                    listDefaultDisks += " Tamanho: " + String.valueOf(inventoryDiskHistory.getSizeDisk());
                    listDefaultDisks += " Serial: " + inventoryDiskHistory.getSerial() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultDisks;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesDisks() {
        try {
            String listChangesDisks = "";
            Date defaultDate = inventoryMachine.getMachineDisksHistory().get(0).getInsertDate();

            for (InventoryDiskHistory inventoryDiskHistory : inventoryMachine.getMachineDisksHistory()) {
                if (!inventoryDiskHistory.getInsertDate().equals(defaultDate)) {
                    listChangesDisks += " Fabricante: " + inventoryDiskHistory.getManufacturer();
                    listChangesDisks += " Modelo: " + inventoryDiskHistory.getModel();
                    listChangesDisks += " Tamanho: " + String.valueOf(inventoryDiskHistory.getSizeDisk());
                    listChangesDisks += " Serial: " + inventoryDiskHistory.getSerial() + "  |  ";
                }
            }

            return listChangesDisks;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListAudios() {
        String listAudios = "";

        for (InventoryAudio inventoryAudio : inventoryMachine.getMachineAudios()) {
            listAudios += " Fabricante: " + inventoryAudio.getManufacturer();
            listAudios += " Modelo: " + inventoryAudio.getModel() + "  |  ";
        }

        return listAudios;
    }

    public String getListDefaultAudios() {
        try {
            String listDefaultAudios = "";
            Date defaultDate = inventoryMachine.getMachineAudiosHistory().get(0).getInsertDate();

            for (InventoryAudioHistory inventoryAudioHistory : inventoryMachine.getMachineAudiosHistory()) {
                if (inventoryAudioHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultAudios += " Fabricante: " + inventoryAudioHistory.getManufacturer();
                    listDefaultAudios += " Modelo: " + inventoryAudioHistory.getModel() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultAudios;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesAudios() {
        try {
            String listChangesAudios = "";
            Date defaultDate = inventoryMachine.getMachineAudiosHistory().get(0).getInsertDate();

            for (InventoryAudioHistory inventoryAudioHistory : inventoryMachine.getMachineAudiosHistory()) {
                if (!inventoryAudioHistory.getInsertDate().equals(defaultDate)) {
                    listChangesAudios += " Fabricante: " + inventoryAudioHistory.getManufacturer();
                    listChangesAudios += " Modelo: " + inventoryAudioHistory.getModel() + "  |  ";
                }
            }

            return listChangesAudios;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListGpus() {
        String listGpus = "";

        for (InventoryGPU inventoryGPU : inventoryMachine.getMachineGpus()) {
            listGpus += " Fabricante: " + inventoryGPU.getManufacturer();
            listGpus += " Modelo: " + inventoryGPU.getModel();
            listGpus += " Versão: " + inventoryGPU.getVersion() + "  |  ";
        }

        return listGpus;
    }

    public String getListDefaultGpus() {
        try {
            String listDefaultGpus = "";
            Date defaultDate = inventoryMachine.getMachineGpusHistory().get(0).getInsertDate();

            for (InventoryGPUHistory inventoryGPUHistory : inventoryMachine.getMachineGpusHistory()) {
                if (inventoryGPUHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultGpus += " Fabricante: " + inventoryGPUHistory.getManufacturer();
                    listDefaultGpus += " Modelo: " + inventoryGPUHistory.getModel();
                    listDefaultGpus += " Versão: " + inventoryGPUHistory.getVersion() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultGpus;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesGpus() {
        try {
            String listChangesGpus = "";
            Date defaultDate = inventoryMachine.getMachineGpusHistory().get(0).getInsertDate();

            for (InventoryGPUHistory inventoryGPUHistory : inventoryMachine.getMachineGpusHistory()) {
                if (!inventoryGPUHistory.getInsertDate().equals(defaultDate)) {
                    listChangesGpus += " Fabricante: " + inventoryGPUHistory.getManufacturer();
                    listChangesGpus += " Modelo: " + inventoryGPUHistory.getModel();
                    listChangesGpus += " Versão: " + inventoryGPUHistory.getVersion() + "  |  ";
                }
            }

            return listChangesGpus;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListNetworks() {
        String listNetworks = "";

        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
            listNetworks += " Fabricante: " + inventoryNetwork.getManufacturer();
            listNetworks += " Modelo: " + inventoryNetwork.getModel();
            listNetworks += " Tipo: " + inventoryNetwork.getType();
            listNetworks += " Velocidade: " + String.valueOf(inventoryNetwork.getSpeed());
            listNetworks += " MAC: " + inventoryNetwork.getMac();
            listNetworks += " IPV4: " + inventoryNetwork.getIpv4();
            listNetworks += " IPV6: " + inventoryNetwork.getIpv6() + "  |  ";
        }

        return listNetworks;
    }

    public String getListDefaultNetworks() {
        try {
            String listDefaultNetworks = "";
            Date defaultDate = inventoryMachine.getMachineNetworksHistory().get(0).getInsertDate();

            for (InventoryNetworkHistory inventoryNetworkHistory : inventoryMachine.getMachineNetworksHistory()) {
                if (inventoryNetworkHistory.getInsertDate().equals(defaultDate)) {
                    listDefaultNetworks += " Fabricante: " + inventoryNetworkHistory.getManufacturer();
                    listDefaultNetworks += " Modelo: " + inventoryNetworkHistory.getModel();
                    listDefaultNetworks += " Tipo: " + inventoryNetworkHistory.getType();
                    listDefaultNetworks += " Velocidade: " + String.valueOf(inventoryNetworkHistory.getSpeed());
                    listDefaultNetworks += " MAC: " + inventoryNetworkHistory.getMac();
                    listDefaultNetworks += " IPV4: " + inventoryNetworkHistory.getIpv4();
                    listDefaultNetworks += " IPV6: " + inventoryNetworkHistory.getIpv6() + "  |  ";
                } else {
                    break;
                }
            }

            return listDefaultNetworks;
        } catch (Exception e) {
            return "";
        }
    }

    public String getListChangesNetworks() {
        try {
            String listChangesNetworks = "";
            Date defaultDate = inventoryMachine.getMachineNetworksHistory().get(0).getInsertDate();

            for (InventoryNetworkHistory inventoryNetworkHistory : inventoryMachine.getMachineNetworksHistory()) {
                if (!inventoryNetworkHistory.getInsertDate().equals(defaultDate)) {
                    listChangesNetworks += " Fabricante: " + inventoryNetworkHistory.getManufacturer();
                    listChangesNetworks += " Modelo: " + inventoryNetworkHistory.getModel();
                    listChangesNetworks += " Tipo: " + inventoryNetworkHistory.getType();
                    listChangesNetworks += " Velocidade: " + String.valueOf(inventoryNetworkHistory.getSpeed());
                    listChangesNetworks += " MAC: " + inventoryNetworkHistory.getMac();
                    listChangesNetworks += " IPV4: " + inventoryNetworkHistory.getIpv4();
                    listChangesNetworks += " IPV6: " + inventoryNetworkHistory.getIpv6() + "  |  ";
                }
            }

            return listChangesNetworks;
        } catch (Exception e) {
            return "";
        }
    }

    public List<List<String>> getListChangesHardware() {
        try {
            List<List<String>> listChangesHardware = new ArrayList<List<String>>();
            if (!this.getListChangesMotherboard().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("Placa mãe", this.getListChangesMotherboard())));
            }

            if (!this.getListChangesProcessors().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("Processador", this.getListChangesProcessors())));
            }

            if (!this.getListChangesMemories().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("Memória", this.getListChangesMemories())));
            }

            if (!this.getListChangesDisks().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("HD", this.getListChangesDisks())));
            }

            if (!this.getListChangesAudios().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("Audio", this.getListChangesAudios())));
            }

            if (!this.getListChangesGpus().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("Vídeo", this.getListChangesGpus())));
            }

            if (!this.getListChangesNetworks().isEmpty()) {
                listChangesHardware.add(new ArrayList<String>(asList("Rede", this.getListChangesNetworks())));
            }

            return listChangesHardware;
        } catch (Exception e) {
            return new ArrayList<List<String>>();
        }
    }

    public List<String> getListSoftware() {
        try {
            List<String> listSoftwares = new ArrayList<String>();
            for (InventorySoftware inventorySoftware : inventoryMachine.getMachineSoftwares()) {
                listSoftwares.add(inventorySoftware.getNome());
            }

            return listSoftwares;
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public void updateTable() {
        inventoryMachineBean.clearCache();

        List<InventoryMachine> listMachinesSelectSOAux = new ArrayList<InventoryMachine>();
        for (InventoryMachine inventoryMachine : listMachinesSelectSO) {
            listMachinesSelectSOAux.add(inventoryMachineBean.find(inventoryMachine.getId()));
        }
        listMachinesSelectSO = listMachinesSelectSOAux;

        List<InventoryMachine> listMachinesWindowsAux = new ArrayList<InventoryMachine>();
        for (InventoryMachine inventoryMachine : listMachinesWindows) {
            listMachinesWindowsAux.add(inventoryMachineBean.find(inventoryMachine.getId()));
        }
        listMachinesWindows = listMachinesWindowsAux;
    }

    public void LogTasks(List<InventoryMachine> selectedMachines, String log) {
        for (InventoryMachine inventoryMachine : selectedMachines) {
            InventoryLogTasks inventoryLogTasks = new InventoryLogTasks();
            inventoryLogTasks.setInsertDate(new Date());
            inventoryLogTasks.setLog(log);
            inventoryLogTasks.setMachine(inventoryMachine);
            inventoryLogTasksBean.create(inventoryLogTasks);
        }
    }

    public void poweron() {
        try {
            if (selectedMachines.size() > 0) {
                //Conectando
                Comunicacao co = new Comunicacao();

                if (co.Connect("localhost", 7000)) {
                    //Enviando comandos
                    Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                    for (InventoryMachine inventoryMachine : selectedMachines) {
                        Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                            if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                    && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                            }
                        }
                        dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                    }

                    ArrayList<String> retorno = co.sendToken("--bin=1 poe");
                    if (retorno.get(0).equalsIgnoreCase("ok")) {
                        JSONObject jSONObject = new JSONObject(dicInfoMachine);
                        retorno = co.sendToken(jSONObject.toString());
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            this.LogTasks(selectedMachines, "Enviando comando para ligar equipamento.");
                            JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                        } else {
                            JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }

                    co.closeConnection();
                } else {
                    JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachines.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void poweroff() {
        try {
            if (selectedMachines.size() > 0) {
                //Conectando
                Comunicacao co = new Comunicacao();
                if (co.Connect("localhost", 7000)) {
                    //Enviando comandos
                    Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                    for (InventoryMachine inventoryMachine : selectedMachines) {
                        Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                            if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                    && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                            }
                        }
                        dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                    }

                    ArrayList<String> retorno = co.sendToken("--bin=1 shutdown");
                    if (retorno.get(0).equalsIgnoreCase("ok")) {
                        JSONObject jSONObject = new JSONObject(dicInfoMachine);
                        retorno = co.sendToken(jSONObject.toString());
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            this.LogTasks(selectedMachines, "Enviando comando para desligar equipamento.");
                            JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                        } else {
                            JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }

                    co.closeConnection();
                } else {
                    JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachines.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void restart() {
        try {
            if (selectedMachines.size() > 0) {
                //Conectando
                Comunicacao co = new Comunicacao();
                if (co.Connect("localhost", 7000)) {
                    //Enviando comandos
                    ArrayList<String> listIPs = new ArrayList<String>();
                    //Enviando comandos
                    Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                    for (InventoryMachine inventoryMachine : selectedMachines) {
                        Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                            if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                    && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                            }
                        }
                        dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                    }

                    ArrayList<String> retorno = co.sendToken("--bin=1 restart");
                    if (retorno.get(0).equalsIgnoreCase("ok")) {
                        JSONObject jSONObject = new JSONObject(dicInfoMachine);
                        retorno = co.sendToken(jSONObject.toString());
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            this.LogTasks(selectedMachines, "Enviando comando para reiniciar equipamento.");
                            JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                        } else {
                            JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }

                    co.closeConnection();
                } else {
                    JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachines.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void loadingVnc() {
        try {
            if (selectedMachines.size() > 0) {
                //Conectando
                Comunicacao co = new Comunicacao();
                if (co.Connect("localhost", 7000)) {
                    //Enviando comandos
                    Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                    for (InventoryMachine inventoryMachine : selectedMachines) {
                        Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                            if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                    && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                            }
                        }
                        dicInfoMachine.put(inventoryMachine.getId().toString(), dicInfoNetwork);
                    }

                    ArrayList<String> retorno = co.sendToken("--bin=1 vnc");
                    if (retorno.get(0).equalsIgnoreCase("ok")) {
                        JSONObject jSONObject = new JSONObject(dicInfoMachine);
                        retorno = co.sendToken(jSONObject.toString());
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            this.LogTasks(selectedMachines, "Enviando comando para iniciar o equipamento com vnc.");
                            JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                        } else {
                            JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }

                    co.closeConnection();
                } else {
                    JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachines.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void installSoftware() {
        try {
            if (selectedMachinesInstallSoftware.size() > 0) {
                if (uploadSoftware.getId() != null) {
                    //Conectando
                    Comunicacao co = new Comunicacao();

                    if (co.Connect("localhost", 7000)) {
                        //Enviando comandos
                        Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                        for (InventoryMachine inventoryMachine : selectedMachinesInstallSoftware) {
                            Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                            for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                                if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                        && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                    dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                                }
                            }
                            dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                        }

                        ArrayList<String> retorno;

                        if (uploadSoftwareBean.find(uploadSoftware.getId()).getSoftwareUrl() == null) {
                            retorno = co.sendToken(String.format("--filename=%s --bin=1 installsoftware", uploadSoftwareBean.find(uploadSoftware.getId()).getName()));
                        } else {
                            retorno = co.sendToken(String.format("--filename=%s --bin=1 installsoftware", uploadSoftwareBean.find(uploadSoftware.getId()).getSoftwareUrl().split("/")[3]));
                        }

                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            JSONObject jSONObject = new JSONObject(dicInfoMachine);
                            retorno = co.sendToken(jSONObject.toString());
                            if (retorno.get(0).equalsIgnoreCase("ok")) {
                                this.LogTasks(selectedMachinesInstallSoftware, "Enviando comando para instalar software no equipamento.");
                                JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                            } else {
                                JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                        }
                        co.closeConnection();
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }
                } else {
                    JsfUtil.addErrorMessage("Um software deve ser selecionado.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectSO = "";
            loadConfigUIOS();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void installDrive() {
        try {
            if (selectedMachinesInstallDrive.size() > 0) {
                if (uploadDrive.getId() != null) {
                    //Conectando
                    Comunicacao co = new Comunicacao();

                    if (co.Connect("localhost", 7000)) {
                        //Enviando comandos
                        Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                        for (InventoryMachine inventoryMachine : selectedMachinesInstallDrive) {
                            Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                            for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                                if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                        && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                    dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                                }
                            }
                            dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                        }

                        ArrayList<String> retorno = co.sendToken(String.format("--filename=%s --bin=1 installdrive", uploadDriveBean.find(uploadDrive.getId()).getDriveUrl().split("/")[3]));
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            //Envio do dicionário contendo os IPs e mac de cada maquina
                            JSONObject jSONObject = new JSONObject(dicInfoMachine);
                            retorno = co.sendToken(jSONObject.toString());
                            if (retorno.get(0).equalsIgnoreCase("ok")) {
                                this.LogTasks(selectedMachinesInstallDrive, "Enviando comando para instalar drive no equipamento.");
                                JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                            } else {
                                JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                        }

                        co.closeConnection();
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }
                } else {
                    JsfUtil.addErrorMessage("Um drive deve ser selecionado.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectSO = "";
            loadConfigUIOS();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void installImage() {
        try {
            if (selectedMachinesInstallImage.size() > 0) {
                if (uploadImage.getId() != null) {
                    //Conectando
                    Comunicacao co = new Comunicacao();

                    if (co.Connect("localhost", 7000)) {
                        //Enviando comandos
                        Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                        for (InventoryMachine inventoryMachine : selectedMachinesInstallImage) {
                            Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                            for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                                if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                        && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                    dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                                }
                            }
                            dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                        }

                        ArrayList<String> retorno = co.sendToken(String.format("--imageid=%s --bin=1 baixarimagem", uploadImageBean.find(uploadImage.getId()).getId().toString()));
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            //Envio do dicionário contendo os IPs e mac de cada maquina
                            JSONObject jSONObject = new JSONObject(dicInfoMachine);
                            retorno = co.sendToken(jSONObject.toString());
                            if (retorno.get(0).equalsIgnoreCase("ok")) {
                                this.LogTasks(selectedMachinesInstallImage, "Enviando comando para instalar imagem no equipamento.");
                                JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                            } else {
                                JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                        }

                        co.closeConnection();
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }
                } else {
                    JsfUtil.addErrorMessage("Uma imagem deve ser selecionada.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectSO = "";
            loadConfigUIOS();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void testHardware() {
        try {
            if (selectedMachinesTestHardware.size() > 0) {
                if (selectedTests.size() > 0) {
                    //Conectando
                    Comunicacao co = new Comunicacao();

                    if (co.Connect("localhost", 7000)) {
                        //Enviando comandos
                        Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                        for (InventoryMachine inventoryMachine : selectedMachinesTestHardware) {
                            Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                            for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                                if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                        && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                    dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                                }
                            }
                            dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                        }

                        ArrayList<String> retorno = co.sendToken("--bin=2 testehardware");
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            Map<String, Boolean> dicTests = new HashMap<String, Boolean>();
                            dicTests.put("processador", false);
                            dicTests.put("memoria", false);
                            dicTests.put("hd", false);
                            for (String componente : selectedTests) {
                                if (dicTests.containsKey(componente)) {
                                    dicTests.put(componente, true);
                                }
                            }

                            //Envio do dicionário contendo os testes a serem realizados
                            JSONObject jSONObject = new JSONObject(dicTests);
                            retorno = co.sendToken(jSONObject.toString());
                            if (retorno.get(0).equalsIgnoreCase("ok")) {
                                //Envio do dicionário contendo os IPs e mac de cada maquina
                                jSONObject = new JSONObject(dicInfoMachine);
                                retorno = co.sendToken(jSONObject.toString());
                                if (retorno.get(0).equalsIgnoreCase("ok")) {
                                    this.LogTasks(selectedMachinesTestHardware, "Enviando comando para testar hardware do equipamento.");
                                    JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                                } else {
                                    JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                                }
                            } else {
                                JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                        }

                        co.closeConnection();
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }
                } else {
                    JsfUtil.addErrorMessage("Pelo menos um teste deve ser selecionado.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachinesTestHardware.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void createBackup() {
        try {
            if (selectedMachinesBackup.size() > 0) {
                //Conectando
                Comunicacao co = new Comunicacao();

                if (co.Connect("localhost", 7000)) {
                    //Enviando comandos
                    Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                    for (InventoryMachine inventoryMachine : selectedMachinesBackup) {
                        Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                            if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                    && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                            }
                        }
                        dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                    }

                    ArrayList<String> retorno = co.sendToken("--bin=1 gerarbackup");
                    if (retorno.get(0).equalsIgnoreCase("ok")) {
                        //Envio do dicionário contendo os IPs e mac de cada maquina
                        JSONObject jSONObject = new JSONObject(dicInfoMachine);
                        retorno = co.sendToken(jSONObject.toString());
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            this.LogTasks(selectedMachinesBackup, "Enviando comando para criar backup do equipamento.");
                            JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                        } else {
                            JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }

                    co.closeConnection();
                } else {
                    JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachinesBackup.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public void restoreBackup() {
        try {
            if (selectedMachinesBackup.size() > 0) {
                //Conectando
                Comunicacao co = new Comunicacao();

                if (co.Connect("localhost", 7000)) {
                    //Enviando comandos
                    Map<String, Map<String, String>> dicInfoMachine = new HashMap<String, Map<String, String>>();
                    for (InventoryMachine inventoryMachine : selectedMachinesBackup) {
                        Map<String, String> dicInfoNetwork = new HashMap<String, String>();
                        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
                            if (!inventoryNetwork.getIpv4().isEmpty() && !inventoryNetwork.getMac().isEmpty()
                                    && inventoryNetwork.getIpv4() != null && inventoryNetwork.getMac() != null) {
                                dicInfoNetwork.put(inventoryNetwork.getIpv4(), inventoryNetwork.getMac());
                            }
                        }
                        dicInfoMachine.put(inventoryMachine.getHostName(), dicInfoNetwork);
                    }

                    ArrayList<String> retorno = co.sendToken("--bin=1 restaurarbackup");
                    if (retorno.get(0).equalsIgnoreCase("ok")) {
                        //Envio do dicionário contendo os IPs e mac de cada maquina
                        JSONObject jSONObject = new JSONObject(dicInfoMachine);
                        retorno = co.sendToken(jSONObject.toString());
                        if (retorno.get(0).equalsIgnoreCase("ok")) {
                            this.LogTasks(selectedMachinesBackup, "Enviando comando para restaurar backup do equipamento.");
                            JsfUtil.addSuccessMessage("Comando enviado para servidor.");
                        } else {
                            JsfUtil.addErrorMessage("Falha no envio do comando para servidor.");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                    }

                    co.closeConnection();
                } else {
                    JsfUtil.addErrorMessage("Falha ao conectar no servidor.");
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachinesBackup.clear();
        } catch (UnknownHostException ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao conectar no servidor.");
        } catch (IOException ex) {
            JsfUtil.addErrorMessage(ex, "Falha desconectar no servidor.");
        }
    }

    public String addMachines() {
        try {
            if (!selectedMachinesAdd.isEmpty()) {
                for (InventoryMachine inventoryMachine : selectedMachinesAdd) {
                    if (inventoryMachine.getId() != null && inventoryMachine.getId() > 0) {
                        inventoryMachine.setGroup(inventoryMachineGroupBean.find(inventoryMachineGroup.getId()));
                        inventoryMachineBean.edit(inventoryMachine);
                    }
                }
                JsfUtil.addSuccessMessage("Maquina(s) adicionada(s) com sucesso!");
            } else {
                JsfUtil.addErrorMessage("Selecione pelo menos uma maquina para ser adicionada ao grupo.");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao tentar adicionar máquina(s) do grupo.");
        }

        inventoryMachineGroupBean.clearCache();
        this.inventoryMachineGroup = inventoryMachineGroupBean.find(this.inventoryMachineGroup.getId());
        putFlash("inventoryMachineGroup", this.inventoryMachineGroup);
        return "manage";
    }

    public String removeMachines() {
        try {
            if (!selectedMachinesRemove.isEmpty()) {
                InventoryMachineGroup inventoryMachineGroupDefault = new InventoryMachineGroup();
                inventoryMachineGroupDefault = inventoryMachineGroupBean.find(Long.parseLong("1"));
                for (InventoryMachine inventoryMachine : selectedMachinesRemove) {                    
                    if (inventoryMachine.getId() != null && inventoryMachine.getId() > 0) {
                        inventoryMachine.setGroup(inventoryMachineGroupDefault);

                        inventoryMachineBean.edit(inventoryMachine);
                    }
                }
                JsfUtil.addSuccessMessage("Maquina(s) removida(s) com sucesso!");
            } else {
                JsfUtil.addErrorMessage("Selecione pelo menos uma maquina para ser removida do grupo.");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao tentar remover máquina(s) do grupo.");
        }

        inventoryMachineGroupBean.clearCache();
        this.inventoryMachineGroup = inventoryMachineGroupBean.find(this.inventoryMachineGroup.getId());
        putFlash("inventoryMachineGroup", this.inventoryMachineGroup);
        return "manage";
    }

    public PieChartModel getMachinesOS() {
        PieChartModel chart = new PieChartModel();
        Integer windows = 0, linux = 0;

        inventoryMachineBean.clearCache();
        for (InventoryMachine inventoryMachine : inventoryMachineBean.findAll()) {
            if (inventoryMachine.getOs().toLowerCase().contains("windows")) {
                windows++;
            } else {
                linux++;
            }
        }

        chart.set("Windows", windows);
        chart.set("Linux", linux);

        return chart;
    }

    public PieChartModel getMachinesGroups() {
        PieChartModel chart = new PieChartModel();
        inventoryMachineGroupBean.clearCache();
        for (InventoryMachineGroup inventoryMachineGroup : inventoryMachineGroupBean.findAll()) {
            chart.set(inventoryMachineGroup.getName(), inventoryMachineGroup.getMachines().size());
        }

        return chart;
    }

    public PieChartModel getMachinesAlerts() {
        PieChartModel chart = new PieChartModel();
        List<String> idsAlteradas = new ArrayList<String>();

        inventoryAlertBean.clearCache();
        inventoryMachineBean.clearCache();

        for (InventoryAlert inventoryAlert : inventoryAlertBean.findAll()) {
            if (!idsAlteradas.contains(inventoryAlert.getMachine().getId().toString())) {
                idsAlteradas.add(inventoryAlert.getMachine().getId().toString());
            }
        }

        chart.set("Originais", inventoryMachineBean.findAll().size() - idsAlteradas.size());
        chart.set("Alteradas", idsAlteradas.size());

        return chart;
    }

    public CartesianChartModel getComponents() {
        CartesianChartModel chart = new CartesianChartModel();
        ChartSeries chartComponents = new ChartSeries();
        chartComponents.setLabel("Componentes");

        inventoryMotherboardBean.clearCache();
        chartComponents.set("Placa mãe", inventoryMotherboardBean.findAll().size());

        inventoryProcessorBean.clearCache();
        chartComponents.set("Processador", inventoryProcessorBean.findAll().size());

        inventoryMemoryBean.clearCache();
        chartComponents.set("Memória", inventoryMemoryBean.findAll().size());

        inventoryDiskBean.clearCache();
        chartComponents.set("HD", inventoryDiskBean.findAll().size());

        inventoryGPUBean.clearCache();
        chartComponents.set("Vídeo", inventoryGPUBean.findAll().size());

        inventoryAudioBean.clearCache();
        chartComponents.set("Audio", inventoryAudioBean.findAll().size());

        inventoryNetworkBean.clearCache();
        chartComponents.set("Rede", inventoryNetworkBean.findAll().size());

        chart.addSeries(chartComponents);

        return chart;
    }

    public int getMaxComponents() {
        int maxComponent = 0;

        if (inventoryMotherboardBean.findAll().size() > maxComponent) {
            maxComponent = inventoryMotherboardBean.findAll().size();
        }

        if (inventoryProcessorBean.findAll().size() > maxComponent) {
            maxComponent = inventoryProcessorBean.findAll().size();
        }

        if (inventoryMemoryBean.findAll().size() > maxComponent) {
            maxComponent = inventoryMemoryBean.findAll().size();
        }

        if (inventoryDiskBean.findAll().size() > maxComponent) {
            maxComponent = inventoryDiskBean.findAll().size();
        }

        if (inventoryGPUBean.findAll().size() > maxComponent) {
            maxComponent = inventoryGPUBean.findAll().size();
        }

        if (inventoryAudioBean.findAll().size() > maxComponent) {
            maxComponent = inventoryAudioBean.findAll().size();
        }

        if (inventoryNetworkBean.findAll().size() > maxComponent) {
            maxComponent = inventoryNetworkBean.findAll().size();
        }

        return maxComponent;
    }

    public InventoryMachineBean getInventoryMachineBean() {
        return inventoryMachineBean;
    }

    public void setInventoryMachineBean(InventoryMachineBean inventoryMachineBean) {
        this.inventoryMachineBean = inventoryMachineBean;
    }

    public PaginationHelper<InventoryMachine> getPagination() {
        if (pagination == null) {
            inventoryMachineBean.clearCache();
            this.pagination = new PaginationHelper<InventoryMachine>(inventoryMachineBean);
        }

        if (globalFilter != null) {
            Map<String, String> filters = new HashMap<String, String>();
            if (!globalFilter.trim().equals("")) {
                globalFilter = "%" + globalFilter + "%";
                filters.put("hostName", globalFilter);
                filters.put("os", globalFilter);
                filters.put("group.name", globalFilter);
            }
            this.pagination.setGlobalFilters(filters);
        }

        return pagination;
    }

    public void setPagination(PaginationHelper<InventoryMachine> pagination) {
        this.pagination = pagination;
    }

    public List<InventoryMachine> getListMachinesSelectSO() {
        return listMachinesSelectSO;
    }

    public void setListMachinesSelectSO(List<InventoryMachine> listMachinesSelectSO) {
        this.listMachinesSelectSO = listMachinesSelectSO;
    }

    public List<InventoryMachine> getListMachinesWindows() {
        return listMachinesWindows;
    }

    public void setListMachinesWindows(List<InventoryMachine> listMachinesWindows) {
        this.listMachinesWindows = listMachinesWindows;
    }

    public List<InventoryMachine> getSelectedMachines() {
        return selectedMachines;
    }

    public void setSelectedMachines(List<InventoryMachine> selectedMachines) {
        this.selectedMachines = selectedMachines;
    }

    public List<InventoryMachine> getSelectedMachinesTestHardware() {
        return selectedMachinesTestHardware;
    }

    public void setSelectedMachinesTestHardware(List<InventoryMachine> selectedMachinesTestHardware) {
        this.selectedMachinesTestHardware = selectedMachinesTestHardware;
    }

    public List<InventoryMachine> getSelectedMachinesInstallDrive() {
        return selectedMachinesInstallDrive;
    }

    public void setSelectedMachinesInstallDrive(List<InventoryMachine> selectedMachinesInstallDrive) {
        this.selectedMachinesInstallDrive = selectedMachinesInstallDrive;
    }

    public List<InventoryMachine> getSelectedMachinesInstallSoftware() {
        return selectedMachinesInstallSoftware;
    }

    public void setSelectedMachinesInstallSoftware(List<InventoryMachine> selectedMachinesInstallSoftware) {
        this.selectedMachinesInstallSoftware = selectedMachinesInstallSoftware;
    }

    public InventoryMachine getInventoryMachine() {
        return inventoryMachine;
    }

    public void setInventoryMachine(InventoryMachine inventoryMachine) {
        this.inventoryMachine = inventoryMachine;
    }

    public String getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public String getSelectSO() {
        return selectSO;
    }

    public void setSelectSO(String selectSO) {
        this.selectSO = selectSO;
    }

    public List<String> getSelectedTests() {
        return selectedTests;
    }

    public void setSelectedTests(List<String> selectedTests) {
        this.selectedTests = selectedTests;
    }

    public UploadDriveBean getUploadDriveBean() {
        return uploadDriveBean;
    }

    public void setUploadDriveBean(UploadDriveBean uploadDriveBean) {
        this.uploadDriveBean = uploadDriveBean;
    }

    public List<UploadDrive> getListDrives() {
        return listDrives;
    }

    public void setListDrives(List<UploadDrive> listDrives) {
        this.listDrives = listDrives;
    }

    public UploadSoftwareBean getUploadSoftwareBean() {
        return uploadSoftwareBean;
    }

    public void setUploadSoftwareBean(UploadSoftwareBean uploadSoftwareBean) {
        this.uploadSoftwareBean = uploadSoftwareBean;
    }

    public List<UploadSoftware> getListSoftwares() {
        return listSoftwares;
    }

    public void setListSoftwares(List<UploadSoftware> listSoftwares) {
        this.listSoftwares = listSoftwares;
    }

    public UploadDrive getUploadDrive() {
        return uploadDrive;
    }

    public void setUploadDrive(UploadDrive uploadDrive) {
        this.uploadDrive = uploadDrive;
    }

    public UploadSoftware getUploadSoftware() {
        return uploadSoftware;
    }

    public void setUploadSoftware(UploadSoftware uploadSoftware) {
        this.uploadSoftware = uploadSoftware;
    }

    public List<InventoryMachine> getSelectedMachinesInstallImage() {
        return selectedMachinesInstallImage;
    }

    public void setSelectedMachinesInstallImage(List<InventoryMachine> selectedMachinesInstallImage) {
        this.selectedMachinesInstallImage = selectedMachinesInstallImage;
    }

    public UploadImageBean getUploadImageBean() {
        return uploadImageBean;
    }

    public void setUploadImageBean(UploadImageBean uploadImageBean) {
        this.uploadImageBean = uploadImageBean;
    }

    public UploadImage getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }

    public List<UploadImage> getListImages() {
        return listImages;
    }

    public void setListImages(List<UploadImage> listImages) {
        this.listImages = listImages;
    }

    public List<UploadSoftware> getListSoftwaresSelectSO() {
        return listSoftwaresSelectSO;
    }

    public void setListSoftwaresSelectSO(List<UploadSoftware> listSoftwaresSelectSO) {
        this.listSoftwaresSelectSO = listSoftwaresSelectSO;
    }

    public List<UploadImage> getListImagesSelectSO() {
        return listImagesSelectSO;
    }

    public void setListImagesSelectSO(List<UploadImage> listImagesSelectSO) {
        this.listImagesSelectSO = listImagesSelectSO;
    }

    public List<InventoryMachine> getSelectedMachinesBackup() {
        return selectedMachinesBackup;
    }

    public void setSelectedMachinesBackup(List<InventoryMachine> selectedMachinesBackup) {
        this.selectedMachinesBackup = selectedMachinesBackup;
    }

    public InventoryMotherboardBean getInventoryMotherboardBean() {
        return inventoryMotherboardBean;
    }

    public void setInventoryMotherboardBean(InventoryMotherboardBean inventoryMotherboardBean) {
        this.inventoryMotherboardBean = inventoryMotherboardBean;
    }

    public InventoryProcessorBean getInventoryProcessorBean() {
        return inventoryProcessorBean;
    }

    public void setInventoryProcessorBean(InventoryProcessorBean inventoryProcessorBean) {
        this.inventoryProcessorBean = inventoryProcessorBean;
    }

    public InventoryMemoryBean getInventoryMemoryBean() {
        return inventoryMemoryBean;
    }

    public void setInventoryMemoryBean(InventoryMemoryBean inventoryMemoryBean) {
        this.inventoryMemoryBean = inventoryMemoryBean;
    }

    public InventoryDiskBean getInventoryDiskBean() {
        return inventoryDiskBean;
    }

    public void setInventoryDiskBean(InventoryDiskBean inventoryDiskBean) {
        this.inventoryDiskBean = inventoryDiskBean;
    }

    public InventoryGPUBean getInventoryGPUBean() {
        return inventoryGPUBean;
    }

    public void setInventoryGPUBean(InventoryGPUBean inventoryGPUBean) {
        this.inventoryGPUBean = inventoryGPUBean;
    }

    public InventoryAudioBean getInventoryAudioBean() {
        return inventoryAudioBean;
    }

    public void setInventoryAudioBean(InventoryAudioBean inventoryAudioBean) {
        this.inventoryAudioBean = inventoryAudioBean;
    }

    public InventoryNetworkBean getInventoryNetworkBean() {
        return inventoryNetworkBean;
    }

    public void setInventoryNetworkBean(InventoryNetworkBean inventoryNetworkBean) {
        this.inventoryNetworkBean = inventoryNetworkBean;
    }

    public InventoryMachineGroupBean getInventoryMachineGroupBean() {
        return inventoryMachineGroupBean;
    }

    public void setInventoryMachineGroupBean(InventoryMachineGroupBean inventoryMachineGroupBean) {
        this.inventoryMachineGroupBean = inventoryMachineGroupBean;
    }

    public InventoryMachineGroup getInventoryMachineGroup() {
        return inventoryMachineGroup;
    }

    public void setInventoryMachineGroup(InventoryMachineGroup inventoryMachineGroup) {
        this.inventoryMachineGroup = inventoryMachineGroup;
    }

    public List<InventoryMachine> getSelectedMachinesAdd() {
        return selectedMachinesAdd;
    }

    public void setSelectedMachinesAdd(List<InventoryMachine> selectedMachinesAdd) {
        this.selectedMachinesAdd = selectedMachinesAdd;
    }

    public List<InventoryMachine> getSelectedMachinesRemove() {
        return selectedMachinesRemove;
    }

    public void setSelectedMachinesRemove(List<InventoryMachine> selectedMachinesRemove) {
        this.selectedMachinesRemove = selectedMachinesRemove;
    }

    public InventoryAlertBean getInventoryAlertBean() {
        return inventoryAlertBean;
    }

    public void setInventoryAlertBean(InventoryAlertBean inventoryAlertBean) {
        this.inventoryAlertBean = inventoryAlertBean;
    }

    public InventoryLogTasksBean getInventoryLogTasksBean() {
        return inventoryLogTasksBean;
    }

    public void setInventoryLogTasksBean(InventoryLogTasksBean inventoryLogTasksBean) {
        this.inventoryLogTasksBean = inventoryLogTasksBean;
    }

    public String getTabID() {
        return tabID;
    }

    public void setTabID(String tabID) {
        this.tabID = tabID;
    }

    public void globalFilterChanged(ValueChangeEvent event) {
        if (!event.getNewValue().equals(event.getOldValue())) {
            globalFilter = event.getNewValue().toString();
        }
    }
}
