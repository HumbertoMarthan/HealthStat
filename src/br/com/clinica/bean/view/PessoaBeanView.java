package br.com.clinica.bean.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Pessoa;


@Controller
@ViewScoped
@ManagedBean(name = "pessoaBeanView")
public class PessoaBeanView extends BeanManagedViewAbstract {
	
	private static final long serialVersionUID = 1L;
	
	private Pessoa objetoSelecionado = new Pessoa();
	
	private String url = "/cadastro/cad_pessoaj.sf?faces-redirect=true";
	private String urlFind = "/cadastro/find_pessoa.jsf?faces-redirect=true";
	
	
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

	@Override
	public String save() throws Exception {
	    objetoSelecionado = pessoaController.merge(objetoSelecionado);
	   
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		objetoSelecionado = pessoaController.merge(objetoSelecionado);
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
		objetoSelecionado = new Pessoa();
	}
	
	@Override
	public String editar() throws Exception {
		return getUrl();
	}
	
	@Override
	public void excluir() throws Exception {
		objetoSelecionado = (Pessoa) pessoaController.getSession().get(getClassImp(),  objetoSelecionado.getIdPessoa());
		pessoaController.delete(objetoSelecionado);	
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
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}
	
}
