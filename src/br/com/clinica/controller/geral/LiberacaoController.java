package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.usuario.Liberacao;
import br.com.clinica.repository.interfaces.RepositoryLiberacao;
import br.com.clinica.service.interfaces.SrvLiberacao;

@Controller
public class LiberacaoController extends ImplementacaoCrud<Liberacao> implements 
	InterfaceCrud<Liberacao>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvLiberacao srvLiberacao;
	
	@Resource
	private RepositoryLiberacao repositoryLiberacao;
	
}
