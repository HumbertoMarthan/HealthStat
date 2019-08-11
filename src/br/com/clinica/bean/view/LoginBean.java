package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.bean.geral.LoginAtualizaSenha;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;

@Controller
@ViewScoped
@ManagedBean(name = "loginBean")
public class LoginBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ContextoBean contextoBean;

	private Login loginModel;
	private LoginAtualizaSenha loginAtualizaSenha;
	private String url = "/cadastro/cadUsuario.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findUsuario.jsf?faces-redirect=true";
	private List<Login> lstLogin;
	private String campoBuscaNome = "";
	private String campoBuscaLogin = "";
	private Long selecionado;

	@Autowired
	private LoginController loginController;

	public LoginBean() {
		loginAtualizaSenha = new LoginAtualizaSenha();
		loginModel = new Login();
		lstLogin = new ArrayList<>();
	}

	public void updateSenha() throws Exception {
		Login entidadeLogada = contextoBean.getEntidadeLogada();
		System.out.println("Entidade da Sessão>>>>>><>>>>>>>" + entidadeLogada.getLogin());
		/*
		 * if (!loginAtualizaSenha.getSenhaAtual().equals(entidadeLogada.getSenha())) {
		 * addMsg("A senha atual não é valida."); } if
		 * (loginAtualizaSenha.getSenhaAtual().equals(loginAtualizaSenha.getNovaSenha())
		 * ) { addMsg("A senha atual não pode ser igual a nova senha."); } if
		 * (!loginAtualizaSenha.getNovaSenha().equals(loginAtualizaSenha.
		 * getConfirmaSenha())) { addMsg("A nova senha e a confirmação não conferem.");
		 * } else {
		 */
			entidadeLogada.setSenha(loginAtualizaSenha.getNovaSenha());
			loginController.saveOrUpdate(entidadeLogada);
			entidadeLogada = loginController.findByPorId(Login.class, entidadeLogada.getIdLogin());
		/*
		 * if (entidadeLogada.getSenha().equals(loginAtualizaSenha.getNovaSenha())) {
		 * sucesso(); entidadeLogada = new Login(); } else {
		 * addMsg("A nova senha não foi atualizada"); error(); } }
		 */
	}

	public void onRowSelect(SelectEvent event) {
		loginModel = (Login) event.getObject();
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	public String novo() throws Exception {
		setarVariaveisNulas();
		return getUrl();
	}

	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from Login a where 1=1");

		if (!campoBuscaLogin.equals("")) {
			str.append(" and upper(a.login) like upper('%" + campoBuscaNome + "%')");
		}

		lstLogin = loginController.findListByQueryDinamica(str.toString());
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		loginModel = (Login) loginController.getSession().get(getClassImp(), loginModel.getIdLogin());
		loginController.delete(loginModel);
		loginModel = new Login();
		sucesso();
		busca();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		loginModel = new Login();
	}

	// GETTERS E SETTERS
	public String getUsuarioLogadoSecurity() {
		return contextoBean.getAuthentication().getName();
	}

	public LoginAtualizaSenha getLoginAtualizaSenha() {
		return loginAtualizaSenha;
	}

	public void setLoginAtualizaSenha(LoginAtualizaSenha loginAtualizaSenha) {
		this.loginAtualizaSenha = loginAtualizaSenha;
	}

	public Date getUltimoAcesso() throws Exception {
		return contextoBean.getEntidadeLogada().getUltimoAcesso();
	}

	@Override
	protected Class<Login> getClassImp() {
		return Login.class;
	}

	@Override
	protected InterfaceCrud<Login> getController() {
		return loginController;
	}

	@Override
	public void saveNotReturn() throws Exception {

		if (selecionado == 1) {
			loginModel.setPerfil(new Perfil(getSelecionado()));
		}
		if (selecionado == 2) {
			loginModel.setPerfil(new Perfil(getSelecionado()));
		}
		if (selecionado == 3) {
			loginModel.setPerfil(new Perfil(getSelecionado()));
		}
		if (selecionado == 4) {
			loginModel.setPerfil(new Perfil(getSelecionado()));
		}

		loginModel = loginController.merge(loginModel);
		sucesso();
		loginModel = new Login();

	}

	@Override
	public void saveEdit() throws Exception {
		saveNotReturn();
		busca();
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlFind() {
		return urlFind;
	}

	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
	}

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

	public String getCampoBuscaLogin() {
		return campoBuscaLogin;
	}

	public void setCampoBuscaLogin(String campoBuscaLogin) {
		this.campoBuscaLogin = campoBuscaLogin;
	}

	public Login getLoginModel() {
		return loginModel;
	}

	public void setLoginModel(Login loginModel) {
		this.loginModel = loginModel;
	}

	public List<Login> getLstLogin() {
		return lstLogin;
	}

	public void setLstLogin(List<Login> lstLogin) {
		this.lstLogin = lstLogin;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public Long getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Long selecionado) {
		this.selecionado = selecionado;
	}
}
