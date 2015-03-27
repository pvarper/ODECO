package micrium.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import micrium.odeco.model.FormularioOdeco;
import micrium.odeco.model.ObjetoReporte2;
import micrium.user.controler.ControlParametro;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.log4j.Logger;

/**
 * @author mario
 * 
 */

@Named
public class PrintUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(PrintUtil.class);
	
	@Inject
	ControlParametro controlParametro;

	//public static final String PATH_REPORT = "C:/TigoTrabajo/ODECO DIGITAL/PROYECTO/COD/ODECO_DIGITAL/WebContent/resources/report/";

	public static final String EXTENSION_REPORT = ".jasper";

	public static final String EXTENSION_REPORT_IMAGE = ".jpg";// jpg, png

	//@PersistenceContext
	//private transient EntityManager entityManager;

	//@Resource
	//private transient UserTransaction transaction;

	/*
	 * public <E> void printPdf(String nameReport, Map<String, Object>
	 * parameters, List<E> beanCollection) { ByteArrayOutputStream outputStream
	 * = new ByteArrayOutputStream();
	 * 
	 * try { JasperPrint jasperPrint = createJasperPrint(nameReport, parameters,
	 * beanCollection); JRPdfExporter exporter = new JRPdfExporter();
	 * exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	 * exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
	 * exporter.exportReport(); } catch (JRException ex) {
	 * log.error("JRException ", ex); } catch (IOException e) {
	 * log.error("IOException ", e); } finally {
	 * IOUtils.closeQuietly(outputStream); } }
	 */

	/*
	 * public <E> void printExcel(String nameReport, Map<String, Object>
	 * parameters, List<E> beanCollection) throws Exception {
	 * ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); try {
	 * JasperPrint jasperPrint = createJasperPrint(nameReport, parameters,
	 * beanCollection); JRXlsxExporter exportador = new JRXlsxExporter();
	 * exportador.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	 * exportador.setParameter(JRXlsExporterParameter.OUTPUT_STREAM,
	 * outputStream);
	 * exportador.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
	 * Boolean.FALSE);
	 * exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
	 * Boolean.TRUE); exportador.setParameter(JRXlsExporterParameter.
	 * IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	 * exportador.setParameter
	 * (JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
	 * Boolean.TRUE); exportador.exportReport();
	 * 
	 * } catch (JRException ex) { log.error("JRException ", ex); throw new
	 * Exception("Error print excel", ex); } catch (IOException e) {
	 * log.error("IOException ", e); throw new Exception("Error print excel",
	 * e); } finally { IOUtils.closeQuietly(outputStream); } }
	 */

	/*
	 * public <E> boolean exportReportToPdfFile(String nameReport, String
	 * desFileName, Map<String, Object> parameters, List<E> beanCollection) {
	 * log.info("Exportando el reporte " + nameReport + " a pdf al directorio "
	 * + desFileName); boolean result = Boolean.FALSE;
	 * 
	 * try { JasperPrint jasperPrint = createJasperPrint(nameReport, parameters,
	 * beanCollection); JasperExportManager.exportReportToPdfFile(jasperPrint,
	 * desFileName);
	 * 
	 * if (FileUtil.exists(desFileName)) { log.info("El reporte " + nameReport +
	 * " se eporto a pdf satisfactoriamente al directorio " + desFileName);
	 * result = true; }
	 * 
	 * } catch (JRException ex) {
	 * log.error("Error al intentar exportar el reporte " + nameReport +
	 * " a pdf al directorio " + desFileName, ex); } catch (IOException e) {
	 * log.error("Error al intentar exportar el reporte " + nameReport +
	 * " a pdf al directorio " + desFileName, e); }
	 * 
	 * return result; }
	 */

	public <E> boolean exportReportToXlsFile(String nameReport,
			String desFileName, Map<String, Object> parameters) {
		log.info("Exportando el reporte " + nameReport
				+ " a xls al directorio " + desFileName);
		boolean result = Boolean.FALSE;
	//	File destFile = new File(desFileName + File.separator);
		if (exists(desFileName)) {
			delete(desFileName);
		}

		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, desFileName);
			/*
			 * JRXlsxExporter exportador = new JRXlsxExporter();
			 * exportador.setParameter(JRExporterParameter.JASPER_PRINT,
			 * jasperPrint);
			 * exportador.setParameter(JRXlsExporterParameter.OUTPUT_FILE,
			 * destFile); exportador.setParameter(JRXlsExporterParameter.
			 * IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			 * exportador.setParameter(
			 * JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			 * exportador.setParameter(JRXlsExporterParameter.
			 * IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			 * exportador.setParameter
			 * (JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
			 * Boolean.TRUE);
			 * //exportador.setParameter(JRXlsExporterParameter.PASSWORD,
			 * "aaa"); exportador.exportReport();
			 */

			if (exists(desFileName)) {
				log.info("El reporte " + nameReport
						+ " se eporto a xls satisfactoriamente al directorio "
						+ desFileName);
				result = true;
			}
			return result;

		} catch (JRException ex) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls al directorio " + desFileName, ex);
			return result;
		} catch (IOException e) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls al directorio " + desFileName, e);
			return result;
		} catch (Exception e) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls al directorio " + desFileName, e);
			return result;
		}

		
	}

	
	
	public <E> boolean exportReport(String nameReport, String name,
			Map<String, Object> parameters, List<FormularioOdeco> beanCollection) {
		log.info("Exportando el reporte " + nameReport
				+ " a xls ");
		boolean result = Boolean.FALSE;
		 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			for (FormularioOdeco e : beanCollection) {
				e.setNombreCiudad(e.getNombreCiudad());
				e.setFechaResolucionString(e.getFechaResolucionString());
				e.setFechaReclamoString(e.getFechaResolucionString());
				e.setMedioNotificacionDescripcion(e.getMedioNotificacionDescripcion());
				e.setRespuestaOdecoDescripcion(e.getRespuestaOdecoDescripcion());
				e.setFechaCerradoString(e.getFechaCerradoString());
				
			}
			JasperPrint jasperPrint = createJasperPrint2(nameReport, parameters,
					beanCollection);
			JRXlsxExporter exportador = new JRXlsxExporter();

			exportador.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			//exportador.setParameter(JRXlsExporterParameter.OUTPUT_FILE, destFile);
			exportador.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			// exportador.setParameter(JRXlsExporterParameter.PASSWORD, "aaa");
			exportador.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exportador.exportReport();

			if (showPrintPDF(outputStream,name)) {
				log.info("El reporte " + nameReport
						+ " se eporto a xls");
				result = true;
			}

		} catch (JRException ex) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls", ex);
			ex.printStackTrace();
		
		} catch (Exception e) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls ", e);
		}

		return result;
	}
	public <E> boolean exportReport2(String nameReport,String name,
			Map<String, Object> parameters, List<ObjetoReporte2> beanCollection) {
		
		 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		log.info("Exportando el reporte " + nameReport
				+ " a xls ");
		boolean result = Boolean.FALSE;
		//File destFile = new File(desFileName + File.separator);

		try {
			
			JasperPrint jasperPrint = createJasperPrint2(nameReport, parameters,
					beanCollection);
			JRXlsxExporter exportador = new JRXlsxExporter();

			exportador.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			//exportador.setParameter(JRXlsExporterParameter.OUTPUT_FILE, destFile);
			exportador.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exportador.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// exportador.setParameter(JRXlsExporterParameter.PASSWORD, "aaa");
			exportador.exportReport();
			
			if (showPrintPDF(outputStream,name)) {
				log.info("El reporte " + nameReport
						+ " se eporto a xls");
				result = true;
			}
				
		} catch (JRException ex) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls", ex);
			ex.printStackTrace();
		
		} catch (Exception e) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls ", e);
		}

		return result;
	}
	
	public <E> boolean exportReport3(String nameReport,
			String desFileName, Map<String, Object> parameters, Connection con) {
		log.info("Exportando el reporte " + nameReport
				+ " a xls al directorio " + desFileName);
		boolean result = Boolean.FALSE;
		File destFile = new File(desFileName + File.separator);
		if (exists(desFileName)) {
			delete(desFileName);
		}

		try {
			JasperPrint jasperPrint = createJasperPrint(nameReport, parameters, con);
			JRXlsxExporter exportador = new JRXlsxExporter();

			  exportador.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			  exportador.setParameter(JRXlsExporterParameter.OUTPUT_FILE,destFile);
			  exportador.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
              exportador.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			  exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			  exportador.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
			  exportador.exportReport();

			
			 

			if (exists(desFileName)) {
				log.info("El reporte " + nameReport
						+ " se eporto a xls satisfactoriamente al directorio "
						+ desFileName);
				result = true;
			}

		} catch (JRException ex) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls al directorio " + desFileName, ex);
		} catch (Exception e) {
			log.error("Error al intentar exportar el reporte " + nameReport
					+ " a xls al directorio " + desFileName, e);
		}

		return result;
	}

	/*
	 * public <E> String runReportDirect(String nameReport, Map<String, Object>
	 * parameters, List<E> beanCollection) { String result = null; try {
	 * JasperPrint jasperPrint = createJasperPrint(nameReport, parameters,
	 * beanCollection); JasperPrintManager.printReport(jasperPrint, false); }
	 * catch (JRException ex) { log.error("JRException ", ex); } catch
	 * (IOException e) { log.error("IOException ", e); } return result; }
	 */
	private boolean showPrintPDF(ByteArrayOutputStream outputStream, String nameReport){
		try {
			 FacesContext fcontext = FacesContext.getCurrentInstance();
		       ExternalContext externalContext = fcontext.getExternalContext();

		       HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		       response.setHeader("Content-disposition", "attachment; filename=\"" + nameReport + "\"");
		       response.setContentType("application/vnd.ms-excel");
		       response.setContentLength(outputStream.size());

		       outputStream.writeTo(response.getOutputStream());
		       fcontext.responseComplete();
		       return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	      
	   }
	

	
	private <E> JasperPrint createJasperPrint(String nameReport,
			Map<String, Object> parameters) throws JRException, IOException {

		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(controlParametro.getRutaReporte() + nameReport + EXTENSION_REPORT);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				parameters);

		return jasperPrint;
	}
	private <E> JasperPrint createJasperPrint(String nameReport,
			Map<String, Object> parameters, Connection con)  {
		
			System.out.println(parameters.get("fecha_inicial") + ","+parameters.get("fecha_final") );
			JasperReport jasperReport=null;
			JasperPrint jasperPrint=null;
			try {
				jasperReport = (JasperReport) JRLoader
						.loadObjectFromFile(controlParametro.getRutaReporte() + nameReport + EXTENSION_REPORT);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters,con);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			return jasperPrint;
		
		
	}


	private <E> JasperPrint createJasperPrint2(String nameReport,
			Map<String, Object> parameters, List<E> beanCollection)
			 {
		try {
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(controlParametro.getRutaReporte() + nameReport + EXTENSION_REPORT);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, new JRBeanCollectionDataSource(beanCollection));

			return jasperPrint;
		}catch (JRException ex) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public boolean exists(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	public boolean delete(String pathname) {
		File file = new File(pathname);
		return file.delete();
	}

	public boolean delete(List<String> lstFilesPath) {
		for (String pathname : lstFilesPath) {
			if (!delete(pathname)) {
				return false;
			}
		}
		return true;
	}
}
