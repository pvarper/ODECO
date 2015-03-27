package micrium.odeco.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import micrium.odeco.business.CiudadBL;
import micrium.odeco.business.FlujoBL;
import micrium.odeco.business.FormularioOdecoBL;
import micrium.odeco.business.MotivoReclamoBL;
import micrium.odeco.business.ObjetoReclamoBL;
import micrium.odeco.model.Ciudad;
import micrium.odeco.model.Flujo;
import micrium.odeco.model.FormularioOdeco;
import micrium.odeco.model.Matriz;
import micrium.odeco.model.MotivoReclamo;
import micrium.odeco.model.ObjetoColumna;
import micrium.odeco.model.ObjetoFila;
import micrium.odeco.model.ObjetoMatriz;
import micrium.odeco.model.ObjetoReclamo;
import micrium.odeco.model.ObjetoReporte2;
import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.controler.ControlParametro;
import micrium.user.model.Rol;
import micrium.user.model.Usuario;
import micrium.user.view.ControlerBitacora;
import micrium.util.DescriptorBitacora;
import micrium.util.PrintUtil;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.util.IOUtils;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class ReportesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ReportesBean.class);

	@Inject
	private FormularioOdecoBL ofertaN;

	@Inject
	private ControlParametro controlParametro;

	@Inject
	private ObjetoReclamoBL objetoReclamoBL;

	@Inject
	private MotivoReclamoBL motivoReclamoBL;
	@Inject
	private FlujoBL flujoBL;
	@Inject
	private RoleBL rolBL;
	@Inject
	private ControlerBitacora controlerBitacora;
	
	@Inject
	private CiudadBL ciudadBL;
	@Inject
	private UsuarioBL usuarioBL;
	@Inject
	PrintUtil printUtils;

	private FormularioOdeco oferta;

	private String idOfertaStr;

	private String strIdUs = "";

	private boolean edit;

	private List<FormularioOdeco> listaOferta = new ArrayList<FormularioOdeco>();

	private List<ObjetoReporte2> listaReporte2 = new ArrayList<ObjetoReporte2>();

	private List<FormularioOdeco> listaReporteFiltro = new ArrayList<FormularioOdeco>();

	private List<FormularioOdeco> listaOfertaN = new ArrayList<FormularioOdeco>();

	private String listaObjetos = "";
	private String listaMotivos = "";

	private Date fechaInicialBusqueda;
	private Date fechaFinalBusqueda;

	private Matriz matriz = new Matriz();

	private ObjetoMatriz objetoMatriz = new ObjetoMatriz();
	
	private List<SelectItem> selectCiudades;
	private String selectCiudad;
	private List<SelectItem> selectCoordinadores;
	private String selectCoordinador;
	
	private SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");

	//private List<ObjetoReclamo> listaObj = new ArrayList<ObjetoReclamo>();
	
	private List<Matriz> listaMatrices = new ArrayList<Matriz>();

	@PostConstruct
	public void init() {
		log.info("********* Iniciando Vista Reportes de ODECO **********");
		try {
			// newObject();
			selectCiudad = "-1";
			 fillListasSeleccionables();
			// System.out.println("Devolvato: " + this.oferta);
			// cargarLista();
			
			cargarTablaReporte3();
			cargarTablaReporte4();

		} catch (Exception ex) {
			log.debug("[init] Hubo un error a la hora de crear un nuevo objeto "
					+ ex.getMessage());
		}

	}

	private void fillListasSeleccionables() {
		log.debug("[fillListasSelecionables] cargando datos");
		selectCiudades = new ArrayList<SelectItem>();
		selectCiudades.add(new SelectItem("-1", "Ciudades"));
		List<Ciudad> list = null;
		try {
			list = ciudadBL.getList();

			for (Ciudad item : list) {
				SelectItem s = new SelectItem(item.getCiudadId(),
						item.getNombre());
				selectCiudades.add(s);				
			}
			log.debug("Carga de Ciudades [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Ciudades [fail]", e);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							micrium.util.Parameter.MENSAJE_LISTACIUDADES_ERROR));
		}
		
		selectCoordinadores = new ArrayList<SelectItem>();
		selectCoordinadores.add(new SelectItem("-1", "Coordinadores"));
		List<Usuario> listU = null;
		try {
			Flujo flujo=flujoBL.getFlujoNombre("flujo");
			Rol rol=rolBL.getname(flujo.getCoordinador());
			listU = usuarioBL.getUsersRol(rol.getRolId().toString());

			for (Usuario item : listU) {
				SelectItem s = new SelectItem(item.getUsuarioId(),
						item.getLogin());
				selectCoordinadores.add(s);				
			}
			log.debug("Carga de Coordinadores [ok]");
		} catch (Exception e) {
			log.error("Carga de Lista de Coordinadores [fail]", e);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Error al Obtener la lista de Coordinadores Responsables"));
		}
	}
	
	/*private java.sql.Connection getConexion() throws Exception {
		Connection con;
		try {
			con = getEdbDS().getConnection();
		} catch (SQLException ejbe) {
			throw new EJBException((new StringBuilder())
					.append("No se pudo conectar ").append(ejbe.getMessage())
					.toString());
		} catch (NamingException ejbe) {
			throw new EJBException((new StringBuilder())
					.append("No se pudo conectar ").append(ejbe.getMessage())
					.toString());
		}
		return con;
	}*/

	/*private DataSource getEdbDS() throws NamingException {
		Context c = new InitialContext();
		return (DataSource) c.lookup("java:/odecoDigitalDS");
	}*/

	public void cargarTablaReporte3(){
		log.debug("[cargarTablaReporte3] se va cargar la tabla de reporte 3");
		try {
			List<MotivoReclamo> lst = motivoReclamoBL.getMotivoReclamosList();
			List<ObjetoColumna> lstObjetos = new ArrayList<ObjetoColumna>();

			int cont = 0;
			for (MotivoReclamo motivoReclamo : lst) {
				ObjetoColumna objetoColumna = new ObjetoColumna();
				objetoColumna.setIndice(cont);
				objetoColumna.setMotivoReclamo(motivoReclamo.getMotivoReclamo());
				lstObjetos.add(objetoColumna);
				cont = cont + 1;
			}
			
			matriz.setColumnas(lstObjetos);
			log.debug("[cargarTablaReporte3] se cargo la tabla 3 correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[cargarTablaReporte3] error al cargar la tabla de reporte 3",e);
		}
		

	}

	public void cargarTablaReporte4() throws Exception {

		log.debug("[cargarTablaReporte4] se va cargar la tabla de reporte 4");
		try {
			List<ObjetoReclamo> lstO = objetoReclamoBL.getObjetosReclamosList();
			List<ObjetoColumna> lstObjetosO = new ArrayList<ObjetoColumna>();
			List<Matriz> lstMatriz = new ArrayList<Matriz>();

			int c = 0;
			for (ObjetoReclamo objetoReclamo : lstO) {
				ObjetoColumna objetoColumna = new ObjetoColumna();
				objetoColumna.setIndice(c);
				objetoColumna.setId(objetoReclamo.getObjetoReclamoId());
				objetoColumna.setMotivoReclamo(objetoReclamo.getObjetoReclamo());
				lstObjetosO.add(objetoColumna);
				c = c + 1;

				List<MotivoReclamo> lstM = motivoReclamoBL.getMotivoReclamosList();
				List<ObjetoColumna> lstObjetosM = new ArrayList<ObjetoColumna>();

				Matriz a = new Matriz();
				int cont = 0;
				for (MotivoReclamo motivoReclamo : lstM) {
					ObjetoColumna objetoColumnaO = new ObjetoColumna();
					objetoColumnaO.setIndice(cont);
					objetoColumnaO.setMotivoReclamo(motivoReclamo
							.getMotivoReclamo());
					lstObjetosM.add(objetoColumnaO);
					cont = cont + 1;
				}
				a.setColumnas(lstObjetosM);
				lstMatriz.add(a);

			}

			objetoMatriz.setColumnas(lstObjetosO);
			objetoMatriz.setFilas(lstMatriz);
			log.debug("[cargarTablaReporte4] se cargo la tabla 3 correctamente");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[cargarTablaReporte4] error al cargar la tabla de reporte 4",e);
		}
		

	}

	public void paginar() {

		listaOferta = new ArrayList<FormularioOdeco>();
		// listaReporteFiltro=new ArrayList<FormularioOdeco>();

		if (fechaInicialBusqueda == null || fechaFinalBusqueda == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Colocar Fechas de Busqueda"));

			return;
		}
		
		log.debug("[paginar] se va realizar la busqueda en el reporte 1");
		try {
			listaOfertaN = ofertaN.listarTodo();
			if (listaOfertaN.isEmpty()) {
				/*
				 * FacesContext.getCurrentInstance().addMessage( null, new
				 * FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", ""));
				 */

				return;
			}
			for (FormularioOdeco f : listaOfertaN) {

				Date fechaR = new Date(f.getFechaReclamo().getTime());
				fechaR=formateador.parse(formateador.format(fechaR));
				if ((fechaInicialBusqueda.getTime()<=fechaR.getTime())
						&& fechaR.getTime()<=fechaFinalBusqueda.getTime()) {
					List<Object[]> lo = objetoReclamoBL
							.getObjetosReclamosList(f.getFormularioOdecoId());

					for (int i = 0; i < lo.size(); i++) {

						ObjetoReclamo obj = objetoReclamoBL
								.getObjetoReclamo(Integer.valueOf(lo.get(i)[0]
										.toString()));
						listaObjetos += obj.getObjetoReclamo() + ",";

					}
					f.setListaObjetos(listaObjetos);

					lo = motivoReclamoBL.getMotivoReclamosList(f
							.getFormularioOdecoId());

					for (int i = 0; i < lo.size(); i++) {

						MotivoReclamo obj = motivoReclamoBL
								.getMotivoReclamo(Integer.valueOf(lo.get(i)[0]
										.toString()));
						listaMotivos += obj.getMotivoReclamo() + ",";

					}
					f.setListaMotivos(listaMotivos);
					// listaReporteFiltro.add(f);
					listaOferta.add(f);
					setListaOferta(listaOferta);
					// setListaReporteFiltro(listaReporteFiltro);

				}

				listaMotivos = "";
				listaObjetos = "";
			}
			listaReporteFiltro = listaOferta;
			log.debug("[paginar] se realizo la busqueda en el reporte 1");
		} catch (ParseException e) {
			
			log.error("[Paginar] error al realizar la busqueda del reporte 1 " + e.getMessage(),e);
		}

	}

	public void paginar2() {

		// paginar();
		listaReporte2 = new ArrayList<ObjetoReporte2>();
		if (fechaInicialBusqueda == null || fechaFinalBusqueda == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Colocar Fechas de Busqueda"));

			return;
		}
		
		log.debug("[paginar2] se va realizar la busqueda en el reporte 2");
		
		//try {
			Timestamp fi = new Timestamp(fechaInicialBusqueda.getTime());
			Timestamp ff = new Timestamp(fechaFinalBusqueda.getTime());

			// System.out.println(ofertaN.reclamosAtendidos(fi, ff));

			ObjetoReporte2 reporteEne = new ObjetoReporte2();
			int ene = 0;
			ObjetoReporte2 reporteFeb = new ObjetoReporte2();
			int mar = 0;
			ObjetoReporte2 reporteMar = new ObjetoReporte2();
			int abr = 0;
			ObjetoReporte2 reporteAbr = new ObjetoReporte2();
			int may = 0;
			ObjetoReporte2 reporteMay = new ObjetoReporte2();
			int jun = 0;
			ObjetoReporte2 reporteJun = new ObjetoReporte2();
			int jul = 0;
			ObjetoReporte2 reporteJul = new ObjetoReporte2();
			int ago = 0;
			ObjetoReporte2 reporteAgo = new ObjetoReporte2();
			int sep = 0;
			ObjetoReporte2 reporteSep = new ObjetoReporte2();
			int oct = 0;
			ObjetoReporte2 reporteOct = new ObjetoReporte2();
			int nov = 0;
			ObjetoReporte2 reporteNov = new ObjetoReporte2();
			int dic = 0;
			ObjetoReporte2 reporteDic = new ObjetoReporte2();
			int feb = 0;
			List<Object[]> f = ofertaN.reclamosAtendidos(fi, ff);
			for (int i = 0; i < f.size(); i++) {
				Object fe = f.get(i)[1];
				Timestamp a = (Timestamp) fe;
				Date b = new Date(a.getTime());
				if (b.getMonth() == 0) {
					reporteEne.setMes("ENERO");
					ene++;
					reporteEne.setReclamosAtendidos(ene);

				}
				if (b.getMonth() == 1) {
					reporteFeb.setMes("FEBRERO");
					feb++;
					reporteFeb.setReclamosAtendidos(feb);

				}
				if (b.getMonth() == 2) {
					reporteMar.setMes("MARZO");
					mar++;
					reporteMar.setReclamosAtendidos(mar);

				}
				if (b.getMonth() == 3) {
					reporteAbr.setMes("ABRIL");
					abr++;
					reporteAbr.setReclamosAtendidos(abr);

				}
				if (b.getMonth() == 4) {
					reporteMay.setMes("MAYO");
					may++;
					reporteMay.setReclamosAtendidos(may);

				}
				if (b.getMonth() == 5) {
					reporteJun.setMes("JUNIO");
					jun++;
					reporteJun.setReclamosAtendidos(jun);

				}
				if (b.getMonth() == 6) {
					reporteJul.setMes("JULIO");
					jul++;
					reporteJul.setReclamosAtendidos(jul);

				}
				if (b.getMonth() == 7) {
					reporteAgo.setMes("AGOSTO");
					ago++;
					reporteAgo.setReclamosAtendidos(ago);

				}
				if (b.getMonth() == 8) {
					reporteSep.setMes("SEPTIEMBRE");
					sep++;
					reporteSep.setReclamosAtendidos(sep);

				}
				if (b.getMonth() == 9) {
					reporteOct.setMes("OCTUBRE");
					oct++;
					reporteOct.setReclamosAtendidos(oct);
				}
				if (b.getMonth() == 10) {
					reporteNov.setMes("NOVIEMBRE");
					nov++;
					reporteNov.setReclamosAtendidos(nov);
				}
				if (b.getMonth() == 11) {
					reporteDic.setMes("DICIEMBRE");
					dic++;
					reporteDic.setReclamosAtendidos(dic);
				}

			}
			ene = feb = mar = abr = may = jun = jul = ago = sep = oct = nov = dic = 0;
			f = ofertaN.reclamosPlazoEstablecido(fi, ff);
			for (int i = 0; i < f.size(); i++) {
				Object fe = f.get(i)[1];
				Timestamp a = (Timestamp) fe;
				Date b = new Date(a.getTime());
				if (b.getMonth() == 0) {
					reporteEne.setMes("ENERO");
					ene++;
					reporteEne.setResueltosPlazoEstablecidos(ene);

				}
				if (b.getMonth() == 1) {
					reporteFeb.setMes("FEBRERO");
					feb++;
					reporteFeb.setResueltosPlazoEstablecidos(feb);

				}
				if (b.getMonth() == 2) {
					reporteMar.setMes("MARZO");
					mar++;
					reporteMar.setResueltosPlazoEstablecidos(mar);

				}
				if (b.getMonth() == 3) {
					reporteAbr.setMes("ABRIL");
					abr++;
					reporteAbr.setResueltosPlazoEstablecidos(abr);

				}
				if (b.getMonth() == 4) {
					reporteMay.setMes("MAYO");
					may++;
					reporteMay.setResueltosPlazoEstablecidos(may);

				}
				if (b.getMonth() == 5) {
					reporteJun.setMes("JUNIO");
					jun++;
					reporteJun.setResueltosPlazoEstablecidos(jun);

				}
				if (b.getMonth() == 6) {
					reporteJul.setMes("JULIO");
					jul++;
					reporteJul.setResueltosPlazoEstablecidos(jul);

				}
				if (b.getMonth() == 7) {
					reporteAgo.setMes("AGOSTO");
					ago++;
					reporteAgo.setResueltosPlazoEstablecidos(ago);

				}
				if (b.getMonth() == 8) {
					reporteSep.setMes("SEPTIEMBRE");
					sep++;
					reporteSep.setResueltosPlazoEstablecidos(sep);

				}
				if (b.getMonth() == 9) {
					reporteOct.setMes("OCTUBRE");
					oct++;
					reporteOct.setResueltosPlazoEstablecidos(oct);
				}
				if (b.getMonth() == 10) {
					reporteNov.setMes("NOVIEMBRE");
					nov++;
					reporteNov.setResueltosPlazoEstablecidos(nov);
				}
				if (b.getMonth() == 11) {
					reporteDic.setMes("DICIEMBRE");
					dic++;
					reporteDic.setResueltosPlazoEstablecidos(dic);
				}

			}

			ene = feb = mar = abr = may = jun = jul = ago = sep = oct = nov = dic = 0;
			f = ofertaN.antiguedadPendientes(fi, ff);
			for (int i = 0; i < f.size(); i++) {
				Object fe = f.get(i)[1];
				Timestamp a = (Timestamp) fe;
				Date b = new Date(a.getTime());
				if (b.getMonth() == 0) {
					reporteEne.setMes("ENERO");
					ene++;
					reporteEne.setAntiguedadPendientes(ene);

				}
				if (b.getMonth() == 1) {
					reporteFeb.setMes("FEBRERO");
					feb++;
					reporteFeb.setAntiguedadPendientes(feb);

				}
				if (b.getMonth() == 2) {
					reporteMar.setMes("MARZO");
					mar++;
					reporteMar.setAntiguedadPendientes(mar);

				}
				if (b.getMonth() == 3) {
					reporteAbr.setMes("ABRIL");
					abr++;
					reporteAbr.setAntiguedadPendientes(abr);

				}
				if (b.getMonth() == 4) {
					reporteMay.setMes("MAYO");
					may++;
					reporteMay.setAntiguedadPendientes(may);

				}
				if (b.getMonth() == 5) {
					reporteJun.setMes("JUNIO");
					jun++;
					reporteJun.setAntiguedadPendientes(jun);

				}
				if (b.getMonth() == 6) {
					reporteJul.setMes("JULIO");
					jul++;
					reporteJul.setAntiguedadPendientes(jul);

				}
				if (b.getMonth() == 7) {
					reporteAgo.setMes("AGOSTO");
					ago++;
					reporteAgo.setAntiguedadPendientes(ago);

				}
				if (b.getMonth() == 8) {
					reporteSep.setMes("SEPTIEMBRE");
					sep++;
					reporteSep.setAntiguedadPendientes(sep);

				}
				if (b.getMonth() == 9) {
					reporteOct.setMes("OCTUBRE");
					oct++;
					reporteOct.setAntiguedadPendientes(oct);
				}
				if (b.getMonth() == 10) {
					reporteNov.setMes("NOVIEMBRE");
					nov++;
					reporteNov.setAntiguedadPendientes(nov);
				}
				if (b.getMonth() == 11) {
					reporteDic.setMes("DICIEMBRE");
					dic++;
					reporteDic.setAntiguedadPendientes(dic);
				}

			}
			ene = feb = mar = abr = may = jun = jul = ago = sep = oct = nov = dic = 0;
			f = ofertaN.reclamosPendientes(fi, ff);
			for (int i = 0; i < f.size(); i++) {
				Object fe = f.get(i)[1];
				Timestamp a = (Timestamp) fe;
				Date b = new Date(a.getTime());
				if (b.getMonth() == 0) {
					reporteEne.setMes("ENERO");
					ene++;
					reporteEne.setReclamosPendientes(ene);

				}
				if (b.getMonth() == 1) {
					reporteFeb.setMes("FEBRERO");
					feb++;
					reporteFeb.setReclamosPendientes(feb);

				}
				if (b.getMonth() == 2) {
					reporteMar.setMes("MARZO");
					mar++;
					reporteMar.setReclamosPendientes(mar);

				}
				if (b.getMonth() == 3) {
					reporteAbr.setMes("ABRIL");
					abr++;
					reporteAbr.setReclamosPendientes(abr);

				}
				if (b.getMonth() == 4) {
					reporteMay.setMes("MAYO");
					may++;
					reporteMay.setReclamosPendientes(may);

				}
				if (b.getMonth() == 5) {
					reporteJun.setMes("JUNIO");
					jun++;
					reporteJun.setReclamosPendientes(jun);

				}
				if (b.getMonth() == 6) {
					reporteJul.setMes("JULIO");
					jul++;
					reporteJul.setReclamosPendientes(jul);

				}
				if (b.getMonth() == 7) {
					reporteAgo.setMes("AGOSTO");
					ago++;
					reporteAgo.setReclamosPendientes(ago);

				}
				if (b.getMonth() == 8) {
					reporteSep.setMes("SEPTIEMBRE");
					sep++;
					reporteSep.setReclamosPendientes(sep);

				}
				if (b.getMonth() == 9) {
					reporteOct.setMes("OCTUBRE");
					oct++;
					reporteOct.setReclamosPendientes(oct);
				}
				if (b.getMonth() == 10) {
					reporteNov.setMes("NOVIEMBRE");
					nov++;
					reporteNov.setReclamosPendientes(nov);
				}
				if (b.getMonth() == 11) {
					reporteDic.setMes("DICIEMBRE");
					dic++;
					reporteDic.setReclamosPendientes(dic);
				}

			}
			ene = feb = mar = abr = may = jun = jul = ago = sep = oct = nov = dic = 0;
			f = ofertaN.reclamosAnulados(fi, ff);
			for (int i = 0; i < f.size(); i++) {
				Object fe = f.get(i)[1];
				Timestamp a = (Timestamp) fe;
				Date b = new Date(a.getTime());
				if (b.getMonth() == 0) {
					reporteEne.setMes("ENERO");
					ene++;
					reporteEne.setReclamosAnulados(ene);

				}
				if (b.getMonth() == 1) {
					reporteFeb.setMes("FEBRERO");
					feb++;
					reporteFeb.setReclamosAnulados(feb);

				}
				if (b.getMonth() == 2) {
					reporteMar.setMes("MARZO");
					mar++;
					reporteMar.setReclamosAnulados(mar);

				}
				if (b.getMonth() == 3) {
					reporteAbr.setMes("ABRIL");
					abr++;
					reporteAbr.setReclamosAnulados(abr);

				}
				if (b.getMonth() == 4) {
					reporteMay.setMes("MAYO");
					may++;
					reporteMay.setReclamosAnulados(may);

				}
				if (b.getMonth() == 5) {
					reporteJun.setMes("JUNIO");
					jun++;
					reporteJun.setReclamosAnulados(jun);

				}
				if (b.getMonth() == 6) {
					reporteJul.setMes("JULIO");
					jul++;
					reporteJul.setReclamosAnulados(jul);

				}
				if (b.getMonth() == 7) {
					reporteAgo.setMes("AGOSTO");
					ago++;
					reporteAgo.setReclamosAnulados(ago);

				}
				if (b.getMonth() == 8) {
					reporteSep.setMes("SEPTIEMBRE");
					sep++;
					reporteSep.setReclamosAnulados(sep);

				}
				if (b.getMonth() == 9) {
					reporteOct.setMes("OCTUBRE");
					oct++;
					reporteOct.setReclamosAnulados(oct);
				}
				if (b.getMonth() == 10) {
					reporteNov.setMes("NOVIEMBRE");
					nov++;
					reporteNov.setReclamosAnulados(nov);
				}
				if (b.getMonth() == 11) {
					reporteDic.setMes("DICIEMBRE");
					dic++;
					reporteDic.setReclamosAnulados(dic);
				}

			}
			ene = feb = mar = abr = may = jun = jul = ago = sep = oct = nov = dic = 0;
			f = ofertaN.reclamosAdministrativos(fi, ff);
			for (int i = 0; i < f.size(); i++) {
				Object fe = f.get(i)[1];
				Timestamp a = (Timestamp) fe;
				Date b = new Date(a.getTime());
				if (b.getMonth() == 0) {
					reporteEne.setMes("ENERO");
					ene++;
					reporteEne.setReclamosAdministrativos(ene);

				}
				if (b.getMonth() == 1) {
					reporteFeb.setMes("FEBRERO");
					feb++;
					reporteFeb.setReclamosAdministrativos(feb);

				}
				if (b.getMonth() == 2) {
					reporteMar.setMes("MARZO");
					mar++;
					reporteMar.setReclamosAdministrativos(mar);

				}
				if (b.getMonth() == 3) {
					reporteAbr.setMes("ABRIL");
					abr++;
					reporteAbr.setReclamosAdministrativos(abr);

				}
				if (b.getMonth() == 4) {
					reporteMay.setMes("MAYO");
					may++;
					reporteMay.setReclamosAdministrativos(may);

				}
				if (b.getMonth() == 5) {
					reporteJun.setMes("JUNIO");
					jun++;
					reporteJun.setReclamosAdministrativos(jun);

				}
				if (b.getMonth() == 6) {
					reporteJul.setMes("JULIO");
					jul++;
					reporteJul.setReclamosAdministrativos(jul);

				}
				if (b.getMonth() == 7) {
					reporteAgo.setMes("AGOSTO");
					ago++;
					reporteAgo.setReclamosAdministrativos(ago);

				}
				if (b.getMonth() == 8) {
					reporteSep.setMes("SEPTIEMBRE");
					sep++;
					reporteSep.setReclamosAdministrativos(sep);

				}
				if (b.getMonth() == 9) {
					reporteOct.setMes("OCTUBRE");
					oct++;
					reporteOct.setReclamosAdministrativos(oct);
				}
				if (b.getMonth() == 10) {
					reporteNov.setMes("NOVIEMBRE");
					nov++;
					reporteNov.setReclamosAdministrativos(nov);
				}
				if (b.getMonth() == 11) {
					reporteDic.setMes("DICIEMBRE");
					dic++;
					reporteDic.setReclamosAdministrativos(dic);
				}

			}

			/*
			 * reporte.setMes("ENERO");
			 * reporte.setReclamosAtendidos(ofertaN.reclamosAtendidos(fi,
			 * ff).size());
			 * reporte.setReclamosPendientes(ofertaN.reclamosPendientes(fi,
			 * ff).size()); reporte.setReclamosAnulados(ofertaN.reclamosAnulados(fi,
			 * ff).size());
			 * reporte.setReclamosAdministrativos(ofertaN.reclamosAdministrativos
			 * (fi, ff).size());
			 */
			if (reporteEne.getMes() != null) {
				listaReporte2.add(reporteEne);
			}

			if (reporteFeb.getMes() != null) {
				listaReporte2.add(reporteFeb);
			}
			if (reporteMar.getMes() != null) {
				listaReporte2.add(reporteMar);
			}
			if (reporteMay.getMes() != null) {
				listaReporte2.add(reporteMay);
			}
			if (reporteAbr.getMes() != null) {
				listaReporte2.add(reporteAbr);
			}
			if (reporteJun.getMes() != null) {
				listaReporte2.add(reporteJun);
			}
			if (reporteJul.getMes() != null) {
				listaReporte2.add(reporteJul);
			}
			if (reporteAgo.getMes() != null) {
				listaReporte2.add(reporteAgo);
			}
			if (reporteSep.getMes() != null) {
				listaReporte2.add(reporteSep);
			}
			if (reporteOct.getMes() != null) {
				listaReporte2.add(reporteOct);
			}
			if (reporteNov.getMes() != null) {
				listaReporte2.add(reporteNov);
			}
			if (reporteDic.getMes() != null) {
				listaReporte2.add(reporteDic);
			}
			log.debug("[paginar2] se realizo la busqueda en el reporte 2");

		/*} catch (Exception e) {
			
			log.error("[paginar2] error al realizar la busqueda el reporte 2");
		}*/
		
		
	}

	public void paginar3() {

		listaReporte2 = new ArrayList<ObjetoReporte2>();
		if (fechaInicialBusqueda == null || fechaFinalBusqueda == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Colocar Fechas de Busqueda"));

			return;
		}
		log.debug("[paginar3] se va realizar la busqueda en el reporte 3");
		Timestamp fi = new Timestamp(fechaInicialBusqueda.getTime());
		Timestamp ff = new Timestamp(fechaFinalBusqueda.getTime());
		ObjetoFila reporteEne = new ObjetoFila(), reporteFeb = new ObjetoFila(), reporteMar = new ObjetoFila(), reporteAbr = new ObjetoFila(), reporteMay = new ObjetoFila(), reporteJun = new ObjetoFila(), reporteJul = new ObjetoFila();
		ObjetoFila reporteAgo = new ObjetoFila(), reporteSep = new ObjetoFila(), reporteOct = new ObjetoFila(), reporteNov = new ObjetoFila(), reporteDic = new ObjetoFila();
		int[] eneV = new int[matriz.getColumnas().size()];
		int[] febV = new int[matriz.getColumnas().size()];
		int[] marV = new int[matriz.getColumnas().size()];
		int[] abrV = new int[matriz.getColumnas().size()];
		int[] mayV = new int[matriz.getColumnas().size()];
		int[] junV = new int[matriz.getColumnas().size()];
		int[] julV = new int[matriz.getColumnas().size()];
		int[] agoV = new int[matriz.getColumnas().size()];
		int[] sepV = new int[matriz.getColumnas().size()];
		int[] octV = new int[matriz.getColumnas().size()];
		int[] novV = new int[matriz.getColumnas().size()];
		int[] dicV = new int[matriz.getColumnas().size()];
		List<Object[]> f = ofertaN.reporte2(fi, ff);
		List<ObjetoFila> rr = new ArrayList<ObjetoFila>();

		
		for (int j = 0; j < f.size(); j++) {

			for (int i = 0; i < matriz.getColumnas().size(); i++) {

				if (f.get(j)[5].toString().equalsIgnoreCase("ENERO")) {
					reporteEne.setMes("ENERO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int ene = eneV[i];
						ene = ene + 1;
						eneV[i] = ene;

					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("FEBRERO")) {
					reporteFeb.setMes("FEBRERO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int feb = febV[i];
						feb = feb + 1;
						febV[i] = feb;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("MARZO")) {
					reporteMar.setMes("MARZO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int mar = marV[i];
						mar = mar + 1;
						marV[i] = mar;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("ABRIL")) {
					reporteAbr.setMes("ABRIL");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int abr = abrV[i];
						abr = abr + 1;
						abrV[i] = abr;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("MAYO")) {
					reporteMay.setMes("MAYO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int may = mayV[i];
						may = may + 1;
						mayV[i] = may;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("JUNIO")) {
					reporteJun.setMes("JUNIO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int jun = junV[i];
						jun = jun + 1;
						junV[i] = jun;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("JULIO")) {
					reporteJul.setMes("JULIO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int jul = julV[i];
						jul = jul + 1;
						julV[i] = jul;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("AGOSTO")) {
					reporteAgo.setMes("AGOSTO");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int ago = agoV[i];
						ago = ago + 1;
						agoV[i] = ago;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("SEPTIEMBRE")) {
					reporteSep.setMes("SEPTIEMBRE");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int sep = sepV[i];
						sep = sep + 1;
						sepV[i] = sep;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("OCTUBRE")) {
					reporteOct.setMes("OCTUBRE");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int oct = octV[i];
						oct = oct + 1;
						octV[i] = oct;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("NOVIEMBRE")) {
					reporteNov.setMes("NOVIEMBRE");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int nov = novV[i];
						nov = nov + 1;
						novV[i] = nov;
					} else {

					}

				}
				if (f.get(j)[5].toString().equalsIgnoreCase("DICIEMBRE")) {
					reporteDic.setMes("DICIEMBRE");
					if (matriz.getColumnas().get(i).getMotivoReclamo()
							.equalsIgnoreCase(f.get(j)[1].toString())) {
						int dic = dicV[i];
						dic = dic + 1;
						dicV[i] = dic;
					} else {

					}

				}
			}

		}
		if (reporteEne.getMes() != null) {
			reporteEne.setVector(eneV);
			rr.add(reporteEne);
		}
		if (reporteFeb.getMes() != null) {
			reporteFeb.setVector(febV);
			rr.add(reporteFeb);
		}
		if (reporteMar.getMes() != null) {
			reporteMar.setVector(marV);
			rr.add(reporteMar);
		}
		if (reporteAbr.getMes() != null) {
			reporteAbr.setVector(abrV);
			rr.add(reporteAbr);
		}
		if (reporteMay.getMes() != null) {
			reporteMay.setVector(mayV);
			rr.add(reporteMay);
		}
		if (reporteJun.getMes() != null) {
			reporteJun.setVector(junV);
			rr.add(reporteJun);
		}
		if (reporteJul.getMes() != null) {
			reporteJul.setVector(julV);
			rr.add(reporteJul);
		}
		if (reporteAgo.getMes() != null) {
			reporteAgo.setVector(agoV);
			rr.add(reporteAgo);
		}
		if (reporteSep.getMes() != null) {
			reporteSep.setVector(sepV);
			rr.add(reporteSep);
		}
		if (reporteOct.getMes() != null) {
			reporteOct.setVector(octV);
			rr.add(reporteOct);
		}
		if (reporteNov.getMes() != null) {
			reporteNov.setVector(novV);
			rr.add(reporteNov);
		}
		if (reporteDic.getMes() != null) {
			reporteDic.setVector(dicV);
			rr.add(reporteDic);
		}

		matriz.setFilas(rr);
		log.debug("[paginar3] se realizo la busqueda en el reporte 3");
	}

	public void paginar4() {
		if (fechaInicialBusqueda == null || fechaFinalBusqueda == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Colocar Fechas de Busqueda"));

			return;
		}
		log.debug("[paginar4] se va realizar la busqueda en el reporte 4");
		Timestamp fi = new Timestamp(fechaInicialBusqueda.getTime());
		Timestamp ff = new Timestamp(fechaFinalBusqueda.getTime());
		
		for (int i = 0; i < objetoMatriz.getColumnas().size(); i++) {

			int[] eneV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] febV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] marV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] abrV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] mayV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] junV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] julV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] agoV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] sepV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] octV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] novV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			int[] dicV = new int[objetoMatriz.getFilas().get(i).getColumnas()
					.size()];
			
			ObjetoFila reporteEne = new ObjetoFila(), reporteFeb = new ObjetoFila(), reporteMar = new ObjetoFila(), reporteAbr = new ObjetoFila(), reporteMay = new ObjetoFila(), reporteJun = new ObjetoFila(), reporteJul = new ObjetoFila();
			ObjetoFila reporteAgo = new ObjetoFila(), reporteSep = new ObjetoFila(), reporteOct = new ObjetoFila(), reporteNov = new ObjetoFila(), reporteDic = new ObjetoFila();

			List<Object[]> f = ofertaN.reporte4(fi, ff, objetoMatriz
					.getColumnas().get(i).getId());
			

			for (int k = 0; k < f.size(); k++) {

				for (int j = 0; j < objetoMatriz.getFilas().get(i)
						.getColumnas().size(); j++) {

					if (f.get(k)[6].toString().equalsIgnoreCase("ENERO")) {
						reporteEne.setMes("ENERO");
						// System.out.println(objetoMatriz.getFilas().get(i).getColumnas().get(j).getMotivoReclamo()+"="+f.get(k)[3].toString());
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int ene = eneV[j];
							ene = ene + 1;
							eneV[j] = ene;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("FEBRERO")) {
						reporteFeb.setMes("FEBRERO");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int feb = febV[j];
							feb = feb + 1;
							febV[j] = feb;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("MARZO")) {
						reporteMar.setMes("MARZO");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int mar = marV[j];
							mar = mar + 1;
							marV[j] = mar;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("ABRIL")) {
						reporteAbr.setMes("ABRIL");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int abr = abrV[j];
							abr = abr + 1;
							abrV[j] = abr;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("MAYO")) {
						reporteMay.setMes("MAYO");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int may = mayV[j];
							may = may + 1;
							mayV[j] = may;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("JUNIO")) {
						reporteJun.setMes("JUNIO");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int jun = junV[j];
							jun = jun + 1;
							junV[j] = jun;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("JULIO")) {
						reporteJul.setMes("JULIO");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int jul = julV[j];
							jul = jul + 1;
							julV[j] = jul;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("AGOSTO")) {
						reporteAgo.setMes("AGOSTO");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int ago = agoV[j];
							ago = ago + 1;
							agoV[j] = ago;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("SEPTIEMBRE")) {
						reporteSep.setMes("SEPTIEMBRE");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int sep = sepV[j];
							sep = sep + 1;
							sepV[j] = sep;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("OCTUBRE")) {
						reporteOct.setMes("OCTUBRE");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int oct = octV[j];
							oct = oct + 1;
							octV[j] = oct;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("NOVIEMBRE")) {
						reporteNov.setMes("NOVIEMBRE");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int nov = novV[j];
							nov = nov + 1;
							novV[j] = nov;
						} else {

						}

					}
					if (f.get(k)[6].toString().equalsIgnoreCase("DICIEMBRE")) {
						reporteDic.setMes("DICIEMBRE");
						if (objetoMatriz.getFilas().get(i).getColumnas().get(j)
								.getMotivoReclamo()
								.equalsIgnoreCase(f.get(k)[3].toString())) {
							int dic = dicV[j];
							dic = dic + 1;
							dicV[j] = dic;
						} else {

						}

					}

				}
			}
			List<ObjetoFila> rr = new ArrayList<ObjetoFila>();			
			if(reporteEne.getMes()!=null){
				reporteEne.setVector(eneV);
				rr.add(reporteEne);
			}
			if(reporteFeb.getMes()!=null){
				reporteFeb.setVector(febV);
				rr.add(reporteFeb);
			}
			if(reporteMar.getMes()!=null){
				reporteMar.setVector(marV);
				rr.add(reporteMar);
			}
			if(reporteAbr.getMes()!=null){
				reporteAbr.setVector(abrV);
				rr.add(reporteAbr);
			}
			if(reporteMay.getMes()!=null){
				reporteMay.setVector(mayV);
				rr.add(reporteMay);
			}
			if(reporteJun.getMes()!=null){
				reporteJun.setVector(junV);
				rr.add(reporteJun);
			}
			if(reporteJul.getMes()!=null){
				reporteJul.setVector(julV);
				rr.add(reporteJul);
			}
			if(reporteAgo.getMes()!=null){
				reporteAgo.setVector(agoV);
				rr.add(reporteAgo);
			}
			if(reporteSep.getMes()!=null){
				reporteSep.setVector(sepV);
				rr.add(reporteSep);
			}
			if(reporteOct.getMes()!=null){
				reporteOct.setVector(octV);
				rr.add(reporteOct);
			}
			if(reporteNov.getMes()!=null){
				reporteNov.setVector(novV);
				rr.add(reporteNov);
			}
			if(reporteDic.getMes()!=null){
				reporteDic.setVector(dicV);
				rr.add(reporteDic);
			}
			
			Matriz a = new Matriz();			
			a=objetoMatriz.getFilas().get(i);
			a.setFilas(rr);
			listaMatrices.add(a);			
		}
		
		objetoMatriz.setFilas(listaMatrices);
		log.debug("[paginar4] se realizo la busqueda en el reporte 4");
		
	}
	
	public void paginar5() {
		
		listaReporte2 = new ArrayList<ObjetoReporte2>();		
		if (fechaInicialBusqueda == null || fechaFinalBusqueda == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Colocar Fechas de Busqueda"));

			return;
		}
		if (selectCiudad.equals("-1")) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Seleccionar una Ciudad"));

			return;
		}

		log.debug("[paginar5] se va realizar la busqueda en el reporte 5");
		
		try {
			
			Timestamp fi = new Timestamp(fechaInicialBusqueda.getTime());
			Timestamp ff = new Timestamp(fechaFinalBusqueda.getTime());
			
			SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
			

			
			long cantF=ofertaN.cantidadFormularios(fi, ff, Integer.valueOf(selectCiudad));

			long cantAb=ofertaN.cantidadAbiertos(fi, ff, Integer.valueOf(selectCiudad));

			long cantC=ofertaN.cantidadCerrado(fi, ff, Integer.valueOf(selectCiudad));

			long cantAn=ofertaN.cantidadAnulado(fi, ff, Integer.valueOf(selectCiudad));
		
			
			ObjetoReporte2 reporte= new ObjetoReporte2();
			reporte.setMes(formatoDeFecha.format(fechaInicialBusqueda)+" - "+formatoDeFecha.format(fechaFinalBusqueda));
			reporte.setReclamosAtendidos((int)cantF);
			
			reporte.setResueltosPlazoEstablecidos((int)cantAb);
			reporte.setReclamosPendientes((int)cantC);
			reporte.setReclamosAnulados((int)cantAn);
			
			Ciudad ciudad=ciudadBL.getCiudad(Integer.valueOf(selectCiudad));
			reporte.setCiudad(ciudad.getNombre());
			
			listaReporte2.add(reporte);
			
			log.debug("[paginar5] se realizo la busqueda en el reporte 5");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[paginar5] error al realizar la busqueda en el reporte 5",e);
		}
		
		
		
	}
	
	public void paginar6() {
		
		listaOferta = new ArrayList<FormularioOdeco>();
		// listaReporteFiltro=new ArrayList<FormularioOdeco>();

		if (fechaInicialBusqueda == null || fechaFinalBusqueda == null) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Debe Colocar Fechas de Busqueda"));

			return;
		}
		if (selectCoordinador.equals("-1")) {
			 listaOfertaN = ofertaN.listarTodo();
		}else{
			Usuario us;
			try {
				us = usuarioBL.getUser(Integer.valueOf(selectCoordinador));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("[paginar6] error al obtener el coordinador",e);
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("[paginar6] error al obtener el coordinador",e);
				return;
			}
			listaOfertaN = ofertaN.getListaResponsable(us.getLogin());
		}
		
		log.debug("[paginar6] se va realizar la busqueda en el reporte 1");
		try {	
			if (listaOfertaN.isEmpty()) {
				/*
				 * FacesContext.getCurrentInstance().addMessage( null, new
				 * FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje", ""));
				 */

				return;
			}
			for (FormularioOdeco f : listaOfertaN) {

				Date fechaR = new Date(f.getFechaReclamo().getTime());
				fechaR=formateador.parse(formateador.format(fechaR));
				if ((fechaInicialBusqueda.getTime()<=fechaR.getTime())
						&& fechaR.getTime()<=fechaFinalBusqueda.getTime()) {
					List<Object[]> lo = objetoReclamoBL
							.getObjetosReclamosList(f.getFormularioOdecoId());

					for (int i = 0; i < lo.size(); i++) {

						ObjetoReclamo obj = objetoReclamoBL
								.getObjetoReclamo(Integer.valueOf(lo.get(i)[0]
										.toString()));
						listaObjetos += obj.getObjetoReclamo() + ",";

					}
					f.setListaObjetos(listaObjetos);

					lo = motivoReclamoBL.getMotivoReclamosList(f
							.getFormularioOdecoId());

					for (int i = 0; i < lo.size(); i++) {

						MotivoReclamo obj = motivoReclamoBL
								.getMotivoReclamo(Integer.valueOf(lo.get(i)[0]
										.toString()));
						listaMotivos += obj.getMotivoReclamo() + ",";

					}
					f.setListaMotivos(listaMotivos);
					// listaReporteFiltro.add(f);
					listaOferta.add(f);
					setListaOferta(listaOferta);
					// setListaReporteFiltro(listaReporteFiltro);

				}

				listaMotivos = "";
				listaObjetos = "";
			}
			listaReporteFiltro = listaOferta;
			log.debug("[paginar6] se realizo la busqueda en el reporte 1");
		} catch (ParseException e) {
			
			log.error("[Paginar6] error al realizar la busqueda del reporte 1 " + e.getMessage(),e);
		}catch (NumberFormatException e) {
			// TODO: handle exception
			log.error("[Paginar6] error al realizar la busqueda del reporte 1 " + e.getMessage(),e);
		}catch (Exception e) {
			// TODO: handle exception
			log.error("[Paginar6] error al realizar la busqueda del reporte 1 " + e.getMessage(),e);
		}

		
		
		
	}
	
	
	public void exportar() {

		boolean result = Boolean.FALSE;
		log.debug("[exportar] se va exportar el reporte 1");
		String filenameReport = "report1";
		String name="ReporteTabla1.xls";
		Map<String, Object> rptParameters = new HashMap<String, Object>();
		rptParameters.put("Logo", controlParametro.getRutaLogoTigo());

		result = printUtils.exportReport(filenameReport, name,
				rptParameters, getListaReporteFiltro());
		if (result) {			
			controlerBitacora.exportar(DescriptorBitacora.EXPORTARREPORTE1,  "reporte 1", name);
			log.info("Se Exporto Correctamente el reporte 1");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje ",
							"Se Exporto Correctamente"));

			return;

		}
		log.error("No se exporto el reporte 1");
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje ",
						"No se Exporto Correctamente"));

		// return result;

	}

	public void exportar2() {

		boolean result = Boolean.FALSE;
		
		log.debug("[exportar2] se va exportar el reporte 2");
		String filenameReport = "report2";
		String name="ReporteTabla2.xls";
		//String pathDetalle = controlParametro.getRutaExportarReporte()
		//		+ "ReporteTabla2" + ".xls";
		// String titleReport = f.getTipoFormulario().getDescripcion();
		// String pathLogo = FileUtil
		// .getRealPath("C:/TigoTrabajo/ODECO DIGITAL/PROYECTO/COD/ODECO_DIGITAL/WebContent/resources/report/logo.png");
		Map<String, Object> rptParameters = new HashMap<String, Object>();
		rptParameters.put("Logo", controlParametro.getRutaLogoTigo());


		result = printUtils.exportReport2(filenameReport,name,
				rptParameters, listaReporte2);
		if (result) {
			log.info("Se Exporto Correctamente el reporte 2");
			controlerBitacora.exportar(DescriptorBitacora.EXPORTARREPORTE2,  "reporte 2", name);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje ",
							"Se Exporto Correctamente"));

			return;

		}
		log.error("No se exporto el reporte 2");
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje ",
						"No se Exporto Correctamente"));

		// return result;

	}

	public void exportar5() {

		boolean result = Boolean.FALSE;
		log.debug("[exportar5] se va exportar el reporte 5");
		String filenameReport = "report5";
		String name="ReporteTabla5.xls";
		//String pathDetalle = controlParametro.getRutaExportarReporte()
		//		+ "ReporteTabla5" + ".xls";
		// String titleReport = f.getTipoFormulario().getDescripcion();
		// String pathLogo = FileUtil
		// .getRealPath("C:/TigoTrabajo/ODECO DIGITAL/PROYECTO/COD/ODECO_DIGITAL/WebContent/resources/report/logo.png");
		Map<String, Object> rptParameters = new HashMap<String, Object>();
		rptParameters.put("Logo", controlParametro.getRutaLogoTigo());

		result = printUtils.exportReport2(filenameReport,name,
				rptParameters, listaReporte2);
		if (result) {
			log.info("Se exporto correctamente el reporte 5");
			controlerBitacora.exportar(DescriptorBitacora.EXPORTARREPORTE5,  "reporte 5", name);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje ",
							"Se Exporto Correctamente"));

			return;

		}
		log.info("No se exporto ");
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje ",
						"No se Exporto Correctamente"));

		// return result;

	}
	
	public void exportar6() {

		boolean result = Boolean.FALSE;
		log.debug("[exportar] se va exportar el reporte 1");
		String filenameReport = "report6";
		String name="ReporteTabla6.xls";
		Map<String, Object> rptParameters = new HashMap<String, Object>();
		rptParameters.put("Logo", controlParametro.getRutaLogoTigo());

		result = printUtils.exportReport(filenameReport, name,
				rptParameters, getListaOferta());
		if (result) {			
			controlerBitacora.exportar(DescriptorBitacora.EXPORTARREPORTE6,  "reporte 6", name);
			log.info("Se Exporto Correctamente el reporte 1");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensaje ",
							"Se Exporto Correctamente"));

			return;

		}
		log.error("No se exporto el reporte 6");
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje ",
						"No se Exporto Correctamente"));

		// return result;

	}
	
	public void exportar3(){
		
		HSSFWorkbook libro = new HSSFWorkbook();
		
		log.debug("[exportar3] se va exportar el reporte3");
		HSSFSheet hoja = libro.createSheet();
		try {
			String ruta=controlParametro.getRutaLogoTigo();
			InputStream my_banner_image=null;
			byte[] bytes;
			try {
				 my_banner_image = new FileInputStream(ruta);
				 /* Convert Image to byte array */
				 bytes = IOUtils.toByteArray(my_banner_image);
				 /* Add Picture to workbook and get a index for the picture */
		            int my_picture_id = libro.addPicture(bytes, libro.PICTURE_TYPE_PNG);
		            /* Close Input Stream */
		            my_banner_image.close();
		            /* Create the drawing container */
		            HSSFPatriarch drawing = hoja.createDrawingPatriarch();
		            /* Create an anchor point */
		            ClientAnchor my_anchor = new HSSFClientAnchor();
		            /* Define top left corner, and we can resize picture suitable from there */
		            my_anchor.setCol1(0);
		            my_anchor.setRow1(0);           
		            /* Invoke createPicture and pass the anchor point and ID */
		            HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
		            /* Call resize method, which resizes the image */
		            my_picture.resize();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("[exportar3] no se pudo crear el inputstream",e );
				return;
			}finally{
				if(my_banner_image!=null){
					try {
						my_banner_image.close();
					} catch (IOException e) {							
						e.printStackTrace();
						log.error("[exportar3] no se pudo cerrar el banner");
					}
				}
			}
		
           
		} catch (Exception e) {
		e.printStackTrace();
			log.error("[exportar4]No se pudo cargar el logo");
		}
		HSSFRow fila = hoja.createRow(0);
		HSSFCell celda = fila.createCell((short)5);	
		HSSFCellStyle curStyle = libro.createCellStyle();
		HSSFCellStyle curStyle2 = libro.createCellStyle();
		HSSFCellStyle curStyle3 = libro.createCellStyle();
		
		HSSFFont font = libro.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setUnderline(HSSFFont.U_SINGLE);
        font.setFontHeightInPoints((short) 9);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFFont font2 = libro.createFont();
		font2.setFontName(HSSFFont.FONT_ARIAL);
        font2.setFontHeightInPoints((short) 9);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //font.setColor(HSSFColor.BLUE.index);
        curStyle.setFont(font);
        celda.setCellStyle(curStyle);
        HSSFRichTextString texto = new HSSFRichTextString("TABLA DE REGISTROS DE RECLAMACION DIRECTA No 3");
		celda.setCellValue(texto);
		
		
		//curStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		//curStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		curStyle2.setBorderTop((short) 1); // single line border
		curStyle2.setBorderLeft((short) 1);
		curStyle2.setBorderRight((short) 1);
		curStyle2.setBorderBottom((short) 1); // single line border
		curStyle2.setFont(font2);
		
		curStyle3.setFillPattern(HSSFColor.YELLOW.index);
		//curStyle3.setFillBackgroundColor(HSSFColor.YELLOW.index);
		
		curStyle3.setBorderTop((short) 1); // single line border
		curStyle3.setBorderLeft((short) 1);
		curStyle3.setBorderRight((short) 1);
		curStyle3.setBorderBottom((short) 1); // single line border
		curStyle3.setFont(font2);
       
		
		fila=hoja.createRow(2);
		celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("UNIDAD DE PLANIFICACION NORMAS Y GESTION POR RESULTADOS");
		celda.setCellValue(texto);
		
		celda.setCellStyle(curStyle);
		
		fila=hoja.createRow(4);
		celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("CONTROL DE RECLAMOS");
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle);
		
		fila=hoja.createRow(6);
		celda = fila.createCell((short)4);
        texto = new HSSFRichTextString("PROGRAMA DE SEGUIMIENTO CUMPLIMIENTO INSTRUCTIVO PARA LA ATENCION DE RECLAMACIONES (ODECO)");
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle);
		
		fila=hoja.createRow(8);
		celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("OFICINA DEL CONSUMIDOR TIPO DE RECLAMACIONES ATENDIDAS ("+new Timestamp(new Date().getTime())+")");
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle);
		
		
	        
		fila=hoja.createRow(10);
        celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("MES");
        hoja.autoSizeColumn((short) 1);
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle3);
        int c=1;
        for (ObjetoColumna o : matriz.getColumnas()) {
			
			celda = fila.createCell((short)c);
			texto = new HSSFRichTextString(o.getMotivoReclamo());
			celda.setCellValue(texto);
			celda.setCellStyle(curStyle3);
			c=c+1;
		}	
        try {
        	 int fi=11; int col=0;
             for (ObjetoFila of : matriz.getFilas()) {       	
             	fila=hoja.createRow(fi);
             	celda = fila.createCell((short)col);
             	texto = new HSSFRichTextString(of.getMes());
     			celda.setCellValue(texto);
     			celda.setCellStyle(curStyle2);
     			col=col+1;
             	for (int a : of.getVector()) {
             		
             		celda = fila.createCell((short)col);
                 	texto = new HSSFRichTextString(String.valueOf(a));
         			celda.setCellValue(texto);
         			celda.setCellStyle(curStyle2);
     				col=col+1;
     			}      	
               fi=fi+1;
               col=0;
             }
             
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Mensaje de error",
							"Primero debe Realizar una Busqueda"));

			return;
			
		}
       	
		
		try {
			  /* FileOutputStream elFichero = new FileOutputStream(controlParametro.getRutaExportarReporte()+"ReporteTabla3.xls");
			   libro.write(elFichero);
			   elFichero.close();*/
			FacesContext facesContext = FacesContext.getCurrentInstance();
		    ExternalContext externalContext = facesContext.getExternalContext();
		    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		    

		    response.setHeader("Content-Disposition", "attachment; filename=\"ReporteTabla3.xls\"");
		    response.setContentType("application/vnd.ms-excel");

		    
		   // externalContext.setResponseContentType("application/vnd.ms-excel");
		   // externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"ReporteTabla3.xls\"");

		    libro.write(response.getOutputStream());
		    facesContext.responseComplete();
		    log.info("[exportar3] se exporto correctamente ");
		    controlerBitacora.exportar(DescriptorBitacora.EXPORTARREPORTE3,  "reporte 3", "ReporteTabla3.xls");
			   FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Mensaje de confirmacion",
								"Se exporto Correctamente"));

				return;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("[exportar 3] no se exporto correctamente el reporte 3");
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error",
								"No Se exporto Correctamente"));

				return;
			}
	}
	


	
	public void exportar4(){
		
		HSSFWorkbook libro = new HSSFWorkbook();
		
		log.debug("[exportar4] se va exportar el reporte 4");
		
		HSSFSheet hoja = libro.createSheet();
		try {
			String ruta=controlParametro.getRutaLogoTigo();
			InputStream my_banner_image=null;
			byte[] bytes;
			try {
				 my_banner_image = new FileInputStream(ruta);
				 /* Convert Image to byte array */
				 bytes = IOUtils.toByteArray(my_banner_image);
				 
				 /* Add Picture to workbook and get a index for the picture */
		            int my_picture_id = libro.addPicture(bytes, libro.PICTURE_TYPE_PNG);
		            /* Close Input Stream */
		            my_banner_image.close();
		            /* Create the drawing container */
		            HSSFPatriarch drawing = hoja.createDrawingPatriarch();
		            /* Create an anchor point */
		            ClientAnchor my_anchor = new HSSFClientAnchor();
		            /* Define top left corner, and we can resize picture suitable from there */
		            my_anchor.setCol1(0);
		            my_anchor.setRow1(0);           
		            /* Invoke createPicture and pass the anchor point and ID */
		            HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
		            /* Call resize method, which resizes the image */
		            my_picture.resize();
			} catch (Exception e) {
				e.printStackTrace();
				log.error("[exportar4] no se pudo crear el inputstream",e );
				return;
			}finally{
				if(my_banner_image!=null){
					try {
						my_banner_image.close();
					} catch (IOException e) {							
						e.printStackTrace();
						log.error("[exportar4] error al cerrar el banner");
					}
				}
			}
			
           
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[exportar4]No se pudo cargar el logo");
		}
		
		
		HSSFRow fila = hoja.createRow(0);
		HSSFCell celda = fila.createCell((short)5);	
		HSSFCellStyle curStyle = libro.createCellStyle();
		HSSFCellStyle curStyle2 = libro.createCellStyle();
		HSSFCellStyle curStyle3 = libro.createCellStyle();
		
		HSSFFont font = libro.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setUnderline(HSSFFont.U_SINGLE);
        font.setFontHeightInPoints((short) 9);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFFont font2 = libro.createFont();
		font2.setFontName(HSSFFont.FONT_ARIAL);
        font2.setFontHeightInPoints((short) 9);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //font.setColor(HSSFColor.BLUE.index);
        curStyle.setFont(font);
        celda.setCellStyle(curStyle);
        HSSFRichTextString texto = new HSSFRichTextString("TABLA DE REGISTROS DE RECLAMACION DIRECTA No 4");
		celda.setCellValue(texto);
		
		
		//curStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		//curStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		curStyle2.setBorderTop((short) 1); // single line border
		curStyle2.setBorderLeft((short) 1);
		curStyle2.setBorderRight((short) 1);
		curStyle2.setBorderBottom((short) 1); // single line border
		curStyle2.setFont(font2);
		
		curStyle3.setFillPattern(HSSFColor.YELLOW.index);
		//curStyle3.setFillBackgroundColor(HSSFColor.YELLOW.index);
		
		curStyle3.setBorderTop((short) 1); // single line border
		curStyle3.setBorderLeft((short) 1);
		curStyle3.setBorderRight((short) 1);
		curStyle3.setBorderBottom((short) 1); // single line border
		curStyle3.setFont(font2);
       
		
		fila=hoja.createRow(2);
		celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("UNIDAD DE PLANIFICACION NORMAS Y GESTION POR RESULTADOS");
		celda.setCellValue(texto);
		
		celda.setCellStyle(curStyle);
		
		fila=hoja.createRow(4);
		celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("CONTROL DE RECLAMOS");
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle);
		
		fila=hoja.createRow(6);
		celda = fila.createCell((short)4);
        texto = new HSSFRichTextString("PROGRAMA DE SEGUIMIENTO CUMPLIMIENTO INSTRUCTIVO PARA LA ATENCION DE RECLAMACIONES (ODECO)");
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle);
		
		fila=hoja.createRow(8);
		celda = fila.createCell((short)0);
        texto = new HSSFRichTextString("OFICINA DEL CONSUMIDOR TIPO DE RECLAMACIONES ATENDIDAS ("+new Timestamp(new Date().getTime())+")");
		celda.setCellValue(texto);
		celda.setCellStyle(curStyle);
		
		int c=0; int cc=10; 
        for (ObjetoColumna objetoColumna : objetoMatriz.getColumnas()) {
        	
        	fila=hoja.createRow(cc);
            celda = fila.createCell((short)0);
            texto = new HSSFRichTextString(objetoColumna.getMotivoReclamo());
			celda.setCellValue(texto);
			celda.setCellStyle(curStyle2);
        	cc=cc+1;
			
 	        List<Matriz> lstmat=objetoMatriz.getFilas();
 	        Matriz ma= lstmat.get(c);
 	        fila=hoja.createRow(cc);
 	        celda = fila.createCell((short)0);
 	        texto = new HSSFRichTextString("MES");
 			celda.setCellValue(texto);
 			celda.setCellStyle(curStyle3);
 			
 			int c1=1;
 	        for (ObjetoColumna a : ma.getColumnas()) {
 	        	celda = fila.createCell((short)c1);
 				texto = new HSSFRichTextString(a.getMotivoReclamo());
 				celda.setCellValue(texto);
 				celda.setCellStyle(curStyle3);
 				c1=c1+1;

			}
 	        try {
 	        	 cc=cc+1; int sw=0;
 		 	       for (ObjetoFila of : ma.getFilas()) {
 		 	    	   	fila=hoja.createRow(cc);
 		 	    	   	celda = fila.createCell((short)0);
 		 				texto = new HSSFRichTextString(of.getMes());
 		 				celda.setCellValue(texto);
 		 				celda.setCellStyle(curStyle2);
 		 				
 		 				int t=1;
 			        	for (int a : of.getVector()) {
 			        		celda = fila.createCell((short)t);
 			 				texto = new HSSFRichTextString(String.valueOf(a));
 			 				celda.setCellValue(texto);
 			 				celda.setCellStyle(curStyle2);
 			 				t=t+1;
 						}
 			        	cc=cc+1;
 			           sw=sw+1;
 			        }
 		 	     if(sw==0){
 		 	    	
 		 	    	fila=hoja.createRow(cc);
 	 	    	   	celda = fila.createCell((short)2);
 	 				texto = new HSSFRichTextString("NO TIENE DATOS PARA MOSTRAR");
 	 				celda.setCellValue(texto);
 	 				celda.setCellStyle(curStyle2);
 	 				cc=cc+1;
 		 	     }
 	 	        c=c+1;
 	 	        cc=cc+1;
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error",
								"Primero debe Realizar una Busqueda"));

				return;
			}
 	       
			
		}
		
	        
		
      
		try {
			
		        
			  /* FileOutputStream elFichero = new FileOutputStream(controlParametro.getRutaExportarReporte()+"ReporteTabla4.xls");
			   libro.write(elFichero);
			   elFichero.close();*/
			   
			   FacesContext facesContext = FacesContext.getCurrentInstance();
			    ExternalContext externalContext = facesContext.getExternalContext();
			    
			    HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
			    

			    response.setHeader("Content-Disposition", "attachment; filename=\"ReporteTabla4.xls\"");
			    response.setContentType("application/vnd.ms-excel");
			    
			    
			    //externalContext.setResponseContentType("application/vnd.ms-excel");
			   // externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"ReporteTabla4.xls\"");

			    libro.write(response.getOutputStream());
			    facesContext.responseComplete();
		       log.info("[exportar4] se exporto correctamente el reporte 4");
		       controlerBitacora.exportar(DescriptorBitacora.EXPORTARREPORTE4,  "reporte 4", "ReporteTabla4.xls");
			   FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Mensaje de confirmacion",
								"Se exporto Correctamente"));

				return;
			} catch (Exception e) {
			   e.printStackTrace();
			   log.error("[exportar4] no se exporto correctamente el reporte 4");
			   FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Mensaje de error",
								"No Se exporto Correctamente"));

				return;
			}
	}
	
