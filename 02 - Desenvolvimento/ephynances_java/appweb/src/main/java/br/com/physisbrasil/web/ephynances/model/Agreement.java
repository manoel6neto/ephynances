package br.com.physisbrasil.web.ephynances.model;

import static br.com.physisbrasil.web.ephynances.model.BaseModel.EMAIL_REGEX;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "agreement", uniqueConstraints = @UniqueConstraint(columnNames = {"physis_agreement_number"}))
public class Agreement implements BaseModel {

    private static final List<String> AGREEMENT_TYPES = new ArrayList<String>() {
        {
            add(getTYPE_MUNICIPAL());
            add(getTYPE_PARLAMENTAR());
            add(getTYPE_FUNDACAO());
            add(getTYPE_CONSORCIO());
            add(getTYPE_ENTIDADE_PRIVADA());
            add(getTYPE_ESTADUAL());
        }
    };

    private static final String TYPE_MUNICIPAL = "MUNICIPAL";
    private static final String TYPE_PARLAMENTAR = "FEDERAL";
    private static final String TYPE_FUNDACAO = "ORGANISMO INTERNACIONAL";
    private static final String TYPE_CONSORCIO = "CONSORCIO PUBLICO";
    private static final String TYPE_ENTIDADE_PRIVADA = "PRIVADA";
    private static final String TYPE_ESTADUAL = "ESTADUAL";

    private static final String STATE_ATIVO = "Ativo";
    private static final String STATE_INCOMPLETO = "Incompleto";
    private static final String STATE_CANCELADO = "Cancelado";
    private static final String STATE_SUSPENSO = "Suspenso por falta de pagamento";
    private static final String STATE_FINALIZADO = "Finalizado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "agreement_type", length = 100, nullable = false)
    private String agreementType;

    @Column(name = "contact_email", length = 200, nullable = true)
    @Pattern(regexp = EMAIL_REGEX, message = "Email mal formatado")
    @Size(max = 200)
    private String contactEmail;

    @Column(name = "contact_phone", length = 30, nullable = true)
    private String contactPhone;

    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @Size(max = 100)
    @Column(name = "contact_agreement_number", length = 100, nullable = true)
    private String contactAgreementNumber;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "physis_agreement_number", length = 100, unique = true, nullable = false)
    private String physisAgreementNumber;

    @NotNull
    @Column(name = "expire_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireDate;
    
    @NotNull
    @Column(name = "period", nullable = false)
    @Max(12)
    @DefaultValue(value = "12")
    private int period;

    @DefaultValue(value = "Ativo")
    @NotNull
    @Column(name = "status", length = 100, nullable = false)
    private String status;

    @NotNull
    @Column(name = "cnpj_amount", nullable = false)
    private int cnpjAmount;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "document_number", length = 100, nullable = false)
    private String documentNumber;

    //Manager Data
    @NotNull
    @Column(name = "manager_email", length = 200, nullable = false, unique = true)
    @Pattern(regexp = EMAIL_REGEX, message = "Email mal formatado")
    @Size(max = 200)
    private String managerEmail;

    @NotNull
    @Column(name = "manager_name", length = 200, nullable = false)
    @Size(max = 200)
    private String managerName;

    @NotNull
    @Column(name = "manager_cpf", length = 16, nullable = false, unique = true)
    @Size(max = 16)
    private String managerCpf;
    
    @NotNull
    @Column(name = "manager_phone", length = 30, nullable = true)
    private String managerPhone;
    
    @NotNull
    @Column(name = "manager_cell_phone", length = 30, nullable = true)
    private String managerCellPhone;

    @Column(name = "manager_entity", length = 100, nullable = false)
    @NotEmpty
    @Size(max = 100)
    private String managerEntity;
    
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "agreement", orphanRemoval = true)
    private List<AgreementInstallment> agreementInstallments;

    @OneToMany(mappedBy = "agreement", orphanRemoval = true)
    private List<AgreementDocument> agreementDocuments;
    
    @OneToMany(mappedBy = "agreement")
    private List<ProponentSiconv> proponents;

    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getContactAgreementNumber() {
        return contactAgreementNumber;
    }

    public void setContactAgreementNumber(String contactAgreementNumber) {
        this.contactAgreementNumber = contactAgreementNumber;
    }

    public String getPhysisAgreementNumber() {
        return physisAgreementNumber;
    }

    public void setPhysisAgreementNumber(String physisAgreementNumber) {
        this.physisAgreementNumber = physisAgreementNumber;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public int getCnpjAmount() {
        return cnpjAmount;
    }

    public void setCnpjAmount(int cnpjAmount) {
        this.cnpjAmount = cnpjAmount;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public static String getSTATE_INCOMPLETO() {
        return STATE_INCOMPLETO;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public static List<String> getAGREEMENT_TYPES() {
        return AGREEMENT_TYPES;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AgreementInstallment> getAgreementInstallments() {
        return agreementInstallments;
    }

    public void setAgreementInstallments(List<AgreementInstallment> agreementInstallments) {
        this.agreementInstallments = agreementInstallments;
    }

    public List<AgreementDocument> getAgreementDocuments() {
        return agreementDocuments;
    }

    public void setAgreementDocuments(List<AgreementDocument> agreementDocuments) {
        this.agreementDocuments = agreementDocuments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerCpf() {
        return managerCpf;
    }

    public void setManagerCpf(String managerCpf) {
        this.managerCpf = managerCpf;
    }

    public List<ProponentSiconv> getProponents() {
        return proponents;
    }

    public void setProponents(List<ProponentSiconv> proponents) {
        this.proponents = proponents;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getManagerCellPhone() {
        return managerCellPhone;
    }

    public void setManagerCellPhone(String managerCellPhone) {
        this.managerCellPhone = managerCellPhone;
    }

    public String getManagerEntity() {
        return managerEntity;
    }

    public void setManagerEntity(String managerEntity) {
        this.managerEntity = managerEntity;
    }

    public static String getSTATE_ATIVO() {
        return STATE_ATIVO;
    }

    public static String getSTATE_CANCELADO() {
        return STATE_CANCELADO;
    }

    public static String getSTATE_SUSPENSO() {
        return STATE_SUSPENSO;
    }

    public static String getSTATE_FINALIZADO() {
        return STATE_FINALIZADO;
    }

    public static String getTYPE_MUNICIPAL() {
        return TYPE_MUNICIPAL;
    }

    public static String getTYPE_PARLAMENTAR() {
        return TYPE_PARLAMENTAR;
    }

    public static String getTYPE_FUNDACAO() {
        return TYPE_FUNDACAO;
    }

    public static String getTYPE_CONSORCIO() {
        return TYPE_CONSORCIO;
    }

    public static String getTYPE_ENTIDADE_PRIVADA() {
        return TYPE_ENTIDADE_PRIVADA;
    }

    public static String getTYPE_ESTADUAL() {
        return TYPE_ESTADUAL;
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
        if (!(object instanceof Agreement)) {
            return false;
        }
        final Agreement other = (Agreement) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
