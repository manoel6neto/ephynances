package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.SellerContributor;
import br.com.physisbrasil.web.ephynances.model.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class AgreementController extends BaseController {

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;
    private List<Agreement> filteredAgreements;
    private Agreement agreement;

    @EJB
    private UserBean userBean;
    private User agreementUser;

    @PostConstruct
    public void init() {
        if (agreements == null) {
            agreements = new ArrayList<Agreement>();
            setAgreements(agreementBean.findAll());
        }

        if ((User) getFlash("userAgreement") != null) {
            agreementUser = (User) getFlash("userAgreement");
        } else {
            if (agreementUser == null) {
                agreementUser = new User();
            }
        }

        if ((Agreement) getFlash("agreement") != null) {
            agreement = (Agreement) getFlash("agreement");
        } else {
            if (agreement == null) {
                agreement = new Agreement();
            }
        }
        
        if ((List<Agreement>) getFlash("agreements") != null) {
            filteredAgreements = (List<Agreement>) getFlash("agreements");
        } else {
            if (filteredAgreements == null) {
                filteredAgreements = new ArrayList<Agreement>();
            }
        }
    }

    public void getListFilteredBySeller(Long userId) {
        User tempUser = userBean.find(userId);
        if (tempUser != null) {
            if (tempUser.getProfileRule().equals(User.getRULER_CONTRIBUTOR())) {
                filteredAgreements = tempUser.getSellers().get(0).getSeller().getAgreements();
            } else {
                filteredAgreements = tempUser.getAgreements();
            }
            
            putFlash("agreements", getFilteredAgreements());
        }
    }
    
    public String startAgreement(Long userId) {
        User tempUser = userBean.find(userId);
        if (tempUser != null) {
            if (tempUser.getProfileRule().equals(User.getRULER_CONTRIBUTOR())) {
                setAgreementUser(tempUser);
            } else {
                setAgreementUser(tempUser.getSellers().get(0).getSeller());
            }
            
            putFlash("userAgreement", getAgreementUser());
            return "create";
        }
        
        return "list";
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public User getAgreementUser() {
        return agreementUser;
    }

    public void setAgreementUser(User agreementUser) {
        this.agreementUser = agreementUser;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public List<Agreement> getFilteredAgreements() {
        return filteredAgreements;
    }

    public void setFilteredAgreements(List<Agreement> filteredAgreements) {
        this.filteredAgreements = filteredAgreements;
    }

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }
    
    public List<String> getAgreementTypes() {
        return Agreement.getAGREEMENT_TYPES();
    }
}
