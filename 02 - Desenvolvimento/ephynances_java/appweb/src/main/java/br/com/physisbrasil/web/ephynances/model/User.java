package br.com.physisbrasil.web.ephynances.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
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

    @Column(name="name", length = 100, nullable = false)
    private String name;
        
    @Column(name="email", length = 80, unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX)
    @Size(max = 80)
    private String email;
    
    @Column(name="phone", length = 20, nullable = true)
    private String phone;
    
    @Column(name="cell_phone", length = 20, nullable = true)
    private String cellPhone;
    
    @Column(name="cpf", length = 11, nullable = false)
    @NotEmpty
    @Size(min = 11, max = 11, message = "Deve conter 11 dígitos.")
    @Pattern(regexp = CPF_REGEX)
    private String cpf;
    
    @Column(name="max_sales_amount", nullable = false)
    @NotEmpty
    private int maxSalesAmount;
    
    @Column(name="password", length = 40, nullable = false)
    @NotEmpty
    @Size(min = 6, max = 40, message = "Tamanho deve ser entre 6 e 40 caracteres.")
    private String password;
    
    @Column(name="is_verified", nullable = false)
    @NotEmpty
    private boolean isVerified;
    
    @Column(name="delete_date", nullable = true)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date deleteDate;
    
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

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return !(!this.id.equals(other.id) && (this.id == null || !this.id.equals(other.id)));
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
