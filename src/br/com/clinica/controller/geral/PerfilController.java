package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.repository.interfaces.RepositoryPerfil;
import br.com.clinica.service.interfaces.SrvPerfil;

@Controller
public class PerfilController extends ImplementacaoCrud<Perfil> implements 
	InterfaceCrud<Perfil>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPerfil srvPerfil;
	
	@Resource
	private RepositoryPerfil repositoryPerfil;
	
}
