package br.com.physisbrasil.web.ephynances.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Table(name = "ephynances_inventory_backup")
@Entity
public class InventoryBackup implements BaseModel {

    private static final long serialVersionUID = 456565187932147L;        

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @NotEmpty    
    @Column(length = 250, nullable = false, name = "name")
    private String name;       
    
    @NotNull
    @NotEmpty    
    @Column(length = 250, nullable = false, name = "file_path")
    private String filePath;
    
    @NotNull
    @NotEmpty    
    @Column(length = 250, nullable = false, name = "origin_path")
    private String originPath;
    
    @NotNull
    @Column(name = "insert_date", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date insertDate;             
    
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }    

    public Date getInsertDate() {
        return insertDate;
    }  
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString() {
        return id.toString();
    }
    
    @Override
    public boolean equals(Object object) {        
        if (!(object instanceof InventoryBackup)) {
            return false;
        }
        InventoryBackup other = (InventoryBackup) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
