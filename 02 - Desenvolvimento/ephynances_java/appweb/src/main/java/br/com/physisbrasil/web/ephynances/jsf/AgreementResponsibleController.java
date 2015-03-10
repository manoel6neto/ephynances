package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementResponsibleBean;
import br.com.physisbrasil.web.ephynances.model.AgreementResponsible;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.ValidaCpf;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class AgreementResponsibleController extends BaseController {

    @EJB
    private AgreementResponsibleBean agreementResponsibleBean;
    private List<AgreementResponsible> agreementResponsibles;
    private AgreementResponsible agreementResponsible;

    @PostConstruct
    public void init() {
        if (agreementResponsibles == null) {
            agreementResponsibles = new ArrayList<AgreementResponsible>();
            setAgreementResponsibles(agreementResponsibleBean.findAll());
        }

        AgreementResponsible requestAgreementResponsible = (AgreementResponsible) getFlash("agreementResponsible");
        if (requestAgreementResponsible != null) {
            agreementResponsible = requestAgreementResponsible;
        } else {
            agreementResponsible = new AgreementResponsible();
        }

        putFlash("agreementResponsible", null);
    }

    public String create() {
        if (agreementResponsible == null) {
            agreementResponsible = new AgreementResponsible();
        }

        return "create";
    }

    public String save() {
        try {
            if (agreementResponsible != null) {
                if (agreementResponsible.getId() != null && agreementResponsible.getId() > 0) {
                    if (agreementResponsible.getCpf() != null && !agreementResponsible.getCpf().equals("")) {
                        if (!ValidaCpf.isCPF(agreementResponsible.getCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("agreementResponsible", agreementResponsible);
                            return "edit";
                        }
                    }

                    agreementResponsibleBean.edit(agreementResponsible);
                    agreementResponsibleBean.clearCache();
                    JsfUtil.addSuccessMessage("Responsável atualizado com sucesso!");
                } else {
                    if (agreementResponsible.getCpf() != null && !agreementResponsible.getCpf().equals("")) {
                        if (!ValidaCpf.isCPF(agreementResponsible.getCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("agreementResponsible", null);
                            return "list";
                        }
                    }
                    agreementResponsibleBean.create(agreementResponsible);
                    agreementResponsibleBean.clearCache();
                    JsfUtil.addSuccessMessage("Responsável cadastrado com sucesso!");
                }
            } else {
                throw new Exception("Falha ao carregar dados do formulário.");
            }
        } catch (Throwable e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }

        return "list";
    }

    public String edit(AgreementResponsible agreementResponsible) {
        try {
            if (agreementResponsible.getId() != null && agreementResponsible.getId() > 0) {
                agreementResponsible = agreementResponsibleBean.find(agreementResponsible.getId());
                putFlash("agreementResponsible", agreementResponsible);
            } else {
                JsfUtil.addErrorMessage("Responsável não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "edit";
    }

    public String view(AgreementResponsible agreementResponsible) {
        try {
            if (agreementResponsible.getId() != null && agreementResponsible.getId() > 0) {
                agreementResponsible = agreementResponsibleBean.find(agreementResponsible.getId());
                putFlash("agreementResponsible", agreementResponsible);
            } else {
                JsfUtil.addErrorMessage("Responsável não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "view";
    }

    public String delete(Long id) {
        try {
            agreementResponsible = agreementResponsibleBean.find(id);
            if (agreementResponsible.getId() != null && agreementResponsible.getId() > 0) {
                agreementResponsibleBean.remove(agreementResponsible);
                agreementResponsibleBean.clearCache();
                JsfUtil.addSuccessMessage("Responsável apagado com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar registro.");
        }

        return "list";
    }

    public AgreementResponsible getAgreementResponsible() {
        return agreementResponsible;
    }

    public void setAgreementResponsible(AgreementResponsible agreementResponsible) {
        this.agreementResponsible = agreementResponsible;
    }

    public List<AgreementResponsible> getAgreementResponsibles() {
        return agreementResponsibles;
    }

    public void setAgreementResponsibles(List<AgreementResponsible> agreementResponsibles) {
        this.agreementResponsibles = agreementResponsibles;
    }
}
