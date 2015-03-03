package br.com.ipsoftbrasil.web.agility.jsf;

import br.com.ipsoftbrasil.web.agility.ejb.ConfigurationBean;
import br.com.ipsoftbrasil.web.agility.model.Configuration;
import br.com.ipsoftbrasil.web.agility.util.JsfUtil;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ConfigurationController extends BaseController {

    @EJB
    private ConfigurationBean configurationBean;
    private Configuration configuration;

    @PostConstruct
    public void init() {
        try {
            this.configuration = configurationBean.find(Long.valueOf("1"));           
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar banco de dados.");
        }             
    }

    public String save() {
        try {
            configurationBean.edit(this.configuration);
            JsfUtil.addSuccessMessage("Configurações atualizadas com sucesso!!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao atualizar as configurações. Verifique as informações informadas.");
            return "edit";
        }

        return "/machine/list";
    }  

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public ConfigurationBean getConfigurationBean() {
        return configurationBean;
    }

    public void setConfigurationBean(ConfigurationBean configurationBean) {
        this.configurationBean = configurationBean;
    }       
}
