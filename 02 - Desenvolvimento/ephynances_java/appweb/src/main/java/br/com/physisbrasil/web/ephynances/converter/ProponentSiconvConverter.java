package br.com.physisbrasil.web.ephynances.converter;

import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
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
@FacesConverter("proponentSiconvConverter")
public class ProponentSiconvConverter implements Converter {
    
    @EJB
    private ProponentSiconvBean proponentSiconvBean;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                ProponentSiconv proponent = proponentSiconvBean.find(Long.valueOf(value));
                return proponent;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Não é um proponente siconv válido."));
            }
        }
        else {
            return null;
        }
    }
 
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((ProponentSiconv) object).getId());
        }
        else {
            return null;
        }
    }   
    
}
