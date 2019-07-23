package br.com.projeto.util.all;

public class UtilitarioRegex {
	
	public String retiraAcentos(String string) {
		String aux = new String(string);
		aux = aux.replaceAll("[èëÈéêÉÊË]", "e");
		aux = aux.replaceAll("[ûùüúÛÚÙÜ]", "u");
		aux = aux.replaceAll("[ïîíìÏÎÍÌ]", "i");
		aux = aux.replaceAll("[àâáäãÁÀÂÄ]", "a");
		aux = aux.replaceAll("[óòôöõÓÒÔÖ]", "o");
		return aux;
	}
}
