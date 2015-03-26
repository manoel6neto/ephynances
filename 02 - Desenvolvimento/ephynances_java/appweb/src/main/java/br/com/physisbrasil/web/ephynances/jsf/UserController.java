package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.ActivationBean;
import br.com.physisbrasil.web.ephynances.ejb.ConfigurationBean;
import br.com.physisbrasil.web.ephynances.ejb.ProponentSiconvBean;
import br.com.physisbrasil.web.ephynances.ejb.SellerContributorBean;
import br.com.physisbrasil.web.ephynances.ejb.UserBean;
import br.com.physisbrasil.web.ephynances.model.Activation;
import br.com.physisbrasil.web.ephynances.model.AdministrativeSphere;
import br.com.physisbrasil.web.ephynances.model.Configuration;
import br.com.physisbrasil.web.ephynances.model.ProponentSiconv;
import br.com.physisbrasil.web.ephynances.model.State;
import br.com.physisbrasil.web.ephynances.model.User;
import br.com.physisbrasil.web.ephynances.util.Criptografia;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import br.com.physisbrasil.web.ephynances.util.Utils;
import br.com.physisbrasil.web.ephynances.util.ValidaCpf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private int ordem;
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

        User requestUser = (User) getFlash("user");
        if (requestUser != null) {
            user = requestUser;
        } else {
            user = new User();
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
                    usuarioBean.edit(user);
                    usuarioBean.clearCache();
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
                    usuarioBean.create(user);
                    usuarioBean.clearCache();

                    Activation activation = new Activation();
                    activation.setUser(user);
                    activation.setDueDate(null);
                    activation.setToken(Utils.generateToken(user));
                    activationBean.create(activation);
                    activationBean.clearCache();

                    Configuration config = configurationBean.findAll().get(0);
                    Utils.sendEmail(user.getEmail(), user.getName(), "<html><body><a href='http://192.168.0.100:8080/ephynances/activation/active.xhtml?token=" + activation.getToken() + "'>Ativar Ephynances</a></body></html>", config.getSmtpServer(), config.getEmail(), "Ativação Ephynances", config.getUserName(), config.getPassword(), config.getSmtpPort(), "Ativador Physis Ephynances");

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

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
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

    private void loadUsers() {
        try {
            if (getUsuarioLogado() != null) {
                User loggedUser = usuarioBean.find(getUsuarioLogado().getId());

                if (loggedUser.getProfileRule().equalsIgnoreCase(getRULER_ADMIN())) {
                    listUser = usuarioBean.findAll();
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
                    ordem = 0;
                    List<String> municipiosTemp = new ArrayList<String>();
                    for (ProponentSiconv prop : user.getProponents()) {
                        // Verificando a ordem
                        if (prop.getOrderVisit() > ordem) {
                            ordem = prop.getOrderVisit();
                        }
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
                    ordem++;
                } else {
                    ordem = 1;
                }
            } else {
                ordem = 1;
            }
        }
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
                                    prop.setOrderVisit(ordem);
                                    proponentSiconvBean.edit(prop);
                                }
                                proponentSiconvBean.clearCache();
                                quantidadeMunicipiosDisponiveis--;
                                selectedProponents = null;
                                selectNameCity = null;
                                selectedState = null;
                                selectedAdministrativeSphere = null;
                                proponentsFiltered = null;
                            }
                        } else {
                            if (selectedProponents != null && selectedProponents.size() > 0) {
                                if (selectedProponents.size() > quantidadeCnjsDisponiveis) {
                                    JsfUtil.addErrorMessage("Você não possui quantidade de cnpjs suficiente. Necessários: " + selectedProponents.size() + ", disponíveis:  " + quantidadeCnjsDisponiveis);
                                } else {
                                    for (ProponentSiconv prop : selectedProponents) {
                                        prop.setUser(user);
                                        prop.setOrderVisit(ordem);
                                        proponentSiconvBean.edit(prop);
                                    }
                                    proponentSiconvBean.clearCache();
                                    quantidadeCnjsDisponiveis = quantidadeCnjsDisponiveis - selectedProponents.size();
                                    selectedProponents = null;
                                    selectNameCity = null;
                                    selectedState = null;
                                    selectedAdministrativeSphere = null;
                                    proponentsFiltered = null;
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
                prop.setOrderVisit(0);
                prop.setUser(null);
                proponentSiconvBean.edit(prop);
            }
            proponentSiconvBean.clearCache();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Falha ao remover CNPJ's");
        }
    }
}
