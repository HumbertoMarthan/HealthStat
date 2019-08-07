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

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EspecialidadeController;
import br.com.clinica.controller.geral.MedicoController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.Especialidade;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.utils.ValidaCPF;

@Controller
@ViewScoped
@ManagedBean(name = "medicoBean")
public class MedicoBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Medico medicoModel;
	private Especialidade espeModel;
	private String url = "/cadastro/cadMedico.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findMedico.jsf?faces-redirect=true";
	List<Medico> lstMedico;
	private String campoBuscaNome;
	private String campoBuscaCPF;
	
	@Autowired
	private MedicoController medicoController; // Injetando o Controller do Médico

	@Autowired
	private EspecialidadeController especialidadeController;
	
	public MedicoBean() {
		medicoModel = new Medico();
		lstMedico = new ArrayList<Medico>();
	}
	
	public List<Especialidade> completeEspecialidade(String q) throws Exception {
		return especialidadeController.
				findListByQueryDinamica(" from Especialidade where nomeEspecialidade like '%" + q.toUpperCase()  + "%'");
	}
	
	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from Medico a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.pessoa.pessoaNome) like upper('%" + campoBuscaNome + "%')");
		}
		if (!campoBuscaCPF.equals("")) {
			str.append(" and a.pessoa.pessoaCPF like'%" + campoBuscaCPF + "%'");
		}
		
		lstMedico=  medicoController.findListByQueryDinamica(str.toString());
	}
	
	// Relatório
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_medico");
		super.setNomeRelatorioSaida("report_medico");
		super.setListDataBeanCollectionReport(medicoController.findList(getClassImp()));
		return super.getArquivoReport();
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void onRowSelect(SelectEvent event) {
		medicoModel = (Medico) event.getObject();
	}

	public void pesquisarCep(AjaxBehaviorEvent event) throws Exception {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + medicoModel.getPessoa().getCep() + "/json/");
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

	public void limpa() {
		/* Dados */
		medicoModel.getPessoa().setPessoaNome("");
		medicoModel.getPessoa().setPessoaDataNascimento(null);
		medicoModel.getPessoa().setPessoaSexo("");
		medicoModel.getPessoa().setPessoaEmail("");
		medicoModel.getPessoa().setPessoaRG("");
		medicoModel.getPessoa().setPessoaCPF("");
		medicoModel.getPessoa().setPessoaObservacao("");
		medicoModel.getPessoa().setPessoaTelefonePrimario("");
		medicoModel.getPessoa().setPessoaTelefoneSecundario("");
		//medicoModel.setDataInscricaoCrm(null);
		medicoModel.setNumeroCrm("");

		/* Endereço */
		medicoModel.getPessoa().setCep("");
		medicoModel.getPessoa().setBairro("");
		medicoModel.getPessoa().setUf("");
		medicoModel.getPessoa().setLogradouro("");
		medicoModel.getPessoa().setComplemento("");
		medicoModel.getPessoa().setLocalidade("");
	}

	@Override
	public String save() throws Exception {
		medicoModel = medicoController.merge(medicoModel);
		medicoModel = new Medico();
		return "";
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
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"A idade informada deve ser maior que 24 Anos", "");
			System.out.println("TRUE IDADE ATENDENTE:>>>" + age);
			addMessage(message);
			return false;
		} else {
			System.out.println("FALSE IDADE ATENDENTE:>>>" + age);
			return true;
		}

	}

	@Override
	public void saveNotReturn() throws Exception {
		if (idadeMinimaFuncionario() == true) {
			if (ValidaCPF.isCPF(medicoModel.getPessoa().getPessoaCPF())) { // VALIDA CPF
				medicoModel = medicoController.merge(medicoModel);
				medicoModel = new Medico();
				sucesso();
				//busca();
				System.out.println("CPF Válido");
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, 
						"Cpf Inválido: "+medicoModel.getPessoa().getPessoaCPF(), "");
				addMessage(message);
				System.out.println("ERRO CPF INVÁLIDO");

			}
		} else {
			System.out.println("ERRO IDADE MINIMA INVALIDA>>>");
		}
	}
	
	@Override
	public void saveEdit() throws Exception {
		saveNotReturn();
	}

	@Override
	public String novo() throws Exception {
		setarVariaveisNulas();
		return getUrl();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		medicoModel = new Medico();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		medicoModel = (Medico) medicoController.getSession().get(getClassImp(), medicoModel.getIdMedico());
		medicoController.delete(medicoModel);
		medicoModel = new Medico();
		sucesso();
	}

	@Override
	protected Class<Medico> getClassImp() {
		return Medico.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Medico> getController() {
		return medicoController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		medicoModel = new Medico();
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
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
	
	

}
