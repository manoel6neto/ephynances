package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "seller_contributor")
@NamedQueries({
    @NamedQuery(name = "SellerContributor.findBySellerId",
            query = "SELECT c FROM SellerContributor c WHERE c.seller.id = :seller_id")})
public class SellerContributor implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "seller_id", referencedColumnName = "id", nullable = false)
    private User seller;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "contributor_id", referencedColumnName = "id", nullable = false)
    private User contributor;

    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public User getContributor() {
        return contributor;
    }

    public void setContributor(User contributor) {
        this.contributor = contributor;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
