package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.ValidaCpf;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class AgreementController extends BaseController {

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;
    private List<Agreement> filteredAgreements;
    private Agreement agreement;
    private List<ProponentSiconv> filteredProponents;

    @EJB
    private UserBean userBean;
    private User agreementUser;

    @EJB
    private ConfigurationBean configurationBean;

    @EJB
    private ProponentSiconvBean proponentSiconvBean;
    private ProponentSiconv proponentSiconv;

    private boolean disableQuantCnpjs;

    @PostConstruct
    public void init() {
        if (agreements == null) {
            agreements = new ArrayList<Agreement>();
            setAgreements(agreementBean.findAll());
        }

        if ((User) getFlash("userAgreement") != null) {
            agreementUser = (User) getFlash("userAgreement");
        } else {
            if (agreementUser == null) {
                agreementUser = new User();
            }
        }

        if ((Agreement) getFlash("agreement") != null) {
            agreement = (Agreement) getFlash("agreement");
        } else {
            if (agreement == null) {
                agreement = new Agreement();
                agreement.setPhysisAgreementNumber(createPhysisAgreementNumber());
                agreement.setPeriod(12);
            }
        }

        if ((List<Agreement>) getFlash("agreements") != null) {
            filteredAgreements = (List<Agreement>) getFlash("agreements");
        } else {
            if (filteredAgreements == null) {
                filteredAgreements = new ArrayList<Agreement>();
            }
        }

        if ((ProponentSiconv) getFlash("proponent") != null) {
            proponentSiconv = (ProponentSiconv) getFlash("proponent");
        } else {
            proponentSiconv = null;
        }

        disableQuantCnpjs = false;
    }

    public void getListFilteredBySeller(Long userId) {
        User tempUser = userBean.find(userId);
        if (tempUser != null) {
            if (tempUser.getProfileRule().equals(User.getRULER_CONTRIBUTOR())) {
                filteredAgreements = tempUser.getSellers().get(0).getSeller().getAgreements();
            } else {
                filteredAgreements = tempUser.getAgreements();
            }

            putFlash("agreements", getFilteredAgreements());
        }
    }

    public String startAgreement(Long userId) {
        User tempUser = userBean.find(userId);
        if (tempUser != null) {
            if (!tempUser.getProfileRule().equals(User.getRULER_CONTRIBUTOR())) {
                setAgreementUser(tempUser);
            } else {
                setAgreementUser(tempUser.getSellers().get(0).getSeller());
            }

            putFlash("userAgreement", getAgreementUser());
            return "/agreement/create";
        }

        return "/agreement/list";
    }

    public String editAgreement(Long agreementId) {
        if (agreementBean.find(agreementId) != null) {
            agreement = agreementBean.find(agreementId);
            proponentSiconv = agreement.getProponents().get(0);
            agreementUser = agreement.getUser();

            putFlash("agreement", agreement);
            putFlash("userAgreement", agreementUser);
            putFlash("proponent", proponentSiconv);

            return "/agreement/edit";
        } else {
            JsfUtil.addErrorMessage("Falha ao consultar o contrato informado.");
            return "/agreement/list";
        }
    }

    public String addAgreement() {
        try {
            if (agreement != null) {
                if (proponentSiconv != null) {
                    if (agreement.getManagerCpf() != null && !agreement.getManagerCpf().equals("")) {
                        if (!ValidaCpf.isCPF(agreement.getManagerCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("userAgreement", getAgreementUser());
                            return "/agreement/create";
                        }

                        if (agreement.getId() != null) {
                            //Update  
                            agreementBean.edit(agreement);
                            agreementBean.clearCache();

                            JsfUtil.addSuccessMessage("Contrato atualizado com sucesso.");
                        } else {
                            //Create
                            //setuser and save agreement
                            agreement.setUser(agreementUser);
                            agreement.setStatus(Agreement.getSTATE_INCOMPLETO());

                            //Calculando a data para expirar o contrato (limite - data atual + o periodo de vigencia em meses)
                            Date d = new Date(System.currentTimeMillis());
                            Calendar c = Calendar.getInstance();
                            c.setTime(d);
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + agreement.getPeriod());
                            agreement.setExpireDate(c.getTime());

                            agreementBean.create(agreement);
                            agreementBean.clearCache();

                            //Save relationship agreement - proponent
                            proponentSiconv.setAgreement(agreement);
                            proponentSiconvBean.edit(proponentSiconv);
                            proponentSiconvBean.clearCache();

                            //save seed with a seed plus one
                            Configuration config = configurationBean.findAll().get(0);
                            config.setContractSeed(config.getContractSeed() + 1);
                            configurationBean.edit(config);
                            configurationBean.clearCache();

                            JsfUtil.addSuccessMessage("Contrato cadastrado com sucesso.");
                        }

                        return "/agreement/list";
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao cadastrar contrato.");
        }

        return "/agreement/create";
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public User getAgreementUser() {
        return agreementUser;
    }

    public void setAgreementUser(User agreementUser) {
        this.agreementUser = agreementUser;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public List<Agreement> getFilteredAgreements() {
        return filteredAgreements;
    }

    public void setFilteredAgreements(List<Agreement> filteredAgreements) {
        this.filteredAgreements = filteredAgreements;
    }

    public ProponentSiconv getProponentSiconv() {
        return proponentSiconv;
    }

    public void setProponentSiconv(ProponentSiconv proponentSiconv) {
        this.proponentSiconv = proponentSiconv;
    }

    public List<ProponentSiconv> getFilteredProponents() {
        return filteredProponents;
    }

    public void setFilteredProponents(List<ProponentSiconv> filteredProponents) {
        this.filteredProponents = filteredProponents;
    }

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }

    public List<String> getAgreementTypes() {
        return Agreement.getAGREEMENT_TYPES();
    }

    public boolean isDisableQuantCnpjs() {
        return disableQuantCnpjs;
    }

    public void setDisableQuantCnpjs(boolean disableQuantCnpjs) {
        this.disableQuantCnpjs = disableQuantCnpjs;
    }

    public String createPhysisAgreementNumber() {
        //5 digitos sequenciais / ANO
        Configuration config = configurationBean.findAll().get(0);
        String number = String.valueOf(config.getContractSeed());
        while (number.length() < 6) {
            number = "0" + number;
        }

        Date data = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("yyyy");
        String contractNumber = number + "/" + format.format(data);
        return contractNumber;
    }

    public void filterProponentsForAgreementType() {
        List<ProponentSiconv> tempList = new ArrayList<ProponentSiconv>();
        if (agreement != null) {
            if (!agreement.getAgreementType().equals("")) {
                for (ProponentSiconv prop : agreementUser.getProponents()) {
                    if (prop.getEsferaAdministrativa().equalsIgnoreCase(agreement.getAgreementType())) {
                        tempList.add(prop);
                    }
                }
            }
        }

        setFilteredProponents(tempList);
    }

    public void checkQuantCnpjMunicipal() {
        if (agreement != null) {
            if (!agreement.getAgreementType().equals("")) {
                if (agreement.getAgreementType().equalsIgnoreCase("MUNICIPAL")) {
                    if (proponentSiconv != null) {
                        if (proponentSiconv.getId() > 0) {
                            agreement.setCnpjAmount(proponentSiconvBean.findBySphereStateCityAll(proponentSiconv.getEsferaAdministrativa(), proponentSiconv.getMunicipioUfNome(), proponentSiconv.getMunicipio()).size());
                            disableQuantCnpjs = true;
                        }
                    } else {
                        disableQuantCnpjs = false;
                    }
                } else {
                    disableQuantCnpjs = false;
                }
            } else {
                disableQuantCnpjs = false;
            }
        } else {
            disableQuantCnpjs = false;
        }
    }
}
