package br.com.projeto.geral.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Evento;
import br.com.repository.interfaces.RepositoryEvento;
import br.com.srv.interfaces.SrvEvento;

@Controller
public class EventoController extends ImplementacaoCrud<Evento> implements 
InterfaceCrud<Evento> {
	
	private static final long serialVersionUID = 1L;

	@Resource
	private SrvEvento srvEvento;
	
	@Resource
	private RepositoryEvento repositoryEvento;
	
	//Buscando no Banco de dados a lista de especialidades
	
	public List<Evento> listarEventos() throws Exception{
		return super.findListByQueryDinamica(" from Evento ");
	}
	
}
