package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
import java.util.ArrayList;
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
public class UserController extends BaseController {

    @EJB
    private UserBean usuarioBean;
    //private PaginationHelper<User> pagination;
    private User user;
    private String oldPass;
    private String recoverEmail;
    private List<User> listUser;
    @EJB
    private ConfigurationBean configurationBean;

    @PostConstruct
    public void init() {
        if (listUser == null) {
            listUser = new ArrayList<User>();
            this.loadUsers();
        }

        User requestUser = (User) getFlash("user");
        if (requestUser != null) {
            user = requestUser;
        } else {
            user = new User();
        }

        this.oldPass = "";
        putFlash("user", null);
    }

    public String create() {
        if (this.user == null) {
            this.user = new User();
        }

        return "create";
    }

    public String save() {
        Integer activeAdmins = 0;
        try {
            user.setPassword(Criptografia.criptografar(user.getPassword()));
            if (user.getId() != null && user.getId() > 0) {
                if (user.getProfileRule().equals(getRULER_ADMIN())) {
                    for (User usuarioTemp : usuarioBean.findAll()) {
                        if (usuarioTemp.getProfileRule().equals(getRULER_ADMIN())
                         && usuarioTemp.getDeleteDate() == null) {
                            activeAdmins++;
                        }
                    }

                    if (activeAdmins > 1 || user.getDeleteDate() != null) {
                        usuarioBean.edit(user);
                        JsfUtil.addSuccessMessage("Usuário atualizado com sucesso!!");
                    } else {
                        JsfUtil.addErrorMessage("Não é possível atualizar o usuário pois ele é o único 'Administrador' ativo no sistema.");
                    }
                } else {
                    usuarioBean.edit(user);
                    JsfUtil.addSuccessMessage("Usuário atualizado com sucesso!!");
                }
            } else {
                usuarioBean.create(user);
                JsfUtil.addSuccessMessage("Usuário criado com sucesso!!");
            }
            usuarioBean.clearCache();
            LoginController l = new LoginController();
            if (l.getLoggedUser().equals(user)) {
                l.getLoggedUser().setProfileRule(user.getProfileRule());
            }
        } catch (Throwable e) {
            JsfUtil.addErrorMessage("Falha ao criar/atualizar usuário.");
            return "create";
        }

        return "list";
    }

    public String edit(User user) {
        try {
            if (user.getId() != null && user.getId() > 0) {
                this.user = usuarioBean.find(user.getId());

                putFlash("user", this.user);
            } else {
                JsfUtil.addErrorMessage("Objeto não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "edit";
    }

    public String view(User user) {
        try {
            if (user.getId() != null && user.getId() > 0) {
                this.user = usuarioBean.find(user.getId());

                putFlash("user", this.user);
            } else {
                JsfUtil.addErrorMessage("Objeto não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "view";
    }

    public String delete() {
        Integer activeAdmins = 0;

        try {
            if (this.user.getId() != null && this.user.getId() > 0) {
                LoginController l = new LoginController();
                if (!l.getLoggedUser().equals(this.user)) {
                    for (User usuarioTemp : usuarioBean.findAll()) {
                        if (usuarioTemp.getProfileRule().equals(getRULER_ADMIN())
                                && usuarioTemp.getDeleteDate() == null) {
                            activeAdmins++;
                        }
                    }

                    if (activeAdmins > 1) {
                        this.user.setDeleteDate(new Date());
                        usuarioBean.edit(this.user);
                        usuarioBean.clearCache();
                        JsfUtil.addSuccessMessage("Desativado com sucesso!!");
                    } else {
                        JsfUtil.addErrorMessage("Não é possível desativar o usuário pois ele é o único 'Administrador' ativo no sistema.");
                    }
                } else {
                    JsfUtil.addErrorMessage("Impossível desativar um usuário logado.");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Exception: Falha ao apagar registro.");
        }

        return "list";
    }

    public String active() {
        try {
            if (this.user.getId() != null && this.user.getId() > 0) {

                this.user.setDeleteDate(null);
                usuarioBean.edit(this.user);
                usuarioBean.clearCache();
                JsfUtil.addSuccessMessage("Reativado com sucesso!!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Exception: Falha ao apagar registro.");
        }

        return "list";
    }

    public String changePassword() {
        User loggedUser = usuarioBean.find(getUsuarioLogado().getId());
        try {

            String criptOldPass = Criptografia.criptografar(oldPass);

            if (!criptOldPass.equals(loggedUser.getPassword())) {
                JsfUtil.addErrorMessage("As senhas estão diferentes!");
                return null;
            }

            loggedUser.setPassword(Criptografia.criptografar(user.getPassword()));

            usuarioBean.edit(loggedUser);
            usuarioBean.clearCache();
            JsfUtil.addSuccessMessage("Senha alterada com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao alterar senha!");
            return null;
        }

        return "password";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOldPass() {
        return oldPass;
    }

    public String getRecoverEmail() {
        return recoverEmail;
    }

    public void recoverPassword() {
        try {
            User usr = usuarioBean.findByEmail(recoverEmail);
            if (usr == null) {
                JsfUtil.addErrorMessage("Email inválido!");
                return;
            }

            String decriptedPass = Utils.randomPassword();
            usr.setPassword(Criptografia.criptografar(decriptedPass));
            
            if(sendMail(usr, decriptedPass, true)) {
                usuarioBean.edit(usr);
                JsfUtil.addSuccessMessage("E-mail de recuperação de senha enviado com sucesso!");
            }else {
                JsfUtil.addErrorMessage("Falha ao enviar e-mail de recuperação de senha!");
            }          
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha resetar senha!");

        }
    }

    public void setRecoverEmail(String recoverEmail) {
        this.recoverEmail = recoverEmail;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    private boolean sendMail(User user, String decriptedPass, boolean edit) {

        try {
            String message = "Sua conta foi cadastrada no sistema.";
            if (edit) {
                message = "Sua conta foi alterada no sistema.";
            }
            message += "\nSua senha é: " + decriptedPass;

            Configuration config = configurationBean.find(1);

            return Utils.sendEmail(user.getEmail(), user.getName(), message, config.getSmtpServer(), config.getEmail(), "Nova senha", config.getUserName(), config.getPassword(),
                    config.getSmtpPort(), "Academia WEB");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao enviar email. Solicite ajuda do Administrador.");
            return false;
        }
    }

    public List<User> getListUsers() {
        return listUser;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUser = listUsers;
    }

    public String getRULER_ADMIN() {
        return User.getRULER_ADMIN();
    }

    public String getRULER_CONTRIBUTOR() {
        return User.getRULER_CONTRIBUTOR();
    }

    public String getRULER_SELLER() {
        return User.getRULER_SELLER();
    }

    public void ruleCheck(List<String> profileRule) {
        JsfUtil.ruleCheck(profileRule);
    }

    private void loadUsers() {
        try {
            if (getUsuarioLogado() != null) {
                User loggedUser = usuarioBean.find(getUsuarioLogado().getId());

                if (loggedUser.getProfileRule().equalsIgnoreCase(getRULER_ADMIN())) {
                    this.listUser = usuarioBean.findAll();
                } else {
                    //Pegar apenas os Colaboradores em caso de vendedor
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Erro.");
        }
    }
}
