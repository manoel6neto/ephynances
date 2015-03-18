package br.com.physisbrasil.web.ephynances.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "seller_contract")
public class SellerContract implements BaseModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "accept_date", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date acceptDate;
    
    @Lob
    @Column(name = "contract")
    private byte[] contract;
    
    //References
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    
    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public byte[] getContract() {
        return contract;
    }

    public void setContract(byte[] contract) {
        this.contract = contract;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SellerContract)) {
            return false;
        }
        final SellerContract other = (SellerContract) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
