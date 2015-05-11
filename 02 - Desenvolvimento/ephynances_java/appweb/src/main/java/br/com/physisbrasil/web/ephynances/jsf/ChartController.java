package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.Payment;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Thomas
 */
@ManagedBean
@SessionScoped
public class ChartController extends BaseController {

    @EJB
    private UserBean userBean;
    private List<User> users;

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;

    @EJB
    private PaymentBean paymentBean;
    private List<Payment> payments;

    @EJB
    private ProponentSiconvBean propSiconvBean;

    private BarChartModel agreementsDateValueChartModel;
    private BarChartModel lastPayments;
    private HorizontalBarChartModel contractPaymentsForMonth;
    private HorizontalBarChartModel contractForTopSellers;

    private Map<String, Integer> months;

    private User logUser;

    @PostConstruct
    public void init() {

        logUser = (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);

        //clearCache
        userBean.clearCache();
        agreementBean.clearCache();

        if (users == null) {
            users = new ArrayList<User>();
            userBean.clearCache();
            setUsers(userBean.findAll());
        }

        agreements = agreementBean.findAll("assignmentDate", false);
        if (logUser.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
            List<Agreement> tempRemove = new ArrayList<Agreement>();
            for (Agreement tempA : agreements) {
                if (!tempA.getUser().equals(logUser)) {
                    tempRemove.add(tempA);
                }
            }

            agreements.removeAll(tempRemove);
        }

        //Get Top Five
        if (agreements.size() > 5) {
            agreements = agreements.subList(0, 5);
        }

        payments = paymentBean.findAll("paymentDate", false);
        if (logUser.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
            List<Payment> tempRemovePayment = new ArrayList<Payment>();
            for (Payment tempP : payments) {
                if (tempP.getAgreementInstallment() != null) {
                    if (!tempP.getAgreementInstallment().getAgreement().getUser().equals(logUser)) {
                        tempRemovePayment.add(tempP);
                    }
                } else {
                    if (!tempP.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser().equals(logUser)) {
                        tempRemovePayment.add(tempP);
                    }
                }
            }
            
            payments.removeAll(tempRemovePayment);
        }

        //Get Top Five
        if (payments.size() > 5) {
            payments = payments.subList(0, 5);
        }

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

        createBarModel();
        createBarModelPayments();
        createBarModelContractsPaymentsMonth();
        createBarModelContractsForTopSellers();
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

    public PieChartModel getChartActiveUsers() {
        PieChartModel chart = new PieChartModel();
        Integer active = 0, inactive = 0;

        for (User systemUser : users) {
            if (systemUser.isIsVerified()) {
                active++;
            } else {
                inactive++;
            }
        }

        chart.set("Ativos", active);
        chart.set("Inativos", inactive);

        return chart;
    }

    public PieChartModel sellerForContracts() {
        PieChartModel chartModel = new PieChartModel();

        List<User> sellers = userBean.findSellers();
        for (User sel : sellers) {
            if (sel.getAgreements().size() > 0) {
                chartModel.set(sel.getName(), sel.getAgreements().size());
            }
        }

        return chartModel;
    }

    private void createBarModelContractsPaymentsMonth() {
        contractPaymentsForMonth = new HorizontalBarChartModel();

        ChartSeries contractSeries = new ChartSeries();
        contractSeries.setLabel("Contratos");
        ChartSeries paymentSeries = new ChartSeries();
        paymentSeries.setLabel("Pagamentos");

        List<Agreement> contractList = agreementBean.findAll();
        User logged = (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
        if (logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
            List<Agreement> removeNotSeller = new ArrayList<Agreement>();
            for (Agreement agree : contractList) {
                if (!agree.getUser().equals(logged)) {
                    removeNotSeller.add(agree);
                }
            }

            contractList.removeAll(removeNotSeller);
        }

        List<Payment> paymentList = paymentBean.findAll();
        if (logged.getProfileRule().equalsIgnoreCase(User.getRULER_SELLER())) {
            List<Payment> removeNotSeller = new ArrayList<Payment>();
            for (Payment paym : paymentList) {
                if (paym.getAgreementInstallment() != null) {
                    if (!paym.getAgreementInstallment().getAgreement().getUser().equals(logged)) {
                        removeNotSeller.add(paym);
                    }
                } else {
                    if (!paym.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getUser().equals(logged)) {
                        removeNotSeller.add(paym);
                    }
                }
            }

            paymentList.removeAll(removeNotSeller);
        }

        Calendar c;
        Calendar atual = Calendar.getInstance();
        atual.setTime(new Date(System.currentTimeMillis()));
        Map<String, List<Agreement>> contractsForMonths = new HashMap<String, List<Agreement>>();
        Map<String, List<Payment>> paymentsForMonths = new HashMap<String, List<Payment>>();
        for (String month : returnListOfMonthsFromHashMap()) {
            //Contracts
            List<Agreement> tempListAgreement = new ArrayList<Agreement>();
            for (Agreement a : contractList) {
                c = Calendar.getInstance();
                c.setTime(a.getAssignmentDate());
                if (c.get(Calendar.YEAR) == atual.get(Calendar.YEAR) && c.get(Calendar.MONTH) == months.get(month)) {
                    tempListAgreement.add(a);
                }
            }
            contractsForMonths.put(month, tempListAgreement);

            //Payments
            List<Payment> tempListPayments = new ArrayList<Payment>();
            for (Payment p : paymentList) {
                c = Calendar.getInstance();
                c.setTime(p.getPaymentDate());
                if (c.get(Calendar.YEAR) == atual.get(Calendar.YEAR) && c.get(Calendar.MONTH) == months.get(month)) {
                    tempListPayments.add(p);
                }
            }
            paymentsForMonths.put(month, tempListPayments);
        }

        //Create chart logic
        for (String month : returnListOfMonthsFromHashMap()) {
            contractSeries.set(month, checkTotalListContracts(contractsForMonths.get(month)).intValue());
            paymentSeries.set(month, checkTotalListPayments(paymentsForMonths.get(month)).intValue());
        }

        contractPaymentsForMonth.addSeries(contractSeries);
        contractPaymentsForMonth.addSeries(paymentSeries);

        contractPaymentsForMonth.setTitle("Contratos e Pagamentos por Mês");
        contractPaymentsForMonth.setLegendPosition("nw");
        contractPaymentsForMonth.setAnimate(true);
        contractPaymentsForMonth.setStacked(true);
        contractPaymentsForMonth.setShadow(true);
        contractPaymentsForMonth.setShowDatatip(true);
        contractPaymentsForMonth.setShowPointLabels(true);
    }

    private void createBarModelContractsForTopSellers() {
        contractForTopSellers = new HorizontalBarChartModel();

        ChartSeries contractSeries = new ChartSeries();
        contractSeries.setLabel("Qntd. Contratos");

        List<Agreement> contractList = agreementBean.findAll();

        Map<String, Integer> contractsForSeller = new HashMap<String, Integer>();

        for (Agreement a : contractList) {
            if (contractsForSeller.containsKey(a.getUser().getName())) {
                Integer tempQntd = contractsForSeller.get(a.getUser().getName());
                tempQntd++;
                contractsForSeller.replace(a.getUser().getName(), tempQntd);
            } else {
                contractsForSeller.put(a.getUser().getName(), 1);
            }
        }

        //Create chart logic
        int count = 0;
        for (String name : contractsForSeller.keySet()) {
            if (count < 10) {
                contractSeries.set(name, (contractsForSeller.get(name)));
            } else {
                break;
            }
        }
        contractForTopSellers.addSeries(contractSeries);

        contractForTopSellers.setTitle("Top 10 representantes");
        contractForTopSellers.setLegendPosition("nw");
        contractForTopSellers.setAnimate(true);
        contractForTopSellers.setShadow(true);
        contractForTopSellers.setShowDatatip(true);
        contractForTopSellers.setShowPointLabels(true);
    }

    public BigDecimal checkTotalListPayments(List<Payment> paymentList) {
        BigDecimal total = new BigDecimal(0);
        for (Payment p : paymentList) {
            total = total.add(p.getTotalValue());
        }

        return total;
    }

    public BigDecimal checkTotalListContracts(List<Agreement> agreementList) {
        BigDecimal total = new BigDecimal(0);
        for (Agreement a : agreementList) {
            total = total.add(a.getTotalPrice());
        }

        return total;
    }

    private void createBarModel() {
        agreementsDateValueChartModel = initBarModel();

        agreementsDateValueChartModel.setTitle("Contratos Assinados");
        agreementsDateValueChartModel.setLegendPosition("nw");
        agreementsDateValueChartModel.setAnimate(true);
        agreementsDateValueChartModel.setShadow(true);
        agreementsDateValueChartModel.setShowDatatip(true);
        agreementsDateValueChartModel.setShowPointLabels(true);

        //Axis xAxis = agreementsDateValueChartModel.getAxis(AxisType.X);
        Axis yAxis = agreementsDateValueChartModel.getAxis(AxisType.Y);
        //yAxis.setLabel("Valor");
        yAxis.setMin(0);
        yAxis.setMax(checkMaxValueAgreements());
    }

    private BarChartModel initBarModel() {
        BarChartModel model = new BarChartModel();
        int count = 1;

        for (Agreement a : agreements) {
            ChartSeries agrees = new ChartSeries();
            agrees.setLabel(propSiconvBean.find(a.getIdPrimaryCnpj()).getNome());
            agrees.set(count, a.getTotalPrice().intValue());
            model.addSeries(agrees);
            count = count + 2;
        }

        return model;
    }

    public int checkMaxValueAgreements() {
        int max = 0;

        for (Agreement a : agreements) {
            if (a.getTotalPrice().intValue() > max) {
                max = a.getTotalPrice().intValue();
            }
        }

        return max + (max / 100 * 20);
    }

    private void createBarModelPayments() {
        lastPayments = initBarModelPayments();

        lastPayments.setTitle("Últimos Pagamentos");
        lastPayments.setLegendPosition("nw");
        lastPayments.setAnimate(true);
        lastPayments.setShadow(true);
        lastPayments.setShowDatatip(true);
        lastPayments.setShowPointLabels(true);

        //Axis xAxis = agreementsDateValueChartModel.getAxis(AxisType.X);
        Axis yAxis = lastPayments.getAxis(AxisType.Y);
        //yAxis.setLabel("Valor");
        yAxis.setMin(0);
        yAxis.setMax(checkMaxValuePayments());
    }

    private BarChartModel initBarModelPayments() {
        BarChartModel model = new BarChartModel();
        int count = 1;

        for (Payment p : payments) {
            ChartSeries pay = new ChartSeries();
            if (p.getAgreementInstallment() != null) {
                pay.setLabel(String.format("%s", p.getAgreementInstallment().getAgreement().getPhysisAgreementNumber()));
            } else {
                pay.setLabel(String.format("%s", p.getSubAgreementInstallment().getAgreementInstallment().getAgreement().getPhysisAgreementNumber()));
            }
            pay.set(count, p.getTotalValue().intValue());
            model.addSeries(pay);
            count = count + 2;
        }

        return model;
    }

    public int checkMaxValuePayments() {
        int max = 0;

        for (Payment p : payments) {
            if (p.getTotalValue().intValue() > max) {
                max = p.getTotalValue().intValue();
            }
        }

        return max + (max / 100 * 20);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public String formatValueToReais(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format(value);

        return formatado;
    }

    public BarChartModel getAgreementsDateValueChartModel() {
        return agreementsDateValueChartModel;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public BarChartModel getLastPayments() {
        return lastPayments;
    }

    public Map<String, Integer> getMonths() {
        return months;
    }

    public void setMonths(Map<String, Integer> months) {
        this.months = months;
    }

    public HorizontalBarChartModel getContractPaymentsForMonth() {
        return contractPaymentsForMonth;
    }

    public HorizontalBarChartModel getContractForTopSellers() {
        return contractForTopSellers;
    }

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }

    public User getLogUser() {
        return logUser;
    }

    public void setLogUser(User logUser) {
        this.logUser = logUser;
    }
}
