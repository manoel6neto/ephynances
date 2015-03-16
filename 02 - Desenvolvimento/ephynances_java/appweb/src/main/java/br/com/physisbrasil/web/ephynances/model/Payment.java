package br.com.physisbrasil.web.ephynances.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "payment")
public class Payment implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "value")
    private BigDecimal value;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentDate;

    @NotNull
    @Column(name = "confirmation_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date confirmationDate;

    //References
    @OneToOne(optional = true)
    @JoinColumn(name = "agreement_installment_id", referencedColumnName = "id", nullable = true)
    private AgreementInstallment agreementInstallment;

    @OneToOne(optional = true)
    @JoinColumn(name = "sub_agreement_installment_id", referencedColumnName = "id", nullable = true)
    private SubAgreementInstallment subAgreementInstallment;

    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public AgreementInstallment getAgreementInstallment() {
        return agreementInstallment;
    }

    public void setAgreementInstallment(AgreementInstallment agreementInstallment) {
        this.agreementInstallment = agreementInstallment;
    }

    public SubAgreementInstallment getSubAgreementInstallment() {
        return subAgreementInstallment;
    }

    public void setSubAgreementInstallment(SubAgreementInstallment subAgreementInstallment) {
        this.subAgreementInstallment = subAgreementInstallment;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        final Payment other = (Payment) object;
        return this.id.equals(other.id) || (this.id != null && this.id.equals(other.id));
    }
}
