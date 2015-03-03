package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UploadImageBean;
import br.com.physisbrasil.web.ephynances.model.UploadImage;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.PaginationHelper;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class UploadImageController extends BaseController {

    @EJB
    private UploadImageBean uploadImageBean;
    private PaginationHelper<UploadImage> pagination;
    private UploadImage uploadImage;

    private String globalFilter;

    @PostConstruct
    public void init() {
        UploadImage requestUploadImage = (UploadImage) getFlash("uploadImage");
        if (requestUploadImage != null) {
            uploadImage = requestUploadImage;
        } else {
            uploadImage = new UploadImage();
        }

        putFlash("uploadImage", null);
    }

    public String create() {
        try {
            uploadImage = new UploadImage();
            return "create";
        } catch (Throwable e) {
            return "list";
        }
    }

    public String save() {
        try {
            if (uploadImage.getId() != null && uploadImage.getId() > 0) {
                uploadImageBean.edit(uploadImage);
            } else {
                uploadImageBean.create(uploadImage);
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao adicionar/atualizar imagem.");
            return "create";
        }

        return "list";
    }

    public String edit(UploadImage uploadImage) {
        try {
            if (uploadImage.getId() != null && uploadImage.getId() > 0) {
                this.uploadImage = uploadImageBean.find(uploadImage.getId());
                putFlash("uploadImage", this.uploadImage);
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
            if (this.uploadImage.getId() != null && this.uploadImage.getId() > 0) {
                uploadImageBean.remove(this.uploadImage);

                JsfUtil.addSuccessMessage("Removido com sucesso!!");
            }
        } catch (Throwable e) {
        }

        return "list";
    }

    public PaginationHelper<UploadImage> getPagination() {
        if (pagination == null) {
            uploadImageBean.clearCache();
            this.pagination = new PaginationHelper<UploadImage>(uploadImageBean);
        }

        if (globalFilter != null) {
            Map<String, String> filters = new HashMap<String, String>();
            if (!globalFilter.trim().equals("")) {
                globalFilter = "%" + globalFilter + "%";
                filters.put("name", globalFilter);
                filters.put("os", globalFilter);
                filters.put("id", globalFilter);
            }
            this.pagination.setGlobalFilters(filters);
        }

        return pagination;
    }

    public void setPagination(PaginationHelper<UploadImage> pagination) {
        this.pagination = pagination;
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
