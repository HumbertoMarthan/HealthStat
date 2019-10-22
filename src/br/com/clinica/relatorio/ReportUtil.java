package br.com.clinica.relatorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;


@SuppressWarnings("deprecation")
@Component
public class ReportUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final String UNDERLINE = "_";
	private static final String FOLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String EXTENSIO_ODS= "ods";
	private static final String EXTENSIO_XLS= "xls";
	private static final String EXTENSIO_HTML = "html";
	private static final String EXTENSIO_PDF = "pdf";
	
	private String SEPARATOR = File.separator;
	
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL= 2 ;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFFICE  = 4;
	
	private static final String PONTO = ".";
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = null;
	@SuppressWarnings("rawtypes")
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private String caminhoSubreport_dir = "";
	private File arquivoGerado = null;
	
	
	
	
	
	/*Cria a lista DataSource de beans que carregam os dados para o relatório*/
	@SuppressWarnings("unchecked")
	public StreamedContent geraRelatorio(List<?> listDataBeanColletionReport,  @SuppressWarnings("rawtypes") 
	HashMap parametrosRelatorio,
			String nomeRelatorioJasper, String nomeRelatorioSaida, int tipoRelatorio ) throws Exception{
		
		/*Cria a lista de collectionDataSource de beans que carregam os dados para o relatório*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanColletionReport);
		
		/*
		 * Fornece o caminho fisico até a pasta de contém os relatórios compilados .jasper
		 * */
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
		
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
		
		String caminhoRelatorio = scontext.getRealPath(FOLDER_RELATORIOS);
		
		// EX: -> c:/aplicação/relatorios/rel_bairo.jasper
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");
		
		if(caminhoRelatorio == null 
				|| (caminhoRelatorio != null && caminhoRelatorio.isEmpty())
				|| !file.exists()) {
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();
			SEPARATOR = "";
			
		}
			
		
		/*CAMINHO PARA IMAGENS*/
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);
		
		/*CAMINHO COMPLETO ATÉ O RELATÓRIO COMPILADO INDICADO*/
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper";
		
		/*FAZ O CARREGAMENTO DO RELATORIO INDICADO*/
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);
		
		/*SETA PARAMENTRO SUBREPORT_DIR COMO CAMINHO FISICO PARA SUB-REPORTS*/
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR; 
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);
		
		/*Carrega o arquivo compilado para memoria*/
		JasperPrint impressoraJasper = JasperFillManager.
				fillReport(relatorioJasper, parametrosRelatorio, jrbcds);
		
		switch (tipoRelatorio) {
		case RELATORIO_PDF: 
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSIO_PDF;
			break;
		case RELATORIO_HTML: 
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSIO_HTML;
			break;
		case RELATORIO_EXCEL: 
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSIO_XLS;
			break;
		case RELATORIO_PLANILHA_OPEN_OFFICE: 
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTENSIO_ODS;
			break;
			
			
			default:
				tipoArquivoExportado = new JRPdfExporter();
				extensaoArquivoExportado = EXTENSIO_PDF;
				break;
		}
		
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();
	
		/*CAMINHO RELATORIO EXPORTADO*/
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida + PONTO + extensaoArquivoExportado;
		
		
		/*CRIA NOVO FILE EXPORTADO*/
		arquivoGerado = new File(caminhoArquivoRelatorio);
		
		/*Prepara a impressão*/
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
		
		/*Nome do arquivo fisico a ser impresso/exportado*/
		
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/*executa a exportação*/
		tipoArquivoExportado.exportReport();
		
		/*REMOVE O ARQUIVO DE SERVIDOR APOS SER FEITO DOWNLOAD PELO USUARIO*/
		arquivoGerado.deleteOnExit();
		
		/*CRIAR O INPUTSTREAM PARA SER USADO PELO PRIMEFACES*/
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/*FAZ O RETORNO PARA A APLICAÇÃO*/
		
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio,"application/"+extensaoArquivoExportado,
				nomeRelatorioSaida + PONTO+extensaoArquivoExportado);
		
		return arquivoRetorno;
	}
		




}
