
  package br.com.projeto.geral.controller;
  
  import org.springframework.stereotype.Controller;
  
  import br.com.framework.implementacao.crud.ImplementacaoCrud; import
  br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Mensagem;
  
  @Controller public class MensagemController extends
  ImplementacaoCrud<Mensagem> implements InterfaceCrud<Mensagem>{
  
  private static final long serialVersionUID = -5111511724656572882L;
  
  @Override 
  
 public Mensagem merge(Mensagem obj) throws Exception { return
		 super.merge(obj); }
  
  }
 