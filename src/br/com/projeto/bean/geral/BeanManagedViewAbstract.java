package br.com.projeto.bean.geral;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.anotacoes.IdentificaCampoPesquisa;
import br.com.projeto.enums.CondicaoPesquisa;
import br.com.projeto.report.util.BeanReportView;
import br.com.projeto.util.all.UtilitarioRegex;

@Component
public abstract class BeanManagedViewAbstract extends BeanReportView {

	private static final long serialVersionUID = 1L;

	protected abstract Class<?> getClassImp();

	protected abstract InterfaceCrud<?> getController();

	public ObjetoCampoConsulta objetoCampoConsultaSelecionado;

	public List<SelectItem> listaCampoPesquisa;

	public List<SelectItem> listaCondicaoPesquisa;

	public CondicaoPesquisa condicaoPesquisaSelecionado;

	public String valorPesquisa;
	
	public abstract String condicaoAndParaPesquisa() throws Exception;
	
	//GETTERS E SETTERS
	
	public String getValorPesquisa() {
		return valorPesquisa != null ? new UtilitarioRegex().retiraAcentos(valorPesquisa) : "";
	}

	public void setValorPesquisa(String valorPesquisa) {
		this.valorPesquisa = valorPesquisa;
	}

	public CondicaoPesquisa getCondicaoPesquisaSelecionado() {
		return condicaoPesquisaSelecionado;
	}

	public void setCondicaoPesquisaSelecionado(CondicaoPesquisa condicaoPesquisaSelecionado) {
		this.condicaoPesquisaSelecionado = condicaoPesquisaSelecionado;
	}


	public List<SelectItem> getListaCondicaoPesquisa() {
		listaCondicaoPesquisa = new ArrayList<SelectItem>();
		for (CondicaoPesquisa condicaoPesquisa : CondicaoPesquisa.values()) {
			listaCondicaoPesquisa.add(new SelectItem(condicaoPesquisa, condicaoPesquisa.toString()));
			
		}
		return listaCondicaoPesquisa;
	}
	
	public ObjetoCampoConsulta getObjetoCampoConsultaSelecionado() {
		return objetoCampoConsultaSelecionado;
	}

	public void setObjetoCampoConsultaSelecionado(ObjetoCampoConsulta objetoCampoConsultaSelecionado) {
		if (objetoCampoConsultaSelecionado != null) {
			for (Field field : getClassImp().getDeclaredFields()) {
				if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
					if (objetoCampoConsultaSelecionado.getCampoBanco().equalsIgnoreCase(field.getName())) {
						String descricaoCampo = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();
						objetoCampoConsultaSelecionado.setDescricao(descricaoCampo);
						objetoCampoConsultaSelecionado.setTipoClass(field.getType().getCanonicalName());
						objetoCampoConsultaSelecionado
								.setPrincipal(field.getAnnotation(IdentificaCampoPesquisa.class).principal());
						break;
					}
				}
			}
		}

		this.objetoCampoConsultaSelecionado = objetoCampoConsultaSelecionado;
	}

	public List<SelectItem> getListaCampoPesquisa() {
		listaCampoPesquisa = new ArrayList<SelectItem>();
		List<ObjetoCampoConsulta> listTemp = new ArrayList<ObjetoCampoConsulta>();

		for (Field field : getClassImp().getDeclaredFields()) {
			if (field.isAnnotationPresent(IdentificaCampoPesquisa.class)) {
				String descricao = field.getAnnotation(IdentificaCampoPesquisa.class).descricaoCampo();
				String descricaoCampoPesquisa = field.getAnnotation(IdentificaCampoPesquisa.class).campoConsulta();
				int isPrincipal = field.getAnnotation(IdentificaCampoPesquisa.class).principal();

				ObjetoCampoConsulta objetoCampoConsulta = new ObjetoCampoConsulta();
				objetoCampoConsulta.setDescricao(descricao);
				objetoCampoConsulta.setCampoBanco(descricaoCampoPesquisa);
				objetoCampoConsulta.setTipoClass(field.getType().getCanonicalName());
				objetoCampoConsulta.setPrincipal(isPrincipal);
				listTemp.add(objetoCampoConsulta);
			}
		}

		orderListaCampo(listTemp);

		for (ObjetoCampoConsulta objetoCampoConsulta : listTemp)
			listaCampoPesquisa.add(new SelectItem(objetoCampoConsulta));
		return listaCampoPesquisa;
	}
	//OK
	private void orderListaCampo(List<ObjetoCampoConsulta> listTemp) { Collections.sort(listTemp, new Comparator<ObjetoCampoConsulta>() {

			@Override
			public int compare(ObjetoCampoConsulta obj1, ObjetoCampoConsulta obj2) {
				return obj1.getPrincipal().compareTo(obj2.getPrincipal());
			}

		});
	}

	public void setListaCampoPesquisa(List<SelectItem> listaCampoPesquisa) {

		this.listaCampoPesquisa = listaCampoPesquisa;
	}
	//OK
	public String getSqlLazyQuery() throws Exception {
		StringBuilder sql =  new StringBuilder();
		sql.append(" select entity from ");
		sql.append(getQueryConsulta());
		sql.append(" order by entity.");
		sql.append(objetoCampoConsultaSelecionado.getCampoBanco());
		return sql.toString();
	}
	//OK
	private String getQueryConsulta() throws Exception {
		valorPesquisa = new UtilitarioRegex().retiraAcentos(valorPesquisa);
		StringBuilder sql = new StringBuilder();
		sql.append(getClassImp().getSimpleName());
		sql.append(" entity where ");
		
		sql.append(" retira_acentos(upper(cast(entity.");
		sql.append(objetoCampoConsultaSelecionado.getCampoBanco());
		sql.append(" as text))) ");
		
		if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.IGUAL_A.name())){
			sql.append(" = retira_acentos(upper('");
			sql.append(valorPesquisa);
			sql.append("'))");
		}else
			if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.CONTEM.name())){
				sql.append(" like retira_acentos(upper('%");
				sql.append(valorPesquisa);
				sql.append("%'))");
			}
		
			else
				if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.TERMINA_COM.name())){
					sql.append(" like retira_acentos(upper('%");
					sql.append(valorPesquisa);
					sql.append("'))");
				}
		
				else
					if (condicaoPesquisaSelecionado.name().equals(CondicaoPesquisa.INICIA_COM.name())){
						sql.append(" like retira_acentos(upper('");
						sql.append(valorPesquisa);
						sql.append("%'))");
					}
		
		   sql.append(" ");
		   sql.append(condicaoAndParaPesquisa());
		
		return sql.toString();
	}

	public int totalRegistroConsulta() throws Exception {
		Query query = getController().obterQuery(" select count(entity) from " + getQueryConsulta());
		Number result = (Number) query.uniqueResult();
		return result.intValue();
	}
}
