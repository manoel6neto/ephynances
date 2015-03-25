package br.com.physisbrasil.web.ephynances.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "administrative_sphere", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class AdministrativeSphere implements BaseModel {

    private static final String SPHERE_MUNICIPAL = "MUNICIPAL";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 250, nullable = false, unique = true)
    @NotEmpty
    private String name;

    //References
    @JoinTable(name = "user_administrative_sphere", joinColumns = {
        @JoinColumn(name = "administrative_sphere_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany()
    private List<User> users;
    
    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static String getSPHERE_MUNICIPAL() {
        return SPHERE_MUNICIPAL;
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
        if (!(object instanceof AdministrativeSphere)) {
            return false;
        }
        final AdministrativeSphere other = (AdministrativeSphere) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
