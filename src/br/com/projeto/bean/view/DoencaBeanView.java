package br.com.projeto.bean.view;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.bean.geral.BeanManagedViewAbstract;
import br.com.projeto.carregamento.lazy.CarregamentoLazyListForObjeto;
import br.com.projeto.geral.controller.DoencaController;
import br.com.projeto.model.Doenca;


@Controller
@Scope(value = "session")
@ManagedBean(name = "doencaBeanView")
public class DoencaBeanView extends BeanManagedViewAbstract {
	
	private static final long serialVersionUID = 1L;
	
	private Doenca objetoSelecionado = new Doenca();
	
	private String url = "/cadastro/cadDoenca.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findDoenca.jsf?faces-redirect=true";
	
	private CarregamentoLazyListForObjeto<Doenca> list
	= new CarregamentoLazyListForObjeto<Doenca>();
	 
	@Autowired
	private DoencaController doencaController;
	
	//IMPRIMIR OS RELATÓRIOS
	
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_doenca");
		super.setNomeRelatorioSaida("report_doenca");
		super.setListDataBeanCollectionReport(doencaController.findList(getClassImp()));
		return super.getArquivoReport();
	}
	
	/**
	 * Metodos Getter E Setters dos objetos
	 * @return
	 */
	
	public Doenca getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Doenca objetoSelecionado) {

		this.objetoSelecionado = objetoSelecionado;
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

	public CarregamentoLazyListForObjeto<Doenca> getList() throws Exception {
		return list;
	}

	/**
	 * Metodos para manipular salvamento, exclusões, editar, novo 
	 */
	
	@Override
	public String save() throws Exception {
	    objetoSelecionado = doencaController.merge(objetoSelecionado);
	   
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		list.clean();
		objetoSelecionado = doencaController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Doenca();
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
		list.clean();
		objetoSelecionado = new Doenca();
	}
	
	@Override
	public String editar() throws Exception {
		list.clean();	
		return getUrl();
	}
	
	@Override
	public void excluir() throws Exception {
		objetoSelecionado = (Doenca) doencaController.getSession().get(getClassImp(),  objetoSelecionado.getIdDoenca());
		doencaController.delete(objetoSelecionado);	
		list.remove(objetoSelecionado);
		objetoSelecionado = new Doenca();
		sucesso();
	}

	@Override
	protected Class<Doenca> getClassImp() {
		return Doenca.class;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Doenca> getController() {
		return doencaController;
	}
	@Override
	public void consultarEntidade() throws Exception {
		 objetoSelecionado = new Doenca();
		 list.clean();
		 list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}
	
}
