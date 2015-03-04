package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.servlet.SecurityFilter;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thomas
 */
public abstract class BaseController implements Serializable {

    private static final long serialVersionUID = 9848454652211L;

    protected void putFlash(String name, Object obj) {
        JsfUtil.putFlash(name, obj);
    }

    protected Object getFlash(String name) {
        return JsfUtil.getFlash(name);
    }
    
    public User getUsuarioLogado() {
        return (User) JsfUtil.getSessionAttribute(SecurityFilter.USER_KEY);
    }    
}
