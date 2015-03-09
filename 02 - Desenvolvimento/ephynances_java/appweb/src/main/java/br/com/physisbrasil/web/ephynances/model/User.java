package br.com.physisbrasil.web.ephynances.model;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name="user")
@NamedQueries({
    @NamedQuery(name = "Usuario.findByEmailSenha",
    query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password"),
    @NamedQuery(name = "Usuario.findByEmail",
    query = "SELECT u FROM User u WHERE u.email = :email")})
public class User implements BaseModel {
    
    private static final String RULER_ADMIN = "Administrador";
    private static final String RULER_SELLER = "Vendedor";
    private static final String RULER_CONTRIBUTOR = "Colaborador";
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "profile_rule", nullable = false)
    private String profileRule;       
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", length = 150, nullable = false)
    private String name;
        
    @Column(name = "email", length = 200, unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX, message = "Email mal formatado")
    @Size(max = 200)
    private String email;
    
    @Column(name="phone", length = 30, nullable = true)
    private String phone;
    
    @Column(name="cell_phone", length = 30, nullable = true)
    private String cellPhone;
    
    @Column(name="cpf", length = 16, nullable = false)
    @NotEmpty
    private String cpf;
    
    @Column(name="max_sales_amount")
    private int maxSalesAmount;
    
    @Column(name="password", length = 40, nullable = false)
    @NotEmpty
    @Size(min = 6, max = 40, message = "Tamanho deve ser entre 6 e 40 caracteres.")
    private String password;
    
    @Column(name="is_verified", nullable = false)
    private boolean isVerified;
    
    //References
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Agreement> agreements;
    
    public User() {
    }
    
    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public String getProfileRule() {
        return profileRule;
    }

    public void setProfileRule(String profileRule) {
        this.profileRule = profileRule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getMaxSalesAmount() {
        return maxSalesAmount;
    }

    public void setMaxSalesAmount(int maxSalesAmount) {
        this.maxSalesAmount = maxSalesAmount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
    public static String getRULER_ADMIN() {
        return RULER_ADMIN;
    }

    public static String getRULER_SELLER() {
        return RULER_SELLER;
    }

    public static String getRULER_CONTRIBUTOR() {
        return RULER_CONTRIBUTOR;
    }
    
}
