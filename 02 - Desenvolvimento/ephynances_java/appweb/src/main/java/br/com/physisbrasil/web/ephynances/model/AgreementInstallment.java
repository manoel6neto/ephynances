package br.com.physisbrasil.web.ephynances.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "agreement_installment")
public class AgreementInstallment implements BaseModel {

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

    @NotNull
    @Column(name = "due_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;
    
    //References
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "agreement_id", referencedColumnName = "id", nullable = false)
    private Agreement agreement;
    
    @OneToMany(mappedBy = "agreementInstallment", orphanRemoval = true)
    private List<SubAgreementInstallment> subAgreementInstallments;
    
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public List<SubAgreementInstallment> getSubAgreementInstallments() {
        return subAgreementInstallments;
    }

    public void setSubAgreementInstallments(List<SubAgreementInstallment> subAgreementInstallments) {
        this.subAgreementInstallments = subAgreementInstallments;
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
}
