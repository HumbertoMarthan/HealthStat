package br.com.projeto.geral.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Medico;
import br.com.repository.interfaces.RepositoryMedico;
import br.com.srv.interfaces.SrvMedico;

@Controller
public class MedicoController extends ImplementacaoCrud<Medico> implements 
	InterfaceCrud<Medico>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvMedico srvMedico ;
	
	@Resource
	private RepositoryMedico repositoryMedico;
	
	public List<SelectItem> getListMedicos() throws Exception {
		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Medico> medicos = super.findListByQueryDinamica(" from Medico");

		for (Medico medico : medicos) {
			list.add(new SelectItem(medico, medico.getPessoa().getPessoaNome()));
		}
		return list;
	}
	
	
	
}
