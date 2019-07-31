package br.com.clinica.service.interfaces;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public interface SrvLoginSessao extends Serializable {
	
	Date getUltimoAcessoEntidadeLogada(String name);
	void updateUltimoAcessoUser(String login);
	boolean existeUsuario(String login);
}
