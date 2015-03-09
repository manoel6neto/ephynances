package br.com.physisbrasil.web.ephynances.model;

import static br.com.physisbrasil.web.ephynances.model.BaseModel.EMAIL_REGEX;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
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
@Table(name = "agreement")
public class Agreement implements BaseModel {

    private static final List<String> AGREEMENT_TYPES = new ArrayList<String>() {{
        add("Município");
        add("Parlamentar");
        add("Fundação");
        add("Consórcio");
        add("Entidades privadas sem fins lucrativos");            
    }};
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contact_email", length = 200, unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX, message = "Email mal formatado")
    @Size(max = 200)
    private String contactEmail;

    @Column(name = "contact_phone", length = 30, nullable = true)
    private String contactPhone;

    @NotNull
    @DefaultValue(value = "0")
    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "contact_agreement_number", length = 100, unique = true, nullable = false)
    private String contactAgreementNumber;
    
    @NotEmpty
    @Size(max = 100)
    @Column(name = "physis_agreement_number", length = 100, unique = true, nullable = false)
    private String physisAgreementNumber;
    
    @NotNull
    @Column(name = "expire_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expireDate;
    
    @DefaultValue(value = "0")
    @Column(name="status", nullable = false)
    private boolean status;
    
    @NotNull
    @Column(name="cnpj_amount", nullable = false)
    private int cnpjAmount;
    
    @NotEmpty
    @Size(max = 100)
    @Column(name = "document_number", length = 100, nullable = false)
    private String documentNumber;

    //References
    @OneToOne(optional = false)
    @NotNull    
    @JoinColumn(name = "agreement_responsible_id", referencedColumnName = "id", nullable = false)
    private AgreementResponsible agreementResponsible;
    
    @OneToOne(optional = false)
    @NotNull    
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    
    @JoinTable(name = "agreement_city_organ", joinColumns = {
        @JoinColumn(name = "agreement_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "city_organ_id", referencedColumnName = "id")})
    @ManyToMany()
    private List<CityOrgan> cityOrgans;
    
    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public static List<String> getAGREEMENT_TYPES() {
        return AGREEMENT_TYPES;
    }

    public AgreementResponsible getAgreementResponsible() {
        return agreementResponsible;
    }

    public void setAgreementResponsible(AgreementResponsible agreementResponsible) {
        this.agreementResponsible = agreementResponsible;
    }

    public List<CityOrgan> getCityOrgans() {
        return cityOrgans;
    }

    public void setCityOrgans(List<CityOrgan> cityOrgans) {
        this.cityOrgans = cityOrgans;
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
}
