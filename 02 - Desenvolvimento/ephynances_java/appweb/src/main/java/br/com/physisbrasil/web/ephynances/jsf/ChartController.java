package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.Payment;
import br.com.physisbrasil.web.ephynances.model.User;
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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
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

    @PostConstruct
    public void init() {

        //clearCache
        userBean.clearCache();
        agreementBean.clearCache();

        if (users == null) {
            users = new ArrayList<User>();
            userBean.clearCache();
            setUsers(userBean.findAll());
        }

        agreements = agreementBean.findAll("assignmentDate", false);
        if (agreements.size() > 5) {
            agreements = agreements.subList(0, 5);
        }

        payments = paymentBean.findAll("paymentDate", false);
        if (payments.size() > 5) {
            payments = payments.subList(0, 5);
        }

        createBarModel();
        createBarModelPayments();
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

    private void createBarModel() {
        agreementsDateValueChartModel = initBarModel();

        agreementsDateValueChartModel.setTitle("Contratos Assinados");
        agreementsDateValueChartModel.setLegendPosition("nw");

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

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }
}
