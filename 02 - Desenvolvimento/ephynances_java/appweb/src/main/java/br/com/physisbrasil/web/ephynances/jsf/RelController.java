package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.StateBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.Payment;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
import br.com.physisbrasil.web.ephynances.model.State;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.servlet.AbstractFilter;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Thomas
 */
@ManagedBean
@SessionScoped
public class RelController extends BaseController {

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;
    private Agreement selectAgreement;

    @EJB
    private UserBean userBean;
    private List<User> sellerList;
    private User selectUser;

    @EJB
    private StateBean stateBean;
    private List<State> states;
    private State selectedState;

    @EJB
    private ProponentSiconvBean proponentSiconvBean;

    private List<Payment> payments;
    private BigDecimal totalValuePayments;
    private BigDecimal totalValueAgreements;

    @PostConstruct
    public void init() {

        //check stateAgreements
        User logged = (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
        if (!logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
            agreementBean.clearCache();
            if ((List<Agreement>) getFlash("agreements") != null) {
                agreements = (List<Agreement>) getFlash("agreements");
            } else {
                if (agreements == null) {
                    agreements = agreementBean.findAll();
                    totalValueAgreements = new BigDecimal(0);
                    for (Agreement a : agreements) {
                        totalValueAgreements = totalValueAgreements.add(a.getTotalPrice());
                    }
                }
            }
        } else {
            agreementBean.clearCache();
            if ((List<Agreement>) getFlash("agreements") != null) {
                agreements = (List<Agreement>) getFlash("agreements");
            } else {
                if (agreements == null) {
                    agreements = agreementBean.findAll();
                    for (Agreement tAgree : agreements) {
                        if (!tAgree.getUser().equals(logged)) {
                            agreements.remove(tAgree);
                        }
                    }
                    totalValueAgreements = new BigDecimal(0);
                    for (Agreement a : agreements) {
                        totalValueAgreements = totalValueAgreements.add(a.getTotalPrice());
                    }
                }
            }
        }

        //check sellerList
        userBean.clearCache();
        if ((List<User>) getFlash("sellerList") != null) {
            sellerList = (List<User>) getFlash("sellerList");
        } else {
            if (sellerList == null) {
                sellerList = userBean.findSellers();
            }
        }

        //get states list
        stateBean.clearCache();
        if ((List<State>) getFlash("states") != null) {
            states = (List<State>) getFlash("states");
        } else {
            if (states == null) {
                states = stateBean.findAll();
            }
        }
    }

    //get agreement for select state or all states
    public void calcAgreementForState() {
        try {
            agreementBean.clearCache();
            if (selectedState != null) {
                agreements = new ArrayList<Agreement>();
                for (Agreement agree : agreementBean.findAll()) {
                    if (agree.getProponents() != null) {
                        if (agree.getProponents().size() > 0) {
                            for (ProponentSiconv prop : agree.getProponents()) {
                                if (prop.getMunicipioUfSigla().equalsIgnoreCase(selectedState.getAcronym())) {
                                    if (!agreements.contains(agree)) {
                                        agreements.add(agree);
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                agreements = agreementBean.findAll();
            }

            totalValueAgreements = new BigDecimal(0);
            for (Agreement a : agreements) {
                totalValueAgreements = totalValueAgreements.add(a.getTotalPrice());
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os contratos para o estado informado");
        }
    }

    //get payments for state
    public void calcPaymentsForStates() {
        try {
            calcAgreementForState();
            payments = new ArrayList<Payment>();
            if (agreements != null) {
                if (agreements.size() > 0) {
                    for (Agreement agree : agreements) {
                        // get first payments for installments
                        if (agree.getAgreementInstallments() != null) {
                            if (agree.getAgreementInstallments().size() > 0) {
                                for (AgreementInstallment installment : agree.getAgreementInstallments()) {
                                    // check payments
                                    if (installment.getPayment() != null) {
                                        payments.add(installment.getPayment());
                                    }

                                    //check subAgreementInstallment
                                    if (installment.getSubAgreementInstallment() != null) {
                                        if (installment.getSubAgreementInstallment().getPayment() != null) {
                                            payments.add(installment.getSubAgreementInstallment().getPayment());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            totalValuePayments = new BigDecimal(0);
            if (payments.size() > 0) {
                for (Payment pay : payments) {
                    totalValuePayments = totalValuePayments.add(pay.getTotalValue());
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os pagamentos para o estado informado");
        }
    }

    public String formatValueToReais(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format(value);

        return formatado;
    }

    public String checkProponent(Long proponentId) {
        return proponentSiconvBean.find(proponentId).getNome();
    }

    public String formatDate(Date date) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(date);
    }

    public int countParc(Long agreementId) {
        Agreement tempAgreement = agreementBean.find(agreementId);
        return tempAgreement.getAgreementInstallments().size();
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public Agreement getSelectAgreement() {
        return selectAgreement;
    }

    public void setSelectAgreement(Agreement selectAgreement) {
        this.selectAgreement = selectAgreement;
    }

    public List<User> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<User> sellerList) {
        this.sellerList = sellerList;
    }

    public User getSelectUser() {
        return selectUser;
    }

    public void setSelectUser(User selectUser) {
        this.selectUser = selectUser;
    }

    public State getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public BigDecimal getTotalValuePayments() {
        return totalValuePayments;
    }

    public void setTotalValuePayments(BigDecimal totalValuePayments) {
        this.totalValuePayments = totalValuePayments;
    }

    public BigDecimal getTotalValueAgreements() {
        return totalValueAgreements;
    }

    public void setTotalValueAgreements(BigDecimal totalValueAgreements) {
        this.totalValueAgreements = totalValueAgreements;
    }
}
