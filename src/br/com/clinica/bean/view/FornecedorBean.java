package br.com.clinica.bean.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.FornecedorController;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "fornecedorModel")
public class FornecedorBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Fornecedor fornecedorModel = new Fornecedor();

	private String url = "/cadastro/cadFornecedor.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findFornecedor.jsf?faces-redirect=true";
	
	private List<Fornecedor> lstFornecedor = new ArrayList<>();
	
	private String campoBuscaFornecedor = "";
	private String campoBuscaAtivo = "A";

	@PostConstruct
	public void init() {
		busca();
	}

	@Autowired
	private FornecedorController fornecedorController;

	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstFornecedor, "fornecedor.jrxml");
		try {
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public void busca() {
		try {
			StringBuilder str = new StringBuilder();
			str.append("from Fornecedor a where 1=1");
			if (!campoBuscaFornecedor.equals("")) {
				str.append(" and (upper(a.nomeFornecedor) like upper('%" + campoBuscaFornecedor + "%'))");
			}
			if (campoBuscaAtivo.equals("A") || campoBuscaAtivo.equals("I")) {
				str.append(" and a.ativo = '" + campoBuscaAtivo.toUpperCase() + "'");
			}
			if (campoBuscaAtivo.equals("T")) {
				str.append(" and (a.ativo = 'A' or a.ativo = 'I') ");
			}

			lstFornecedor = fornecedorController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar Fornecedor");
			e.printStackTrace();
		}
	}

	public void inativar() {

		if (fornecedorModel.getAtivo().equals("I")) {
			fornecedorModel.setAtivo("A");
		} else {
			fornecedorModel.setAtivo("I");
		}

		try {
			fornecedorController.saveOrUpdate(fornecedorModel);
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		limpar();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar atendente");
			e.printStackTrace();
		}
	}

	public void pesquisarCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + fornecedorModel.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); // is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // br

			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCEP.append(cep);

			}

			Fornecedor gson = new Gson().fromJson(jsonCEP.toString(), Fornecedor.class);

			fornecedorModel.setCep(gson.getCep());
			fornecedorModel.setLogradouro(gson.getLogradouro());
			fornecedorModel.setBairro(gson.getBairro());
			fornecedorModel.setComplemento(gson.getComplemento());
			fornecedorModel.setUf(gson.getUf());
			fornecedorModel.setLocalidade(gson.getLocalidade());
			System.out.println("CEP Saindo " + jsonCEP);

		} catch (Exception e) {
			e.printStackTrace();
			error();
			System.out.println("Erro ao buscar cep 'Internet' ");
		}
	}

	@Override
	public String save()  {
		try {
			fornecedorModel = fornecedorController.merge(fornecedorModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	private void limpar() {
		fornecedorModel = new Fornecedor();
	}

	@Override
	public void saveNotReturn() {
		try {
			fornecedorModel = fornecedorController.merge(fornecedorModel);
			limpar();
			sucesso();
		} catch (Exception e) {
			System.out.println("Erro ao salvar Fornecedor");
			e.printStackTrace();
		}
		busca();
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
			fornecedorModel = (Fornecedor) fornecedorController.getSession().get(Fornecedor.class,fornecedorModel.getIdFornecedor());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			fornecedorController.delete(fornecedorModel);
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

	public void onRowSelect(SelectEvent event) {
		fornecedorModel = (Fornecedor) event.getObject();
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

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}
}
