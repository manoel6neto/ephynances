package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.SubAgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.User;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ComissionController extends BaseController {

    @EJB
    private UserBean userBean;
    private List<User> users;
    private User selectedUser;

    private List<AgreementInstallment> agreementInstallmentsForTotal;
    private List<SubAgreementInstallment> subAgreementInstallmentsForTotal;
    private BigDecimal total;
    
    private Map<Integer, String> months;
    

    @PostConstruct
    public void init() {

        if ((User) getFlash("selectedUser") != null) {
            selectedUser = (User) getFlash("selectedUser");
        } else {
            if (selectedUser == null) {
                selectedUser = new User();
            }
        }

        if ((List<User>) getFlash("users") != null) {
            users = (List<User>) getFlash("users");
        } else {
            if (users == null) {
                users = userBean.findSellers();
            }
        }
        
        months = new HashMap<Integer, String>() {
            {
                put(1, "RIO BRANCO");
                put(2, "MACAPÁ");
                put(3, "MANAUS");
                put(4, "BELÉM");
                put(5, "PORTO VELHO");
                put(6, "BOA VISTA");
                put(7, "PALMAS");
                put("Alagoas", "MACEIÓ");
                put("Bahia", "SALVADOR");
                put("Ceará", "FORTALEZA");
                put("Maranhão", "SÃO LUÍS");
                put("Paraíba", "JOÃO PESSOA");
                put("Piauí", "TERESINA");
                put("Pernambuco", "RECIFE");
                put("Rio Grande do Norte", "NATAL");
                put("Sergipe", "ARACAJU");
                put("São Paulo", "SÃO PAULO");
                put("Minas Gerais", "BELO HORIZONTE");
                put("Rio de Janeiro", "RIO DE JANEIRO");
                put("Espírito Santo", "VITÓRIA");
                put("Goiás", "GOIANIA");
                put("Distrito Federal", "BRASÍLIA");
                put("Mato Grosso", "CUIABÁ");
                put("Mato Grosso do Sul", "CAMPO GRANDE");
                put("Paraná", "CURITÍBA");
                put("Santa Catarina", "FLORIANÓPOLIS");
                put("Rio Grande do Sul", "PORTO ALEGRE");
            }
        ;
    }

    ;
    }
    
    public void calcComission(int month) {
        if (selectedUser != null) {
            if (selectedUser.getAgreements() != null) {
                if (selectedUser.getAgreements().size() > 0) {
                    //create code to calculate comission
                }
            }
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<AgreementInstallment> getAgreementInstallmentsForTotal() {
        return agreementInstallmentsForTotal;
    }

    public void setAgreementInstallmentsForTotal(List<AgreementInstallment> agreementInstallmentsForTotal) {
        this.agreementInstallmentsForTotal = agreementInstallmentsForTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<SubAgreementInstallment> getSubAgreementInstallmentsForTotal() {
        return subAgreementInstallmentsForTotal;
    }

    public void setSubAgreementInstallmentsForTotal(List<SubAgreementInstallment> subAgreementInstallmentsForTotal) {
        this.subAgreementInstallmentsForTotal = subAgreementInstallmentsForTotal;
    }
}
