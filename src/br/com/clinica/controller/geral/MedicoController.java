package br.com.clinica.controller.geral;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.repository.interfaces.RepositoryMedico;
import br.com.clinica.service.interfaces.SrvMedico;

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
