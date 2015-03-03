package br.com.ipsoftbrasil.web.agility.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="upload_software")
public class UploadSoftware implements BaseModel {
    
    public static final String FILE_URL = "softwares/";
    private static final String TYPE_WINDOWS = "Windows";
    private static final String TYPE_LINUX = "Linux";       

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic(optional = true)
    @Column(name = "software_url", length = 255, unique = true, nullable = true)
    private String softwareUrl;
    
    @Column(name = "name", length = 255, unique = true, nullable = false)
    @NotEmpty
    private String name;
    
    @Column(name = "os", length = 255, nullable = false)
    @NotEmpty
    private String os;     

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSoftwareUrl() {
        return softwareUrl;
    }

    public void setSoftwareUrl(String softwareUrl) {
        this.softwareUrl = softwareUrl;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final UploadSoftware other = (UploadSoftware) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }   
    
    public static String getTYPE_WINDOWS() {
        return TYPE_WINDOWS;
    }

    public static String getTYPE_LINUX() {
        return TYPE_LINUX;
    }     
}
