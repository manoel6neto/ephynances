package br.com.physisbrasil.web.ephynances.util;

import br.com.physisbrasil.web.ephynances.jsf.LoginController;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class JsfUtil {

    private static final Logger logger = Logger.getLogger(JsfUtil.class.getName());

    public static String getContextPath() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
        return req.getContextPath();

    }

    /**
     * Retorna o endereço desde o http:// até o /app_name.
     *
     * @return
     */
    public static String getContextUrl() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
        StringBuffer requestURL = req.getRequestURL();

        return requestURL.toString().replace(req.getRequestURI().toString(), "") + req.getContextPath();
    }

    public static String getRealPath() {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return ctx.getRealPath("/");
    }

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        addErrorMessage(ex, defaultMsg, false);
    }

    public static void addErrorMessage(Exception ex, String defaultMsg, boolean printStack) {
        String exceptionMessage = ex.getLocalizedMessage();
        Throwable cause = ex.getCause();

        String causeMessage = null;
        if (cause != null) {
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            causeMessage = cause.getLocalizedMessage();
        }

        if (defaultMsg == null) {
            defaultMsg = "";
        } else {
            defaultMsg = getBundleMessage(defaultMsg);
        }

        if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
            defaultMsg += " -> " + exceptionMessage;
        }

        if (causeMessage != null && !causeMessage.isEmpty()) {
            defaultMsg += ": " + causeMessage;
        }
        if (printStack) {
            logger.log(Level.WARNING, defaultMsg, ex);
        } else {
            logger.log(Level.WARNING, defaultMsg);
        }
        addErrorMessage(defaultMsg);
    }

    public static String getBundleMessage(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("/bundle");
        String msg;

        try {
            msg = bundle.getString(key);
        } catch (MissingResourceException ex) {
            msg = key;
        }

        return msg;
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        msg = getBundleMessage(msg);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addErrorMessage(String clientId, String msg) {
        msg = getBundleMessage(msg);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        msg = getBundleMessage(msg);
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static void setRequestAttribute(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(key, value);
    }

    public static Object getRequestAttribute(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);
    }

    public static void putFlash(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put(key, value);
    }

    public static Object getFlash(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static void setSessionAttribute(String key, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
    }

    public static Object removeSessionAttribute(String key) {
        try {
            return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public static Object getSessionAttribute(String key) {
        try {
            return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    private static String constraintViolationsToString(ConstraintViolationException exception) {

        StringBuilder sb = new StringBuilder();
        Iterator<ConstraintViolation<?>> iterator = exception.getConstraintViolations().iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<?> next = iterator.next();
            sb.append("'");
            sb.append(next.getPropertyPath());
            sb.append("' ");
            sb.append(next.getMessage());
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static String getExceptionMessage(Exception e) {
        String message = e.getMessage();

        if (e instanceof EJBException) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cause = (ConstraintViolationException) e.getCause();
                message = constraintViolationsToString(cause);
            } else {
                message = e.getCause().getMessage();
            }
        }
        return message;
    }

    public static void ruleCheck(List<String> profileRule) {
        LoginController l = new LoginController();
        
        if (!profileRule.contains(l.getLoggedUser().getProfileRule().toString())) {
            redirectUser();
        }
    }

    private static void redirectUser() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/agility/acesso_negado.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
