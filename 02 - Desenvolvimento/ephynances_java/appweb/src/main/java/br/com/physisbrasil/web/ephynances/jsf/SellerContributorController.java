package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.model.SellerContributor;
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
    
    @PostConstruct
    public void init() {
        if (sellerContributors == null) {
            sellerContributors = new ArrayList<SellerContributor>();
            setSellerContributors(sellerContributorBean.findAll());
        }
    }

    public List<SellerContributor> getSellerContributors() {
        return sellerContributors;
    }

    public void setSellerContributors(List<SellerContributor> sellerContributors) {
        this.sellerContributors = sellerContributors;
    }
    
    public String listBySeller(Long userId) {
        sellerContributors = sellerContributorBean.findBySellerId(userId);
        
        return "/sellerContributor/list";
    }
}
