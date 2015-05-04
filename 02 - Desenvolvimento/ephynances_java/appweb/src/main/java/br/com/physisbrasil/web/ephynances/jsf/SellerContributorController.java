package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ActivationBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Activation;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.SellerContributor;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
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
public class SellerContributorController extends BaseController {

    @EJB
    private SellerContributorBean sellerContributorBean;
    private List<SellerContributor> sellerContributors;
    private SellerContributor sellerContributor;

    @EJB
    private UserBean userBean;
    private User seller;
    private List<User> contributors;
    private User tempUser;
    
    @EJB
    private ActivationBean activationBean;
    
    @EJB
    private ConfigurationBean configurationBean;

    @PostConstruct
    public void init() {
        if (sellerContributors == null) {
            sellerContributors = new ArrayList<SellerContributor>();
            setSellerContributors(sellerContributorBean.findAll());
        }

        seller = (User) getFlash("seller");
        if (seller == null) {
            seller = new User();
        }

        contributors = (List<User>) getFlash("contributors");
        if (contributors == null) {
            contributors = new ArrayList<User>();
        }

        tempUser = (User) getFlash("tempuser");
        if (tempUser == null) {
            tempUser = new User();
        }

        //putFlash("seller", null);
        //putFlash("contributors", null);
        //putFlash("tempuser", null);
    }

    public List<SellerContributor> getSellerContributors() {
        return sellerContributors;
    }

    public void setSellerContributors(List<SellerContributor> sellerContributors) {
        this.sellerContributors = sellerContributors;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public List<User> getContributors() {
        return contributors;
    }

    public void setContributors(List<User> contributors) {
        this.contributors = contributors;
    }

    public User getTempUser() {
        return tempUser;
    }

    public void setTempUser(User tempUser) {
        this.tempUser = tempUser;
    }

    public SellerContributor getSellerContributor() {
        return sellerContributor;
    }

    public void setSellerContributor(SellerContributor sellerContributor) {
        this.sellerContributor = sellerContributor;
    }

    public String edit(User user) {
        try {
            if (user.getId() != null) {
                tempUser = userBean.find(user.getId());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao carregar dados do colaborador!");

            putFlash("seller", seller);
            putFlash("contributors", contributors);

            return "/sellerContributor/list";
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);
        putFlash("tempuser", tempUser);

        return "edit";
    }

    public String view(User user) {
        try {
            if (user.getId() != null) {
                tempUser = userBean.find(user.getId());
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao carregar dados do colaborador!");

            putFlash("seller", seller);
            putFlash("contributors", contributors);

            return "/sellerContributor/list";
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);
        putFlash("tempuser", tempUser);

        return "view";
    }

    public String active_desactive(Long id) {
        try {
            //Alterando o status do usuário
            tempUser = userBean.find(id);
            contributors.remove(tempUser);
            tempUser.setIsVerified(!tempUser.isIsVerified());
            userBean.edit(tempUser);

            //Atualizando o usuário dentro da lista
            contributors.add(tempUser);

            //Limpando o cache
            sellerContributorBean.clearCache();
            userBean.clearCache();

            JsfUtil.addSuccessMessage("Status atualizado com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao alterar status do colaborador!");
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);

        return "list";
    }

    public String delete(Long id) {
        try {
            //Removendo o usuário e apagando da lista. Não precisa remover o relacionamento pois faz automaticamente
            tempUser = userBean.find(id);
            contributors.remove(tempUser);
            userBean.remove(tempUser);
            
            //Limpando o cache
            userBean.clearCache();
            sellerContributorBean.clearCache();

            JsfUtil.addSuccessMessage("Colaborador removido com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover colaborador!");
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);

        return "list";
    }

    public String create() {
        tempUser = new User();
        tempUser.setProfileRule(User.getRULER_CONTRIBUTOR());

        putFlash("seller", seller);
        putFlash("contributors", contributors);
        putFlash("tempuser", tempUser);

        return "add";
    }

    public String save() {
        try {
            if (seller.getId() != null) {
                if (tempUser.getId() == null) {
                    //Criando o usuário
                    tempUser.setCommission(0);
                    tempUser.setSalary(0);
                    tempUser.setNumberCnpjs(0);
                    tempUser.setNumberCities(0);
                    tempUser.setNumberAutority(0);
                    tempUser.setIsVerified(false);
                    tempUser.setPassword(Criptografia.criptografar(tempUser.getCpf() + String.valueOf(System.currentTimeMillis())));
                    tempUser.setSystemEsicar(seller.getSystemEsicar());
                    tempUser.setProfileRule(User.getRULER_CONTRIBUTOR());
                    userBean.create(tempUser);

                    //Criando o relacionamento
                    sellerContributor = new SellerContributor();
                    sellerContributor.setContributor(tempUser);
                    sellerContributor.setSeller(seller);
                    sellerContributorBean.create(sellerContributor);

                    //Adicionando na lista
                    contributors.add(tempUser);
                    
                    Activation activation = new Activation();
                    activation.setUser(tempUser);
                    activation.setDueDate(null);
                    activation.setToken(Utils.generateToken(tempUser));
                    activationBean.create(activation);
                    activationBean.clearCache();

                    Configuration config = configurationBean.findAll().get(0);
                    Utils.sendEmail(tempUser.getEmail(), tempUser.getName(), String.format("<html><div align='center' style=\"background-image: url('%s'); width: 542px; height: 549px; margin-left: 250px;\"><a href='http://esicar.physisbrasil.com.br:8080/ephynances/activation/active.xhtml?token=%s'><img src='%s' width='331' height='40' style=\"margin-top: 400px;\"/></a></div></html>", "http://esicar.physisbrasil.com.br:8080/ephynances/resources/img/bg_ativar_1.png", activation.getToken(), "http://esicar.physisbrasil.com.br:8080/ephynances/resources/img/bt_ativar_1.png"),
                            config.getSmtpServer(), config.getEmail(), "Ativação Ephynances", config.getUserName(), config.getPassword(), config.getSmtpPort(), "Ativador Physis Ephynances");

                    
                    JsfUtil.addSuccessMessage("Colaborador adicionado com sucesso!");
                } else {
                    //Editando o usuário
                    contributors.remove(tempUser);
                    userBean.edit(tempUser);
                    
                    //Atualizando o colaborador na lista
                    contributors.add(tempUser);
                    
                    JsfUtil.addSuccessMessage("Colaborador atualizado com sucesso!");
                }

                sellerContributorBean.clearCache();
                userBean.clearCache();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao adicionar colaborador!");
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);

        return "list";
    }

    public String listBySeller(Long userId) {
        sellerContributors = sellerContributorBean.findBySellerId(userId);
        seller = userBean.find(userId);

        contributors = new ArrayList<User>();
        for (SellerContributor contribs : sellerContributors) {
            contributors.add(userBean.find(contribs.getContributor().getId()));
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);

        return "/sellerContributor/list";
    }
}
