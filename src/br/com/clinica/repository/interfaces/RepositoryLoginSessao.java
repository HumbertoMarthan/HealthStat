package br.com.clinica.repository.interfaces;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public interface RepositoryLoginSessao extends Serializable {
	
	Date getUltimoAcessoEntidadeLogada(String name);
	void updateUltimoAcessoUser(String login);
	boolean existeUsuario(String login);
}
