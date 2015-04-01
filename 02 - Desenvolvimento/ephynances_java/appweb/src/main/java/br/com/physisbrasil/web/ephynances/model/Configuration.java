package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "configuration")
public class Configuration implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "smtp_server", length = 150, nullable = false)
    @NotEmpty
    private String smtpServer;

    @NotNull
    @Column(name = "user_name", length = 150, nullable = false)
    @NotEmpty
    private String userName;

    @NotNull
    @Column(name = "password", length = 150, nullable = false)
    @NotEmpty
    private String password;

    @Column(name = "smtp_port", nullable = false)
    @NotNull
    private Integer smtpPort;

    @NotNull
    @Column(name = "email", length = 200, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX)
    @Size(max = 200)
    private String email;
    
    @Column(name = "CSLL", nullable = false)
    @Max(100)
    @DefaultValue(value = "0")
    private int CSLL;
    
    @Column(name = "PIS", nullable = false)
    @Max(100)
    @DefaultValue(value = "0")
    private int PIS;
    
    @Column(name = "COFINS", nullable = false)
    @Max(100)
    @DefaultValue(value = "0")
    private int COFINS;
    
    @Column(name = "IRPJ", nullable = false)
    @Max(100)
    @DefaultValue(value = "0")
    private int IRPJ;
    
    @Column(name = "ISSQN", nullable = false)
    @Max(100)
    @DefaultValue(value = "0")
    private int ISSQN;
    
    @Column(name = "ICMS", nullable = false)
    @Max(100)
    @DefaultValue(value = "0")
    private int ICMS;
    
    @Column(name = "contract_seed")
    private int contractSeed;

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

    public int getCSLL() {
        return CSLL;
    }

    public void setCSLL(int CSLL) {
        this.CSLL = CSLL;
    }

    public int getPIS() {
        return PIS;
    }

    public void setPIS(int PIS) {
        this.PIS = PIS;
    }

    public int getCOFINS() {
        return COFINS;
    }

    public void setCOFINS(int COFINS) {
        this.COFINS = COFINS;
    }

    public int getIRPJ() {
        return IRPJ;
    }

    public void setIRPJ(int IRPJ) {
        this.IRPJ = IRPJ;
    }

    public int getISSQN() {
        return ISSQN;
    }

    public void setISSQN(int ISSQN) {
        this.ISSQN = ISSQN;
    }

    public int getICMS() {
        return ICMS;
    }

    public void setICMS(int ICMS) {
        this.ICMS = ICMS;
    }

    public int getContractSeed() {
        return contractSeed;
    }

    public void setContractSeed(int contractSeed) {
        this.contractSeed = contractSeed;
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
        if (!(object instanceof Configuration)) {
            return false;
        }
        final Configuration other = (Configuration) object;
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
