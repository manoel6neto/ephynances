package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ActivationBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.SellerContractBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Activation;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.SellerContract;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.servlet.AbstractFilter;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Thomas
 */
@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private String email;
    private String senha;
    private String profileRule;
    private User user;
    private boolean showPdfContract;
    private StreamedContent file;
    private SellerContract contract;

    @EJB
    private UserBean usuarioBean;

    @EJB
    private ActivationBean activationBean;

    @EJB
    private ConfigurationBean configurationBean;

    @EJB
    private SellerContractBean sellerContractBean;

    public String login() {
        try {
            usuarioBean.clearCache();
            file = new DefaultStreamedContent();
            setShowPdfContract(false);
            if (email.contains("@")) {
                user = usuarioBean.findByEmailSenhaProfile(email, Criptografia.criptografar(senha), profileRule);
            } else {
                if (email.length() != 11) {
                    JsfUtil.addErrorMessage("Informe um cpf ou email válido. Utilize apenas números para o cpf!");
                    return "login";
                }
                email = new StringBuilder(email).insert(3, ".").toString();
                email = new StringBuilder(email).insert(7, ".").toString();
                email = new StringBuilder(email).insert(11, "-").toString();
                user = usuarioBean.findByCpfSenhaProfile(email, Criptografia.criptografar(senha), profileRule);
            }

            if (user.isIsVerified()) {
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute(AbstractFilter.USER_KEY, user);

                if (user.getProfileRule().equals(User.getRULER_SELLER())) {
                    if (user.getSellerContract() == null) {

                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                        //Criar e exibir contrato PDF
                        Document document = new Document();
                        PdfWriter.getInstance(document, out);
                        document.open();
                        document.add(new Paragraph("CONTRATO DE PRESTAÇÃO DE SERVIÇO !! "));
                        document.add(new Paragraph("VENDEDOR: " + user.getName()));
                        document.close();

                        ByteArrayInputStream stream = new ByteArrayInputStream(out.toByteArray());
                        file = new DefaultStreamedContent(stream, "application/pdf", "vendor.pdf");
                        setShowPdfContract(true);

                        //Salvar no banco de dados
                        contract = new SellerContract();
                        contract.setUser(user);

                        // Escrevendo o arquivo no banco de dados
                        byte[] byteArrayDoc = out.toByteArray();
                        contract.setContract(byteArrayDoc);
                        sellerContractBean.create(contract);

                        usuarioBean.clearCache();
                        sellerContractBean.clearCache();
                    }
                }

                return "index";
            } else {
                JsfUtil.addErrorMessage("Usuário não ativado. Procure um administrador.");
                return "login";
            }

        } catch (DocumentException e) {
            JsfUtil.addErrorMessage("Falha ao gerar o contrato do vendedor. Consulte o administrador.");
            return "login";
        }
    }
    
    public String accept_contract(boolean status) {
        try {
            if (status) {
                contract.setAcceptDate(new Date(System.currentTimeMillis()));
                sellerContractBean.edit(contract);
                sellerContractBean.clearCache();
                
                JsfUtil.addSuccessMessage("Contrato aceito com sucesso !!");
                return "index";
            } else {
                sellerContractBean.remove(contract);
                sellerContractBean.clearCache();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao alterar o status do contrato.");
        }
        
        return "/login?faces-redirect=true";
    }

    public String recoverPassword() {
        try {
            if (email.contains("@")) {
                user = usuarioBean.findByEmailProfile(email, profileRule);
                if (user.isIsVerified()) {
                    user.setPassword(Criptografia.criptografar(user.getCpf() + String.valueOf(System.currentTimeMillis())));
                    usuarioBean.edit(user);
                    usuarioBean.clearCache();
                    //Gerar ativação para recuperação de senha

                    Activation activation = new Activation();
                    activation.setUser(user);
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, 1);
                    activation.setDueDate(c.getTime());
                    activation.setToken(Utils.generateToken(user));
                    activationBean.create(activation);
                    activationBean.clearCache();

                    Configuration config = configurationBean.findAll().get(0);
                    Utils.sendEmail(user.getEmail(), user.getName(), "<html><body><a href='http://192.168.0.105:8080/ephynances/activation/active.xhtml?token=" + activation.getToken() + "'>Recuperar Senha Ephynances</a></body></html>", config.getSmtpServer(), config.getEmail(), "Ativação Ephynances", config.getUserName(), config.getPassword(), config.getSmtpPort(), "Ativador Physis Ephynances");

                    JsfUtil.addSuccessMessage("Recuperação solicitada com sucesso!");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao recuperar informações para recuperação de senha.");
        }

        return "/login?faces-redirect=true";
    }

    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(AbstractFilter.USER_KEY, null);
        session.invalidate();

        return "/login?faces-redirect=true";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getProfileRule() {
        return profileRule;
    }

    public void setProfileRule(String profileRule) {
        this.profileRule = profileRule;
    }

    public User getLoggedUser() {
        return (User) JsfUtil.getSessionAttribute(AbstractFilter.USER_KEY);
    }

    public Boolean isUserInRuller(String ruller) {
        return getLoggedUser().getProfileRule().equals(ruller);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isShowPdfContract() {
        return showPdfContract;
    }

    public void setShowPdfContract(boolean showPdfContract) {
        this.showPdfContract = showPdfContract;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public SellerContract getContract() {
        return contract;
    }

    public void setContract(SellerContract contract) {
        this.contract = contract;
    }
}
