package br.com.clinica.controller.geral;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.agendamento.Evento;
import br.com.clinica.repository.interfaces.RepositoryEvento;
import br.com.clinica.service.interfaces.SrvEvento;

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
