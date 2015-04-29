package br.com.physisbrasil.web.ephynances.ejb;

import br.com.physisbrasil.web.ephynances.dao.DAO;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author Thomas
 */
@Stateless
public class ProponentSiconvBean extends DAO<ProponentSiconv> {

    public List<ProponentSiconv> findBySphereState(String esferaAdministrativa, String municipioUfNome) {
        TypedQuery<ProponentSiconv> namedQuery = getEntityManager().createNamedQuery("ProponentSiconv.findBySphereState", ProponentSiconv.class);

        namedQuery.setParameter("esferaAdministrativa", esferaAdministrativa);
        namedQuery.setParameter("municipioUfNome", municipioUfNome);
        

        List<ProponentSiconv> proponentsSiconv;
        try {
            proponentsSiconv = namedQuery.getResultList();
        } catch (Exception e) {
            proponentsSiconv = null;
        }

        return proponentsSiconv;
    }

    public List<String> findCitiesNamesBySphereState(String esferaAdministrativa, String municipioUfNome) {
        TypedQuery<ProponentSiconv> namedQuery = getEntityManager().createNamedQuery("ProponentSiconv.findBySphereState", ProponentSiconv.class);

        namedQuery.setParameter("esferaAdministrativa", esferaAdministrativa);
        namedQuery.setParameter("municipioUfNome", municipioUfNome);

        List<ProponentSiconv> proponentsSiconv;
        List<String> cities;
        try {
            proponentsSiconv = namedQuery.getResultList();
            cities = new ArrayList<String>();
            for (ProponentSiconv prop : proponentsSiconv) {
                if (!cities.contains(prop.getMunicipio())) {
                    cities.add(prop.getMunicipio());
                }
            }
        } catch (Exception e) {
            cities = null;
        }

        return cities;
    }

    public List<ProponentSiconv> findBySphereStateCity(String esferaAdministrativa, String municipioUfNome, String municipio) {
        TypedQuery<ProponentSiconv> namedQuery = getEntityManager().createNamedQuery("ProponentSiconv.findBySphereStateCity", ProponentSiconv.class);

        namedQuery.setParameter("esferaAdministrativa", esferaAdministrativa);
        namedQuery.setParameter("municipioUfNome", municipioUfNome);
        namedQuery.setParameter("municipio", municipio);

        List<ProponentSiconv> proponentsSiconv;
        try {
            proponentsSiconv = namedQuery.getResultList();
        } catch (Exception e) {
            proponentsSiconv = null;
        }

        return proponentsSiconv;
    }
    
    public List<ProponentSiconv> findBySphereStateCityNotNUll(String esferaAdministrativa, String municipioUfNome, String municipio) {
        TypedQuery<ProponentSiconv> namedQuery = getEntityManager().createNamedQuery("ProponentSiconv.findBySphereStateCityNotNULL", ProponentSiconv.class);

        namedQuery.setParameter("esferaAdministrativa", esferaAdministrativa);
        namedQuery.setParameter("municipioUfNome", municipioUfNome);
        namedQuery.setParameter("municipio", municipio);

        List<ProponentSiconv> proponentsSiconv;
        try {
            proponentsSiconv = namedQuery.getResultList();
        } catch (Exception e) {
            proponentsSiconv = null;
        }

        return proponentsSiconv;
    }
    
    public List<ProponentSiconv> findBySphereStateCityAll(String esferaAdministrativa, String municipioUfNome, String municipio) {
        TypedQuery<ProponentSiconv> namedQuery = getEntityManager().createNamedQuery("ProponentSiconv.findBySphereStateCityAll", ProponentSiconv.class);

        namedQuery.setParameter("esferaAdministrativa", esferaAdministrativa);
        namedQuery.setParameter("municipioUfNome", municipioUfNome);
        namedQuery.setParameter("municipio", municipio);

        List<ProponentSiconv> proponentsSiconv;
        try {
            proponentsSiconv = namedQuery.getResultList();
        } catch (Exception e) {
            proponentsSiconv = null;
        }

        return proponentsSiconv;
    }
    
    public List<ProponentSiconv> findBySphereStateAll(String esferaAdministrativa, String municipioUfNome) {
        TypedQuery<ProponentSiconv> namedQuery = getEntityManager().createNamedQuery("ProponentSiconv.findBySphereStateAll", ProponentSiconv.class);

        namedQuery.setParameter("esferaAdministrativa", esferaAdministrativa);
        namedQuery.setParameter("municipioUfNome", municipioUfNome);

        List<ProponentSiconv> proponentsSiconv;
        try {
            proponentsSiconv = namedQuery.getResultList();
        } catch (Exception e) {
            proponentsSiconv = null;
        }

        return proponentsSiconv;
    }
}
