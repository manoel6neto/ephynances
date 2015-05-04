package br.com.physisbrasil.web.ephynances.converter;

import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.User;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Thomas
 */
@FacesConverter("userConverter")
public class UserConverter implements Converter {

    @EJB
    private UserBean userBean;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (!value.equals("Selecione um administrador regional") && !value.equalsIgnoreCase("Todos")) {
                try {
                    User user = userBean.find(Long.valueOf(value));
                    return user;
                } catch (NumberFormatException e) {
                    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Não é um usuário válido."));
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((User) object).getId());
        } else {
            return null;
        }
    }

}
