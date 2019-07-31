package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.repository.interfaces.RepositoryPerfil;
@Repository
public class DaoPerfil extends ImplementacaoCrud<Perfil> implements RepositoryPerfil{

	
	private static final long serialVersionUID = 1L;

}
