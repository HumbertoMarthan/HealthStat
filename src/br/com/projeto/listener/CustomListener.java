package br.com.projeto.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import br.com.framework.utils.UtilFrameworks;
import br.com.projeto.model.InformacaoRevisao;
import br.com.projeto.model.Login;

@Service
public class CustomListener implements RevisionListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void newRevision(Object revisionEntity) {
		InformacaoRevisao informacaoRevisao = (InformacaoRevisao) revisionEntity;
		Long codUser = UtilFrameworks.getThreadLocal().get();
		
		

		Login entidade = new Login();
		if (codUser != null && codUser !=0L){
			entidade.setIdLogin(codUser);
			informacaoRevisao.setEntidade(entidade);
		}
	}
	
	
	
	

}
