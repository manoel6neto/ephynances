/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.physisbrasil.web.ephynances.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "ephynances_file")
public class AgilityFile implements BaseModel {

    public static final int IMAGE = 1;
    public static final int VIDEO = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    @Column(name = "original_file", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] originalFile;

    @JsonIgnore
    @Lob
    @Basic(fetch = FetchType.LAZY, optional = false)
    @Column(name = "thumb_image", nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte[] thumbImage;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "sent_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date sentDate;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(byte[] originalFile) {
        this.originalFile = originalFile;
    }

    public byte[] getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(byte[] thumbImage) {
        this.thumbImage = thumbImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

}
