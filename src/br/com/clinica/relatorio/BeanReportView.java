package br.com.clinica.relatorio;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.clinica.utils.BeanViewAbstract;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public abstract class BeanReportView extends BeanViewAbstract {

	private static final long serialVersionUID = 1L;

	/*
	 * public JasperPrint imprimir( List<?> lista,String
	 * relatorioCompilado,Map<String, Object> parameters ) { try { String caminho =
	 * "C:\\Users\\Humberto\\Documents\\workspace-spring-clinica\\clinica\\src\\relatorio\\"
	 * +relatorioCompilado;
	 * 
	 * JRBeanCollectionDataSource collection = new
	 * JRBeanCollectionDataSource(lista);
	 * 
	 * JasperPrint relatorio = JasperFillManager.fillReport(caminho, parameters,
	 * collection);
	 * 
	 * //JasperPrintManager.printReport(relatorio, true); return relatorio; } catch
	 * (JRException erro) { erro.printStackTrace(); } catch (Exception e) {
	 * e.printStackTrace(); } return null;
	 * 
	 * //JasperExportManager.exportReportToPdfFile(
	 * "C:\\Users\\Humberto\\Documents\\workspace-spring-clinica\\clinica\\src\\relatorio\\convenio.jrprint"
	 * );
	 * 
	 * }
	 */
	
	public JasperPrint imprimir(List<?> lista, String relatorioSemCompila)
	{
		String caminho = "C:\\Users\\Humberto\\Documents\\clinicaH\\clinica\\clinica\\src\\relatorio\\";
		//String caminho = "C:\\Users\\Humberto\\Documents\\workspace-spring-clinica\\clinica\\src\\relatorio\\";
		
		JasperReport report;
		JasperPrint print = null;
		try {
			report = JasperCompileManager.compileReport(caminho + relatorioSemCompila);
			print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(lista));
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		return print;
	}
	
	/*
	 * protected StreamedContent arquivoReport; protected int tipoRelatorio;
	 * protected List<?> listDataBeanCollectionReport; protected HashMap<Object,
	 * Object> parametrosRelatorio; protected String nomeRelatorioJasper =
	 * "default"; protected String nomeRelatorioSaida = "default";
	 * 
	 * @Resource private ReportUtil reportUtil;
	 * 
	 * @SuppressWarnings("rawtypes") public BeanReportView() { parametrosRelatorio =
	 * new HashMap<Object, Object>(); listDataBeanCollectionReport = new
	 * ArrayList(); }
	 * 
	 * public ReportUtil getReportUtil() { return reportUtil; }
	 * 
	 * public void setReportUtil(ReportUtil reportUtil) { this.reportUtil =
	 * reportUtil; }
	 * 
	 * public StreamedContent getArquivoReport() throws Exception { return
	 * getReportUtil().geraRelatorio(getListDataBeanCollectionReport(),
	 * getParametrosRelatorio(), getNomeRelatorioJasper(), getNomeRelatorioSaida(),
	 * getTipoRelatorio()); }
	 * 
	 * public int getTipoRelatorio() { return tipoRelatorio; }
	 * 
	 * public void setTipoRelatorio(int tipoRelatorio) { this.tipoRelatorio =
	 * tipoRelatorio; }
	 * 
	 * public List<?> getListDataBeanCollectionReport() { return
	 * listDataBeanCollectionReport; }
	 * 
	 * public void setListDataBeanCollectionReport(List<?>
	 * listDataBeanCollectionReport) { this.listDataBeanCollectionReport =
	 * listDataBeanCollectionReport; }
	 * 
	 * public HashMap<Object, Object> getParametrosRelatorio() { return
	 * parametrosRelatorio; }
	 * 
	 * public void setParametrosRelatorio(HashMap<Object, Object>
	 * parametroRelatorio) { this.parametrosRelatorio = parametroRelatorio; }
	 * 
	 * public String getNomeRelatorioJasper() { return nomeRelatorioJasper; }
	 * 
	 * public void setNomeRelatorioJasper(String nomeRelatorioJasper) { if
	 * (nomeRelatorioJasper == null || nomeRelatorioJasper.isEmpty()) {
	 * nomeRelatorioJasper = "default"; } this.nomeRelatorioJasper =
	 * nomeRelatorioJasper; }
	 * 
	 * public String getNomeRelatorioSaida() { return nomeRelatorioSaida; }
	 * 
	 * public void setNomeRelatorioSaida(String nomeRelatorioSaida) { if
	 * (nomeRelatorioSaida == null || nomeRelatorioSaida.isEmpty()) {
	 * nomeRelatorioSaida = "default"; }
	 * 
	 * this.nomeRelatorioSaida = nomeRelatorioSaida; }
	 */
	
	
	

}
