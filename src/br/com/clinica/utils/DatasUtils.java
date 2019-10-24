package br.com.clinica.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DatasUtils {
	
	public static String getDataExtenso(Date hoje){
		DateFormat dfmt = new SimpleDateFormat("d 'de' MMMM 'de' yyyy",new Locale("pt","BR"));  
	    return dfmt.format(hoje);
	}
	
	public static String getDataExtenso(){
		Date data =  new Date();
		Locale local = new Locale("pt","BR");
		DateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy",local);
		return formato.format(data);
	}
	
	public static String getMesExtenso(Date hoje){
		DateFormat dfmt = new SimpleDateFormat("MMMM",new Locale("pt","BR"));  
	    return dfmt.format(hoje);
	  }
	
	public static String getMesExtensoAbreviado(Date hoje){
		System.out.println(hoje);
		DateFormat dfmt = new SimpleDateFormat("MMMM",new Locale("pt","BR"));  
	    return dfmt.format(hoje).substring(0, 3);
	  }

	public static String diaDaSemana(){  

	        int dia_semana;  
	  
	        Calendar data = Calendar.getInstance();  
	        dia_semana = data.get(Calendar.DAY_OF_WEEK);  
	  
	        switch (dia_semana) {  
	            case 1: return "Domingo";    
	            case 2: return "Segunda-feira";  
	            case 3: return "Ter&ccedil;a-feira";  
	            case 4: return "Quarta-feira";  
	            case 5: return "Quinta-feira";  
	            case 6: return "Sexta-feira";  
	            case 7: return "S&aacute;bado";  
	        }  
	 return "Err!";
	}
	
	public static String diaData(){  
		Calendar calendar = Calendar.getInstance();
		if((calendar.get(Calendar.MONTH)+1) > 9){
			return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);
		}else{
			return calendar.get(Calendar.DAY_OF_MONTH) + "/0" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);
		}
	}
	public static String horaNow(){  
		Calendar calendar = Calendar.getInstance();
		String retur = "";
		
		if(calendar.get(Calendar.HOUR_OF_DAY) > 9){
			retur = calendar.get(Calendar.HOUR_OF_DAY) + ":";
		}else{
			retur = "0" + calendar.get(Calendar.HOUR_OF_DAY) + ":";
		}
		
		if(calendar.get(Calendar.MINUTE) > 9){
			retur = retur + calendar.get(Calendar.MINUTE);
		}else{
			retur = retur + "0" + calendar.get(Calendar.MINUTE);
		}
		
		return retur;
	}
	
	public static String diaNow(){  
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);//+ " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE)
	}
	
	public static String diaHoraNow(){  
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
	}
	
	public static Date formatDate(String dataStr){
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  //Jan 6, 2012 12:00:00 AM
		java.util.Date data;
		
		try {
			data = new Date(format.parse(dataStr).getTime());
			return data;
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	public static String formatDateLong(Date data){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}


	public static String formatDate(Date data){
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatDia(Date data){
		SimpleDateFormat format = new SimpleDateFormat("dd");  
		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatDateTime(Date data){
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  

		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatDateTimeSql(Date data){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  

		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatDateSql(Date data){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  

		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatTime(Date data){
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");  

		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatDateStr(String dataStr){
		
		SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd");  //Jan 6, 2012 12:00:00 AM
		SimpleDateFormat formatOut = new SimpleDateFormat("dd/MM/yyyy");  //Jan 6, 2012 12:00:00 AM
		
		java.util.Date data;
		
		try {
			data = new Date(formatIn.parse(dataStr).getTime());
			return formatOut.format(data);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	
	public static String formatDateSQL(String dataStr){
		
		SimpleDateFormat formatIn = new SimpleDateFormat("dd/MM/yyyy");  //Jan 6, 2012 12:00:00 AM
		SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd");  //Jan 6, 2012 12:00:00 AM
		
		java.util.Date data;
		
		try {
			data = new Date(formatIn.parse(dataStr).getTime());
			return formatOut.format(data);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String formatDateDb(Date data){
		SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd");  //Jan 6, 2012 12:00:00 AM
		return formatOut.format(data);
	}
	
	public static Date getFirstDay()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
	
	public static Date getLastDay()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
	
	public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
	
	public static Date addMonth(Date date, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month); //minus number would decrement the days
        return cal.getTime();
    }
	
	public static String getHoje(){
        SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");  
        return sdf.format(new Date());
	}
	
	public static int getDia(){
		GregorianCalendar calendar = new GregorianCalendar();
		int dia = calendar.get(GregorianCalendar.DAY_OF_MONTH);
		return dia;
	}
	
	public static int diferencaEntreDatas(String data1, String data2) throws ParseException{  
        GregorianCalendar ini = new GregorianCalendar();  
        GregorianCalendar fim = new GregorianCalendar();  
        SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");  
        ini.setTime(sdf.parse(data1));  
        fim.setTime(sdf.parse(data2));  
        long dt1 = ini.getTimeInMillis();  
        long dt2 = fim.getTimeInMillis();  
        return (int) (((dt2 - dt1) / 86400000)+1);  
    }
	
	
	public static int diferencaEntreDatasDias(String data1, String data2) throws ParseException{  
        try {
        	GregorianCalendar ini = new GregorianCalendar();  
            GregorianCalendar fim = new GregorianCalendar();  
            SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");  
            ini.setTime(sdf.parse(data1));  
            fim.setTime(sdf.parse(data2));  
            long dt1 = ini.getTimeInMillis();  
            long dt2 = fim.getTimeInMillis();  
            return (int) (((dt2 - dt1) / 86400000)+1);  
		} catch (Exception e) {
			return 0;  
		} 
    } 
	
	public static int diferencaEntreDatas(Date data1, Date data2) throws ParseException{  
		try {  
		long tempo1 = data1.getTime();  
        long tempo2 = data2.getTime();  
        long difTempo = tempo2 - tempo1;  
        return (int) ((difTempo + 60L * 60 * 1000) / (24L * 60 * 60 * 1000)) + 1; 
		} catch (Exception e) {
			return 0;  
		} 
        
    }
	
//	public static int diferencaEntreDatas(Date data1, Date data2) throws ParseException{  
//        
//		long tempo1 = data1.getTime();  
//        long tempo2 = data2.getTime();  
//        long difTempo = tempo2 - tempo1;  
//        return (int) ((difTempo + 60L * 60 * 1000) / (24L * 60 * 60 * 1000)) + 1; 
//    } 
	
	public static String formatMesAno(Date data){
		SimpleDateFormat format = new SimpleDateFormat("MM/yy");  
		try {
			return format.format(data);
		} catch (Exception e) {
			
		} 
		return null;
	}
	
	public static String formatDateStrInOut(String dataStr, String dataFormatIn, String dataFormatOut){
		System.out.println("formatDateStrInOut: " + dataStr);
		
		try {
			dataStr = dataStr.replace("T", "").replace("Z", "");
			
			SimpleDateFormat formatIn = new SimpleDateFormat(dataFormatIn);  //Jan 6, 2012 12:00:00 AM
			SimpleDateFormat formatOut = new SimpleDateFormat(dataFormatOut);  //Jan 6, 2012 12:00:00 AM
			
			java.util.Date data;
		
			data = new Date(formatIn.parse(dataStr).getTime());
			return formatOut.format(data);
		
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "NAO INFORMADO";
	}
	
	
	public static boolean comparaData(Date dataA, Date dataB) {
		try {
			long longA = Long.parseLong(formatDateLong(dataA));
			long longB = Long.parseLong(formatDateLong(dataB));
			
			return longA>longB;
			
		}catch(Exception e){
			return false;
		}
	}
}

