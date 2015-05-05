package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.ValidaCpf;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class AgreementController extends BaseController {

    @EJB
    private AgreementBean agreementBean;
    private List<Agreement> agreements;
    private List<Agreement> filteredAgreements;
    private Agreement agreement;
    private List<ProponentSiconv> filteredProponents;

    @EJB
    private UserBean userBean;
    private User agreementUser;

    @EJB
    private ConfigurationBean configurationBean;

    @EJB
    private ProponentSiconvBean proponentSiconvBean;
    private ProponentSiconv proponentSiconv;

    @EJB
    private AgreementInstallmentBean agreementInstallmentBean;

    private boolean disableQuantCnpjs;

    private Map<String, String> statesCapital;

    @PostConstruct
    public void init() {
        if (agreements == null) {
            agreements = new ArrayList<Agreement>();
            setAgreements(agreementBean.findAll());
        }

        if ((User) getFlash("userAgreement") != null) {
            agreementUser = (User) getFlash("userAgreement");
        } else {
            if (agreementUser == null) {
                agreementUser = new User();
            }
        }

        if ((Agreement) getFlash("agreement") != null) {
            agreement = (Agreement) getFlash("agreement");
        } else {
            if (agreement == null) {
                agreement = new Agreement();
                agreement.setPhysisAgreementNumber(createPhysisAgreementNumber());
                agreement.setPeriod(12);
            }
        }

        if ((List<Agreement>) getFlash("agreements") != null) {
            filteredAgreements = (List<Agreement>) getFlash("agreements");
        } else {
            if (filteredAgreements == null) {
                filteredAgreements = new ArrayList<Agreement>();
            }
        }

        if ((ProponentSiconv) getFlash("proponent") != null) {
            proponentSiconv = (ProponentSiconv) getFlash("proponent");
        } else {
            proponentSiconv = null;
        }

        disableQuantCnpjs = false;

        statesCapital = new HashMap<String, String>() {
            {
                put("Acre", "RIO BRANCO");
                put("Amapá", "MACAPÁ");
                put("Amazonas", "MANAUS");
                put("Pará", "BELÉM");
                put("Rondônia", "PORTO VELHO");
                put("Roraima", "BOA VISTA");
                put("Tocantins", "PALMAS");
                put("Alagoas", "MACEIÓ");
                put("Bahia", "SALVADOR");
                put("Ceará", "FORTALEZA");
                put("Maranhão", "SÃO LUÍS");
                put("Paraíba", "JOÃO PESSOA");
                put("Piauí", "TERESINA");
                put("Pernambuco", "RECIFE");
                put("Rio Grande do Norte", "NATAL");
                put("Sergipe", "ARACAJU");
                put("São Paulo", "SÃO PAULO");
                put("Minas Gerais", "BELO HORIZONTE");
                put("Rio de Janeiro", "RIO DE JANEIRO");
                put("Espírito Santo", "VITÓRIA");
                put("Goiás", "GOIANIA");
                put("Distrito Federal", "BRASÍLIA");
                put("Mato Grosso", "CUIABÁ");
                put("Mato Grosso do Sul", "CAMPO GRANDE");
                put("Paraná", "CURITÍBA");
                put("Santa Catarina", "FLORIANÓPOLIS");
                put("Rio Grande do Sul", "PORTO ALEGRE");
            }
        ;
    }

    ;
    }

    public void getListFilteredBySeller(Long userId) {
        userBean.clearCache();
        User tempUser = userBean.find(userId);
        boolean isSetContributor = false;
        if (tempUser != null) {
            if (tempUser.getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
                filteredAgreements = agreementBean.findAll();
            } else {
                if (tempUser.getProfileRule().equals(User.getRULER_CONTRIBUTOR())) {
                    if (tempUser.getSellers() != null) {
                        if (tempUser.getSellers().size() > 0) {
                            filteredAgreements = tempUser.getSellers().get(0).getSeller().getAgreements();
                            isSetContributor = true;
                        }
                    }
                    
                    if (!isSetContributor) {
                        if (tempUser.getContributors() != null) {
                            if (tempUser.getContributors().size() > 0) {
                                filteredAgreements = tempUser.getContributors().get(0).getSeller().getAgreements();
                            }
                        }
                    }
                    
                } else {
                    filteredAgreements = tempUser.getAgreements();
                }
            }

            putFlash("agreements", getFilteredAgreements());
        }
    }

    public String startAgreement(Long userId) {
        userBean.clearCache();
        User tempUser = userBean.find(userId);
        boolean isSetContributor = false;
        if (tempUser != null) {
            if (!tempUser.getProfileRule().equals(User.getRULER_CONTRIBUTOR())) {
                setAgreementUser(tempUser);
            } else {
                if (tempUser.getSellers() != null) {
                    if (tempUser.getSellers().size() > 0) {
                        setAgreementUser(tempUser.getSellers().get(0).getSeller());
                        isSetContributor = true;
                    }
                }
                
                if (!isSetContributor) {
                    if (tempUser.getContributors() != null) {
                        if (tempUser.getContributors().size() > 0) {
                            setAgreementUser(tempUser.getContributors().get(0).getSeller());
                        }
                    }
                }
                
            }

            putFlash("userAgreement", getAgreementUser());
            return "/agreement/create";
        }

        return "/agreement/list";
    }

    public String viewAgreement(Long agreementId) {
        agreementBean.clearCache();
        try {
            Agreement tempAgreement = agreementBean.find(agreementId);
            if (tempAgreement != null) {

            }

            return "/agreement/view";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao carregar contrato para visualização");
        }

        return "/agreement/list";
    }

    public String editAgreement(Long agreementId) {
        agreementBean.clearCache();
        if (agreementBean.find(agreementId) != null) {
            agreement = agreementBean.find(agreementId);
            proponentSiconv = proponentSiconvBean.find(agreement.getIdPrimaryCnpj());
            agreementUser = agreement.getUser();

            putFlash("agreement", agreement);
            putFlash("userAgreement", agreementUser);
            putFlash("proponent", proponentSiconv);

            return "/agreement/edit";
        } else {
            JsfUtil.addErrorMessage("Falha ao consultar o contrato informado.");
            return "/agreement/list";
        }
    }

    public String delete(Long agreementId) {
        try {
            if (agreementId > 0) {
                agreementBean.clearCache();
                Agreement tempAgreement = agreementBean.find(agreementId);
                if (tempAgreement != null) {
                    proponentSiconvBean.clearCache();
                    for (ProponentSiconv p : tempAgreement.getProponents()) {
                        p.setAgreement(null);
                        proponentSiconvBean.edit(p);
                        proponentSiconvBean.clearCache();
                    }

                    agreementBean.clearCache();
                    agreement = agreementBean.find(tempAgreement.getId());
                    Long userId = agreement.getUser().getId();
                    agreementBean.remove(agreement);
                    agreementBean.clearCache();

                    getListFilteredBySeller(userId);
                    JsfUtil.addSuccessMessage("Contrato removido com sucesso.");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar o contrato.");
        }

        return "/agreement/list";
    }

    public void forgiveDebits(Long agreementId) {
        try {
            agreementBean.clearCache();
            Agreement tempAgreement = agreementBean.find(agreementId);
            if (tempAgreement != null) {
                if (tempAgreement.getStatus().equalsIgnoreCase(Agreement.getSTATE_SUSPENSO())) {
                    for (AgreementInstallment installment : tempAgreement.getAgreementInstallments()) {
                        if (installment.getStatus().equalsIgnoreCase(AgreementInstallment.getSTATUS_PENDENTE())) {
                            installment.setStatus(AgreementInstallment.getSTATUS_PENDENTE_COM_LIBERACAO());

                            //Definindo a data para vencimento após liberação
                            Date d = new Date(System.currentTimeMillis());

                            //Calcula de hoje mais 15 dias
                            Calendar c = Calendar.getInstance();
                            c.setTime(d);
                            c.set(Calendar.DATE, c.get(Calendar.DATE) + 15);
                            installment.setLiberationDate(c.getTime());

                            agreementInstallmentBean.edit(installment);
                        }
                        agreementInstallmentBean.clearCache();

                        //Change status Agreement
                        agreementBean.clearCache();
                        tempAgreement = agreementBean.find(tempAgreement.getId());
                        tempAgreement.setStatus(Agreement.getSTATE_ATIVO());
                        agreementBean.edit(tempAgreement);
                        agreementBean.clearCache();

                        //Mudar status no esicar
                        changeStatusGestorEsicar(tempAgreement.getManagerCpf(), "A");

                        JsfUtil.addSuccessMessage("Contrato reativado com sucesso !!");
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao consultar/alterar status do contrato.");
        }
    }

    public String hasMissingPayment(Long agreementId) {
        try {
            agreementBean.clearCache();
            Agreement tempAgreement = agreementBean.find(agreementId);
            if (tempAgreement != null) {
                if (tempAgreement.getStatus().equalsIgnoreCase(Agreement.getSTATE_SUSPENSO())) {
                    return String.valueOf(true);
                }
            }
        } catch (Exception e) {
            return String.valueOf(false);
        }
        return String.valueOf(false);
    }

    public void activateAgreement(Long agreementId) {
        agreementBean.clearCache();
        Agreement tempAgreement = agreementBean.find(agreementId);
        if (tempAgreement != null) {
            //Check and activate
            if (tempAgreement.getAgreementInstallments() == null) {
                JsfUtil.addErrorMessage("Não pode ativar contrato sem parcelas definidas.");
            } else {
                //Ativa
                tempAgreement.setStatus(Agreement.getSTATE_ATIVO());
                agreementBean.edit(tempAgreement);
                agreementBean.clearCache();

                //register manager esicar
                if (tempAgreement.getAgreementType().equalsIgnoreCase("PARLAMENTAR")) {
                    insertGestorEsicar(tempAgreement.getId(), "1");
                } else {
                    insertGestorEsicar(tempAgreement.getId(), "0");
                }
                JsfUtil.addSuccessMessage("Contrato ativado com sucesso!!");
            }
        }
    }

    public String checkStatusAgreement(Long agreementId) {
        if (getUsuarioLogado().getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
            Agreement tempAgreement = agreementBean.find(agreementId);
            if (tempAgreement != null) {
                if (tempAgreement.getStatus().equalsIgnoreCase(Agreement.getSTATE_INCOMPLETO())) {
                    return String.valueOf(true);
                }
            }
        }

        return String.valueOf(false);
    }

    public String addAgreement() {
        try {
            if (agreement != null) {
                if (proponentSiconv != null) {
                    if (agreement.getManagerCpf() != null && !agreement.getManagerCpf().equals("")) {
                        if (!ValidaCpf.isCPF(agreement.getManagerCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("userAgreement", getAgreementUser());
                            return "/agreement/create";
                        }

                        proponentSiconvBean.clearCache();
                        if (agreement.getId() != null) {
                            //Update  
                            agreementBean.edit(agreement);
                            agreementBean.clearCache();

                            JsfUtil.addSuccessMessage("Contrato atualizado com sucesso.");
                        } else {
                            //Create
                            //setuser and save agreement
                            agreement.setUser(agreementUser);
                            agreement.setStatus(Agreement.getSTATE_INCOMPLETO());

                            //Calculando a data para expirar o contrato (limite - data atual + o periodo de vigencia em meses)
                            Date d = agreement.getAssignmentDate();
                            Calendar c = Calendar.getInstance();
                            c.setTime(d);
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + agreement.getPeriod());
                            agreement.setExpireDate(c.getTime());

                            // Salva o cnpj principal caso exista
                            if (proponentSiconv != null) {
                                if (proponentSiconv.getId() != null) {
                                    agreement.setIdPrimaryCnpj(proponentSiconv.getId());
                                }
                            }

                            agreementBean.create(agreement);
                            agreementBean.clearCache();

                            //Save relationship agreement - proponent - PARLAMENTAR não tem relacionamento com nenhum cnpj
                            if (agreement.getAgreementType().equalsIgnoreCase("MUNICIPAL")) {
                                for (ProponentSiconv propSiconv : proponentSiconvBean.findBySphereStateCityAll(proponentSiconv.getEsferaAdministrativa(), proponentSiconv.getMunicipioUfNome(), proponentSiconv.getMunicipio())) {
                                    propSiconv.setAgreement(agreement);
                                    proponentSiconvBean.edit(propSiconv);
                                    proponentSiconvBean.clearCache();
                                }
                            } else if (agreement.getAgreementType().equalsIgnoreCase("ESTADUAL")) {
                                for (ProponentSiconv propSiconv : proponentSiconvBean.findBySphereStateAll(proponentSiconv.getEsferaAdministrativa(), proponentSiconv.getMunicipioUfNome())) {
                                    propSiconv.setAgreement(agreement);
                                    proponentSiconvBean.edit(propSiconv);
                                    proponentSiconvBean.clearCache();
                                }
                            } else if (agreement.getAgreementType().equalsIgnoreCase("PRIVADO")) {
                                proponentSiconv.setAgreement(agreement);
                                proponentSiconvBean.edit(proponentSiconv);
                                proponentSiconvBean.clearCache();

                                //salvar proponentes do datatable
                            } else if (agreement.getAgreementType().equalsIgnoreCase("CONSÓRCIO")) {
                                proponentSiconv.setAgreement(agreement);
                                proponentSiconvBean.edit(proponentSiconv);
                                proponentSiconvBean.clearCache();
                            }

                            //save seed with a seed plus one
                            Configuration config = configurationBean.findAll().get(0);
                            config.setContractSeed(config.getContractSeed() + 1);
                            configurationBean.edit(config);
                            configurationBean.clearCache();

                            JsfUtil.addSuccessMessage("Contrato cadastrado com sucesso.");
                        }

                        return "/agreement/list";
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao cadastrar contrato.");
        }

        return "/agreement/create";
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public User getAgreementUser() {
        return agreementUser;
    }

    public void setAgreementUser(User agreementUser) {
        this.agreementUser = agreementUser;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public List<Agreement> getFilteredAgreements() {
        return filteredAgreements;
    }

    public void setFilteredAgreements(List<Agreement> filteredAgreements) {
        this.filteredAgreements = filteredAgreements;
    }

    public ProponentSiconv getProponentSiconv() {
        return proponentSiconv;
    }

    public void setProponentSiconv(ProponentSiconv proponentSiconv) {
        this.proponentSiconv = proponentSiconv;
    }

    public List<ProponentSiconv> getFilteredProponents() {
        return filteredProponents;
    }

    public void setFilteredProponents(List<ProponentSiconv> filteredProponents) {
        this.filteredProponents = filteredProponents;
    }

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }

    public String formatDateToMysql(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("yyyyMMdd");
        return outputFormatter.format(dateToFormat);
    }

    public List<String> getAgreementTypes() {
        return Agreement.getAGREEMENT_TYPES();
    }

    public boolean isDisableQuantCnpjs() {
        return disableQuantCnpjs;
    }

    public void setDisableQuantCnpjs(boolean disableQuantCnpjs) {
        this.disableQuantCnpjs = disableQuantCnpjs;
    }

    public String createPhysisAgreementNumber() {
        //5 digitos sequenciais / ANO
        Configuration config = configurationBean.findAll().get(0);
        String number = String.valueOf(config.getContractSeed());
        while (number.length() < 6) {
            number = "0" + number;
        }

        Date data = new Date(System.currentTimeMillis());
        DateFormat format = new SimpleDateFormat("yyyy");
        String contractNumber = number + "/" + format.format(data);
        return contractNumber;
    }

    public void filterProponentsForAgreementType() {
        List<ProponentSiconv> tempList = new ArrayList<ProponentSiconv>();
        String type = "";
        if (agreement != null) {
            if (!agreement.getAgreementType().equals("")) {
                for (ProponentSiconv prop : agreementUser.getProponents()) {
                    if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_CONSORCIO())) {
                        type = "CONSORCIO PUBLICO";
                    } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_ESTADUAL())) {
                        type = "ESTADUAL";
                    } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_MUNICIPAL())) {
                        type = "MUNICIPAL";
                    } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_PRIVADO())) {
                        type = "PRIVADA";
                    }

                    if (!agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_PARLAMENTAR())) {
                        if (prop.getEsferaAdministrativa().equalsIgnoreCase(type)) {
                            if (prop.getAgreement() == null) {
                                tempList.add(prop);
                            }
                        }
                    }
                }
            }
        }

        setFilteredProponents(tempList);
    }

    public void checkQuantCnpjMunicipal() {
        if (agreement != null) {
            if (!agreement.getAgreementType().equals("")) {
                if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_MUNICIPAL())) {
                    if (proponentSiconv != null) {
                        if (proponentSiconv.getId() > 0) {
                            agreement.setCnpjAmount(proponentSiconvBean.findBySphereStateCityAll(proponentSiconv.getEsferaAdministrativa(), proponentSiconv.getMunicipioUfNome(), proponentSiconv.getMunicipio()).size());
                            disableQuantCnpjs = true;
                        }
                    } else {
                        disableQuantCnpjs = false;
                    }
                } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_CONSORCIO())) {
                    agreement.setCnpjAmount(1);
                    disableQuantCnpjs = true;
                } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_PARLAMENTAR())) {
                    agreement.setCnpjAmount(0);
                    disableQuantCnpjs = true;
                } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_ESTADUAL())) {
                    List<ProponentSiconv> tempProp = proponentSiconvBean.findBySphereStateAll(proponentSiconv.getEsferaAdministrativa(), proponentSiconv.getMunicipioUfNome());
                    if (getUsuarioLogado().getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
                        agreement.setCnpjAmount(tempProp.size());
                    } else {
                        List<ProponentSiconv> tempPropNotAdmin = new ArrayList<ProponentSiconv>();
                        for (ProponentSiconv p : tempProp) {
                            if (!p.getMunicipio().equalsIgnoreCase(statesCapital.get(p.getMunicipioUfNome()))) {
                                tempPropNotAdmin.add(p);
                            }
                        }
                        agreement.setCnpjAmount(tempPropNotAdmin.size());
                    }
                    disableQuantCnpjs = true;
                }
            } else {
                disableQuantCnpjs = false;
            }
        } else {
            disableQuantCnpjs = false;
        }
    }

    public String checkIsType(String type) {
        if (agreement.getAgreementType() != null) {
            return String.valueOf(agreement.getAgreementType().equalsIgnoreCase(type));
        } else {
            return String.valueOf(false);
        }
    }

    public void insertGestorEsicar(Long agreementId, String tipoGestor) {
        //Propriedades de conexao
        String HOSTNAME = "localhost";
        String USERNAME = "root";
        String PASSWORD = "Physis_2013";
        String DATABASE = "physis_esicar";
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DATABASE;
        String URLESICAR = "http://esicar.physisbrasil.com.br/esicar/index.php/confirma_email/finaliza_cadastro_importacao?id=";
        //String URLESICAR = "http://192.168.0.103/esicar/esicar/index.php/confirma_email/finaliza_cadastro_importacao?id=";

        Connection conn;
        Statement stmt;

        //Dados do sistema
        //Recarregando o objeto para carregar as referencias dos proponentes siconv
        agreement = agreementBean.find(agreementId);
        if (agreement.getManagerCpf() != null && !agreement.getManagerCpf().equalsIgnoreCase("")) {
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
                stmt = conn.createStatement();
                String sql;
                int id = 0;
                int id_gestor = 0;
                int id_cnpj;
                String usuarioSistema = "";

                sql = String.format("SELECT id_usuario FROM usuario WHERE login = '%s' AND id_nivel = %s", agreement.getManagerCpf().replace(".", "").replace("-", ""), 2);
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    id = rs.getInt("id_usuario");
                }
                rs.close();

                if (id == 0) {

                    if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_MUNICIPAL())) {
                        usuarioSistema = "M";
                    } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_PARLAMENTAR())) {
                        usuarioSistema = "P";
                    } else if (agreement.getAgreementType().equalsIgnoreCase(Agreement.getTYPE_PRIVADO())) {
                        usuarioSistema = "E";
                    }

                    //Insert
                    sql = "INSERT INTO usuario (nome, email, login, id_nivel, entidade, data_cadastro, senha, status, usuario_sistema) VALUES ('" + agreement.getManagerName() + "', '" + agreement.getManagerEmail() + "', '"
                            + agreement.getManagerCpf().replace(".", "").replace("-", "") + "', " + 2 + ", '" + agreement.getManagerEntity() + "', " + "NOW()" + ", '', 'I', " + "'" + usuarioSistema + "')";

                    if (stmt.executeUpdate(sql) == 1) {
                        //ler o id
                        sql = "SELECT id_usuario FROM usuario WHERE login = " + agreement.getManagerCpf().replace(".", "").replace("-", "");
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            id = rs.getInt("id_usuario");
                        }
                        rs.close();

                        if (id != 0) {
                            //Salvar as outras tabelas

                            // gestor //
                            if (tipoGestor.equalsIgnoreCase("P")) {
                                sql = String.format("INSERT INTO gestor (validade, quantidade_cnpj, id_usuario, inicio_vigencia, tipo_gestor, nivel_gestor, numero_parlamentar, estado_parlamentar) VALUES ('%s', %s, %s, %s, %s, %s, %s, %s)", formatDateToMysql(agreement.getExpireDate()), agreement.getCnpjAmount(), id, "NOW()", tipoGestor, agreement.getAgreementSubType().substring(0, 1), agreement.getParlamentNumber(), agreement.getParlamentState());
                                stmt.executeUpdate(sql);
                            } else {
                                sql = String.format("INSERT INTO gestor (validade, quantidade_cnpj, id_usuario, inicio_vigencia, tipo_gestor) VALUES ('%s', %s, %s, %s, %s)", formatDateToMysql(agreement.getExpireDate()), agreement.getCnpjAmount(), id, "NOW()", tipoGestor);
                                stmt.executeUpdate(sql);
                            }

                            sql = String.format("SELECT id_gestor FROM gestor WHERE id_usuario = %s", id);
                            rs = stmt.executeQuery(sql);
                            while (rs.next()) {
                                id_gestor = rs.getInt("id_gestor");
                            }
                            rs.close();
                            // fim gestor //
                            if (id_gestor != 0) {
                                // cnpj_siconv && usuario_cnpj //
                                for (ProponentSiconv prop : agreement.getProponents()) {
                                    //insert
                                    id_cnpj = 0;
                                    sql = String.format("INSERT INTO cnpj_siconv (cnpj, id_cidade, cnpj_instituicao, sigla, esfera_administrativa) VALUES ('%s', '%s', '%s', '%s', '%s')", prop.getCnpj().replace(".", "").replace("-", "").replace("/", ""), prop.getCodigoMunicipio(), prop.getNome(), prop.getMunicipioUfSigla(), prop.getEsferaAdministrativa());
                                    stmt.executeUpdate(sql);
                                    //get id cnpj
                                    sql = String.format("SELECT id_cnpj_siconv FROM cnpj_siconv WHERE cnpj = %s", prop.getCnpj().replace(".", "").replace("-", "").replace("/", ""));
                                    rs = stmt.executeQuery(sql);
                                    while (rs.next()) {
                                        id_cnpj = rs.getInt("id_cnpj_siconv");
                                    }
                                    rs.close();
                                    if (id_cnpj != 0) {
                                        sql = String.format("INSERT INTO usuario_cnpj (id_usuario, id_cnpj) VALUES (%s, %s)", id, id_cnpj);
                                        stmt.executeUpdate(sql);
                                    }
                                }

                                //Ativar usuário no esicar                              
                                URL urlcon = new URL(URLESICAR + id);
                                HttpURLConnection connect = (HttpURLConnection) urlcon.openConnection();
                                connect.connect();
                                if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                                    JsfUtil.addErrorMessage("Falha ao solicitar ativação no esicar");
                                }
                                // fim cnpj_siconv && usuario_cnpj //
                            } else {
                                //rollback
                                sql = String.format("DELETE FROM usuario WHERE id_usuario = %s", id);
                                stmt.executeUpdate(sql);
                            }
                        }
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                }
            } catch (ClassNotFoundException e) {
                JsfUtil.addErrorMessage(e, "Falha ao inserir o gestor no banco de dados");
            } catch (SQLException e) {
                JsfUtil.addErrorMessage(e, "Falha ao inserir o gestor no banco de dados");
            } catch (MalformedURLException ex) {
                Logger.getLogger(AgreementController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AgreementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void changeStatusGestorEsicar(String cpf, String status) {
        //Propriedades de conexao
        String HOSTNAME = "localhost";
        String USERNAME = "root";
        String PASSWORD = "Physis_2013";
        String DATABASE = "physis_esicar";
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DATABASE;
        String URLESICAR = "http://" + HOSTNAME + "/esicar/index.php/comunica_financeiro/ativa_desativa_usuario?id=";
        //String URLESICAR = "http://" + HOSTNAME + "/esicar/esicar/index.php/comunica_financeiro/ativa_desativa_usuario?id=";

        Connection conn;
        Statement stmt;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            stmt = conn.createStatement();
            String sql;
            int id = 0;

            sql = String.format("SELECT id_usuario FROM usuario WHERE login = '%s' AND id_nivel = %s", agreement.getManagerCpf().replace(".", "").replace("-", ""), 2);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt("id_usuario");
            }
            rs.close();

            if (id != 0) {
                //Ativar usuário no esicar                              
                URL urlcon = new URL(URLESICAR + id + "&status=" + status);
                HttpURLConnection connect = (HttpURLConnection) urlcon.openConnection();
                connect.connect();
                if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                    JsfUtil.addErrorMessage("Falha ao solicitar alteração de status no esicar");
                }
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o status no banco de dados");
        } catch (SQLException e) {
            JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o status no banco de dados");
        } catch (MalformedURLException ex) {
            Logger.getLogger(AgreementController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AgreementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String formatValueToReais(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String formatado = nf.format(value);

        return formatado;
    }
}
