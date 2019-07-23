package br.com.projeto.util.all;

import java.io.Serializable;

import javax.annotation.PostConstruct;

public interface ActionViewPadrao extends Serializable {
	
	abstract void limpaLista() throws Exception;
	// manda pra outra url
	abstract String save() throws Exception;
	//salva na tela
	abstract void saveNotReturn() throws Exception;
	//Salvando uma Edição
	abstract void saveEdit() throws Exception;
	//Exclui..
	abstract void excluir() throws Exception;
	//redireciona pra algum lugar
	abstract String ativar() throws Exception;
	
	/**
	 * realzia inicio de metodos, valores ou variaveis 
	 * @return
	 * @throws Exception
	 */
	@PostConstruct
	abstract String novo() throws Exception;
	
	abstract String editar() throws Exception;
	// no managed bens acontece de limpar as variaveis
	abstract void setarVariaveisNulas() throws Exception;
	//consultar registros cadastrados
	abstract void consultarEntidade() throws Exception;
	
	abstract void StatusOperation(EstatusPersistencia a) throws Exception;
	
	abstract String redirecionarNewEntidade() throws Exception;
	//fazer consulta e redirecionar para outra pagina
	abstract String redirecionarFindEntidade() throws Exception;
	
	abstract void addMsg(String s) throws Exception;
	
}
