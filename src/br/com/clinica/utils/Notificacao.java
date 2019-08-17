package br.com.clinica.utils;

import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

public class Notificacao {

	public static void alertSucessVelho(){
		RequestContext.getCurrentInstance().execute("swal({title:\"Atenção\", text:\"Salvo com sucesso!\", type:\"success\",timer: 2000,showConfirmButton: false});");
	}
	public static void alertSucessNovo(){
		PrimeFaces.current().ajax().update("swal({title:\"Atenção\", text:\"Salvo com sucesso!\", type:\"success\",timer: 2000,showConfirmButton: false});");
	}
	
	
	public static void alertSucess(String text){
		RequestContext.getCurrentInstance().execute("swal({title:\"Atenção\", text:\"" + text + "\", type:\"success\",timer: 2000,showConfirmButton: false});");
	}	
	
	public static void alertError(String text){
		RequestContext.getCurrentInstance().execute("swal({title:\"Atenção\", text:\"" + text + "\", type:\"error\",timer: 2000,showConfirmButton: false});");
	}
	
	public static void alertErrorTempoMaior(String text){
		RequestContext.getCurrentInstance().execute("swal({title:\"Atenção\", text:\"" + text + "\", type:\"error\",timer: 4000,showConfirmButton: false});");
	}
	
	public static void closeDialog(String dialog){
		RequestContext.getCurrentInstance().execute("PF('" + dialog + "').hide();");
	}
	
	public static void openDialog(String dialog){
		RequestContext.getCurrentInstance().execute("PF('" + dialog + "').show();");
	}
	
	public static void updateForm(String form){
		RequestContext.getCurrentInstance().update(form);
	}
	
	public static void execute(String js){
		RequestContext.getCurrentInstance().execute(js);
	}
	
	public static void reloadPage(){
		RequestContext.getCurrentInstance().execute("location.reload();");
	}
}
