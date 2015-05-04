package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.Payment;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class ComissionController extends BaseController {

    @EJB
    private UserBean userBean;
    private List<User> users;
    private User selectedUser;

    private List<Payment> payments;
    private BigDecimal total;
    private BigDecimal comission;

    private Map<String, Integer> months;
    private String selectedMonth;
    private int selectedYear;

    private boolean showForms;

    @PostConstruct
    public void init() {

        if ((User) getFlash("selectedUser") != null) {
            selectedUser = (User) getFlash("selectedUser");
        } else {
            if (selectedUser == null) {
                selectedUser = new User();
            }
        }

        if ((List<User>) getFlash("users") != null) {
            users = (List<User>) getFlash("users");
        } else {
            if (users == null) {
                users = userBean.findSellers();
            }
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
        payments = new ArrayList<Payment>();
        selectedYear = Calendar.getInstance().get(Calendar.YEAR);
        total = new BigDecimal(0);
        comission = new BigDecimal(0);
        showForms = false;
    }

    public void calcComission() {
        try {
            if (selectedUser != null) {
                if (selectedUser.getAgreements() != null) {
                    if (selectedUser.getAgreements().size() > 0) {
                        for (Agreement agreement : selectedUser.getAgreements()) {
                            if (agreement.getAgreementInstallments() != null) {
                                if (agreement.getAgreementInstallments().size() > 0) {
                                    for (AgreementInstallment installment : agreement.getAgreementInstallments()) {
                                        if (installment.getPayment() != null) {
                                            if (installment.getPayment().getConfirmationDate() != null) {
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(installment.getPayment().getConfirmationDate());
                                                if (c.get(Calendar.YEAR) == selectedYear && c.get(Calendar.MONTH) == months.get(selectedMonth)) {
                                                    payments.add(installment.getPayment());
                                                }

                                                if (installment.getSubAgreementInstallment() != null) {
                                                    if (installment.getSubAgreementInstallment().getPayment() != null) {
                                                        if (installment.getSubAgreementInstallment().getPayment().getConfirmationDate() != null) {
                                                            c = Calendar.getInstance();
                                                            c.setTime(installment.getSubAgreementInstallment().getPayment().getConfirmationDate());
                                                            if (c.get(Calendar.YEAR) == selectedYear && c.get(Calendar.MONTH) == months.get(selectedMonth)) {
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
                        }

                        if (payments.size() > 0) {
                            for (Payment pay : payments) {
                                total = total.add(pay.getTotalValue());
                            }

                            comission = total.multiply(new BigDecimal(Double.valueOf(String.valueOf(selectedUser.getCommission())) / 100));
                            comission = comission.setScale(2, RoundingMode.CEILING);
                            showForms = true;
                        } else {
                            JsfUtil.addErrorMessage("Nenhum pagamento identificado no mês e ano informados. Nenhum valor a receber !!");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Usuário não fechou nenhum contrato no período.");
                    }
                } else {
                    JsfUtil.addErrorMessage("Usuário não fechou nenhum contrato no período.");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao executar consultas para calcular a comissão do vendedor.");
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public int getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public boolean isShowForms() {
        return showForms;
    }

    public void setShowForms(boolean showForms) {
        this.showForms = showForms;
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
    
    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }
    
    public String formatValueToReais(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format(value);

        return formatado;
    }
}
