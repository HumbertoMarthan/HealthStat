package br.com.clinica.controller.geral;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.repository.interfaces.RepositoryFornecedor;
import br.com.clinica.service.interfaces.SrvFornecedor;

@Controller
public class FornecedorController extends ImplementacaoCrud<Fornecedor> implements 
	InterfaceCrud<Fornecedor>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvFornecedor srvFornecedor ;
	
	@Resource
	private RepositoryFornecedor repositoryFornecedor;
	
	public List<SelectItem> getListFornecedores() throws Exception {
		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Fornecedor> fornecedores = super.findListByQueryDinamica(" from Fornecedor");

		for (Fornecedor fornecedor : fornecedores) {
			list.add(new SelectItem(fornecedor,fornecedor.getNomeFornecedor()));
		}
		return list;
	}
	
}
