package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EspecialidadeController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.Especialidade;

@Controller
@ViewScoped
@ManagedBean(name = "especialidadeBean")
public class EspecialidadeBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Especialidade especialidadeModel;
	private String url = "/cadastro/cadEspecialidade.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findEspecialidade.jsf?faces-redirect=true";
	List<Especialidade> lstEspecialidade;
	private String campoBuscaEspecialidade;

	@Autowired
	private EspecialidadeController especialidadeController;

	public EspecialidadeBean() {
		especialidadeModel = new Especialidade();
		lstEspecialidade = new ArrayList<Especialidade>();
	}

	public void busca() throws Exception {
		lstEspecialidade = new ArrayList<Especialidade>();
		StringBuilder str = new StringBuilder();
		str.append("from Especialidade a where 1=1");

		if (!campoBuscaEspecialidade.equals("")) {
			str.append(" and upper(a.nomeEspecialidade) like upper('%" + campoBuscaEspecialidade + "%')");
		}

		lstEspecialidade = especialidadeController.findListByQueryDinamica(str.toString());
	}

	public void onRowSelect(SelectEvent event) {
		especialidadeModel = (Especialidade) event.getObject();
	}

	@Override
	public String save() throws Exception {
		especialidadeModel = especialidadeController.merge(especialidadeModel);
		especialidadeModel = new Especialidade();
		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		especialidadeModel = especialidadeController.merge(especialidadeModel);
		especialidadeModel = new Especialidade();
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

	@Override
	public void setarVariaveisNulas() throws Exception {
		especialidadeModel = new Especialidade();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		especialidadeModel = (Especialidade) especialidadeController.getSession().get(getClassImp(),
				especialidadeModel.getIdEspecialidade());
		especialidadeController.delete(especialidadeModel);
		especialidadeModel = new Especialidade();
		sucesso();
	}

	@Override
	protected Class<Especialidade> getClassImp() {
		return Especialidade.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Especialidade> getController() {
		return especialidadeController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		especialidadeModel = new Especialidade();
	}

	public Especialidade getEspecialidadeModel() {
		return especialidadeModel;
	}

	public void setEspecialidadeModel(Especialidade especialidadeModel) {
		this.especialidadeModel = especialidadeModel;
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

	public List<Especialidade> getLstEspecialidade() {
		return lstEspecialidade;
	}

	public void setLstEspecialidade(List<Especialidade> lstEspecialidade) {
		this.lstEspecialidade = lstEspecialidade;
	}

	public String getCampoBuscaEspecialidade() {
		return campoBuscaEspecialidade;
	}

	public void setCampoBuscaEspecialidade(String campoBuscaEspecialidade) {
		this.campoBuscaEspecialidade = campoBuscaEspecialidade;
	}

	public EspecialidadeController getEspecialidadeController() {
		return especialidadeController;
	}

	public void setEspecialidadeController(EspecialidadeController especialidadeController) {
		this.especialidadeController = especialidadeController;
	}
}
