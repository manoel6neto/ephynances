package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.InventoryMachineBean;
import br.com.physisbrasil.web.ephynances.ejb.ScheduledTaskBean;
import br.com.physisbrasil.web.ephynances.ejb.UploadImageBean;
import br.com.physisbrasil.web.ephynances.ejb.UploadSoftwareBean;
import br.com.physisbrasil.web.ephynances.model.InventoryMachine;
import br.com.physisbrasil.web.ephynances.model.InventoryNetwork;
import br.com.physisbrasil.web.ephynances.model.ScheduledTask;
import br.com.physisbrasil.web.ephynances.model.UploadImage;
import br.com.physisbrasil.web.ephynances.model.UploadSoftware;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.PaginationHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class ScheduledTaskController extends BaseController {

    @EJB
    private ScheduledTaskBean scheduledTaskBean;
    private PaginationHelper<ScheduledTask> pagination;
    private ScheduledTask scheduledTask;

    @EJB
    private InventoryMachineBean inventoryMachineBean;
    private InventoryMachine inventoryMachine;
    private List<InventoryMachine> listMachinesSelectSO;
    private List<InventoryMachine> selectedMachines;
    private List<String> selectedTests;

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

    private String globalFilter;

    @PostConstruct
    public void init() {
        selectedTests = new ArrayList<String>();
        listMachinesSelectSO = new ArrayList<InventoryMachine>();
        selectedMachines = new ArrayList<InventoryMachine>();
        listSoftwaresSelectSO = new ArrayList<UploadSoftware>();
        listImagesSelectSO = new ArrayList<UploadImage>();

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

        ScheduledTask requestScheduledTask = (ScheduledTask) getFlash("scheduledTask");
        if (requestScheduledTask != null) {
            scheduledTask = requestScheduledTask;
            selectedMachines = scheduledTask.getMachineList();
            this.updateSubTask();
        } else {
            scheduledTask = new ScheduledTask();
        }

        InventoryMachine requestInventoryMachine = (InventoryMachine) getFlash("inventoryMachine");
        if (requestInventoryMachine != null) {
            inventoryMachineBean.clearCache();
            inventoryMachine = requestInventoryMachine;
        } else {
            inventoryMachine = new InventoryMachine();
        }

        putFlash("inventoryMachine", null);
        putFlash("scheduledTask", null);

        this.loadConfigUIOS();
    }

    public String create() {
        try {
            scheduledTask = new ScheduledTask();
            return "create";
        } catch (Throwable e) {
            return "list";
        }
    }

    public String save() {
        try {
            if (selectedMachines.size() > 0) {
                if (scheduledTask.getId() != null && scheduledTask.getId() > 0) {
                    if (scheduledTask.getTask().equalsIgnoreCase("Ligar")) {
                        this.editScheduledPoweron();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Desligar")) {
                        this.editScheduledPoweroff();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Software")) {
                        this.editScheduledInstallSoftware();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Imagem")) {
                        this.editScheduledInstallImage();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Testes")) {
                        this.editScheduledTestHardware();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Backup")) {
                        this.editScheduledCreateBackup();
                    }
                } else {
                    if (scheduledTask.getTask().equalsIgnoreCase("Ligar")) {
                        this.createScheduledPoweron();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Desligar")) {
                        this.createScheduledPoweroff();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Software")) {
                        this.createScheduledInstallSoftware();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Imagem")) {
                        this.createScheduledInstallImage();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Testes")) {
                        this.createScheduledTestHardware();
                    } else if (scheduledTask.getTask().equalsIgnoreCase("Backup")) {
                        this.createScheduledCreateBackup();
                    }
                }
            } else {
                JsfUtil.addErrorMessage("Pelo menos uma máquina deve ser selecionada.");
            }
            selectedMachines.clear();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao adicionar/atualizar Agendamento.");
            return "create";
        }

        return "list";
    }

    public String edit(ScheduledTask scheduledTask) {
        try {
            if (scheduledTask.getId() != null && scheduledTask.getId() > 0) {
                this.scheduledTask = scheduledTaskBean.find(scheduledTask.getId());
                putFlash("scheduledTask", this.scheduledTask);
            } else {
                JsfUtil.addErrorMessage("Objeto não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "edit";
    }

    public String delete() {
        try {
            if (this.scheduledTask.getId() != null && this.scheduledTask.getId() > 0) {
                scheduledTaskBean.remove(this.scheduledTask);

                JsfUtil.addSuccessMessage("Removido com sucesso!!");
            }
        } catch (Throwable e) {
        }

        return "list";
    }

    public PaginationHelper<ScheduledTask> getPagination() {
        if (pagination == null) {
            scheduledTaskBean.clearCache();
            this.pagination = new PaginationHelper<ScheduledTask>(scheduledTaskBean);
        }

        if (globalFilter != null) {
            Map<String, String> filters = new HashMap<String, String>();
            if (!globalFilter.trim().equals("")) {
                globalFilter = "%" + globalFilter + "%";
                filters.put("name", globalFilter);
                filters.put("os", globalFilter);
            }
            this.pagination.setGlobalFilters(filters);
        }

        return pagination;
    }

    public boolean viewSelectOneMenuSoftwares() {
        if (scheduledTask.getTask() != null && scheduledTask.getTask().equalsIgnoreCase("Software")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean viewSelectOneMenuImages() {
        if (scheduledTask.getTask() != null && scheduledTask.getTask().equalsIgnoreCase("Imagem")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean viewSelectManyCheckboxTests() {
        if (scheduledTask.getTask() != null && scheduledTask.getTask().equalsIgnoreCase("Testes")) {
            return true;
        } else {
            return false;
        }
    }

    public void updateSubTask() {
        if (scheduledTask.getTask().equalsIgnoreCase("Software")) {
            uploadSoftware.setId(uploadSoftwareBean.findByProperty("name", scheduledTask.getCommand().split(" ")[0].split("=")[1]).getId());
        } else if (scheduledTask.getTask().equalsIgnoreCase("Imagem")) {
            uploadImage.setId(uploadImageBean.findByProperty("id", scheduledTask.getCommand().split(" ")[0].split("=")[1]).getId());
        } else if (scheduledTask.getTask().equalsIgnoreCase("Testes")) {
            try {
                JSONArray jSONArrayTests = new JSONArray(scheduledTask.getScheduledParameter1());
                for (int i = 0; i < jSONArrayTests.length(); i++) {
                    String teste = jSONArrayTests.getString(i);
                    selectedTests.add(teste);
                }
            } catch (Exception ex) {
                JsfUtil.addErrorMessage(ex, "Falha ao carregar testes do agendamento.");
            }

        }
    }

    public void loadConfigUIOS() {
        listMachinesSelectSO.clear();
        if (scheduledTask.getSo() == null || scheduledTask.getSo().equalsIgnoreCase("Todos")) {
            listMachinesSelectSO = inventoryMachineBean.findAll();
            listSoftwaresSelectSO.clear();
            listImagesSelectSO.clear();
        } else {
            for (InventoryMachine inventoryMachine : inventoryMachineBean.findAll()) {
                if (inventoryMachine.getOs().contains("Windows")) {
                    if (scheduledTask.getSo().equalsIgnoreCase("Windows")) {
                        listMachinesSelectSO.add(inventoryMachine);
                    }
                } else {
                    if (scheduledTask.getSo().equalsIgnoreCase("Linux")) {
                        listMachinesSelectSO.add(inventoryMachine);
                    }
                }
            }

            listSoftwaresSelectSO.clear();
            for (UploadSoftware uploadSoftware : uploadSoftwareBean.findAll()) {
                if (uploadSoftware.getOs().contains("Windows") && scheduledTask.getSo().equalsIgnoreCase("Windows")) {
                    listSoftwaresSelectSO.add(uploadSoftware);
                }

                if (!uploadSoftware.getOs().contains("Windows") && scheduledTask.getSo().equalsIgnoreCase("Linux")) {
                    listSoftwaresSelectSO.add(uploadSoftware);
                }
            }

            listImagesSelectSO.clear();
            for (UploadImage uploadImage : uploadImageBean.findAll()) {
                if (uploadImage.getOs().contains("Windows") && scheduledTask.getSo().equalsIgnoreCase("Windows")) {
                    listImagesSelectSO.add(uploadImage);
                }

                if (!uploadImage.getOs().contains("Windows") && scheduledTask.getSo().equalsIgnoreCase("Linux")) {
                    listImagesSelectSO.add(uploadImage);
                }
            }
        }
    }

    public String listIps(InventoryMachine inventoryMachine) {
        String ips = "";

        for (InventoryNetwork inventoryNetwork : inventoryMachine.getMachineNetworks()) {
            ips += " [" + inventoryNetwork.getIpv4() + "]";
        }

        return ips;
    }

    public Map<String, Map<String, String>> getDicInfoMachine(List<InventoryMachine> selectedMachines) {
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

        return dicInfoMachine;
    }

    public void createScheduled(Integer status, String command, String setScheduledParameter1, String setScheduledParameter2, List<InventoryMachine> selectedMachines) {
        try {
            scheduledTask.setStatus(status);
            scheduledTask.setCommand(command);
            scheduledTask.setScheduledParameter1(setScheduledParameter1);
            scheduledTask.setScheduledParameter2(setScheduledParameter2);
            scheduledTask.setMachineList(selectedMachines);
            scheduledTaskBean.create(scheduledTask);
            JsfUtil.addSuccessMessage("Agendamento adicionado com sucesso!!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduled(Integer status, String command, String setScheduledParameter1, String setScheduledParameter2, List<InventoryMachine> selectedMachines) {
        try {
            scheduledTask.setStatus(status);
            scheduledTask.setCommand(command);
            scheduledTask.setScheduledParameter1(setScheduledParameter1);
            scheduledTask.setScheduledParameter2(setScheduledParameter2);
            scheduledTask.setMachineList(selectedMachines);
            scheduledTaskBean.edit(scheduledTask);
            JsfUtil.addSuccessMessage("Agendamento atualizado com sucesso!!");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao editar agendamento de tarefa.");
        }
    }

    public void createScheduledPoweron() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.createScheduled(0, "--bin=1 poe", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduledPoweron() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.editScheduled(0, "--bin=1 poe", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void createScheduledPoweroff() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.createScheduled(0, "--bin=1 shutdown", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduledPoweroff() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.editScheduled(0, "--bin=1 shutdown", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void createScheduledInstallSoftware() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            if (uploadSoftwareBean.find(uploadSoftware.getId()).getSoftwareUrl() == null) {
                this.createScheduled(0, "--filename=" + uploadSoftwareBean.find(uploadSoftware.getId()).getName().toString() + " --bin=1 installsoftware", jSONObject.toString(), null, selectedMachines);
            } else {
                this.createScheduled(0, "--filename=" + uploadSoftwareBean.find(uploadSoftware.getId()).getSoftwareUrl().split("/")[3].toString() + " --bin=1 installsoftware", jSONObject.toString(), null, selectedMachines);
            }
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduledInstallSoftware() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.editScheduled(0, "--filename=" + uploadSoftwareBean.find(uploadSoftware.getId()).getName().toString() + " --bin=1 installsoftware", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void createScheduledInstallImage() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.createScheduled(0, "--imageid=" + uploadImage.getId().toString() + " --bin=1 baixarimagem", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduledInstallImage() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.editScheduled(0, "--imageid=" + uploadImage.getId().toString() + " --bin=1 baixarimagem", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void createScheduledTestHardware() {
        try {
            JSONObject jSONObjectInfoMachine = new JSONObject(getDicInfoMachine(selectedMachines));

            //Envio do dicionário contendo os testes a serem realizados
            JSONArray jSONArrayTests = new JSONArray(selectedTests);
            this.createScheduled(0, "--bin=2 testehardware", jSONArrayTests.toString(), jSONObjectInfoMachine.toString(), selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduledTestHardware() {
        try {
            JSONObject jSONObjectInfoMachine = new JSONObject(getDicInfoMachine(selectedMachines));

            //Envio do dicionário contendo os testes a serem realizados
            JSONArray jSONArrayTests = new JSONArray(selectedTests);
            this.editScheduled(0, "--bin=2 testehardware", jSONArrayTests.toString(), jSONObjectInfoMachine.toString(), selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void createScheduledCreateBackup() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.createScheduled(0, "--bin=1 gerarbackup", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public void editScheduledCreateBackup() {
        try {
            JSONObject jSONObject = new JSONObject(getDicInfoMachine(selectedMachines));
            this.editScheduled(0, "--bin=1 gerarbackup", jSONObject.toString(), null, selectedMachines);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "Falha ao agendar tarefa.");
        }
    }

    public ScheduledTaskBean getScheduledTaskBean() {
        return scheduledTaskBean;
    }

    public void setScheduledTaskBean(ScheduledTaskBean scheduledTaskBean) {
        this.scheduledTaskBean = scheduledTaskBean;
    }

    public InventoryMachineBean getInventoryMachineBean() {
        return inventoryMachineBean;
    }

    public void setInventoryMachineBean(InventoryMachineBean inventoryMachineBean) {
        this.inventoryMachineBean = inventoryMachineBean;
    }

    public InventoryMachine getInventoryMachine() {
        return inventoryMachine;
    }

    public void setInventoryMachine(InventoryMachine inventoryMachine) {
        this.inventoryMachine = inventoryMachine;
    }

    public List<InventoryMachine> getListMachinesSelectSO() {
        return listMachinesSelectSO;
    }

    public void setListMachinesSelectSO(List<InventoryMachine> listMachinesSelectSO) {
        this.listMachinesSelectSO = listMachinesSelectSO;
    }

    public ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    public void setScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public List<InventoryMachine> getSelectedMachines() {
        return selectedMachines;
    }

    public void setSelectedMachines(List<InventoryMachine> selectedMachines) {
        this.selectedMachines = selectedMachines;
    }

    public UploadSoftwareBean getUploadSoftwareBean() {
        return uploadSoftwareBean;
    }

    public void setUploadSoftwareBean(UploadSoftwareBean uploadSoftwareBean) {
        this.uploadSoftwareBean = uploadSoftwareBean;
    }

    public UploadSoftware getUploadSoftware() {
        return uploadSoftware;
    }

    public void setUploadSoftware(UploadSoftware uploadSoftware) {
        this.uploadSoftware = uploadSoftware;
    }

    public List<UploadSoftware> getListSoftwares() {
        return listSoftwares;
    }

    public void setListSoftwares(List<UploadSoftware> listSoftwares) {
        this.listSoftwares = listSoftwares;
    }

    public List<UploadSoftware> getListSoftwaresSelectSO() {
        return listSoftwaresSelectSO;
    }

    public void setListSoftwaresSelectSO(List<UploadSoftware> listSoftwaresSelectSO) {
        this.listSoftwaresSelectSO = listSoftwaresSelectSO;
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

    public List<UploadImage> getListImagesSelectSO() {
        return listImagesSelectSO;
    }

    public void setListImagesSelectSO(List<UploadImage> listImagesSelectSO) {
        this.listImagesSelectSO = listImagesSelectSO;
    }

    public List<String> getSelectedTests() {
        return selectedTests;
    }

    public void setSelectedTests(List<String> selectedTests) {
        this.selectedTests = selectedTests;
    }

    public String getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public void globalFilterChanged(ValueChangeEvent event) {
        if (!event.getNewValue().equals(event.getOldValue())) {
            globalFilter = event.getNewValue().toString();
        }
    }
}
