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
import br.com.clinica.controller.geral.AcompanhanteController;
import br.com.clinica.controller.geral.PacienteController;
import br.com.clinica.model.cadastro.pessoa.Acompanhante;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.utils.ValidaCPF;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "pacienteBean")
public class PacienteBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Paciente pacienteModel = new Paciente();
	private Acompanhante acompaModel = new Acompanhante();
	
	private List<Acompanhante> lstAcompanhante = new ArrayList<Acompanhante>();
	private List<Paciente> 	lstPaciente = new ArrayList<Paciente>();
	
	private String url = "/cadastro/cadPaciente.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findPaciente.jsf?faces-redirect=true";
	
	private String campoBuscaNome = "";
	private String campoBuscaAtivo = "T";
	private String campoBuscaCPF = "";
	private int idade = 20;

	@Autowired
	private PacienteController pacienteController;

	@Autowired
	private AcompanhanteController acompanhanteController;

	@PostConstruct
	public void init(){
		busca();
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstPaciente, "paciente.jrxml");
		try {
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	

	public void busca() {
		try {
			StringBuilder str = new StringBuilder();
			str.append("from Paciente a where 1=1");
	
			if (!campoBuscaNome.equals("")) {
				str.append(" and upper(a.pessoa.pessoaNome) like '%" + campoBuscaNome.toUpperCase() + "%'");
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
			
		str.append(" and pessoa.tipoPessoa = 'PAC' " );
		
		lstPaciente = pacienteController.findListByQueryDinamica(str.toString());
		}catch (Exception e) {
			System.out.println("Erro ao buscar Paciente");
			e.printStackTrace();
		}
		campoBuscaCPF = "";
		campoBuscaNome = "";
	}
	
	public void inativar() {

		if (pacienteModel.getAtivo().equals("I")) {
			pacienteModel.setAtivo("A");
		} else {
			pacienteModel.setAtivo("I");
		}

		try {
			pacienteController.saveOrUpdate(pacienteModel);
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		this.pacienteModel = new Paciente();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar atendente");
			e.printStackTrace();
		}
	}


	public void buscaAcompanhante()  {
		StringBuilder str = new StringBuilder();
		str.append("from Acompanhante a where 1=1");
		str.append(" AND a.idPacienteFK = " + pacienteModel.getIdPaciente());

		try {
			lstAcompanhante = acompanhanteController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onRowSelect(SelectEvent event){
		try {
			pacienteModel = (Paciente) event.getObject();
			//buscaAcompanhante();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void onRowSelectDouble(SelectEvent event) {
		pacienteModel = (Paciente) event.getObject();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/cadastro/cadPaciente.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void removerAcompanhante() {
		for (Acompanhante item : lstAcompanhante) {
			try {
				acompanhanteController.merge(item);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean idadeMinimaPaciente() {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(pacienteModel.getPessoa().getPessoaDataNascimento());
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		idade = age;
		System.out.println("IDADE RESULTADO DO IDADE MINIMA>>>>" + age);
		if (age < 18) {
			addMsg("O paciente deve ser acompanhado por um Responsável que tenha idade maior que 18 anos ");
			System.out.println(" de menor false IDADE PACIENTE:>>>" + age);
			return false;
		}
		if (age >= 60) {
			addMsg("O paciente deve ser acompanhado por um Responsável ");
			System.out.println(" de menor false IDADE PACIENTE:>>>" + age);
			return false;
		} else {
			System.out.println(" de maior true IDADE ATENDENTE:>>>" + age);
			return true;
		}

	}

	// PESQUISA CEP
	public void pesquisarCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pacienteModel.getPessoa().getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); // is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // br

			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCEP.append(cep);

			}

			Pessoa gson = new Gson().fromJson(jsonCEP.toString(), Pessoa.class);

			pacienteModel.getPessoa().setCep(gson.getCep());
			pacienteModel.getPessoa().setLogradouro(gson.getLogradouro());
			pacienteModel.getPessoa().setBairro(gson.getBairro());
			pacienteModel.getPessoa().setComplemento(gson.getComplemento());
			pacienteModel.getPessoa().setUf(gson.getUf());
			pacienteModel.getPessoa().setLocalidade(gson.getLocalidade());
			System.out.println("CEP Saindo " + jsonCEP);

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			addMsg("Cep Inválido (Erro ao Buscar|Sem internet");
			error();
		} catch (IOException e) {
			addMsg("Cep Inválido");
		}
	}


	@Override
	public String save() {

		try {
			pacienteModel.getPessoa().setTipoPessoa("PAC");
			pacienteModel = pacienteController.merge(pacienteModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	private void limpar() {
		pacienteModel = new Paciente();
	}

	@Override
	public void saveNotReturn() {
		try {
			//idadeMinimaPaciente();
			if (ValidaCPF.isValid(pacienteModel.getPessoa().getPessoaCPF())) {
				System.out.println("CPF Válido");
				pacienteModel.getPessoa().setTipoPessoa("PAC");
				pacienteModel = pacienteController.merge(pacienteModel);
				limpar();
				sucesso();
			} else {
				addMsg("Cpf Inválido: " + pacienteModel.getPessoa().getPessoaCPF());
				System.out.println("ERRO CPF INVÁLIDO");
			}
		}catch (Exception e) {
			System.out.println("Erro ao salvar Paciente");
			e.printStackTrace();
		}
		busca();
		
	}

	public void salvarAcompanhante() {
		/*
		 * idadeMinimaPaciente(); Acompanhante acompanhante = new Acompanhante();
		 * acompanhante = acompaModel; if (pacienteModel.getId() != null) { if
		 * (pacienteModel.getPessoa().getPessoaNome() != "") {
		 * acompaModel.setIdPacienteFK(pacienteModel.getId());
		 * lstAcompanhante.add(acompanhante);
		 * acompanhanteController.merge(acompanhante); acompanhante = new
		 * Acompanhante(); addMsg("Adicionado com sucesso"); } else {
		 * addMsg("Preencha todos os dados do acompanhante "); } }
		 */

		if (pacienteModel.getId() == null) {
			try {
				pacienteController.persist(pacienteModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (pacienteModel.getId() != null) {
			try {
				pacienteController.merge(pacienteModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Acompanhante a = new Acompanhante();
		a = acompaModel;
		lstAcompanhante.add(a);
		acompaModel = new Acompanhante();
		addMsg("Adicionado");


		lstAcompanhante.add(acompaModel);
		acompaModel = new Acompanhante();
		addMsg("Adicionado");
		buscaAcompanhante();
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
			pacienteModel = (Paciente) pacienteController.getSession().get(Paciente.class, pacienteModel.getIdPaciente());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			pacienteController.delete(pacienteModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pacienteModel = new Paciente();
		sucesso();
		busca();
	}

	public void excluirAcompanhante(long cod) {
		System.out.println(">>>>>>>>>>>" + cod);
		acompaModel.setIdAcompanhante(cod);
		try {
			acompanhanteController.delete(acompaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// acompaModel = new Acompanhante();
		buscaAcompanhante();
	}

	@Override
	public String redirecionarFindEntidade(){
		limpar();
		return getUrlFind();
	}
	
	public Paciente getPacienteModel() {
		return pacienteModel;
	}

	public void setPacienteModel(Paciente pacienteModel) {
		this.pacienteModel = pacienteModel;
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

	public String getCampoBuscaCPF() {
		return campoBuscaCPF;
	}

	public void setCampoBuscaCPF(String campoBuscaCPF) {
		this.campoBuscaCPF = campoBuscaCPF;
	}

	public List<Acompanhante> getLstAcompanhante() {
		return lstAcompanhante;
	}

	public void setLstAcompanhante(List<Acompanhante> lstAcompanhante) {
		this.lstAcompanhante = lstAcompanhante;
	}

	public List<Paciente> getLstPaciente() {
		return lstPaciente;
	}

	public void setLstPaciente(List<Paciente> lstPaciente) {
		this.lstPaciente = lstPaciente;
	}

	public PacienteController getPacienteController() {
		return pacienteController;
	}

	public void setPacienteController(PacienteController pacienteController) {
		this.pacienteController = pacienteController;
	}

	public AcompanhanteController getAcompanhanteController() {
		return acompanhanteController;
	}

	public void setAcompanhanteController(AcompanhanteController acompanhanteController) {
		this.acompanhanteController = acompanhanteController;
	}

	public Acompanhante getAcompaModel() {
		return acompaModel;
	}

	public void setAcompaModel(Acompanhante acompaModel) {
		this.acompaModel = acompaModel;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}
}
