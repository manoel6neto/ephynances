package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.HistoryProponentUserBean;
import br.com.physisbrasil.web.ephynances.model.HistoryProponentUser;
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
public class HistoryProponentUserController extends BaseController {

    @EJB
    private HistoryProponentUserBean historyProponentUserBean;
    private List<HistoryProponentUser> historyProponentUsers;

    @PostConstruct
    public void init() {
        if (historyProponentUsers == null) {
            historyProponentUserBean.clearCache();
            historyProponentUsers = new ArrayList<HistoryProponentUser>();
            setHistoryProponentUsers(historyProponentUserBean.findAll());
        }
    }

    public List<HistoryProponentUser> getHistoryProponentUsers() {
        return historyProponentUsers;
    }

    public void setHistoryProponentUsers(List<HistoryProponentUser> historyProponentUsers) {
        this.historyProponentUsers = historyProponentUsers;
    }
    
    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }
}
