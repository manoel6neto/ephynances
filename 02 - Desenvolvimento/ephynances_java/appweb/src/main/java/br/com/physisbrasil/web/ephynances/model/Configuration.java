package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="configuration")
public class Configuration implements BaseModel {        
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "smtp_server", length = 100, nullable = false)    
    @NotEmpty
    private String smtpServer;      
    
    @Column(name = "user_name", length = 100, nullable = false)
    @NotEmpty
    private String userName;
    
    @Column(name = "password", length = 100, nullable = false)
    @NotEmpty
    private String password;  
            
    @Column(name = "smtp_port", nullable = false)
    @NotNull
    private Integer smtpPort;
    
    @Column(name = "email", length = 80, unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX)
    @Size(min = 10, max = 80)
    private String email;      
   
    public Configuration() {
    }

    @Override
    public Long getId() {
        return id;
    }    

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
        
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Configuration other = (Configuration) obj;
        if (!this.id.equals(other.id) && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id.toString();
    }
        
}