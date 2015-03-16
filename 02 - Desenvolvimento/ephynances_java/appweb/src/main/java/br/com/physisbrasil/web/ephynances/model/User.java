package br.com.physisbrasil.web.ephynances.model;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "Usuario.findByEmailSenhaProfile",
            query = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.profileRule = :profile"),
    @NamedQuery(name = "Usuario.findByCpfSenhaProfile",
            query = "SELECT u FROM User u WHERE u.cpf = :cpf AND u.password = :password AND u.profileRule = :profile"),
    @NamedQuery(name = "Usuario.findByEmail",
            query = "SELECT u FROM User u WHERE u.email = :email")})
public class User implements BaseModel {

    private static final String RULER_ADMIN = "Administrador";
    private static final String RULER_SELLER = "Representante";
    private static final String RULER_CONTRIBUTOR = "Colaborador";

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "profile_rule", nullable = false)
    private String profileRule;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    @Size(max = 150)
    private String name;

    @Column(name = "email", length = 200, nullable = false)
    @Pattern(regexp = EMAIL_REGEX, message = "Email mal formatado")
    @Size(max = 200)
    private String email;

    @Column(name = "phone", length = 30, nullable = true)
    private String phone;

    @Column(name = "cell_phone", length = 30, nullable = true)
    private String cellPhone;

    @Column(name = "cpf", length = 16, nullable = false)
    private String cpf;

    @Column(name = "rg", length = 11, nullable = false)
    private String rg;

    @Column(name = "entity", length = 100, nullable = false)
    @Size(max = 100)
    private String entity;

    @Column(name = "password", length = 40, nullable = false)
    @Size(min = 6, max = 40, message = "Tamanho deve ser entre 6 e 40 caracteres.")
    private String password;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Column(name = "number_cnpjs", nullable = true)
    private int numberCnpjs;

    @Column(name = "number_cities", nullable = true)
    private int numberCities;

    @Column(name = "street", length = 200, nullable = true)
    private String street;

    @Column(name = "neighborhood", length = 200, nullable = true)
    private String neighborhood;

    @Column(name = "city", length = 200, nullable = true)
    private String city;

    @Column(name = "state", length = 200, nullable = true)
    private String state;

    @Column(name = "postal_code", length = 9, nullable = true)
    private String postalCode;

    @Column(name = "commission", nullable = false)
    private int commission;

    @Column(name = "salary", nullable = false)
    @DefaultValue(value = "0")
    private int salary;

    //References
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Agreement> agreements;

    @OneToMany(mappedBy = "contributor", orphanRemoval = true)
    private List<SellerContributor> contributors;

    @OneToMany(mappedBy = "seller", orphanRemoval = true)
    private List<SellerContributor> sellers;

    @JoinTable(name = "user_states", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "state_id", referencedColumnName = "id")})
    @ManyToMany()
    private List<State> states;

    @JoinTable(name = "user_administrative_sphere", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "administrative_sphere_id", referencedColumnName = "id")})
    @ManyToMany()
    private List<AdministrativeSphere> administrativeSpheres;
    
    @OneToMany(mappedBy = "user")
    private List<ProponentSiconv> proponents;
    
    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public String getProfileRule() {
        return profileRule;
    }

    public void setProfileRule(String profileRule) {
        this.profileRule = profileRule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public int getNumberCnpjs() {
        return numberCnpjs;
    }

    public void setNumberCnpjs(int numberCnpjs) {
        this.numberCnpjs = numberCnpjs;
    }

    public int getNumberCities() {
        return numberCities;
    }

    public void setNumberCities(int numberCities) {
        this.numberCities = numberCities;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public List<SellerContributor> getContributors() {
        return contributors;
    }

    public void setContributors(List<SellerContributor> contributors) {
        this.contributors = contributors;
    }

    public List<SellerContributor> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerContributor> sellers) {
        this.sellers = sellers;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<AdministrativeSphere> getAdministrativeSpheres() {
        return administrativeSpheres;
    }

    public void setAdministrativeSpheres(List<AdministrativeSphere> administrativeSpheres) {
        this.administrativeSpheres = administrativeSpheres;
    }

    public List<ProponentSiconv> getProponents() {
        return proponents;
    }

    public void setProponents(List<ProponentSiconv> proponents) {
        this.proponents = proponents;
    }

    public static String getRULER_ADMIN() {
        return RULER_ADMIN;
    }

    public static String getRULER_SELLER() {
        return RULER_SELLER;
    }

    public static String getRULER_CONTRIBUTOR() {
        return RULER_CONTRIBUTOR;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        final User other = (User) object;
        
        if (this.id != null) {
            return this.id.equals(other.id);
        }
        
        return false;
    }
}
