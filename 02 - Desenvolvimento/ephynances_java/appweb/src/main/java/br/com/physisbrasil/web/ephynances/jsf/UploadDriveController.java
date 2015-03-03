package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UploadDriveBean;
import br.com.physisbrasil.web.ephynances.model.UploadDrive;
import br.com.physisbrasil.web.ephynances.util.FileUtil;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.PaginationHelper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class UploadDriveController extends BaseController {

    @EJB
    private UploadDriveBean uploadDriveBean;
    private PaginationHelper<UploadDrive> pagination;
    private UploadDrive uploadDrive;
    private UploadedFile uploadedFile;

    private String globalFilter;

    @PostConstruct
    public void init() {
        UploadDrive requestUploadDrive = (UploadDrive) getFlash("uploadDrive");
        if (requestUploadDrive != null) {
            uploadDrive = requestUploadDrive;
        } else {
            uploadDrive = new UploadDrive();
        }

        putFlash("uploadDrive", null);
    }

    public String create() {
        try {
            uploadDrive = new UploadDrive();
            return "create";
        } catch (Throwable e) {
            return "list";
        }
    }

    public String save() {
        try {
            if (uploadedFile != null && uploadedFile.getSize() > 0) {
                String fileExtension = FileUtil.getFileExtension(uploadedFile.getFileName()).toLowerCase();
                if (fileExtension.equalsIgnoreCase("zip")) {
                    try {
                        String filePath = "";
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            filePath = System.getenv("SystemDrive") + "/agility/" + UploadDrive.FILE_URL + uploadedFile.getFileName().toString();
                        } else {
                            filePath = "/agility/" + UploadDrive.FILE_URL + uploadedFile.getFileName().toString();
                        }

                        File destFile = new File(filePath);
                        destFile.getParentFile().mkdirs();
                        destFile.createNewFile();
                        this.uploadDrive.setDriveUrl(filePath);
                        FileUtil.copyFileContents(uploadedFile.getInputstream(), destFile);
                    } catch (Exception ex) {
                        JsfUtil.addErrorMessage(ex, "Erro ao salvar arquivo no servidor.");
                        return "create";
                    }
                } else {
                    JsfUtil.addErrorMessage("Só é permitido arquivos no formato .zip.");
                    return "list";
                }
            }

            if (uploadDrive.getId() != null && uploadDrive.getId() > 0) {
                uploadDriveBean.edit(uploadDrive);
                JsfUtil.addSuccessMessage("Drive atualizado com sucesso!!");
            } else {
                uploadDriveBean.create(uploadDrive);
                JsfUtil.addSuccessMessage("Drive adicionado com sucesso!!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao adicionar/atualizar drive.");
            return "create";
        }

        return "list";
    }

    public String edit(UploadDrive uploadDrive) {
        try {
            if (uploadDrive.getId() != null && uploadDrive.getId() > 0) {
                this.uploadDrive = uploadDriveBean.find(uploadDrive.getId());
                putFlash("uploadDrive", this.uploadDrive);
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
            if (this.uploadDrive.getId() != null && this.uploadDrive.getId() > 0) {
                uploadDriveBean.remove(this.uploadDrive);

                JsfUtil.addSuccessMessage("Removido com sucesso!!");
            }
        } catch (Throwable e) {
        }

        return "list";
    }

    public PaginationHelper<UploadDrive> getPagination() {
        if (pagination == null) {
            uploadDriveBean.clearCache();
            this.pagination = new PaginationHelper<UploadDrive>(uploadDriveBean);
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

    public void setPagination(PaginationHelper<UploadDrive> pagination) {
        this.pagination = pagination;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UploadDriveBean getUploadDriveBean() {
        return uploadDriveBean;
    }

    public void setUploadDriveBean(UploadDriveBean uploadDriveBean) {
        this.uploadDriveBean = uploadDriveBean;
    }

    public UploadDrive getUploadDrive() {
        return uploadDrive;
    }

    public void setUploadDrive(UploadDrive uploadDrive) {
        this.uploadDrive = uploadDrive;
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
