package br.com.physisbrasil.web.ephynances.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class AgreementInstallment implements BaseModel, Comparable<AgreementInstallment> {

    private static final String STATUS_PENDENTE = "Pendente";
    private static final String STATUS_ATRASADO = "Atrasado";
    private static final String STATUS_PAGO_SEM_CONFIRMACAO = "Paga sem confirmação";
    private static final String STATUS_PAGO_COM_CONFIRMACAO = "Paga com confirmação";
    private static final String STATUS_PENDENTE_COM_LIBERACAO = "Pendente - Liberado manualmente";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "value")
    private BigDecimal value;

    @DefaultValue(value = "pendente")
    @Column(name = "status", nullable = false, length = 200)
    private String status;

    @NotNull
    @Column(name = "due_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;

    @Column(name = "liberation_date", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date liberationDate;

    //References
    @ManyToOne(optional = true)
    @JoinColumn(name = "agreement_id", referencedColumnName = "id", nullable = true)
    private Agreement agreement;

    @OneToOne(optional = true)
    @JoinColumn(name = "sub_agreement_installment_id", referencedColumnName = "id", nullable = true)
    private SubAgreementInstallment subAgreementInstallment;

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

    public SubAgreementInstallment getSubAgreementInstallment() {
        return subAgreementInstallment;
    }

    public void setSubAgreementInstallment(SubAgreementInstallment subAgreementInstallment) {
        this.subAgreementInstallment = subAgreementInstallment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Date getLiberationDate() {
        return liberationDate;
    }

    public void setLiberationDate(Date liberationDate) {
        this.liberationDate = liberationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getSTATUS_PENDENTE() {
        return STATUS_PENDENTE;
    }

    public static String getSTATUS_PAGO_SEM_CONFIRMACAO() {
        return STATUS_PAGO_SEM_CONFIRMACAO;
    }

    public static String getSTATUS_PAGO_COM_CONFIRMACAO() {
        return STATUS_PAGO_COM_CONFIRMACAO;
    }

    public static String getSTATUS_PENDENTE_COM_LIBERACAO() {
        return STATUS_PENDENTE_COM_LIBERACAO;
    }

    public static String getSTATUS_ATRASADO() {
        return STATUS_ATRASADO;
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
        if (!(object instanceof AgreementInstallment)) {
            return false;
        }
        final AgreementInstallment other = (AgreementInstallment) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }

        return false;
    }

    @Override
    public int compareTo(AgreementInstallment o) {
        if (this.dueDate.before(o.dueDate)) {
            return -1;
        }
        if (this.dueDate.after(o.dueDate)) {
            return 1;
        }
        return 0;
    }
}
