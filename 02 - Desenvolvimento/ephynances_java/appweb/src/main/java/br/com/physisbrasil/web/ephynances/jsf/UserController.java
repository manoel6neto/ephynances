package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ActivationBean;
import br.com.physisbrasil.web.ephynances.ejb.AdministrativeSphereBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.HistoryProponentUserBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.ejb.StateBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Activation;
import br.com.physisbrasil.web.ephynances.model.AdministrativeSphere;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.HistoryProponentUser;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
import br.com.physisbrasil.web.ephynances.model.State;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
import br.com.physisbrasil.web.ephynances.util.ValidaCpf;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class UserController extends BaseController {

    private static final long serialVersionUID = 9848454652211L;

    @EJB
    private UserBean usuarioBean;
    private User user;
    private String oldPass;
    private String recoverEmail;
    private List<User> listUser;

    @EJB
    private ConfigurationBean configurationBean;

    @EJB
    private SellerContributorBean sellerContributorBean;

    @EJB
    private ActivationBean activationBean;

    @EJB
    private ProponentSiconvBean proponentSiconvBean;

    @EJB
    private HistoryProponentUserBean historyProponentUserBean;

    @EJB
    private StateBean stateBean;

    @EJB
    private AdministrativeSphereBean administrativeSphereBean;

    //remove propronents
    private List<ProponentSiconv> selectedRemoveProponents;

    //Cadastro AdminGestor
    private User adminGestor;

    /// ATRELAMENTO CNPJS ///
    private AdministrativeSphere selectedAdministrativeSphere;
    private State selectedState;
    private ProponentSiconv selectedProponentSiconv;
    private String selectNameCity;
    private List<ProponentSiconv> proponentsFiltered;
    private List<ProponentSiconv> selectedProponents;
    private List<String> citiesFiltered;
    private int quantidadeMunicipiosDisponiveis;
    private int quantidadeCnjsDisponiveis;
    /// --- ///

    private Map<String, String> statesCapital;

    @PostConstruct
    public void init() {
        if (listUser == null) {
            listUser = new ArrayList<User>();
            loadUsers();
        }

        if (user != null) {
            if (user.getId() == 0) {
                User requestUser = (User) getFlash("user");
                if (requestUser != null) {
                    user = requestUser;
                } else {
                    user = new User();
                    user.setCommission(20);
                }
            }
        } else {
            User requestUser = (User) getFlash("user");
            if (requestUser != null) {
                user = requestUser;
            } else {
                user = new User();
                user.setCommission(20);
            }
        }

        if (getFlash("qCity") != null) {
            quantidadeMunicipiosDisponiveis = Integer.valueOf(getFlash("qCity").toString());
        }

        if (getFlash("qCnpj") != null) {
            quantidadeCnjsDisponiveis = Integer.valueOf(getFlash("qCnpj").toString());
        }

        if (user.getIdAdminGestor() != null) {
            adminGestor = usuarioBean.find(user.getIdAdminGestor());
        }

        this.oldPass = "";
        selectedAdministrativeSphere = null;
        selectedState = null;

        proponentsFiltered = new ArrayList<ProponentSiconv>();
        citiesFiltered = new ArrayList<String>();
        //putFlash("user", null);

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

    public String create() {
        if (user == null) {
            user = new User();
        }

        return "create";
    }

    public String save() {
        try {
            if (user != null) {

                if (user.getId() != null) {
                    if (user.getCpf() != null && !user.getCpf().equals("")) {
                        if (!ValidaCpf.isCPF(user.getCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("user", user);
                            return "edit";
                        }
                    }

                    if (!user.getProfileRule().equals(User.getRULER_SELLER())) {
                        if (sellerContributorBean.findBySellerId(user.getId()).size() > 0) {
                            JsfUtil.addErrorMessage("Impossível alterar o nível de acesso do vendedor. Primeiro remova os colaboradores!");
                        }
                    }

                    if (adminGestor != null) {
                        user.setIdAdminGestor(adminGestor.getId());
                    }

                    if (user.getStates() == null || user.getStates().isEmpty()) {
                        user.setStates(stateBean.findAll());
                    }

                    if (user.getAdministrativeSpheres() == null || user.getAdministrativeSpheres().isEmpty()) {
                        user.setAdministrativeSpheres(administrativeSphereBean.findAll());
                    }

                    usuarioBean.edit(user);
                    usuarioBean.clearCache();
                    insertSellerEsicar(user.getId());
                    JsfUtil.addSuccessMessage("Usuário atualizado com sucesso!");
                } else {
                    if (user.getCpf() != null && !user.getCpf().equals("")) {
                        if (!ValidaCpf.isCPF(user.getCpf().replace(".", "").replace("-", ""))) {
                            JsfUtil.addErrorMessage("Cpf inválido!");
                            putFlash("user", null);
                            return "list";
                        }
                    }
                    user.setIsVerified(false);
                    user.setPassword(Criptografia.criptografar(user.getCpf() + String.valueOf(System.currentTimeMillis())));

                    if (adminGestor != null) {
                        user.setIdAdminGestor(adminGestor.getId());
                    }

                    if (user.getStates() == null || user.getStates().isEmpty()) {
                        user.setStates(stateBean.findAll());
                    }

                    if (user.getAdministrativeSpheres() == null || user.getAdministrativeSpheres().isEmpty()) {
                        user.setAdministrativeSpheres(administrativeSphereBean.findAll());
                    }

                    usuarioBean.create(user);
                    usuarioBean.clearCache();
                    insertSellerEsicar(user.getId());

                    Activation activation = new Activation();
                    activation.setUser(user);
                    activation.setDueDate(null);
                    activation.setToken(Utils.generateToken(user));
                    activationBean.create(activation);
                    activationBean.clearCache();

                    Configuration config = configurationBean.findAll().get(0);
                    Utils.sendEmail(user.getEmail(), user.getName(),  String.format("<html><div align='center' style=\"background-image: url('%s'); width: 542px; height: 549px; margin-left: 250px;\"><a href='http://esicar.physisbrasil.com.br:8080/ephynances/activation/active.xhtml?token=%s'><img src='%s' width='331' height='40' style=\"margin-top: 400px;\"/></a></div></html>", "http://esicar.physisbrasil.com.br:8080/ephynances/resources/img/bg_ativar_1.png", activation.getToken(), "http://esicar.physisbrasil.com.br:8080/ephynances/resources/img/bt_ativar_1.png"),
                             config.getSmtpServer(), config.getUserName(), "Ativação e-Phynance", config.getUserName(), config.getPassword(), config.getSmtpPort(), "Ativador Physis e-Phynance");

                    JsfUtil.addSuccessMessage("Usuário cadastrado com sucesso!");
                }
            } else {
                throw new Exception("Falha ao carregar dados do formulário.");
            }
        } catch (Throwable e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }

        return "list";
    }

    public String edit(User user) {
        try {
            if (user.getId() != null && user.getId() > 0) {
                user = usuarioBean.find(user.getId());
                putFlash("user", user);
            } else {
                JsfUtil.addErrorMessage("Usuário não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "edit";
    }

    public String view(User user) {
        try {
            if (user.getId() != null && user.getId() > 0) {
                user = usuarioBean.find(user.getId());
                putFlash("user", user);
            } else {
                JsfUtil.addErrorMessage("Usuário não encontrado!!");
                return "list";
            }
        } catch (Throwable e) {
            return "list";
        }

        return "view";
    }

    public String delete(Long id) {
        try {
            user = usuarioBean.find(id);
            if (user.getId() != null && user.getId() > 0) {
                LoginController l = new LoginController();
                if (user.equals(l.getLoggedUser())) {
                    JsfUtil.addErrorMessage("Impossível apagar sua própria conta.");
                } else {
                    usuarioBean.remove(user);
                    usuarioBean.clearCache();
                    JsfUtil.addSuccessMessage("Usuário apagado com sucesso!");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar registro.");
        }

        return "list";
    }

    public String active_desactive(Long id) {
        try {
            user = usuarioBean.find(id);
            if (user.getId() != null && user.getId() > 0) {
                user.setIsVerified(!user.isIsVerified());
                usuarioBean.edit(user);
                usuarioBean.clearCache();
                changeStatusUserEsicar(user.getId());
                JsfUtil.addSuccessMessage("Status alterado com sucesso!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao alterar registro.");
        }

        return "list";
    }

    public String changePassword() {
        User loggedUser = usuarioBean.find(getUsuarioLogado().getId());
        try {

            String criptOldPass = Criptografia.criptografar(oldPass);

            if (!criptOldPass.equals(loggedUser.getPassword())) {
                JsfUtil.addErrorMessage("As senhas estão diferentes!");
                return null;
            }

            loggedUser.setPassword(Criptografia.criptografar(user.getPassword()));

            usuarioBean.edit(loggedUser);
            usuarioBean.clearCache();
            JsfUtil.addSuccessMessage("Senha alterada com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Erro ao alterar senha!");
            return null;
        }

        return "password";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOldPass() {
        return oldPass;
    }

    public String getRecoverEmail() {
        return recoverEmail;
    }

    public List<User> getListUser() {
        return listUser;
    }

    public void setListUser(List<User> listUser) {
        this.listUser = listUser;
    }

    public void setRecoverEmail(String recoverEmail) {
        this.recoverEmail = recoverEmail;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public List<User> getListUsers() {
        return listUser;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUser = listUsers;
    }

    public String getRULER_ADMIN() {
        return User.getRULER_ADMIN();
    }

    public String getRULER_CONTRIBUTOR() {
        return User.getRULER_CONTRIBUTOR();
    }

    public String getRULER_SELLER() {
        return User.getRULER_SELLER();
    }

    public String getRULER_ADMIN_GESTOR() {
        return User.getRULER_ADMIN_GESTOR();
    }

    public void ruleCheck(List<String> profileRule) {
        JsfUtil.ruleCheck(profileRule);
    }

    public AdministrativeSphere getSelectedAdministrativeSphere() {
        return selectedAdministrativeSphere;
    }

    public void setSelectedAdministrativeSphere(AdministrativeSphere selectedAdministrativeSphere) {
        this.selectedAdministrativeSphere = selectedAdministrativeSphere;
    }

    public State getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
    }

    public ProponentSiconv getSelectedProponentSiconv() {
        return selectedProponentSiconv;
    }

    public void setSelectedProponentSiconv(ProponentSiconv selectedProponentSiconv) {
        this.selectedProponentSiconv = selectedProponentSiconv;
    }

    public String getSelectNameCity() {
        return selectNameCity;
    }

    public void setSelectNameCity(String selectNameCity) {
        this.selectNameCity = selectNameCity;
    }

    public List<ProponentSiconv> getProponentsFiltered() {
        return proponentsFiltered;
    }

    public void setProponentsFiltered(List<ProponentSiconv> proponentsFiltered) {
        this.proponentsFiltered = proponentsFiltered;
    }

    public List<String> getCitiesFiltered() {
        return citiesFiltered;
    }

    public User getAdminGestor() {
        return adminGestor;
    }

    public void setAdminGestor(User adminGestor) {
        this.adminGestor = adminGestor;
    }

    public void setCitiesFiltered(List<String> citiesFiltered) {
        this.citiesFiltered = citiesFiltered;
    }

    public List<ProponentSiconv> getSelectedProponents() {
        return selectedProponents;
    }

    public void setSelectedProponents(List<ProponentSiconv> selectedProponents) {
        this.selectedProponents = selectedProponents;
    }

    public List<User> getAdminGestores() {
        return usuarioBean.listByProperty("profileRule", getRULER_ADMIN_GESTOR());
    }

    public int getQuantidadeMunicipiosDisponiveis() {
        return quantidadeMunicipiosDisponiveis;
    }

    public void setQuantidadeMunicipiosDisponiveis(int quantidadeMunicipiosDisponiveis) {
        this.quantidadeMunicipiosDisponiveis = quantidadeMunicipiosDisponiveis;
    }

    public int getQuantidadeCnjsDisponiveis() {
        return quantidadeCnjsDisponiveis;
    }

    public void setQuantidadeCnjsDisponiveis(int quantidadeCnjsDisponiveis) {
        this.quantidadeCnjsDisponiveis = quantidadeCnjsDisponiveis;
    }

    public Map<String, String> getStatesCapital() {
        return statesCapital;
    }

    public void setStatesCapital(Map<String, String> statesCapital) {
        this.statesCapital = statesCapital;
    }

    public List<ProponentSiconv> getSelectedRemoveProponents() {
        return selectedRemoveProponents;
    }

    public void setSelectedRemoveProponents(List<ProponentSiconv> selectedRemoveProponents) {
        this.selectedRemoveProponents = selectedRemoveProponents;
    }

    private void loadUsers() {
        try {
            if (getUsuarioLogado() != null) {
                User loggedUser = usuarioBean.find(getUsuarioLogado().getId());

                if (loggedUser.getProfileRule().equalsIgnoreCase(getRULER_ADMIN())) {
                    listUser = usuarioBean.findAll();
                } else if (loggedUser.getProfileRule().equalsIgnoreCase(getRULER_ADMIN_GESTOR())) {
                    List<User> tempUsers = usuarioBean.findAll();
                    for (User tempUser : tempUsers) {
                        if (tempUser.getIdAdminGestor() != null) {
                            if (tempUser.getIdAdminGestor().equals(loggedUser.getId())) {
                                listUser.add(tempUser);
                            }
                        }
                    }
                } else {
                    listUser = new ArrayList<User>();
                    listUser.add(loggedUser);
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Erro.");
        }
    }

    public void setSeller(Long id) {
        if (id > 0) {
            usuarioBean.clearCache();
            user = usuarioBean.find(id);

            //Carregando os valores do banco
            quantidadeCnjsDisponiveis = user.getNumberCnpjs();
            quantidadeMunicipiosDisponiveis = user.getNumberCities();

            //Ajustando os valores removendo os já cadastrados
            if (user.getProponents() != null) {
                if (user.getProponents().size() > 0) {
                    List<String> municipiosTemp = new ArrayList<String>();
                    for (ProponentSiconv prop : user.getProponents()) {
                        // Verificando quantidade de Cnpjs não municipais
                        if (!prop.getEsferaAdministrativa().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                            quantidadeCnjsDisponiveis--;
                        }
                        // Verificando Cnpjs Municipais
                        if (prop.getEsferaAdministrativa().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                            if (!municipiosTemp.contains(prop.getMunicipio())) {
                                quantidadeMunicipiosDisponiveis--;
                                municipiosTemp.add(prop.getMunicipio());
                            }
                        }
                    }
                }
            }
        }
    }

    public String setSellerExternal(Long id) {
        if (id > 0) {
            usuarioBean.clearCache();
            user = usuarioBean.find(id);

            //Carregando os valores do banco
            quantidadeCnjsDisponiveis = user.getNumberCnpjs();
            quantidadeMunicipiosDisponiveis = user.getNumberCities();

            //Ajustando os valores removendo os já cadastrados
            if (user.getProponents() != null) {
                if (user.getProponents().size() > 0) {
                    List<String> municipiosTemp = new ArrayList<String>();
                    for (ProponentSiconv prop : user.getProponents()) {
                        // Verificando quantidade de Cnpjs não municipais
                        if (!prop.getEsferaAdministrativa().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                            quantidadeCnjsDisponiveis--;
                        }
                        // Verificando Cnpjs Municipais
                        if (prop.getEsferaAdministrativa().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                            if (!municipiosTemp.contains(prop.getMunicipio())) {
                                quantidadeMunicipiosDisponiveis--;
                                municipiosTemp.add(prop.getMunicipio());
                            }
                        }
                    }
                }
            }
        }

        putFlash("qCity", quantidadeMunicipiosDisponiveis);
        putFlash("qCnpj", quantidadeCnjsDisponiveis);
        putFlash("user", user);
        return "cnpjext";
    }

    public void updateCityCnpj() {
        if (selectedAdministrativeSphere != null) {
            if (selectedState != null) {
                citiesFiltered = proponentSiconvBean.findCitiesNamesBySphereState(selectedAdministrativeSphere.getName(), selectedState.getName());
                citiesFiltered.sort(null);
                if (user.getProfileRule().equals(getRULER_SELLER()) || user.getProfileRule().equals(getRULER_CONTRIBUTOR()) || user.getProfileRule().equals(getRULER_ADMIN_GESTOR())) {
                    citiesFiltered.remove(statesCapital.get(selectedState.getName()));
                }
                if (selectedAdministrativeSphere.getName().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                    proponentsFiltered = new ArrayList<ProponentSiconv>();
                } else {
                    if (selectNameCity != null) {
                        proponentsFiltered = proponentSiconvBean.findBySphereStateCity(selectedAdministrativeSphere.getName(), selectedState.getName(), selectNameCity);
                    }
                }
            }
        }
    }

    public void addCpnjSeller() {
        try {
            if (user != null) {
                if (user.getId() > 0) {
                    if (selectNameCity != null && selectedAdministrativeSphere != null && selectedState != null) {
                        if (selectedAdministrativeSphere.getName().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                            if (quantidadeMunicipiosDisponiveis == 0) {
                                JsfUtil.addErrorMessage("Você atingiu o limite de municípios!");
                            } else {
                                for (ProponentSiconv prop : proponentSiconvBean.findBySphereStateCity(selectedAdministrativeSphere.getName(), selectedState.getName(), selectNameCity)) {
                                    prop.setUser(user);
                                    proponentSiconvBean.edit(prop);

                                    //Historico
                                    HistoryProponentUser historyProponentUser = new HistoryProponentUser();
                                    historyProponentUser.setCnpj(prop.getCnpj());
                                    historyProponentUser.setInsertDate(new Date(System.currentTimeMillis()));
                                    historyProponentUser.setNameSeller(user.getName());
                                    historyProponentUser.setUfAcronym(prop.getMunicipioUfSigla());
                                    historyProponentUser.setUfMunicipio(prop.getMunicipio());
                                    historyProponentUserBean.create(historyProponentUser);
                                }
                                proponentSiconvBean.clearCache();
                                quantidadeMunicipiosDisponiveis--;
                                selectedProponents = null;
                                selectNameCity = null;
                                selectedState = null;
                                selectedAdministrativeSphere = null;
                                proponentsFiltered = null;
                                insertSellerEsicar(user.getId());
                            }
                        } else {
                            if (selectedProponents != null && selectedProponents.size() > 0) {
                                if (selectedProponents.size() > quantidadeCnjsDisponiveis) {
                                    JsfUtil.addErrorMessage("Você não possui quantidade de cnpjs suficiente. Necessários: " + selectedProponents.size() + ", disponíveis:  " + quantidadeCnjsDisponiveis);
                                } else {
                                    for (ProponentSiconv prop : selectedProponents) {
                                        prop.setUser(user);
                                        proponentSiconvBean.edit(prop);

                                        //Historico
                                        HistoryProponentUser historyProponentUser = new HistoryProponentUser();
                                        historyProponentUser.setCnpj(prop.getCnpj());
                                        historyProponentUser.setInsertDate(new Date(System.currentTimeMillis()));
                                        historyProponentUser.setNameSeller(user.getName());
                                        historyProponentUser.setUfAcronym(prop.getMunicipioUfSigla());
                                        historyProponentUser.setUfMunicipio(prop.getMunicipio());
                                        historyProponentUserBean.create(historyProponentUser);
                                    }
                                    proponentSiconvBean.clearCache();
                                    quantidadeCnjsDisponiveis = quantidadeCnjsDisponiveis - selectedProponents.size();
                                    selectedProponents = null;
                                    selectNameCity = null;
                                    selectedState = null;
                                    selectedAdministrativeSphere = null;
                                    proponentsFiltered = null;
                                    insertSellerEsicar(user.getId());
                                }
                            }
                        }
                    } else {
                        JsfUtil.addErrorMessage("Selecione os filtro e os cnpj's desejados.");
                    }
                }
            }
        } catch (ConstraintViolationException e) {
            JsfUtil.addErrorMessage(e, "Falha ao adicionar cnpj's.");
        }
    }

    public void addCpnjSellerExt() {
        try {
            if (user != null) {
                if (user.getId() > 0) {
                    if (selectNameCity != null && selectedAdministrativeSphere != null && selectedState != null) {
                        if (selectedAdministrativeSphere.getName().equals(AdministrativeSphere.getSPHERE_MUNICIPAL())) {
                            if (quantidadeMunicipiosDisponiveis == 0) {
                                JsfUtil.addErrorMessage("Você atingiu o limite de municípios!");
                            } else {
                                for (ProponentSiconv prop : proponentSiconvBean.findBySphereStateCity(selectedAdministrativeSphere.getName(), selectedState.getName(), selectNameCity)) {
                                    prop.setUser(user);
                                    proponentSiconvBean.edit(prop);

                                    //Historico
                                    HistoryProponentUser historyProponentUser = new HistoryProponentUser();
                                    historyProponentUser.setCnpj(prop.getCnpj());
                                    historyProponentUser.setInsertDate(new Date(System.currentTimeMillis()));
                                    historyProponentUser.setNameSeller(user.getName());
                                    historyProponentUser.setUfAcronym(prop.getMunicipioUfSigla());
                                    historyProponentUser.setUfMunicipio(prop.getMunicipio());
                                    historyProponentUserBean.create(historyProponentUser);
                                }
                                proponentSiconvBean.clearCache();
                                quantidadeMunicipiosDisponiveis--;
                                selectedProponents = null;
                                selectNameCity = null;
                                selectedState = null;
                                selectedAdministrativeSphere = null;
                                proponentsFiltered = null;
                                setSellerExternal(user.getId());
                                insertSellerEsicar(user.getId());
                            }
                        } else {
                            if (selectedProponents != null && selectedProponents.size() > 0) {
                                if (selectedProponents.size() > quantidadeCnjsDisponiveis) {
                                    JsfUtil.addErrorMessage("Você não possui quantidade de cnpjs suficiente. Necessários: " + selectedProponents.size() + ", disponíveis:  " + quantidadeCnjsDisponiveis);
                                } else {
                                    for (ProponentSiconv prop : selectedProponents) {
                                        prop.setUser(user);
                                        proponentSiconvBean.edit(prop);

                                        //Historico
                                        HistoryProponentUser historyProponentUser = new HistoryProponentUser();
                                        historyProponentUser.setCnpj(prop.getCnpj());
                                        historyProponentUser.setInsertDate(new Date(System.currentTimeMillis()));
                                        historyProponentUser.setNameSeller(user.getName());
                                        historyProponentUser.setUfAcronym(prop.getMunicipioUfSigla());
                                        historyProponentUser.setUfMunicipio(prop.getMunicipio());
                                        historyProponentUserBean.create(historyProponentUser);
                                    }
                                    proponentSiconvBean.clearCache();
                                    quantidadeCnjsDisponiveis = quantidadeCnjsDisponiveis - selectedProponents.size();
                                    selectedProponents = null;
                                    selectNameCity = null;
                                    selectedState = null;
                                    selectedAdministrativeSphere = null;
                                    proponentsFiltered = null;
                                    setSellerExternal(user.getId());
                                    insertSellerEsicar(user.getId());
                                }
                            }
                        }
                    } else {
                        JsfUtil.addErrorMessage("Selecione os filtro e os cnpj's desejados.");
                    }
                }
            }
        } catch (ConstraintViolationException e) {
            JsfUtil.addErrorMessage(e, "Falha ao adicionar cnpj's.");
        }
    }

    public void clearCnpjs() {
        try {
            proponentSiconvBean.clearCache();
            for (ProponentSiconv prop : user.getProponents()) {
                prop.setUser(null);
                proponentSiconvBean.edit(prop);
            }
            proponentSiconvBean.clearCache();
            insertSellerEsicar(user.getId());
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover CNPJ's");
        }
    }

    public void removeCnpjs() {
        try {
            if (selectedRemoveProponents != null) {
                if (selectedRemoveProponents.size() > 0) {
                    for (ProponentSiconv prop : selectedRemoveProponents) {
                        if (prop.getEsferaAdministrativa().equalsIgnoreCase("MUNICIPAL")) {
                            for (ProponentSiconv propMunicipal : proponentSiconvBean.findBySphereStateCityNotNUll(prop.getEsferaAdministrativa(), prop.getMunicipioUfNome(), prop.getMunicipio())) {
                                propMunicipal.setUser(null);
                                proponentSiconvBean.edit(propMunicipal);
                            }
                        } else {
                            prop.setUser(null);
                            proponentSiconvBean.edit(prop);
                        }
                    }
                    proponentSiconvBean.clearCache();
                    insertSellerEsicar(user.getId());
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover CNPJ's");
        }
    }

    public void removeCnpjsExt() {
        try {
            if (selectedRemoveProponents != null) {
                if (selectedRemoveProponents.size() > 0) {
                    for (ProponentSiconv prop : selectedRemoveProponents) {
                        if (prop.getEsferaAdministrativa().equalsIgnoreCase("MUNICIPAL")) {
                            for (ProponentSiconv propMunicipal : proponentSiconvBean.findBySphereStateCity(prop.getEsferaAdministrativa(), prop.getMunicipioUfNome(), prop.getMunicipio())) {
                                propMunicipal.setUser(null);
                                proponentSiconvBean.edit(propMunicipal);
                            }
                        } else {
                            prop.setUser(null);
                            proponentSiconvBean.edit(prop);
                        }
                    }
                    proponentSiconvBean.clearCache();
                    setSellerExternal(user.getId());
                    insertSellerEsicar(user.getId());
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover CNPJ's");
        }
    }

    public void clearCnpjsExt() {
        try {
            proponentSiconvBean.clearCache();
            for (ProponentSiconv prop : user.getProponents()) {
                prop.setUser(null);
                proponentSiconvBean.edit(prop);
            }
            proponentSiconvBean.clearCache();
            setSellerExternal(user.getId());
            insertSellerEsicar(user.getId());
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover CNPJ's");
        }
    }

    public void insertSellerEsicar(Long userId) {
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
        usuarioBean.clearCache();
        User tempUser = usuarioBean.find(userId);
        if (tempUser != null) {
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
                stmt = conn.createStatement();
                String sql;
                int id = 0;

                int nivel;
                if (tempUser.getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
                    nivel = 1;
                } else {
                    nivel = 4;
                }

                sql = String.format("SELECT id_usuario FROM usuario WHERE login = '%s' AND id_nivel = %s", tempUser.getCpf().replace(".", "").replace("-", ""), nivel);
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    id = rs.getInt("id_usuario");
                }
                rs.close();

                if (id == 0) {
                    //Insert
                    if (!tempUser.getProfileRule().equals(getRULER_ADMIN())) {
                        sql = "INSERT INTO usuario (nome, email, login, id_nivel, entidade, data_cadastro, senha, usuario_sistema) VALUES ('" + tempUser.getName() + "', '" + tempUser.getEmail() + "', '"
                                + tempUser.getCpf().replace(".", "").replace("-", "") + "', " + 4 + ", '" + tempUser.getEntity() + "', " + "NOW()" + ", '', '" + tempUser.getSystemEsicar().substring(0, 1) + "')";
                    } else {
                        sql = "INSERT INTO usuario (nome, email, login, id_nivel, entidade, data_cadastro, senha, usuario_sistema) VALUES ('" + tempUser.getName() + "', '" + tempUser.getEmail() + "', '"
                                + tempUser.getCpf().replace(".", "").replace("-", "") + "', " + 1 + ", '" + tempUser.getEntity() + "', " + "NOW()" + ", '', '" + tempUser.getSystemEsicar().substring(0, 1) + "')";
                    }

                    if (stmt.executeUpdate(sql) == 1) {
                        //ler o id
                        sql = "SELECT id_usuario FROM usuario WHERE login = " + tempUser.getCpf().replace(".", "").replace("-", "");
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            id = rs.getInt("id_usuario");
                        }
                        rs.close();

                        if (id != 0) {
                            //Salvar as outras tabelas
                            //Estados
                            for (State state : tempUser.getStates()) {
                                sql = "INSERT INTO estados_direito_vendedor (id_vendedor, estado_sigla) VALUES (" + id + ", '" + state.getAcronym() + "')";
                                stmt.executeUpdate(sql);
                            }

                            //Esferas
                            for (AdministrativeSphere sphere : tempUser.getAdministrativeSpheres()) {
                                sql = "INSERT INTO esfadm_direito_vendedor (id_vendedor, esfera_administrativa) VALUES (" + id + ", '" + sphere.getName() + "')";
                                stmt.executeUpdate(sql);
                            }

                            //Cnpj's
                            for (ProponentSiconv prop : tempUser.getProponents()) {
                                sql = String.format("INSERT INTO proponente_direito_vendedor (id_vendedor, proponente) VALUES (%s, '%s')", id, prop.getCnpj());
                                stmt.executeUpdate(sql);
                            }

                            //Ativar usuário no esicar                              
                            URL urlcon = new URL(URLESICAR + id);
                            HttpURLConnection connect = (HttpURLConnection) urlcon.openConnection();
                            connect.connect();
                            if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                                JsfUtil.addErrorMessage("Falha ao solicitar ativação no esicar");
                            }
                        }
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } else {
                    //update
                    sql = String.format("UPDATE usuario SET nome='%s', email='%s', login='%s', entidade='%s', usuario_sistema='%s' WHERE id_usuario=%s", tempUser.getName(), tempUser.getEmail(), tempUser.getCpf().replace(".", "").replace("-", ""), tempUser.getEntity(), tempUser.getSystemEsicar().substring(0, 1), id);

                    if (stmt.executeUpdate(sql) == 1) {
                        // Limpando estados e esferas
                        sql = String.format("DELETE FROM estados_direito_vendedor WHERE id_vendedor = %s", id);
                        stmt.executeUpdate(sql);

                        sql = String.format("DELETE FROM esfadm_direito_vendedor WHERE id_vendedor = %s", id);
                        stmt.executeUpdate(sql);

                        sql = String.format("DELETE FROM proponente_direito_vendedor WHERE id_vendedor = %s", id);
                        stmt.executeUpdate(sql);

                        //Inserindo novamente (Update)
                        for (State state : tempUser.getStates()) {
                            sql = "INSERT INTO estados_direito_vendedor (id_vendedor, estado_sigla) VALUES (" + id + ", '" + state.getAcronym() + "')";
                            stmt.executeUpdate(sql);
                        }

                        //Esferas
                        for (AdministrativeSphere sphere : tempUser.getAdministrativeSpheres()) {
                            sql = "INSERT INTO esfadm_direito_vendedor (id_vendedor, esfera_administrativa) VALUES (" + id + ", '" + sphere.getName() + "')";
                            stmt.executeUpdate(sql);
                        }

                        //Cnpj's
                        for (ProponentSiconv prop : tempUser.getProponents()) {
                            sql = String.format("INSERT INTO proponente_direito_vendedor (id_vendedor, proponente) VALUES (%s, '%s')", id, prop.getCnpj());
                            stmt.executeUpdate(sql);
                        }
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                }
            } catch (ClassNotFoundException e) {
                JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o usuário no banco de dados");
            } catch (SQLException e) {
                JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o usuário no banco de dados");
            } catch (MalformedURLException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void changeStatusUserEsicar(Long userId) {
        //Propriedades de conexao
        String HOSTNAME = "localhost";
        String USERNAME = "root";
        String PASSWORD = "Physis_2013";
        String DATABASE = "physis_esicar";
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DBURL = "jdbc:mysql://" + HOSTNAME + "/" + DATABASE;

        Connection conn;
        Statement stmt;

        usuarioBean.clearCache();
        User tempUser = usuarioBean.find(userId);
        if (tempUser != null) {
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
                stmt = conn.createStatement();
                String sql;
                int id = 0;

                int nivel = 0;
                if (tempUser.getProfileRule().equalsIgnoreCase(User.getRULER_ADMIN())) {
                    nivel = 1;
                } else {
                    nivel = 4;
                }

                sql = String.format("SELECT id_usuario FROM usuario WHERE login = '%s' AND id_nivel = %s", tempUser.getCpf().replace(".", "").replace("-", ""), nivel);
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    id = rs.getInt("id_usuario");
                }
                rs.close();

                if (id != 0) {
                    if (tempUser.isIsVerified()) {
                        sql = String.format("UPDATE usuario SET status='%s' WHERE id_usuario=%s", "A", id);
                    } else {
                        sql = String.format("UPDATE usuario SET status='%s' WHERE id_usuario=%s", "I", id);
                    }

                    stmt.executeUpdate(sql);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (ClassNotFoundException e) {
                JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o status no banco de dados");
            } catch (SQLException e) {
                JsfUtil.addErrorMessage(e, "Falha ao inserir/atualizar o status no banco de dados");
            }
        }
    }

    public List<String> getSystems() {
        return User.getSYSTEMS();
    }
}
