package br.com.clinica.utils;
 
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FloatUtils {

	public static String formataMoeda(double vlr){
		try {
			NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
			return moneyFormat.format( vlr );
		}catch(Exception e){
			return "***";
		}
	}

	public static String formataMoeda(String vlr) {
		try {
			double vl = Double.parseDouble(vlr);
			NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
			return moneyFormat.format(vl);
		} catch (Exception e) {
			return "***";
		}
	}
	public static String formataMoedaNrs(double vlr){
		try{
			NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
			return moneyFormat.format( vlr ).replace("R$", "");
		}catch(Exception e){
			return "***";
		}
	}
	public static String formataMoedaNrs(String vlr){
		try{
			double vl = Double.parseDouble(vlr);
			NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
			return moneyFormat.format( vl ).replace("R$", "");
		}catch(Exception e){
			return "***";
		}
	}
	
	
	public static double unFormatDecimal(String value) {
		
		if(value.indexOf(".") != -1 && value.indexOf(",") != -1){
			return Double.valueOf(value.replace(".", "").replace(",", ".").replace(" ", ""));
		}else{
			return Double.valueOf(value.replace(",", ".").replace(" ", ""));
		}
		 
	}
	
	
	public static String formatDecimal(double number) {
		DecimalFormat df2 = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(new Locale("pt","BR"))); 
		return df2.format(number);
	}
	
	public static String formatDecimal(Object number) {
		try{
			DecimalFormat df2 = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(new Locale("pt","BR"))); 
			return df2.format(number);
			
		}catch(Exception e){
			
			System.out.println("ErroFormat:" + number);
			return "0";
		}
	}
	
	public static String formatMilhar(Object number) {
		try{
			DecimalFormat decimal = new DecimalFormat ("#,###.###", new DecimalFormatSymbols (new Locale ("pt", "BR"))); 
			return decimal.format(number);
		}catch(Exception e){
			return String.valueOf(number);
		}
	}
	
	public static String formatPorc(Object number) {
		DecimalFormat decimal = new DecimalFormat ("#,###.##", new DecimalFormatSymbols (new Locale ("pt", "BR"))); 
		return decimal.format(number);
	}
	
	
}
