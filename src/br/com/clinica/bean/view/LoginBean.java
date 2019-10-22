package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.bean.geral.LoginAtualizaSenha;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;

@Controller
@ViewScoped
@ManagedBean(name = "loginBean")
public class LoginBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ContextoBean contextoBean;

	private Login loginModel = new Login();
	private Perfil perfilModel;
	private Pessoa pessoaModel;

	private LoginAtualizaSenha loginAtualizaSenha = new LoginAtualizaSenha();
	
	private String url = "/cadastro/cadUsuario.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findUsuario.jsf?faces-redirect=true";
	
	private List<Login> lstLogin = new ArrayList<>();
	
	private String campoBuscaNome = "";
	private String campoBuscaLogin = "";

	@Autowired
	private LoginController loginController;

	@PostConstruct
	public void init() {
		busca();
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
	public String redirecionarFindEntidade()  {
		limpar();
		return getUrlFind();
	}

	private void limpar() {
		loginModel = new Login();
	}

	@Override
	public String novo()  {
		limpar();
		return getUrl();
	}

	public void busca()  {
		StringBuilder str = new StringBuilder();
		str.append("from Login a where 1=1");

		if (!campoBuscaLogin.equals("")) {
			str.append(" and upper(a.login) like upper('%" + campoBuscaLogin + "%')");
		}

		try {
			lstLogin = loginController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void inativar() {

		if (loginModel.getAtivo().equals("I")) {
			loginModel.setAtivo("A");
			loginModel.setInativo(false);
		} else {
			loginModel.setAtivo("I");
			loginModel.setInativo(true);
		}

		try {
			loginController.saveOrUpdate(loginModel);
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		limpar();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar atendente");
			e.printStackTrace();
		}
	}

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			loginModel = (Login) loginController.getSession().get(Login.class, loginModel.getIdLogin());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			loginController.delete(loginModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sucesso();
		busca();
	}

	@Override
	public void saveNotReturn() {

		loginModel.setPerfil(new Perfil(perfilModel.getIdPerfil()));
		loginModel.setPessoa(new Pessoa(pessoaModel.getIdPessoa()));
		try {
			loginModel = loginController.merge(loginModel);
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
		sucesso();
		busca();
		limpar();
		
	}

	@Override
	public void saveEdit(){
		saveNotReturn();
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

	public Perfil getPerfilModel() {
		return perfilModel;
	}

	public void setPerfilModel(Perfil perfilModel) {
		this.perfilModel = perfilModel;
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

	public Pessoa getPessoaModel() {
		return pessoaModel;
	}

	public void setPessoaModel(Pessoa pessoaModel) {
		this.pessoaModel = pessoaModel;
	}
}
