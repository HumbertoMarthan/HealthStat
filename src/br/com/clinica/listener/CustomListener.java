package br.com.clinica.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import br.com.clinica.hibernate.UtilFrameworks;
import br.com.clinica.model.cadastro.outro.InformacaoRevisao;
import br.com.clinica.model.cadastro.usuario.Login;

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
