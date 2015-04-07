package br.com.physisbrasil.web.ephynances.validator;

import br.com.physisbrasil.web.ephynances.util.ValidaCpf;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Thomas
 */
@FacesValidator("cpfValidator")
public class CpfValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (!ValidaCpf.isCPF(value.toString())) {
            FacesMessage msg
                    = new FacesMessage(" CPF inválido.", "Por favor insira um cpf correto.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);

            throw new ValidatorException(msg);
        }
    }
}
