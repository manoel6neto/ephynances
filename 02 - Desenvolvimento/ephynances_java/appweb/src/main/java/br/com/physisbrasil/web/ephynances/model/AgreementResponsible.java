package br.com.physisbrasil.web.ephynances.model;

import static br.com.physisbrasil.web.ephynances.model.BaseModel.EMAIL_REGEX;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "agreement_responsible", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "cpf"}))
public class AgreementResponsible implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "email", length = 200, unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX, message = "Email mal formatado")
    @Size(max = 200)
    private String email;

    @Column(name = "phone", length = 30, nullable = true)
    private String phone;

    @Column(name = "cell_phone", length = 30, nullable = true)
    private String cellPhone;

    @Column(name = "cpf", length = 16, unique = true, nullable = false)
    @NotEmpty
    private String cpf;

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
        if (!(object instanceof AgreementResponsible)) {
            return false;
        }
        final AgreementResponsible other = (AgreementResponsible) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
