package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.SellerContributor;
import br.com.physisbrasil.web.ephynances.model.User;
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

    @EJB
    private UserBean userBean;
    private User seller;
    private List<User> contributors;

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

        putFlash("seller", null);
        putFlash("contributors", null);
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

    public void edit(User user) {

    }

    public void view(User user) {

    }

    public void active_desactive(Long id) {

    }

    public void delete(Long id) {

    }
    
    public void create () {
        
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
