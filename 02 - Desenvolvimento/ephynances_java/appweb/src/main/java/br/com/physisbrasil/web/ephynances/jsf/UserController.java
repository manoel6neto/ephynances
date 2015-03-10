package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
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
public class UserController extends BaseController {

    private static final long serialVersionUID = 9848454652211L;

    @EJB
    private UserBean usuarioBean;
    private User user;
    private String oldPass;
    private String recoverEmail;
    private List<User> listUser;

    @EJB
    private ConfigurationBean configurationBean;

    @EJB
    private SellerContributorBean sellerContributorBean;

    @PostConstruct
    public void init() {
        if (listUser == null) {
            listUser = new ArrayList<User>();
            loadUsers();
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
        if (user == null) {
            user = new User();
        }

        return "create";
    }

    public String save() {
        try {
            if (user != null) {
                if (user.getId() != null) {
                    if (user.getCpf() != null && !user.getCpf().equals("")) {
                        if (!ValidaCpf.isCPF(user.getCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("user", user);
                            return "edit";
                        }
                    }

                    if (!user.getProfileRule().equals(User.getRULER_SELLER())) {
                        if (sellerContributorBean.findBySellerId(user.getId()).size() > 0) {
                            JsfUtil.addErrorMessage("Impossível alterar o nível de acesso do vendedor. Primeiro remova os colaboradores!");
                        }
                    }

                    usuarioBean.edit(user);
                    usuarioBean.clearCache();
                    JsfUtil.addSuccessMessage("Usuário atualizado com sucesso!");
                } else {
                    if (user.getCpf() != null && !user.getCpf().equals("")) {
                        if (!ValidaCpf.isCPF(user.getCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            return "list";
                        }
                    }
                    user.setIsVerified(false);
                    usuarioBean.create(user);
                    usuarioBean.clearCache();
                    JsfUtil.addSuccessMessage("Usuário cadastrado com sucesso!");
                }
            } else {
                throw new Exception("Falha ao carregar dados do formulário.");
            }
        } catch (Throwable e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }

        return "list";
    }

    public String edit(User user) {
        try {
            if (user.getId() != null && user.getId() > 0) {
                user = usuarioBean.find(user.getId());
                putFlash("user", user);
            } else {
                JsfUtil.addErrorMessage("Usuário não encontrado!!");
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
                user = usuarioBean.find(user.getId());
                putFlash("user", user);
            } else {
                JsfUtil.addErrorMessage("Usuário não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "view";
    }

    public String delete(Long id) {
        try {
            user = usuarioBean.find(id);
            if (user.getId() != null && user.getId() > 0) {
                LoginController l = new LoginController();
                if (user.equals(l.getLoggedUser())) {
                    JsfUtil.addErrorMessage("Impossível apagar sua própria conta.");
                } else {
                    usuarioBean.remove(user);
                    usuarioBean.clearCache();
                    JsfUtil.addSuccessMessage("Usuário apagado com sucesso!");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar registro.");
        }

        return "list";
    }

    public String active_desactive(Long id) {
        try {
            user = usuarioBean.find(id);
            if (user.getId() != null && user.getId() > 0) {
                user.setIsVerified(!user.isIsVerified());
                usuarioBean.edit(user);
                usuarioBean.clearCache();
                JsfUtil.addSuccessMessage("Status alterado com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao alterar registro.");
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

    public List<User> getListUser() {
        return listUser;
    }

    public void setListUser(List<User> listUser) {
        this.listUser = listUser;
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

            if (sendMail(usr, decriptedPass, true)) {
                usuarioBean.edit(usr);
                JsfUtil.addSuccessMessage("E-mail de recuperação de senha enviado com sucesso!");
            } else {
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
                    config.getSmtpPort(), "Physis Ephynances");
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
