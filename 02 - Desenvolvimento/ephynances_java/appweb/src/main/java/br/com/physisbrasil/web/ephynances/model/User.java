package br.com.physisbrasil.web.ephynances.model;

import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thadeu
 */
@Entity
@Table(name="user")
@NamedQueries({
    @NamedQuery(name = "Usuario.findByEmailSenha",
    query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password"),
    @NamedQuery(name = "Usuario.findByEmail",
    query = "SELECT u FROM User u WHERE u.email = :email")})
public class User implements BaseModel {
    
    private static final String RULER_ADMIN = "ADMIN";
    private static final String RULER_OWNER = "OWNER";
    private static final String RULER_USER = "USER";
    
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
    @Size(min = 10, max = 80)
    private String email;
    
    @Column(name="password", length = 40, nullable = false)
    @Size(min = 6, max = 40, message = "Tamanho deve ser entre 6 e 40 caracteres.")
    private String password;
    
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

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileRule() {
        return profileRule;
    }

    public void setProfileRule(String profileRule) {
        this.profileRule = profileRule;
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
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
    
    public static String getRULER_ADMIN() {
        return RULER_ADMIN;
    }

    public static String getRULER_OWNER() {
        return RULER_OWNER;
    }

    public static String getRULER_USER() {
        return RULER_USER;
    }
    
}
