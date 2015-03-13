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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Thomas
 */
@Entity
@Table(name = "proponente_siconv")
public class ProponentSiconv implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proponente_siconv")
    private Long idProponenteSiconv;

    @Column(name = "cnpj", length = 18, nullable = false, unique = true)
    @NotEmpty
    @Size(max = 18, message = "Deve conter no máximo 18 dígitos.")
    @Pattern(regexp = CNPJ_REGEX)
    private String cnpj;

    @Column(name = "nome", length = 255, nullable = false)
    @NotNull
    private String nome;

    @Column(name = "esfera_administrativa", length = 255, nullable = false)
    @NotNull
    private String esferaAdministrativa;

    @Column(name = "codigo_municipio", length = 40, nullable = false)
    @NotNull
    private String codigoMunicipio;

    @Column(name = "municipio", length = 255, nullable = false)
    @NotNull
    private String municipio;

    @Column(name = "municipio_uf_sigla", length = 2, nullable = false)
    @NotNull
    private String municipioUfSigla;

    @Column(name = "municipio_uf_nome", length = 60, nullable = false)
    @NotNull
    private String municipioUfNome;

    @Column(name = "municipio_uf_regiao", length = 2, nullable = false)
    @NotNull
    private String municipioUfRegiao;

    @Column(name = "endereco", length = 255, nullable = false)
    @NotNull
    private String endereco;

    @Column(name = "cep", length = 8, nullable = false)
    @NotNull
    private String cep;

    @Column(name = "nome_responsavel", length = 255, nullable = false)
    @NotNull
    private String nomeResponsavel;

    @Column(name = "telefone", length = 30, nullable = false)
    @NotNull
    private String telefone;

    @Column(name = "fax", length = 30, nullable = false)
    @NotNull
    private String fax;

    @Column(name = "natureza_juridica", length = 255, nullable = false)
    @NotNull
    private String naturezaJuridica;

    @Column(name = "inscricao_estadual", length = 255, nullable = false)
    @NotNull
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal", length = 255, nullable = false)
    @NotNull
    private String inscricaoMunicipal;

    @Column(name = "situacao", length = 255, nullable = true)
    private String situacao;

    //References
    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user;
    
    @OneToOne(optional = true)
    @JoinColumn(name = "agreement_id", referencedColumnName = "id", nullable = true)
    private Agreement agreement;

    /**
     *
     * @return
     */
    @Override
    public Long getId() {
        return idProponenteSiconv;
    }

    @Override
    public String toString() {
        return idProponenteSiconv.toString();
    }

    public Long getIdProponenteSiconv() {
        return idProponenteSiconv;
    }

    public void setIdProponenteSiconv(Long idProponenteSiconv) {
        this.idProponenteSiconv = idProponenteSiconv;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEsferaAdministrativa() {
        return esferaAdministrativa;
    }

    public void setEsferaAdministrativa(String esferaAdministrativa) {
        this.esferaAdministrativa = esferaAdministrativa;
    }

    public String getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(String codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getMunicipioUfSigla() {
        return municipioUfSigla;
    }

    public void setMunicipioUfSigla(String municipioUfSigla) {
        this.municipioUfSigla = municipioUfSigla;
    }

    public String getMunicipioUfNome() {
        return municipioUfNome;
    }

    public void setMunicipioUfNome(String municipioUfNome) {
        this.municipioUfNome = municipioUfNome;
    }

    public String getMunicipioUfRegiao() {
        return municipioUfRegiao;
    }

    public void setMunicipioUfRegiao(String municipioUfRegiao) {
        this.municipioUfRegiao = municipioUfRegiao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNaturezaJuridica() {
        return naturezaJuridica;
    }

    public void setNaturezaJuridica(String naturezaJuridica) {
        this.naturezaJuridica = naturezaJuridica;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }
}
