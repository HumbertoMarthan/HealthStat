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
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.model.cadastro.pessoa.Medico;
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
	private Pessoa pessoaModel = new Pessoa();
	private Pessoa pessoaAux;
	private Atendente atendenteModel;// = new Atendente();
	private Estoquista estoquistaModel;// = new Estoquista();
	private Medico medicoModel;// = new Medico();

	private LoginAtualizaSenha loginAtualizaSenha = new LoginAtualizaSenha();

	private String url = "/cadastro/cadUsuario.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findUsuario.jsf?faces-redirect=true";

	private List<Login> lstLogin = new ArrayList<>();

	private String campoBuscaNome = "";
	private String campoBuscaLogin = "";

	private String novo = "N";

	@Autowired
	private LoginController loginController;

	@Autowired
	private PessoaController pessoaController;

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
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	private void limpar() {
		loginModel = new Login();
	}

	@Override
	public String novo() {
		limpar();
		return getUrl();
	}

	public void busca() {
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
			System.out.println("Erro ao buscar S");
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
		
		// Seta o Perfil
		loginModel.setPerfil(new Perfil(perfilModel.getIdPerfil()));

		if (novo.equals("N")) {
			try {
				if (!pessoaModel.getTipoPessoa().equals("PAC")) {
					loginModel.setPessoa(new Pessoa(pessoaAux.getIdPessoa()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
			}

			try {
				loginModel = loginController.merge(loginModel);
				addMsg("Salvou o Login");
			} catch (Exception e) {
				error();
				e.printStackTrace();
			}

			limpar();
			pessoaModel = new Pessoa();
			pessoaAux = new Pessoa();

		} else {

			// Seta TipoPessoa
			loginModel.setPessoa(new Pessoa(pessoaModel.getTipoPessoa()));

			try {
				pessoaController.persist(pessoaModel);
				addMsg("Salvou a Pessoa!");
			} catch (Exception e) {
				e.getMessage();
				error();
				e.printStackTrace();
			}

			try {
				loginController.persist(loginModel);
				addMsg("Salvou o Login");
			} catch (Exception e) {
				e.getMessage();
				error();
				e.printStackTrace();
			}

			limpar();
			pessoaModel = new Pessoa();
			pessoaAux = new Pessoa();

		}

	}

	public List<Pessoa> completePessoa(String q) throws Exception {
		return pessoaController.findListByQueryDinamica(" from Pessoa where pessoaNome like '%" + q.toUpperCase()
				+ "%' and tipoPessoa = '" + pessoaModel.getTipoPessoa() + "'  order by pessoaNome ASC");
	}

	@Override
	public void saveEdit() {
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

	public String getNovo() {
		return novo;
	}

	public void setNovo(String novo) {
		this.novo = novo;
	}

	public PessoaController getPessoaController() {
		return pessoaController;
	}

	public void setPessoaController(PessoaController pessoaController) {
		this.pessoaController = pessoaController;
	}

	public Atendente getAtendenteModel() {
		return atendenteModel;
	}

	public void setAtendenteModel(Atendente atendenteModel) {
		this.atendenteModel = atendenteModel;
	}

	public Estoquista getEstoquistaModel() {
		return estoquistaModel;
	}

	public void setEstoquistaModel(Estoquista estoquistaModel) {
		this.estoquistaModel = estoquistaModel;
	}

	public Medico getMedicoModel() {
		return medicoModel;
	}

	public void setMedicoModel(Medico medicoModel) {
		this.medicoModel = medicoModel;
	}

	public Pessoa getPessoaAux() {
		return pessoaAux;
	}

	public void setPessoaAux(Pessoa pessoaAux) {
		this.pessoaAux = pessoaAux;
	}
}
