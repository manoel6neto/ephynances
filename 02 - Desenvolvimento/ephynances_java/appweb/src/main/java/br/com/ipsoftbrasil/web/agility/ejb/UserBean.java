package br.com.ipsoftbrasil.web.agility.ejb;

import br.com.ipsoftbrasil.web.agility.dao.DAO;
import br.com.ipsoftbrasil.web.agility.model.User;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author Thadeu
 */
@Stateless
public class UserBean extends DAO<User> {

    public User findByEmailSenha(String email, String senha) {
        TypedQuery<User> namedQuery = getEntityManager().createNamedQuery("Usuario.findByEmailSenha", User.class);

        namedQuery.setParameter("email", email);
        namedQuery.setParameter("password", senha);

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
