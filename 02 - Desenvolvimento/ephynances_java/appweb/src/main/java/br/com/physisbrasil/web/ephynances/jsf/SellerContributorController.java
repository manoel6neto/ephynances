package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.SellerContributor;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
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

        putFlash("seller", null);
        putFlash("contributors", null);
        putFlash("tempuser", null);
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

        return "/sellerContributor/edit";
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

        return "/sellerContributor/view";
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

        return "/sellerContributor/list";
    }

    public String delete(Long id) {
        try {
            //Removendo o usuário e apagando da lista. Não precisa remover o relacionamento pois faz automaticamente
            tempUser = userBean.find(id);
            userBean.remove(tempUser);
            contributors.remove(tempUser);

            //Limpando o cache
            userBean.clearCache();
            sellerContributorBean.clearCache();

            JsfUtil.addSuccessMessage("Colaborador removido com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover colaborador!");
        }

        putFlash("seller", seller);
        putFlash("contributors", contributors);

        return "/sellerContributor/list";
    }

    public String create() {
        tempUser = new User();
        tempUser.setProfileRule(User.getRULER_CONTRIBUTOR());
        tempUser.setMaxSalesAmount(seller.getMaxSalesAmount());

        putFlash("seller", seller);
        putFlash("contributors", contributors);
        putFlash("tempuser", tempUser);

        return "/sellerContributor/add";
    }

    public String save() {
        try {
            if (seller.getId() != null) {
                if (tempUser.getId() == null) {
                    //Criando o usuário
                    userBean.create(tempUser);

                    //Criando o relacionamento
                    sellerContributor = new SellerContributor();
                    sellerContributor.setContributor(tempUser);
                    sellerContributor.setSeller(seller);
                    sellerContributorBean.create(sellerContributor);

                    //Adicionando na lista
                    contributors.add(tempUser);
                    
                    JsfUtil.addSuccessMessage("Colaborador adicionado com sucesso!");
                } else {
                    //Editando o usuário
                    userBean.edit(tempUser);
                    
                    //Atualizando o colaborador na lista
                    contributors.remove(tempUser);
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

        return "/sellerContributor/list";
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
