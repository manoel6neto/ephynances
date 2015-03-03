package br.com.ipsoftbrasil.web.agility.jsf;

import br.com.ipsoftbrasil.web.agility.model.User;
import br.com.ipsoftbrasil.web.agility.servlet.SecurityFilter;
import br.com.ipsoftbrasil.web.agility.util.JsfUtil;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Thadeu
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
