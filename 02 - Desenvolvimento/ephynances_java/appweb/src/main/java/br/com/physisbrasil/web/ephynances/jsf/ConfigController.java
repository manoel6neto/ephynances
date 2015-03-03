/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ConfigBean;
import br.com.physisbrasil.web.ephynances.model.Config;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Thadeu
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class ConfigController extends BaseController {

    public static final String PROP_IMG_BASE_URL = "img.base.url";

    public static final String PROP_APNS_ENVIROMENT = "apns.environment";

    public static final String PROP_APNS_CERT_DEV_PASS = "apns.cert.dev.password";

    public static final String PROP_APNS_CERT_PROD_PASS = "apns.cert.prod.password";

    public static final String PROP_APNS_CERT_DEV_NAME = "apns.cert.dev.name";

    public static final String PROP_APNS_CERT_PROD_NAME = "apns.cert.prod.name";

    @EJB
    private ConfigBean configBean;

    public static boolean reloadProperties;

    public static Properties properties;

    private static List<Config> configList;

    @PostConstruct
    public void init() {
        configList = getConfigList();
    }

    public List<Config> getConfigList() {
        String reload = JsfUtil.getRequestParameter("reload");
        if (configList == null || reloadProperties || reload != null) {
            configList = configBean.findAll();

            properties = new Properties();
            for (Config conf : configList) {
                properties.put(conf.getPropertyName(), conf.getPropertyValue());
            }

            reloadProperties = false;
        }
        return configList;
    }

    public void save() {
        try {
            for (Config config : configList) {
                Config confUpdate = configBean.findByProperty("propertyName", config.getPropertyName());
                confUpdate.setPropertyValue(config.getPropertyValue());
                configBean.edit(confUpdate);
                configBean.flush();
            }
            reloadProperties = true;

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao ler as configurações do sistema.", true);
        }

    }
}
