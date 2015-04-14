package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.MessagingException;

/**
 *
 * @author Thomas
 */
@ManagedBean
@SessionScoped
public class DaillyProcessController extends BaseController {

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;

    @EJB
    private AgreementInstallmentBean agreementInstallmentBean;

    @EJB
    private ConfigurationBean configurationBean;
    private Configuration configuration;

    @EJB
    private UserBean userBean;
    private List<User> users;

    @PostConstruct
    public void init() {

        if ((List<Agreement>) getFlash("agreements") != null) {
            agreements = (List<Agreement>) getFlash("agreements");
        } else {
            if (agreements == null) {
                agreements = new ArrayList<Agreement>();
            }
        }

        if ((List<User>) getFlash("users") != null) {
            users = (List<User>) getFlash("users");
        } else {
            if (users == null) {
                users = userBean.findAll();
            }
        }

        if ((Configuration) getFlash("configuration") != null) {
            configuration = (Configuration) getFlash("configuration");
        } else {
            if (configuration == null) {
                configuration = configurationBean.findAll().get(0);
            }
        }
    }

    public void checkStatusPaymentsAndSubPayments() {
        try {
            agreementBean.clearCache();
            agreementInstallmentBean.clearCache();
            if (agreements != null) {
                if (agreements.size() > 0) {
                    for (Agreement contract : agreements) {
                        if (!contract.getStatus().equalsIgnoreCase(Agreement.getSTATE_INCOMPLETO()) && !contract.getStatus().equalsIgnoreCase(Agreement.getSTATE_CANCELADO())
                                && !contract.getStatus().equalsIgnoreCase(Agreement.getSTATE_FINALIZADO())) {
                            if (contract.getAgreementInstallments() != null) {
                                if (contract.getAgreementInstallments().size() > 0) {
                                    boolean hasPendingPayment = false;
                                    boolean hasPendingSubAgreementInstallment = false;
                                    for (AgreementInstallment installment : contract.getAgreementInstallments()) {

                                        //Data atual para verificar o prazo de 15 dias
                                        Date d = new Date(System.currentTimeMillis());

                                        // Calculando o atraso
                                        Calendar c = Calendar.getInstance();
                                        if (installment.getLiberationDate() == null) {
                                            c.setTime(installment.getDueDate());
                                            c.set(Calendar.DATE, c.get(Calendar.DATE) + 15);
                                        } else {
                                            c.setTime(installment.getLiberationDate());
                                            c.set(Calendar.DATE, c.get(Calendar.DATE) + 15);
                                        }

                                        //Comparando com a data atual (se maior contrato atrasado)
                                        if (c.getTime().after(d)) {
                                            installment.setStatus(AgreementInstallment.getSTATUS_PENDENTE());
                                            agreementInstallmentBean.edit(installment);
                                            agreementInstallmentBean.clearCache();

                                            hasPendingPayment = true;
                                        } else {
                                            if (installment.getSubAgreementInstallment() != null) {
                                                if (installment.getSubAgreementInstallment().getPayment() == null) {
                                                    hasPendingSubAgreementInstallment = true;
                                                }
                                            }
                                        }

                                        //Comparando se esta próximo da data (alerta de 15 dias restantes)
                                        //Data atual
                                        d = new Date(System.currentTimeMillis());

                                        //Calcula de hoje mais 15 dias
                                        c = Calendar.getInstance();
                                        c.setTime(d);
                                        c.set(Calendar.DATE, c.get(Calendar.DATE) + 15);

                                        //Se teve liberação utilizar a data da liberação como referência
                                        if (installment.getLiberationDate() == null) {
                                            if (c.getTime().equals(installment.getDueDate())) {
                                                //Envia email alertando da proximidade do vencimento
                                                sendEmailToAdmins("Contrato de número: " + contract.getPhysisAgreementNumber() + " com parcela no valor de : R$" + installment.getValue() + " com vencimento em 15 dias");
                                                sendEmailToSeller("Contrato de número: " + contract.getPhysisAgreementNumber() + " com parcela no valor de : R$" + installment.getValue() + " com vencimento em 15 dias", contract.getUser());
                                                sendEmailToManagerAndContact("Contrato de número: " + contract.getPhysisAgreementNumber() + " com parcela no valor de : R$" + installment.getValue() + " com vencimento em 15 dias",
                                                        contract.getManagerEmail(), contract.getManagerName(), contract.getContactEmail());
                                            }
                                        } else {
                                            if (c.getTime().equals(installment.getLiberationDate())) {
                                                //Envia email alertando da proximidade do vencimento (após liberação)
                                                sendEmailToAdmins("Contrato de número: " + contract.getPhysisAgreementNumber() + " com parcela no valor de : R$" + installment.getValue() + " com (novo) vencimento em 15 dias");
                                                sendEmailToSeller("Contrato de número: " + contract.getPhysisAgreementNumber() + " com parcela no valor de : R$" + installment.getValue() + " com (novo) vencimento em 15 dias", contract.getUser());
                                                sendEmailToManagerAndContact("Contrato de número: " + contract.getPhysisAgreementNumber() + " com parcela no valor de : R$" + installment.getValue() + " com vencimento em 15 dias",
                                                        contract.getManagerEmail(), contract.getManagerName(), contract.getContactEmail());
                                            }
                                        }
                                    }

                                    //Mudando status do contrato
                                    if (hasPendingPayment) {
                                        contract.setStatus(Agreement.getSTATE_SUSPENSO());
                                        agreementBean.edit(contract);
                                        agreementBean.clearCache();

                                        changeStatusGestorEsicar(contract.getManagerCpf(), "I");

                                        //Alertar admins e vendedor responsável pelo contrato do atraso. Além do contato e do Manager
                                        sendEmailToAdmins("Contrato de número: " + contract.getPhysisAgreementNumber() + " em atraso!!");
                                        sendEmailToSeller("Contrato de número: " + contract.getPhysisAgreementNumber() + " em atraso!!", contract.getUser());
                                        sendEmailToManagerAndContact("Contrato de número: " + contract.getPhysisAgreementNumber() + " em atraso!!",
                                                contract.getManagerEmail(), contract.getManagerName(), contract.getContactEmail());
                                    } else if (hasPendingSubAgreementInstallment) {
                                        contract.setStatus(Agreement.getSTATE_ALERTA());
                                        agreementBean.edit(contract);
                                        agreementBean.clearCache();
                                    } else {
                                        if (contract.getExpireDate().equals(new Date(System.currentTimeMillis()))) {
                                            contract.setStatus(Agreement.getSTATE_FINALIZADO());
                                            agreementBean.edit(contract);
                                            agreementBean.clearCache();

                                            changeStatusGestorEsicar(contract.getManagerCpf(), "I");

                                            //Alertar admins e vendedor responsável pelo contrato do atraso. Além do contato e do Manager
                                            sendEmailToAdmins("Contrato de número: " + contract.getPhysisAgreementNumber() + " finalizado!!");
                                            sendEmailToSeller("Contrato de número: " + contract.getPhysisAgreementNumber() + " finalizado!!", contract.getUser());
                                            sendEmailToManagerAndContact("Contrato de número: " + contract.getPhysisAgreementNumber() + " finalizado!!",
                                                    contract.getManagerEmail(), contract.getManagerName(), contract.getContactEmail());
                                        } else {
                                            contract.setStatus(Agreement.getSTATE_ATIVO());
                                            agreementBean.edit(contract);
                                            agreementBean.clearCache();
                                            
                                            changeStatusGestorEsicar(contract.getManagerCpf(), "A");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                Utils.sendEmail("manoel.carvalho.neto@gmail.com", "Manoel Carvalho Neto", "Erro ao verificar contratos: " + e.getMessage(), configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                        configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
            } catch (MessagingException ex) {
                Logger.getLogger(DaillyProcessController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendEmailToAdmins(String message) {
        for (User u : users) {
            if (u.getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
                try {
                    Utils.sendEmail(u.getEmail(), u.getName(), message, configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                            configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
                } catch (MessagingException ex) {
                    Logger.getLogger(DaillyProcessController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void sendEmailToSeller(String message, User user) {
        try {
            if (user != null) {
                Utils.sendEmail(user.getEmail(), user.getName(), message, configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                        configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
            }
        } catch (MessagingException ex) {
            Logger.getLogger(DaillyProcessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendEmailToManagerAndContact(String message, String managerEmail, String managerName, String contactEmail) {
        try {
            //First send email to manager
            Utils.sendEmail(managerEmail, managerName, message, configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                    configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
            //After send email to contact
            Utils.sendEmail(contactEmail, "", message, configuration.getSmtpServer(), configuration.getEmail(), "Função automática ephynances",
                    configuration.getUserName(), configuration.getPassword(), configuration.getSmtpPort(), configuration.getEmail());
        } catch (MessagingException ex) {
            Logger.getLogger(DaillyProcessController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void changeStatusGestorEsicar(String cpf, String status) {
        //Propriedades de conexao
        String HOSTNAME = "localhost";
        String USERNAME = "root";
        String PASSWORD = "Physis_2013";
        String DATABASE = "physis_esicar";
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DATABASE;
        String URLESICAR = "http://" + HOSTNAME + "/esicar/esicar/index.php/comunica_financeiro/ativa_desativa_usuario?id=";

        Connection conn;
        Statement stmt;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String sql;
            int id = 0;

            sql = "SELECT id_usuario FROM usuario WHERE login = " + cpf.replace(".", "").replace("-", "");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt("id_usuario");
            }
            rs.close();

            if (id != 0) {
                //Ativar usuário no esicar                              
                URL urlcon = new URL(URLESICAR + id + "&status=" + status);
                HttpURLConnection connect = (HttpURLConnection) urlcon.openConnection();
                connect.connect();
                if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                    JsfUtil.addErrorMessage("Falha ao solicitar alteração de status no esicar");
                }
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o status no banco de dados");
        } catch (SQLException e) {
            JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o status no banco de dados");
        } catch (MalformedURLException ex) {
            Logger.getLogger(AgreementController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AgreementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
