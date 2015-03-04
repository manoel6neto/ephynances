package br.com.physisbrasil.web.ephynances.model;

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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name="state")
public class State implements BaseModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="name", length = 100, nullable = false)
    private String name;
    
    @Column(name="acronym", length = 100, nullable = false)
    private String acronym;
    
    //References
    @OneToMany(mappedBy = "state", orphanRemoval = true)
    private List<CityOrgan> cityOrgans;
    
    @OneToOne(optional = false)
    @NotNull    
    @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
    private Region region;
    
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public List<CityOrgan> getCityOrgans() {
        return cityOrgans;
    }

    public void setCityOrgans(List<CityOrgan> cityOrgans) {
        this.cityOrgans = cityOrgans;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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
        final State other = (State) obj;
        return !(!this.id.equals(other.id) && (this.id == null || !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return name;
    }
    
}
