package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.MedicamentoController;
import br.com.clinica.model.prontuario.Medicamento;


@Controller
@ViewScoped
@ManagedBean(name = "materialBean")
public class MedicamentoBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Medicamento medicamentoModel;
	private String url = "/cadastro/cadMedicamento.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findMedicamento.jsf?faces-redirect=true";
	private List<Medicamento> lstMedicamento;
	private String campoBuscaNome = "";

	@Autowired
	private MedicamentoController medicamentoController; // Injeta o Medicamento Controller

	public MedicamentoBean() {
		medicamentoModel = new Medicamento();
		lstMedicamento = new ArrayList<Medicamento>();
	}

	public void onRowSelect(SelectEvent event) {
		medicamentoModel = (Medicamento) event.getObject();
	}

	public void busca() throws Exception {
		lstMedicamento = new ArrayList<Medicamento>();
		StringBuilder str = new StringBuilder();
		str.append("from Medicamento a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.nomeMedicamento) like upper('%" + campoBuscaNome + "%')");
		}

		lstMedicamento = medicamentoController.findListByQueryDinamica(str.toString());
	}

	@Override
	public String save() throws Exception {

		medicamentoModel = medicamentoController.merge(medicamentoModel);
		medicamentoModel = new Medicamento();

		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		if (medicamentoModel == null) {
			medicamentoModel = new Medicamento();
		}
		medicamentoModel = medicamentoController.merge(medicamentoModel);
		medicamentoModel = new Medicamento();
		sucesso();

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

	public void limpa() throws Exception {
		medicamentoModel = new Medicamento();
	}

	public String edita() throws Exception {
		return getUrl();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		medicamentoModel = (Medicamento) 
				medicamentoController.getSession().get(getClassImp(), 
						medicamentoModel.getIdMedicamento());
		medicamentoController.delete(medicamentoModel);
		medicamentoModel = new Medicamento();
		sucesso();
		busca();
	}

	@Override
	protected Class<Medicamento> getClassImp() {
		return Medicamento.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		medicamentoModel = new Medicamento();
	}

	@Override
	protected MedicamentoController getController() {
		return medicamentoController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		medicamentoModel = new Medicamento();
	}


	// GETTERS E SETTERS

	public Medicamento getmedicamentoModel() {
		return medicamentoModel;
	}

	public void setmedicamentoModel(Medicamento medicamentoModel) {
		this.medicamentoModel = medicamentoModel;
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

	public List<Medicamento> getLstMedicamento() {
		return lstMedicamento;
	}

	public void setLstMedicamento(List<Medicamento> lstMedicamento) {
		this.lstMedicamento = lstMedicamento;
	}

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}


}
