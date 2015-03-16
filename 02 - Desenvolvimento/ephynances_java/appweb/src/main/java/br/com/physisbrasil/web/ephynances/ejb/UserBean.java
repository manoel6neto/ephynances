package br.com.physisbrasil.web.ephynances.ejb;

import br.com.physisbrasil.web.ephynances.dao.DAO;
import br.com.physisbrasil.web.ephynances.model.User;
import javax.ejb.Stateless;
import javax.faces.bean.ViewScoped;
import javax.persistence.TypedQuery;

/**
 *
 * @author Thomas
 */
@Stateless
@ViewScoped
public class UserBean extends DAO<User> {

    public User findByEmailSenhaProfile(String email, String senha, String profile) {
        TypedQuery<User> namedQuery = getEntityManager().createNamedQuery("Usuario.findByEmailSenhaProfile", User.class);

        namedQuery.setParameter("email", email);
        namedQuery.setParameter("password", senha);
        namedQuery.setParameter("profile", profile);

        User usuario;
        try {
            usuario = namedQuery.getSingleResult();
        } catch (Exception e) {
            usuario = null;
        }

        return usuario;
    }
    
    public User findByCpfSenhaProfile(String cpf, String senha, String profile) {
        TypedQuery<User> namedQuery = getEntityManager().createNamedQuery("Usuario.findByCpfSenhaProfile", User.class);

        namedQuery.setParameter("cpf", cpf);
        namedQuery.setParameter("password", senha);
        namedQuery.setParameter("profile", profile);

        User usuario;
        try {
            usuario = namedQuery.getSingleResult();
        } catch (Exception e) {
            usuario = null;
        }

        return usuario;
    }

    public User findByEmail(String email) {
        TypedQuery<User> namedQuery = getEntityManager().createNamedQuery("Usuario.findByEmail", User.class);

        namedQuery.setParameter("email", email);

        User usuario;
        try {
            usuario = namedQuery.getSingleResult();
        } catch (Exception e) {
            usuario = null;
        }

        return usuario;
    }
}
