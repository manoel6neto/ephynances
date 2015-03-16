package br.com.physisbrasil.web.ephynances.converter;

import br.com.physisbrasil.web.ephynances.ejb.AdministrativeSphereBean;
import br.com.physisbrasil.web.ephynances.model.AdministrativeSphere;
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
@FacesConverter("administrativeSphereConverter")
public class AdministrativeSphereConverter implements Converter {
    
    @EJB
    private AdministrativeSphereBean administrativeSphereBean;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                AdministrativeSphere sphere = administrativeSphereBean.find(Long.valueOf(value));
                return sphere;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Não é uma esfera válida."));
            }
        }
        else {
            return null;
        }
    }
 
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((AdministrativeSphere) object).getId());
        }
        else {
            return null;
        }
    }   
    
}
