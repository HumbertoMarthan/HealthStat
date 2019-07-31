package br.com.clinica.relatorio;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static String getDateAtualReportName() {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		return df.format(Calendar.getInstance().getTime());
	}
	
	public static String formatDateSql(Date data) {
		StringBuffer retorno = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		retorno.append("'");
		retorno.append(df.format(data));
		retorno.append("'");
		return retorno.toString();
	}
}
