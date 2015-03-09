package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class ChartController extends BaseController {
    
    @EJB
    private UserBean userBean;
    private List<User> users;
    
    @PostConstruct
    public void init() {
        if (users == null) {
            users = new ArrayList<User>();
            userBean.clearCache();
            setUsers(userBean.findAll());
        }
    }
    
    public PieChartModel getChartActiveUsers() {
        PieChartModel chart = new PieChartModel();
        Integer active = 0, inactive = 0;

        for (User systemUser : users) {
            if (systemUser.isIsVerified()) {
                active++;
            } else {
                inactive++;
            }
        }

        chart.set("Ativos", active);
        chart.set("Inativos", inactive);

        return chart;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
