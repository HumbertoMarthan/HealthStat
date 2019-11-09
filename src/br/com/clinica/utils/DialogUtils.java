package br.com.clinica.utils;

import java.util.Calendar;
import java.util.Date;

import org.primefaces.context.RequestContext;

public class DialogUtils {
	public static void openDialog(String dialog){
		RequestContext.getCurrentInstance().execute("PF('" + dialog + "').show();");
	}
	
	public static void updateForm(String form){
		RequestContext.getCurrentInstance().update(form);
	}
	
	public static void closeDialog(String dialog){
		RequestContext.getCurrentInstance().execute("PF('" + dialog + "').hide();");
	}
	
	public static void execute(String js){
		RequestContext.getCurrentInstance().execute(js);
	}
	
}
