package br.com.physisbrasil.web.ephynances.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "history_proponent_user")
public class HistoryProponentUser implements BaseModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "insert_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date insertDate;
    
    @Column(name = "name_seller", length = 250)
    private String nameSeller;
    
    @Column(name = "cnpj", length = 250)
    private String cnpj;
    
    @Column(name = "uf_municipio", length = 250)
    private String ufMunicipio;
    
    @Column(name = "uf_acronym", length = 250)
    private String ufAcronym;
    
    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getNameSeller() {
        return nameSeller;
    }

    public void setNameSeller(String nameSeller) {
        this.nameSeller = nameSeller;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getUfMunicipio() {
        return ufMunicipio;
    }

    public void setUfMunicipio(String ufMunicipio) {
        this.ufMunicipio = ufMunicipio;
    }

    public String getUfAcronym() {
        return ufAcronym;
    }

    public void setUfAcronym(String ufAcronym) {
        this.ufAcronym = ufAcronym;
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
        if (!(object instanceof HistoryProponentUser)) {
            return false;
        }
        final HistoryProponentUser other = (HistoryProponentUser) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
    
}
