package br.com.physisbrasil.web.ephynances.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "sub_agreement_installment")
public class SubAgreementInstallment implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "value")
    private BigDecimal value;

    @DefaultValue(value = "0")
    @Column(name = "status", nullable = false)
    private boolean status;

    //References
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "agreement_installment_id", referencedColumnName = "id", nullable = false)
    private AgreementInstallment agreementInstallment;
    
    @OneToOne(optional = true)
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = true)
    private Payment payment;
    
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AgreementInstallment getAgreementInstallment() {
        return agreementInstallment;
    }

    public void setAgreementInstallment(AgreementInstallment agreementInstallment) {
        this.agreementInstallment = agreementInstallment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
        if (!(object instanceof SubAgreementInstallment)) {
            return false;
        }
        final SubAgreementInstallment other = (SubAgreementInstallment) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
