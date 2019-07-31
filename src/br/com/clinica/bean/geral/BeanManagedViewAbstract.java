package br.com.clinica.bean.geral;

import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.relatorio.BeanReportView;
import br.com.clinica.utils.UtilitarioRegex;

@Component
public abstract class BeanManagedViewAbstract extends BeanReportView {

	private static final long serialVersionUID = 1L;

	protected abstract Class<?> getClassImp();

	protected abstract InterfaceCrud<?> getController();

	public List<SelectItem> listaCampoPesquisa;

	public List<SelectItem> listaCondicaoPesquisa;


	public String valorPesquisa;
	
	public abstract String condicaoAndParaPesquisa() throws Exception;
	
	//GETTERS E SETTERS
	
	public String getValorPesquisa() {
		return valorPesquisa != null ? new UtilitarioRegex().retiraAcentos(valorPesquisa) : "";
	}

	public void setValorPesquisa(String valorPesquisa) {
		this.valorPesquisa = valorPesquisa;
	}
}
