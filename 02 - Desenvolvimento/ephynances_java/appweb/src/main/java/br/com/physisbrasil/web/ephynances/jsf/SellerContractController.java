package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.SellerContractBean;
import br.com.physisbrasil.web.ephynances.model.SellerContract;
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
public class SellerContractController extends BaseController {

    @EJB
    private SellerContractBean sellerContractBean;
    private List<SellerContract> sellerContracts;
    private SellerContract sellerContract;
    
    @PostConstruct
    public void init() {
        if (sellerContracts == null) {
            sellerContracts = new ArrayList<SellerContract>();
            setSellerContracts(sellerContractBean.findAll());
        }
        
        if (sellerContract == null) {
            sellerContract = new SellerContract();
        }
    }

    public List<SellerContract> getSellerContracts() {
        return sellerContracts;
    }

    public void setSellerContracts(List<SellerContract> sellerContracts) {
        this.sellerContracts = sellerContracts;
    }

    public SellerContract getSellerContract() {
        return sellerContract;
    }

    public void setSellerContract(SellerContract sellerContract) {
        this.sellerContract = sellerContract;
    }
}
