package br.com.clinica.bean.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.FornecedorController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Fornecedor;


@Controller
@ViewScoped
@ManagedBean(name = "fornecedorModel")
public class FornecedorBean extends BeanManagedViewAbstract {
	
	private static final long serialVersionUID = 1L;
	
	private Fornecedor fornecedorModel;
	
	private String url = "/cadastro/cadFornecedor.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findFornecedor.jsf?faces-redirect=true";
	private List<Fornecedor> lstFornecedor;
	private String campoBuscaFornecedor = "";
	
	public FornecedorBean() {
		fornecedorModel = new Fornecedor();
	}
	
	@Autowired
	private FornecedorController fornecedorController;
	
	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from Fornecedor a where 1=1");
		if (!campoBuscaFornecedor.equals("")) {
			str.append(" and upper(a.nomeFornecedor) like upper('%" + campoBuscaFornecedor + "%')");
		}

		lstFornecedor = fornecedorController.findListByQueryDinamica(str.toString());
	}
	
	public void pesquisarCep(AjaxBehaviorEvent event) throws Exception {
		try {
			URL url = new URL("https://viacep.com.br/ws/"+ fornecedorModel.getCep() +"/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); //is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); //br
		
			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();
			
			while((cep = bufferedReader.readLine()) != null)    {
				jsonCEP.append(cep);
				
			}
			
			Fornecedor gson = new Gson().fromJson(jsonCEP.toString(), Fornecedor.class);
			
			fornecedorModel.setCep(gson.getCep());
			fornecedorModel.setLogradouro(gson.getLogradouro());
			fornecedorModel.setBairro(gson.getBairro());
			fornecedorModel.setComplemento(gson.getComplemento());
			fornecedorModel.setUf(gson.getUf());
			fornecedorModel.setLocalidade(gson.getLocalidade());
			System.out.println("CEP Saindo " +jsonCEP);
			
		} catch (Exception e) {
			e.printStackTrace();
			error();
			System.out.println("Erro ao buscar cep 'Internet' ");
		}
	}
	
	@Override
	public String save() throws Exception {
		fornecedorModel = fornecedorController.merge(fornecedorModel);
		fornecedorModel = new Fornecedor();
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		fornecedorModel = fornecedorController.merge(fornecedorModel);
		fornecedorModel = new Fornecedor();
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
		fornecedorModel = new Fornecedor();
	}
	
	@Override
	public String editar() throws Exception {
		return getUrl();
	}
	
	@Override
	public void excluir() throws Exception {
		fornecedorModel = (Fornecedor) fornecedorController.getSession().get(getClassImp(),  fornecedorModel.getIdFornecedor());
		fornecedorController.delete(fornecedorModel);	
		fornecedorModel = new Fornecedor();
		sucesso();
		busca();
	}

	@Override
	protected Class<Fornecedor> getClassImp() {
		return Fornecedor.class;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Fornecedor> getController() {
		return fornecedorController;
	}
	@Override
	public void consultarEntidade() throws Exception {
		 fornecedorModel = new Fornecedor();
	}
	
	public void onRowSelect(SelectEvent event) {
		fornecedorModel = (Fornecedor) event.getObject();
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}

	public Fornecedor getFornecedorModel() {
		return fornecedorModel;
	}

	public void setFornecedorModel(Fornecedor fornecedorModel) {
		this.fornecedorModel = fornecedorModel;
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

	public FornecedorController getFornecedorController() {
		return fornecedorController;
	}

	public void setFornecedorController(FornecedorController fornecedorController) {
		this.fornecedorController = fornecedorController;
	}

	public List<Fornecedor> getLstFornecedor() {
		return lstFornecedor;
	}

	public void setLstFornecedor(List<Fornecedor> lstFornecedor) {
		this.lstFornecedor = lstFornecedor;
	}

	public String getCampoBuscaFornecedor() {
		return campoBuscaFornecedor;
	}

	public void setCampoBuscaFornecedor(String campoBuscaFornecedor) {
		this.campoBuscaFornecedor = campoBuscaFornecedor;
	}
	
	
	
	
	
}
