package br.com.physisbrasil.web.ephynances.converter;

import br.com.physisbrasil.web.ephynances.ejb.StateBean;
import br.com.physisbrasil.web.ephynances.model.State;
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
@FacesConverter("stateConverter")
public class StateConverter implements Converter {

    @EJB
    private StateBean stateBean;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            if (!value.equals("Selecione um estado") && !value.equalsIgnoreCase("Todos")) {
                try {
                    State state = stateBean.find(Long.valueOf(value));
                    return state;
                } catch (NumberFormatException e) {
                    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Não é um estado válido."));
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
            return String.valueOf(((State) object).getId());
        } else {
            return null;
        }
    }

}