/*	private void showPrintPDF(ByteArrayOutputStream outputStream, String nameReport) throws IOException {
	       FacesContext fcontext = FacesContext.getCurrentInstance();
	       ExternalContext externalContext = fcontext.getExternalContext();

	       HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

	       response.setHeader("Content-disposition", "attachment; filename=\"" + nameReport + "\"");
	       response.setContentType("application/pdf");
	       response.setContentLength(outputStream.size());

	       outputStream.writeTo(response.getOutputStream());
	       fcontext.responseComplete();
	   }*/
	
	public boolean booleanEstado(String charEstado) {
		log.info("[booleanEstado] ingreso al metodo boolean estado.");
		if (charEstado.equals("A")) {
			return true;
		} else {
			return false;
		}
	}

	public String headerLabel() {
		System.out.println("ID ODECO:" + this.oferta.getFormularioOdecoId());
		if (this.oferta.getFormularioOdecoId() > 0)
			return "Modificando Datos...";
		else
			return "Creando Nuevo Registro...";
	}

	public FormularioOdeco getOferta() {
		return oferta;
	}

	public void setOferta(FormularioOdeco oferta) {
		this.oferta = oferta;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public List<FormularioOdeco> getListaOferta() {
		return listaOferta;
	}

	public void setListaOferta(List<FormularioOdeco> listaServicio) {
		this.listaOferta = listaServicio;
	}

	public String getIdOfertaStr() {
		return idOfertaStr;
	}

	public void setIdOfertaStr(String idOfertaStr) {
		this.idOfertaStr = idOfertaStr;
	}

	public List<ObjetoReporte2> getListaReporte2() {
		return listaReporte2;
	}

	public void setListaReporte2(List<ObjetoReporte2> listaReporte2) {
		this.listaReporte2 = listaReporte2;
	}

	public String getStrIdUs() {
		return strIdUs;
	}

	public void setStrIdUs(String strIdUs) {
		this.strIdUs = strIdUs;
	}

	public void dateSelect(SelectEvent event) {
	}

	public Date getDateToday() {
		return new Date();
	}

	public String getListaObjetos() {
		return listaObjetos;
	}

	public void setListaObjetos(String listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

	public String getListaMotivos() {
		return listaMotivos;
	}

	public void setListaMotivos(String listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	public Date getFechaInicialBusqueda() {
		return fechaInicialBusqueda;
	}

	public void setFechaInicialBusqueda(Date fechaInicialBusqueda) {
		this.fechaInicialBusqueda = fechaInicialBusqueda;
	}

	public Date getFechaFinalBusqueda() {
		return fechaFinalBusqueda;
	}

	public void setFechaFinalBusqueda(Date fechaFinalBusqueda) {
		this.fechaFinalBusqueda = fechaFinalBusqueda;
	}

	public List<FormularioOdeco> getListaReporteFiltro() {
		return listaReporteFiltro;
	}

	public void setListaReporteFiltro(List<FormularioOdeco> listaReporteFiltro) {
		this.listaReporteFiltro = listaReporteFiltro;
	}

	public Matriz getMatriz() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}

	

	public List<Matriz> getListaMatrices() {
		return listaMatrices;
	}

	public void setListaMatrices(List<Matriz> listaMatrices) {
		this.listaMatrices = listaMatrices;
	}

	public ObjetoMatriz getObjetoMatriz() {
		return objetoMatriz;
	}

	public void setObjetoMatriz(ObjetoMatriz objetoMatriz) {
		this.objetoMatriz = objetoMatriz;
	}

	public List<SelectItem> getSelectCiudades() {
		return selectCiudades;
	}

	public void setSelectCiudades(List<SelectItem> selectCiudades) {
		this.selectCiudades = selectCiudades;
	}

	public String getSelectCiudad() {
		return selectCiudad;
	}

	public void setSelectCiudad(String selectCiudad) {
		this.selectCiudad = selectCiudad;
	}

	public List<SelectItem> getSelectCoordinadores() {
		return selectCoordinadores;
	}

	public void setSelectCoordinadores(List<SelectItem> selectCoordinadores) {
		this.selectCoordinadores = selectCoordinadores;
	}

	public String getSelectCoordinador() {
		return selectCoordinador;
	}

	public void setSelectCoordinador(String selectCoordinador) {
		this.selectCoordinador = selectCoordinador;
	}

	
	
	/**********/

}
