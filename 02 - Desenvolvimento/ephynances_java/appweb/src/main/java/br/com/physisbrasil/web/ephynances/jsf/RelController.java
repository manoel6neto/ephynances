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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private List<Agreement> agreementsSellers;
    private List<Agreement> agreementsMissingPayments;
    private List<Agreement> agreementsMonth;
    private Agreement selectAgreement;
    private String selectedMonth;
    private Map<String, Integer> months;
    private int selectedYear;

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
    private List<Payment> paymentsSeller;
    private List<Payment> paymentsMonth;
    private BigDecimal totalValuePayments;
    private BigDecimal totalValueAgreements;
    private BigDecimal totalValuePaymentsSeller;
    private BigDecimal totalValueAgreementsSeller;
    private BigDecimal totalMissingPayments;
    private BigDecimal totalValueMonth;
    private BigDecimal totalValueMonthPayments;
    private Map<Agreement, List<Payment>> agreementsListPayments;
    private Map<User, List<Payment>> sellerListPayments;

    @PostConstruct
    public void init() {

        userBean.clearCache();
        stateBean.clearCache();
        agreementBean.clearCache();
        proponentSiconvBean.clearCache();

        months = new HashMap<String, Integer>() {
            {
                put("Janeiro", 0);
                put("Fevereiro", 1);
                put("Março", 2);
                put("Abril", 3);
                put("Maio", 4);
                put("Junho", 5);
                put("Julho", 6);
                put("Agosto", 7);
                put("Setembro", 8);
                put("Outubro", 9);
                put("Novembro", 10);
                put("Dezembro", 11);
            }
        ;
        }
    
    ;

        //check stateAgreements
        calcAgreementForState();
        calcPaymentsForStates();

        //check sellers
        calcAgreementForSeller();
        calcPaymentsForSellers();

        //check MissingPayments
        calcDontPaidForState();

        //check sellerList
        if ((List<User>) getFlash("sellerList") != null) {
            sellerList = (List<User>) getFlash("sellerList");
        } else {
            if (sellerList == null) {
                sellerList = userBean.findSellers();
            }
        }

        //get states list
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
            User logged = (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
            agreementBean.clearCache();
            if (selectedState != null) {
                agreements = new ArrayList<Agreement>();
                for (Agreement agree : agreementBean.findAll()) {
                    if (logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
                        if (agree.getUser().equals(logged)) {
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
                }
            } else {
                agreements = agreementBean.findAll();
                if (logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
                    for (Agreement tAgree : agreements) {
                        if (!tAgree.getUser().equals(logged)) {
                            agreements.remove(tAgree);
                        }
                    }
                }
            }

            totalValueAgreements = new BigDecimal(0);
            for (Agreement a : agreements) {
                totalValueAgreements = totalValueAgreements.add(a.getTotalPrice());
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os contratos para o estado informado");
        }
    }

    public void calcAgreementForSeller() {
        try {
            agreementBean.clearCache();
            userBean.clearCache();
            agreementsSellers = new ArrayList<Agreement>();
            if (selectUser != null) {
                if (selectUser.getAgreements() != null) {
                    agreementsSellers = selectUser.getAgreements();
                }
            } else {
                agreementsSellers = agreementBean.findAll();
            }

            totalValueAgreementsSeller = new BigDecimal(0);
            for (Agreement a : agreementsSellers) {
                totalValueAgreementsSeller = totalValueAgreementsSeller.add(a.getTotalPrice());
            }

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os contratos para o representante informado");
        }
    }

    public void calcAgreementForMonth() {
        try {
            agreementBean.clearCache();
            User logged = (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
            Calendar c;
            if (selectedMonth.equalsIgnoreCase("Todos")) {
                //Filtra apenas pelo ano
                List<Agreement> tempAgreements = agreementBean.findAll();
                for (Agreement agree : tempAgreements) {
                    c = Calendar.getInstance();
                    c.setTime(agree.getAssignmentDate());
                    
                }
                
                //Removendo os contratos que não são do vendedor
                if (logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
                    for (Agreement ag : tempAgreements) {
                        if (ag.getUser().equals(logged)) {
                            agreementsMonth.add(ag);
                        }
                    }
                } else {
                    agreementsMonth = tempAgreements;
                }
            } else {
                //Filtra pelo ano e pelo mês
                List<Agreement> tempAgreements = agreementBean.findAll();
                for (Agreement a : tempAgreements) {
                    c = Calendar.getInstance();
                    c.setTime(a.getAssignmentDate());
                    
                }
                
                //Removendo os contratos que não são do vendedor
                if (logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
                    for (Agreement ag : tempAgreements) {
                        if (ag.getUser().equals(logged)) {
                            agreementsMonth.add(ag);
                        }
                    }
                } else {
                    agreementsMonth = tempAgreements;
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os contratos para o mês informado");
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
                                        if (installment.getPayment().getConfirmationDate() != null) {
                                            payments.add(installment.getPayment());
                                        }
                                    }

                                    //check subAgreementInstallment
                                    if (installment.getSubAgreementInstallment() != null) {
                                        if (installment.getSubAgreementInstallment().getPayment() != null) {
                                            if (installment.getSubAgreementInstallment().getPayment().getConfirmationDate() != null) {
                                                payments.add(installment.getSubAgreementInstallment().getPayment());
                                            }
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

            //getGroupPaymentsForContracts
            agreementsListPayments = new HashMap<Agreement, List<Payment>>();
            List<Payment> tempListPayments;
            for (Payment pay : payments) {
                if (pay.getAgreementInstallment() != null) {
                    if (agreementsListPayments.containsKey(pay.getAgreementInstallment().getAgreement())) {
                        tempListPayments = agreementsListPayments.get(pay.getAgreementInstallment().getAgreement());
                        tempListPayments.add(pay);
                        agreementsListPayments.replace(pay.getAgreementInstallment().getAgreement(), tempListPayments);
                    } else {
                        tempListPayments = new ArrayList<Payment>();
                        tempListPayments.add(pay);
                        agreementsListPayments.put(pay.getAgreementInstallment().getAgreement(), tempListPayments);
                    }
                } else {
                    if (agreementsListPayments.containsKey(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement())) {
                        tempListPayments = agreementsListPayments.get(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement());
                        tempListPayments.add(pay);
                        agreementsListPayments.replace(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement(), tempListPayments);
                    } else {
                        tempListPayments = new ArrayList<Payment>();
                        tempListPayments.add(pay);
                        agreementsListPayments.put(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement(), tempListPayments);
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os pagamentos para o estado informado");
        }
    }

    public void calcPaymentsForSellers() {
        try {
            calcAgreementForSeller();
            paymentsSeller = new ArrayList<Payment>();
            if (agreementsSellers != null) {
                if (agreementsSellers.size() > 0) {
                    for (Agreement agree : agreementsSellers) {
                        // get first payments for installments
                        if (agree.getAgreementInstallments() != null) {
                            if (agree.getAgreementInstallments().size() > 0) {
                                for (AgreementInstallment installment : agree.getAgreementInstallments()) {
                                    // check payments
                                    if (installment.getPayment() != null) {
                                        if (installment.getPayment().getConfirmationDate() != null) {
                                            paymentsSeller.add(installment.getPayment());
                                        }
                                    }

                                    //check subAgreementInstallment
                                    if (installment.getSubAgreementInstallment() != null) {
                                        if (installment.getSubAgreementInstallment().getPayment() != null) {
                                            if (installment.getSubAgreementInstallment().getPayment().getConfirmationDate() != null) {
                                                paymentsSeller.add(installment.getSubAgreementInstallment().getPayment());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            totalValuePaymentsSeller = new BigDecimal(0);
            if (paymentsSeller.size() > 0) {
                for (Payment pay : paymentsSeller) {
                    totalValuePaymentsSeller = totalValuePaymentsSeller.add(pay.getTotalValue());
                }
            }

            sellerListPayments = new HashMap<User, List<Payment>>();
            List<Payment> tempListPayments;
            for (Payment pay : paymentsSeller) {
                if (pay.getAgreementInstallment() != null) {
                    if (sellerListPayments.containsKey(pay.getAgreementInstallment().getAgreement().getUser())) {
                        tempListPayments = sellerListPayments.get(pay.getAgreementInstallment().getAgreement().getUser());
                        tempListPayments.add(pay);
                        sellerListPayments.replace(pay.getAgreementInstallment().getAgreement().getUser(), tempListPayments);
                    } else {
                        tempListPayments = new ArrayList<Payment>();
                        tempListPayments.add(pay);
                        sellerListPayments.put(pay.getAgreementInstallment().getAgreement().getUser(), tempListPayments);
                    }
                } else {
                    if (sellerListPayments.containsKey(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser())) {
                        tempListPayments = sellerListPayments.get(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser());
                        tempListPayments.add(pay);
                        sellerListPayments.replace(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser(), tempListPayments);
                    } else {
                        tempListPayments = new ArrayList<Payment>();
                        tempListPayments.add(pay);
                        sellerListPayments.put(pay.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser(), tempListPayments);
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os pagamentos para o representante informado");
        }
    }

    public void calcDontPaidForState() {
        try {
            totalMissingPayments = new BigDecimal(0);
            agreementsMissingPayments = new ArrayList<Agreement>();
            boolean status;
            calcAgreementForState();
            if (agreements != null) {
                if (agreements.size() > 0) {
                    for (Agreement agree : agreements) {
                        status = false;
                        if (!agree.getStatus().equalsIgnoreCase(Agreement.getSTATE_CANCELADO()) && !agree.getStatus().equalsIgnoreCase(Agreement.getSTATE_FINALIZADO())) {
                            if (agree.getAgreementInstallments() != null) {
                                if (agree.getAgreementInstallments().size() > 0) {
                                    for (AgreementInstallment install : agree.getAgreementInstallments()) {
                                        if (!install.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_COM_CONFIRMACAO()) && !install.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO())) {
                                            if (install.getDueDate().before(new Date(System.currentTimeMillis()))) {
                                                status = true;
                                                totalMissingPayments = totalMissingPayments.add(install.getValue());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (status) {
                            agreementsMissingPayments.add(agree);
                        }
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar os contratos para o estado informado");
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

    public List<Agreement> listKeys() {
        List<Agreement> tempListString = new ArrayList<Agreement>(agreementsListPayments.keySet());
        return tempListString;
    }

    public List<User> listKeysSellers() {
        List<User> tempListString = new ArrayList<User>(sellerListPayments.keySet());
        return tempListString;
    }

    public BigDecimal calcTotalPaymentsForContract(Long agreementId) {
        try {
            BigDecimal total = new BigDecimal(0);
            Agreement tempAgreement = agreementBean.find(agreementId);
            for (Payment pay : agreementsListPayments.get(tempAgreement)) {
                total = total.add(pay.getTotalValue());
            }

            return total;
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public BigDecimal calcTotalPaymentsForSeller(Long sellerId) {
        try {
            BigDecimal total = new BigDecimal(0);
            User sellerTemp = userBean.find(sellerId);
            for (Payment pay : sellerListPayments.get(sellerTemp)) {
                total = total.add(pay.getTotalValue());
            }
            return total;
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public List<Payment> returnListPaymentsForKey(Long agreementId) {
        Agreement tempAgreement = agreementBean.find(agreementId);
        return agreementsListPayments.get(tempAgreement);
    }

    public List<Payment> returnListPaymentsForKeySeller(Long sellerId) {
        User sellerTemp = userBean.find(sellerId);
        return sellerListPayments.get(sellerTemp);
    }

    public String returnPhysisContractNumber(Payment payment) {
        if (payment.getAgreementInstallment() != null) {
            return payment.getAgreementInstallment().getAgreement().getPhysisAgreementNumber();
        } else {
            return payment.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getPhysisAgreementNumber();
        }
    }

    public String changeTitleAndSetTempId(Long agreementId) {
        Agreement tempAgreement = agreementBean.find(agreementId);

        return tempAgreement.getPhysisAgreementNumber();
    }

    public String returnContactNumberAndProponenteAndUfSigla(Payment payment) {
        String contactNumber = new String();
        String municipio = new String();
        String propUfSigla = new String();

        if (payment != null) {
            if (payment.getAgreementInstallment() != null) {
                contactNumber = payment.getAgreementInstallment().getAgreement().getContactAgreementNumber();
                municipio = proponentSiconvBean.find(payment.getAgreementInstallment().getAgreement().getIdPrimaryCnpj()).getMunicipio();
                propUfSigla = proponentSiconvBean.find(payment.getAgreementInstallment().getAgreement().getIdPrimaryCnpj()).getMunicipioUfSigla();
            } else {
                contactNumber = payment.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getContactAgreementNumber();
                municipio = proponentSiconvBean.find(payment.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getIdPrimaryCnpj()).getMunicipio();
                propUfSigla = proponentSiconvBean.find(payment.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getIdPrimaryCnpj()).getMunicipioUfSigla();
            }
        }

        return String.format("%s - %s - %s", contactNumber, municipio, propUfSigla);
    }

    public String returnSellerName(Payment payment) {
        String sellerName = new String();

        if (payment != null) {
            if (payment.getAgreementInstallment() != null) {
                sellerName = payment.getAgreementInstallment().getAgreement().getUser().getName();
            } else {
                sellerName = payment.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser().getName();
            }
        }

        return String.format("%s", sellerName);
    }

    public BigDecimal totalMissingForContract(Long agreementId) {
        try {
            BigDecimal tempTotal = new BigDecimal(0);
            for (AgreementInstallment inst : returnListMissingInstallments(agreementId)) {
                tempTotal = tempTotal.add(inst.getValue());
            }

            return tempTotal;
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public List<AgreementInstallment> returnListMissingInstallments(Long agreementId) {
        try {
            List<AgreementInstallment> tempListAgreementInstallment = new ArrayList<AgreementInstallment>();
            Agreement tempAgree = agreementBean.find(agreementId);
            for (AgreementInstallment install : tempAgree.getAgreementInstallments()) {
                if (!install.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_COM_CONFIRMACAO()) && !install.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PAGO_SEM_CONFIRMACAO())) {
                    if (install.getDueDate().before(new Date(System.currentTimeMillis()))) {
                        tempListAgreementInstallment.add(install);
                    }
                }
            }

            return tempListAgreementInstallment;
        } catch (Exception e) {
            return new ArrayList<AgreementInstallment>();
        }
    }

    public List<String> returnListOfMonthsFromHashMap() {
        List<String> tempList = new ArrayList<String>();

        tempList.add("Janeiro");
        tempList.add("Fevereiro");
        tempList.add("Março");
        tempList.add("Abril");
        tempList.add("Maio");
        tempList.add("Junho");
        tempList.add("Julho");
        tempList.add("Agosto");
        tempList.add("Setembro");
        tempList.add("Outubro");
        tempList.add("Novembro");
        tempList.add("Dezembro");

        return tempList;
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

    public Map<Agreement, List<Payment>> getAgreementsListPayments() {
        return agreementsListPayments;
    }

    public void setAgreementsListPayments(Map<Agreement, List<Payment>> agreementsListPayments) {
        this.agreementsListPayments = agreementsListPayments;
    }

    public List<Agreement> getAgreementsSellers() {
        return agreementsSellers;
    }

    public void setAgreementsSellers(List<Agreement> agreementsSellers) {
        this.agreementsSellers = agreementsSellers;
    }

    public List<Payment> getPaymentsSeller() {
        return paymentsSeller;
    }

    public void setPaymentsSeller(List<Payment> paymentsSeller) {
        this.paymentsSeller = paymentsSeller;
    }

    public BigDecimal getTotalValuePaymentsSeller() {
        return totalValuePaymentsSeller;
    }

    public void setTotalValuePaymentsSeller(BigDecimal totalValuePaymentsSeller) {
        this.totalValuePaymentsSeller = totalValuePaymentsSeller;
    }

    public BigDecimal getTotalValueAgreementsSeller() {
        return totalValueAgreementsSeller;
    }

    public void setTotalValueAgreementsSeller(BigDecimal totalValueAgreementsSeller) {
        this.totalValueAgreementsSeller = totalValueAgreementsSeller;
    }

    public Map<User, List<Payment>> getSellerListPayments() {
        return sellerListPayments;
    }

    public void setSellerListPayments(Map<User, List<Payment>> sellerListPayments) {
        this.sellerListPayments = sellerListPayments;
    }

    public List<Agreement> getAgreementsMissingPayments() {
        return agreementsMissingPayments;
    }

    public void setAgreementsMissingPayments(List<Agreement> agreementsMissingPayments) {
        this.agreementsMissingPayments = agreementsMissingPayments;
    }

    public BigDecimal getTotalMissingPayments() {
        return totalMissingPayments;
    }

    public void setTotalMissingPayments(BigDecimal totalMissingPayments) {
        this.totalMissingPayments = totalMissingPayments;
    }

    public List<Agreement> getAgreementsMonth() {
        return agreementsMonth;
    }

    public void setAgreementsMonth(List<Agreement> agreementsMonth) {
        this.agreementsMonth = agreementsMonth;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public Map<String, Integer> getMonths() {
        return months;
    }

    public void setMonths(Map<String, Integer> months) {
        this.months = months;
    }

    public List<Payment> getPaymentsMonth() {
        return paymentsMonth;
    }

    public void setPaymentsMonth(List<Payment> paymentsMonth) {
        this.paymentsMonth = paymentsMonth;
    }

    public BigDecimal getTotalValueMonth() {
        return totalValueMonth;
    }

    public void setTotalValueMonth(BigDecimal totalValueMonth) {
        this.totalValueMonth = totalValueMonth;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public BigDecimal getTotalValueMonthPayments() {
        return totalValueMonthPayments;
    }

    public void setTotalValueMonthPayments(BigDecimal totalValueMonthPayments) {
        this.totalValueMonthPayments = totalValueMonthPayments;
    }
}
