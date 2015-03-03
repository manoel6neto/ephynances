package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UploadSoftwareBean;
import br.com.physisbrasil.web.ephynances.model.UploadSoftware;
import br.com.physisbrasil.web.ephynances.util.FileUtil;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.PaginationHelper;
import br.com.physisbrasil.web.ephynances.util.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
public class UploadSoftwareController extends BaseController {

    @EJB
    private UploadSoftwareBean uploadSoftwareBean;
    private PaginationHelper<UploadSoftware> pagination;
    private UploadSoftware uploadSoftware;
    private UploadedFile uploadedFile;

    private String globalFilter;

    @PostConstruct
    public void init() {
        UploadSoftware requestUploadSoftware = (UploadSoftware) getFlash("uploadSoftware");
        if (requestUploadSoftware != null) {
            uploadSoftware = requestUploadSoftware;
        } else {
            uploadSoftware = new UploadSoftware();
        }

        putFlash("uploadSoftware", null);
    }

    public String create() {
        try {
            uploadSoftware = new UploadSoftware();
            return "create";
        } catch (Throwable e) {
            return "list";
        }
    }

    public String save() {
        try {
            if (uploadedFile != null && uploadedFile.getSize() > 0) {
                String fileExtension = FileUtil.getFileExtension(uploadedFile.getFileName()).toLowerCase();
                if ((uploadSoftware.getOs().equalsIgnoreCase("Windows") && fileExtension.equalsIgnoreCase("msi"))
                 || (uploadSoftware.getOs().equalsIgnoreCase("Linux") && fileExtension.equalsIgnoreCase("deb"))
                 || (uploadSoftware.getOs().equalsIgnoreCase("Linux") && fileExtension.equalsIgnoreCase("rpm"))) {
                    try {
                        String filePath = "";
                        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                            filePath = System.getenv("SystemDrive") + "/ephynances/" + UploadSoftware.FILE_URL + uploadedFile.getFileName().toString();
                        } else {
                            filePath = "/ephynances/" + UploadSoftware.FILE_URL + uploadedFile.getFileName().toString();
                        }

                        File destFile = new File(filePath);
                        destFile.getParentFile().mkdirs();
                        destFile.createNewFile();
                        uploadSoftware.setSoftwareUrl(filePath);                       
                        FileUtil.copyFileContents(uploadedFile.getInputstream(), destFile);
                    } catch (Exception ex) {
                        JsfUtil.addErrorMessage(ex, "Erro ao salvar arquivo no servidor.");
                        return "create";
                    }
                } else {
                    JsfUtil.addErrorMessage("Só é permitido arquivos no formato (.msi) para Windows e (.deb) ou (.rpm) para Linux.");
                    return "list";
                }
            }

            if (uploadSoftware.getId() != null && uploadSoftware.getId() > 0) {
                if (uploadSoftware.getOs().equalsIgnoreCase("Windows") && uploadedFile == null) {
                    JsfUtil.addErrorMessage("Erro ao atualizar software. No Windows é obrigátório o upload do arquivo.");
                } else {
                    uploadSoftwareBean.edit(uploadSoftware);
                    JsfUtil.addSuccessMessage("Software atualizado com sucesso!!");
                }
            } else {
                if (uploadSoftware.getOs().equalsIgnoreCase("Windows") && uploadedFile == null) {
                    JsfUtil.addErrorMessage("Erro ao adicionar software. No Windows é obrigátório o upload do arquivo.");
                } else {
                    uploadSoftwareBean.create(uploadSoftware);
                    JsfUtil.addSuccessMessage("Software adicionado com sucesso!!");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao adicionar/atualizar software.");
            return "create";
        }

        return "list";
    }

    public String edit(UploadSoftware uploadSoftware) {
        try {
            if (uploadSoftware.getId() != null && uploadSoftware.getId() > 0) {
                this.uploadSoftware = uploadSoftwareBean.find(uploadSoftware.getId());
                putFlash("uploadSoftware", this.uploadSoftware);
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
            if (this.uploadSoftware.getId() != null && this.uploadSoftware.getId() > 0) {
                uploadSoftwareBean.remove(this.uploadSoftware);

                JsfUtil.addSuccessMessage("Removido com sucesso!!");
            }
        } catch (Throwable e) {
        }

        return "list";
    }

    public PaginationHelper<UploadSoftware> getPagination() {
        if (pagination == null) {
            uploadSoftwareBean.clearCache();
            this.pagination = new PaginationHelper<UploadSoftware>(uploadSoftwareBean);
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

    public void setPagination(PaginationHelper<UploadSoftware> pagination) {
        this.pagination = pagination;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
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
