package br.com.physisbrasil.web.ephynances.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "configuration", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
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
    @Column(name = "email", length = 200, unique = true, nullable = false)
    @NotEmpty
    @Pattern(regexp = EMAIL_REGEX)
    @Size(min = 10, max = 200)
    private String email;

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
    public String toString() {
        return id.toString();
    }
}
