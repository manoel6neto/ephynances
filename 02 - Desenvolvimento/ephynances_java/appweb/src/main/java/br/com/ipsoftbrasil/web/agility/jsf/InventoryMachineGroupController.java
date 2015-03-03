package br.com.ipsoftbrasil.web.agility.jsf;

import br.com.ipsoftbrasil.web.agility.ejb.InventoryMachineBean;
import br.com.ipsoftbrasil.web.agility.ejb.InventoryMachineGroupBean;
import br.com.ipsoftbrasil.web.agility.model.InventoryMachine;
import br.com.ipsoftbrasil.web.agility.model.InventoryMachineGroup;
import br.com.ipsoftbrasil.web.agility.util.JsfUtil;
import br.com.ipsoftbrasil.web.agility.util.PaginationHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Malvadeza
 */
@ManagedBean
@ViewScoped
public class InventoryMachineGroupController extends BaseController {

    @EJB
    private InventoryMachineGroupBean inventoryMachineGroupBean;
    private PaginationHelper<InventoryMachineGroup> pagination;
    private InventoryMachineGroup inventoryMachineGroup;
    private List<InventoryMachineGroup> inventoryMachineGroups;
    @EJB
    private InventoryMachineBean inventoryMachineBean;
    private List<InventoryMachine> inventoryMachines;    

    private String globalFilter;

    @PostConstruct
    public void init() {
        InventoryMachineGroup requestInventoryMachineGroup = (InventoryMachineGroup) getFlash("inventoryMachineGroup");
        if (requestInventoryMachineGroup != null) {
            inventoryMachineGroup = requestInventoryMachineGroup;
        } else {
            inventoryMachineGroup = new InventoryMachineGroup();
        }

        if (this.inventoryMachineGroups == null) {
            this.inventoryMachineGroups = this.inventoryMachineGroupBean.findAll();
        }

        if (this.inventoryMachines == null) {
            this.inventoryMachines = this.inventoryMachineBean.findAll();
        }

        putFlash("inventoryMachineGroup", null);
    }

    public String create() {
        try {
            this.inventoryMachineGroup = new InventoryMachineGroup();
            return "create";
        } catch (Throwable e) {
            return "list";
        }
    }

    public String view(InventoryMachineGroup inventoryMachineGroup) {
        try {
            if (inventoryMachineGroup.getId() != null && inventoryMachineGroup.getId() > 0) {
                this.inventoryMachineGroup = inventoryMachineGroupBean.find(inventoryMachineGroup.getId());
                putFlash("studentGroup", this.inventoryMachineGroup);
            } else {
                JsfUtil.addErrorMessage("Objeto não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "view";
    }

    public String edit(InventoryMachineGroup inventoryMachineGroup) {
        try {
            if (inventoryMachineGroup.getId() != null && inventoryMachineGroup.getId() > 0) {
                this.inventoryMachineGroup = inventoryMachineGroupBean.find(inventoryMachineGroup.getId());
                putFlash("inventoryMachineGroup", this.inventoryMachineGroup);
            } else {
                JsfUtil.addErrorMessage("Objeto não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "edit";
    }

    public String save() {
        try {
            if (inventoryMachineGroup.getId() != null && inventoryMachineGroup.getId() > 0) {
                inventoryMachineGroupBean.edit(inventoryMachineGroup);
                JsfUtil.addSuccessMessage("Grupo atualizado com sucesso!!");
            } else {
                inventoryMachineGroupBean.create(inventoryMachineGroup);
                JsfUtil.addSuccessMessage("Grupo criado com sucesso!!");
            }
        } catch (Throwable e) {
            JsfUtil.addErrorMessage("Falha ao criar/atualizar grupo. Verifique se o nome já esta em uso.");
            return "create";
        }

        return "list";
    }

    public String remove() {
        try {
            if (inventoryMachineGroup.getId() != null && inventoryMachineGroup.getId() > 0) {
                for (InventoryMachine inventoryMachine : inventoryMachineGroup.getMachines()) {
                    inventoryMachine.setGroup(null);                  
                    inventoryMachineBean.edit(inventoryMachine);
                }
                inventoryMachineGroupBean.remove(inventoryMachineGroup);
                JsfUtil.addSuccessMessage("Grupo removido com sucesso!!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao tentar remover o grupo de alunos.");
            return "list";
        }

        return "list";
    }

    public PaginationHelper<InventoryMachineGroup> getPagination() {
        inventoryMachineGroupBean.clearCache();
        if (pagination == null) {
            this.pagination = new PaginationHelper<InventoryMachineGroup>(inventoryMachineGroupBean);
        }

        if (globalFilter != null) {
            Map<String, String> filters = new HashMap<String, String>();
            if (!globalFilter.trim().equals("")) {
                globalFilter = "%" + globalFilter + "%";
                filters.put("name", globalFilter);
            }
            this.pagination.setGlobalFilters(filters);
        }

        return pagination;
    }

    public void setPagination(PaginationHelper<InventoryMachineGroup> pagination) {
        this.pagination = pagination;
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

    public List<InventoryMachineGroup> getInventoryMachineGroups() {
        return inventoryMachineGroups;
    }

    public void setInventoryMachineGroups(List<InventoryMachineGroup> inventoryMachineGroups) {
        this.inventoryMachineGroups = inventoryMachineGroups;
    }

    public InventoryMachineBean getInventoryMachineBean() {
        return inventoryMachineBean;
    }

    public void setInventoryMachineBean(InventoryMachineBean inventoryMachineBean) {
        this.inventoryMachineBean = inventoryMachineBean;
    }

    public List<InventoryMachine> getInventoryMachines() {
        return inventoryMachines;
    }

    public void setInventoryMachines(List<InventoryMachine> inventoryMachines) {
        this.inventoryMachines = inventoryMachines;
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
