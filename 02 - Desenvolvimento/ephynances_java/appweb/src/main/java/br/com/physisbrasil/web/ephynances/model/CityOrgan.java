package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "city_organ", uniqueConstraints = @UniqueConstraint(columnNames = {"cnpj"}))
public class CityOrgan implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "city_name", length = 100, nullable = false)
    private String cityName;

    @Column(name = "organ_name", length = 100, nullable = false)
    private String organName;

    @Column(name = "cnpj", length = 11, nullable = false, unique = true)
    @NotEmpty
    @Size(min = 17, max = 17, message = "Deve conter 17 dígitos.")
    @Pattern(regexp = CNPJ_REGEX)
    private String cnpj;

    //References
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(name = "state_id", referencedColumnName = "id", nullable = false)
    private State state;

    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
