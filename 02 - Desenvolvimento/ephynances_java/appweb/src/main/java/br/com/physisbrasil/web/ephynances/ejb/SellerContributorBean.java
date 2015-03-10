package br.com.physisbrasil.web.ephynances.ejb;

import br.com.physisbrasil.web.ephynances.dao.DAO;
import br.com.physisbrasil.web.ephynances.model.SellerContributor;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author Thomas
 */
@Stateless
public class SellerContributorBean extends DAO<SellerContributor> {

    public List<SellerContributor> findBySellerId(Long sellerId) {
        TypedQuery<SellerContributor> namedQuery = getEntityManager().createNamedQuery("SellerContributor.findBySellerId", SellerContributor.class);

        namedQuery.setParameter("seller_id", sellerId);

        List<SellerContributor> sellerContributors;
        try {
            sellerContributors = namedQuery.getResultList();
        } catch (Exception e) {
            sellerContributors = null;
        }

        return sellerContributors;
    }
}
