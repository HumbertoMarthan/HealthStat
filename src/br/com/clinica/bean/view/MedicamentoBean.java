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

	private Medicamento medicamentoModel = new Medicamento();

	private List<Medicamento> lstMedicamento = new ArrayList<Medicamento>();;
	
	private String url = "/cadastro/cadMedicamento.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findMedicamento.jsf?faces-redirect=true";
	
	private String campoBuscaNome = "";

	@Autowired
	private MedicamentoController medicamentoController; // Injeta o Medicamento Controller

	public void onRowSelect(SelectEvent event) {
		medicamentoModel = (Medicamento) event.getObject();
	}

	public void busca() {
		StringBuilder str = new StringBuilder();
		str.append("from Medicamento a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.nomeMedicamento) like upper('%" + campoBuscaNome + "%')");
		}

		try {
			lstMedicamento = medicamentoController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String save()  {
		try {
			medicamentoModel = medicamentoController.merge(medicamentoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		medicamentoModel = new Medicamento();

		return "";
	}

	@Override
	public void saveNotReturn() {
		if (medicamentoModel == null) {
			medicamentoModel = new Medicamento();
		}
		try {
			medicamentoModel = medicamentoController.merge(medicamentoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();

	}

	@Override
	public void saveEdit()  {
		saveNotReturn();
	}

	@Override
	public String novo(){
		limpar();
		return getUrl();
	}

	@Override
	public String editar(){
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			medicamentoModel = (Medicamento) 
					medicamentoController.getSession().get(Medicamento.class, 
							medicamentoModel.getIdMedicamento());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			medicamentoController.delete(medicamentoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
		busca();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	public void limpar() {
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

	public Medicamento getMedicamentoModel() {
		return medicamentoModel;
	}

	public void setMedicamentoModel(Medicamento medicamentoModel) {
		this.medicamentoModel = medicamentoModel;
	}

	public MedicamentoController getMedicamentoController() {
		return medicamentoController;
	}

	public void setMedicamentoController(MedicamentoController medicamentoController) {
		this.medicamentoController = medicamentoController;
	}
}
