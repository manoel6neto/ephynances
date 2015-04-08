package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "agreement_document")
public class AgreementDocument implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "extension", length = 5, nullable = false)
    private String extension;
    
    @Column(name = "mime", length = 200, nullable = false)
    private String mime;

    @Lob
    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "size")
    private Long size;

    @Lob
    @Column(name = "file")
    private byte[] file;
    
    //References
    @ManyToOne(optional = true)
    @JoinColumn(name = "agreement_id", referencedColumnName = "id", nullable = true)
    private Agreement agreement;
    
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
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
        if (!(object instanceof AgreementDocument)) {
            return false;
        }
        final AgreementDocument other = (AgreementDocument) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
