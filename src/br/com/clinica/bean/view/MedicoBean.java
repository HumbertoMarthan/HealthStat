package br.com.clinica.bean.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EspecialidadeController;
import br.com.clinica.controller.geral.LiberacaoController;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.controller.geral.MedicoController;
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.model.cadastro.outro.Especialidade;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.model.cadastro.usuario.Liberacao;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.utils.DialogUtils;
import br.com.clinica.utils.ValidaCPF;
import br.com.clinica.utils.ValidaEmail;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "medicoBean")
public class MedicoBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Login loginModel = new Login();
	private Perfil perfilModel;
	private Medico medicoModel = new Medico();
	private Especialidade espeModel;

	private List<Medico> lstMedico = new ArrayList<Medico>();
	private List<Perfil> lstSelecionada = new ArrayList<>();
	
	private String url = "/cadastro/cadMedico.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findMedico.jsf?faces-redirect=true";

	private String campoBuscaAtivo = "T";
	private String campoBuscaNome = "";
	private String campoBuscaCPF = "";

	private String criarLogin;
	private Long idPessoa = 0L;

	@Autowired
	private LoginController loginController;

	@Autowired
	private PessoaController pessoaController;

	@Autowired
	private MedicoController medicoController; // Injetando o Controller do Médico

	@Autowired
	private EspecialidadeController especialidadeController;
	
	@Autowired
	private LiberacaoController liberacaoController;

	@PostConstruct
	public void init() {
		busca();
	}

	public void geraRelatorio() {
		JasperPrint relatorio = imprimir(lstMedico, "medico.jrxml");
		try {
			// JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public List<Especialidade> completeEspecialidade(String q) throws Exception {
		return especialidadeController.findListByQueryDinamica(
				" from Especialidade where nomeEspecialidade like '%" + q.toUpperCase() + "%'");
	}

	public void verificaCPFExiste() {
		List<Medico> lstCPF = new ArrayList<Medico>();
		try {
			lstCPF = medicoController.findListByQueryDinamica(
					"from Medico where pessoa.pessoaCPF = '" + medicoModel.getPessoa().getPessoaCPF() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>CPFCPF " + lstCPF.size());
		if (!lstCPF.isEmpty()) {
			try {
				addMsg("O CPF já foi cadastrado");
				medicoModel.getPessoa().setPessoaCPF("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void busca() {
		lstMedico = new ArrayList<Medico>();
		StringBuilder str = new StringBuilder();
		str.append("from Medico a where 1=1");
		try {
			if (!campoBuscaNome.equals("")) {
				str.append(" and (upper(a.pessoa.pessoaNome) like upper('%" + campoBuscaNome + "%'))");
			}
			if (!campoBuscaCPF.equals("")) {
				str.append(" and a.pessoa.pessoaCPF like'%" + campoBuscaCPF + "%'");
			}
			if (campoBuscaAtivo.equals("A") || campoBuscaAtivo.equals("I")) {
				System.out.println("Entrou no A or I");
				str.append(" and a.ativo = '" + campoBuscaAtivo.toUpperCase() + "'");
			}
			if (campoBuscaAtivo.equals("T")) {
				System.out.println("Entro no T");
				str.append(" and (a.ativo = 'A' or a.ativo = 'I') ");
			}
			
			str.append(" and pessoa.tipoPessoa = 'MED' " );

			lstMedico = medicoController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar Médico");
			e.printStackTrace();
		}
		
		campoBuscaCPF = "";
		campoBuscaNome = "";
	}

	public void inativar() {

		if (medicoModel.getAtivo().equals("I")) {
			medicoModel.setAtivo("A");
		} else {
			medicoModel.setAtivo("I");
		}
		try {
			medicoController.saveOrUpdate(medicoModel);
		} catch (Exception e) {
			System.out.println("Erro ao inativar/ativar");
		}
		limpar();

		busca();

	}

	public void onRowSelect(SelectEvent event) {
		medicoModel = (Medico) event.getObject();
	}
	

	public void onRowSelectDouble(SelectEvent event) {
		medicoModel = (Medico) event.getObject();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/cadastro/cadMedico.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void pesquisarCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + medicoModel.getPessoa().getCep().replace(".", "").replace("-", "") + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); // is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCEP.append(cep);
			}

			Pessoa gson = new Gson().fromJson(jsonCEP.toString(), Pessoa.class);

			medicoModel.getPessoa().setCep(gson.getCep());
			medicoModel.getPessoa().setLogradouro(gson.getLogradouro());
			medicoModel.getPessoa().setBairro(gson.getBairro());
			medicoModel.getPessoa().setComplemento(gson.getComplemento());
			medicoModel.getPessoa().setUf(gson.getUf());
			medicoModel.getPessoa().setLocalidade(gson.getLocalidade());
			System.out.println("CEP Saindo " + jsonCEP);

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			addMsg("Cep Inválido (Erro ao Buscar|Sem internet");
			error();
		} catch (IOException e) {
			addMsg("Cep Inválido");
			e.printStackTrace();
		}
	}

	@Override
	public String save() {
		try {
			medicoModel = medicoController.merge(medicoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	private void limpar() {
		medicoModel = new Medico();
	}

	public boolean idadeMinimaFuncionario() {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(medicoModel.getPessoa().getPessoaDataNascimento());
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		if (age <= 24) {
			addMsg("A idade informada deve ser maior que 24 Anos");
			System.out.println("TRUE IDADE ATENDENTE:>>>" + age);
			return false;
		} else {
			System.out.println("FALSE IDADE ATENDENTE:>>>" + age);
			return true;
		}

	}

	@Override
	public void saveNotReturn() {
		try {
			List<Medico> aux = new ArrayList<>();
			aux = medicoController.findListByQueryDinamica("from Medico where numeroCrm = '"+ medicoModel.getNumeroCrm() + "'");
			
			if (idadeMinimaFuncionario() == true) {
				if (ValidaCPF.isValid(medicoModel.getPessoa().getPessoaCPF())) { // VALIDA CPF
					if (ValidaEmail.validarEmail(medicoModel.getPessoa().getPessoaEmail())) {
						if(aux.isEmpty()) {	
								
							medicoModel.getPessoa().setTipoPessoa("MED");
							medicoModel = medicoController.merge(medicoModel);
							idPessoa = medicoModel.getPessoa().getIdPessoa();
							
							List<Login> lst = new ArrayList<>();
							lst = loginController.findListByQueryDinamica("from Login where pessoa.idPessoa = " + idPessoa);
							
							if(lst.isEmpty()) {
								DialogUtils.openDialog("dialogUsuario");
							}
							
							limpar();
							sucesso();
							
							DialogUtils.openDialog("dialogUsuario");
						}
					}else {
						addMsg("Email Inválido");
					}
				} else {
					addMsg("Cpf Inválido: " + medicoModel.getPessoa().getPessoaCPF());
					System.out.println("ERRO CPF INVÁLIDO");
				}
				
			} else {
				System.out.println("ERRO IDADE MINIMA INVALIDA>>>");
			}
		} catch (Exception e) {
			System.out.println("Erro ao Salvar Médico");
			e.printStackTrace();
		}
		busca();
	}
	
	public void verificaLogin() {
		List<Login> lst = new ArrayList<>();
		try {
			lst = loginController.findListByQueryDinamica("from Login where login = '" +loginModel.getLogin()+"'");
			
			if(!lst.isEmpty()) {
				addMsg("Este usuário já existe!");
				loginModel.setLogin("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvarLogin() {
		Pessoa pessoa = null;
		try {
			pessoa = (Pessoa) pessoaController.findById(Pessoa.class, idPessoa);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			loginModel.setPessoa((pessoa));
			loginModel = loginController.merge(loginModel);
			Liberacao liberacaoModel = new Liberacao();
			
			for(Perfil p :lstSelecionada){
				liberacaoModel.setPerfil(new Perfil(p.getIdPerfil()));
				liberacaoModel.setLogin(new Login(loginModel.getIdLogin()));
				liberacaoController.merge(liberacaoModel);
			}
			addMsg("Usuário salvo com sucesso!!");
			
			DialogUtils.closeDialog("dialogUsuario");
			loginModel = new Login();
			lstSelecionada = new ArrayList<>();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
}
	
	@Override
	public void saveEdit() {
		saveNotReturn();
	}

	@Override
	public String novo() {
		limpar();
		return getUrl();
	}

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			medicoModel = (Medico) medicoController.getSession().get(Medico.class, medicoModel.getIdMedico());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			medicoController.delete(medicoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	// Getter e Setters
	public Medico getmedicoModel() {
		return medicoModel;
	}

	public void setmedicoModel(Medico medicoModel) {

		this.medicoModel = medicoModel;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlFind() {
		return urlFind;
	}

	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Medico getMedicoModel() {
		return medicoModel;
	}

	public void setMedicoModel(Medico medicoModel) {
		this.medicoModel = medicoModel;
	}

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

	public String getCampoBuscaCPF() {
		return campoBuscaCPF;
	}

	public void setCampoBuscaCPF(String campoBuscaCPF) {
		this.campoBuscaCPF = campoBuscaCPF;
	}

	public MedicoController getMedicoController() {
		return medicoController;
	}

	public void setMedicoController(MedicoController medicoController) {
		this.medicoController = medicoController;
	}

	public List<Medico> getLstMedico() {
		return lstMedico;
	}

	public void setLstMedico(List<Medico> lstMedico) {
		this.lstMedico = lstMedico;
	}

	public EspecialidadeController getEspecialidadeController() {
		return especialidadeController;
	}

	public void setEspecialidadeController(EspecialidadeController especialidadeController) {
		this.especialidadeController = especialidadeController;
	}

	public Especialidade getEspeModel() {
		return espeModel;
	}

	public void setEspeModel(Especialidade espeModel) {
		this.espeModel = espeModel;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

	public String getCriarLogin() {
		return criarLogin;
	}

	public void setCriarLogin(String criarLogin) {
		this.criarLogin = criarLogin;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public PessoaController getPessoaController() {
		return pessoaController;
	}

	public void setPessoaController(PessoaController pessoaController) {
		this.pessoaController = pessoaController;
	}

	public Login getLoginModel() {
		return loginModel;
	}

	public void setLoginModel(Login loginModel) {
		this.loginModel = loginModel;
	}

	public Perfil getPerfilModel() {
		return perfilModel;
	}

	public void setPerfilModel(Perfil perfilModel) {
		this.perfilModel = perfilModel;
	}

	public LiberacaoController getLiberacaoController() {
		return liberacaoController;
	}

	public void setLiberacaoController(LiberacaoController liberacaoController) {
		this.liberacaoController = liberacaoController;
	}

	public List<Perfil> getLstSelecionada() {
		return lstSelecionada;
	}

	public void setLstSelecionada(List<Perfil> lstSelecionada) {
		this.lstSelecionada = lstSelecionada;
	}
}
