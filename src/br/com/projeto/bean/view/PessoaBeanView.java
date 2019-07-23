package br.com.projeto.bean.view;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.bean.geral.BeanManagedViewAbstract;
import br.com.projeto.carregamento.lazy.CarregamentoLazyListForObjeto;
import br.com.projeto.geral.controller.PessoaController;
import br.com.projeto.model.Pessoa;


@Controller
@Scope(value = "session")
@ManagedBean(name = "pessoaBeanView")
public class PessoaBeanView extends BeanManagedViewAbstract {
	
	private static final long serialVersionUID = 1L;
	
	private Pessoa objetoSelecionado = new Pessoa();
	
	private String url = "/cadastro/cad_pessoaj.sf?faces-redirect=true";
	private String urlFind = "/cadastro/find_pessoa.jsf?faces-redirect=true";
	
	private CarregamentoLazyListForObjeto<Pessoa> list = new CarregamentoLazyListForObjeto<Pessoa>();
	
	@Autowired
	private PessoaController pessoaController;
	/**
	 * Metodos Getter E Setters dos objetos
	 * @return
	 */
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_cidade");
		super.setNomeRelatorioSaida("report_cidade");
		super.setListDataBeanCollectionReport(pessoaController.findList(getClassImp()));
		return super.getArquivoReport();
	}
	
	public Pessoa getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Pessoa objetoSelecionado) {

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

	public CarregamentoLazyListForObjeto<Pessoa> getList() throws Exception {
		return list;
	}

	/**
	 * Metodos para manipular salvamento, exclusões, editar, novo 
	 */
	
	@Override
	public String save() throws Exception {
	    objetoSelecionado = pessoaController.merge(objetoSelecionado);
	   
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		list.clean();
		objetoSelecionado = pessoaController.merge(objetoSelecionado);
		list.add(objetoSelecionado);
		objetoSelecionado = new Pessoa();
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
		objetoSelecionado = new Pessoa();
	}
	
	@Override
	public String editar() throws Exception {
		list.clean();	
		return getUrl();
	}
	
	@Override
	public void excluir() throws Exception {
		objetoSelecionado = (Pessoa) pessoaController.getSession().get(getClassImp(),  objetoSelecionado.getIdPessoa());
		pessoaController.delete(objetoSelecionado);	
		list.remove(objetoSelecionado);
		objetoSelecionado = new Pessoa();
		sucesso();
	}

	@Override
	protected Class<Pessoa> getClassImp() {
		return Pessoa.class;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return urlFind;
	}

	@Override
	protected InterfaceCrud<Pessoa> getController() {
		return pessoaController;
	}
	@Override
	public void consultarEntidade() throws Exception {
		 objetoSelecionado = new Pessoa();
		 list.clean();
		 list.setTotalRegistroConsulta(super.totalRegistroConsulta(), super.getSqlLazyQuery());
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}
	
}
