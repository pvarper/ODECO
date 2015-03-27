package micrium.util;


import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class Parameter {

	protected final static String configFile = "odeco_digital";
	private static final Logger log = Logger.getLogger(Parameter.class);

	static {
		init();
	}

	// Parametros de Base de Datos de Postgres.
	public static String MENSAJE_GUARDADO ;
	public static String MENSAJE_GUARDADO_ERROR_MAIL  ;
	public static String MENSAJE_GUARDADO_ERROR ;
	public static String MENSAJE_ELIMINAR ;
	public static String MENSAJE_ELIMINAR_ERROR ;
	public static String MENSAJE_REENVIO_ERRORNOTIFICACIONMAIL;
	public static String MENSAJE_REENVIO ;
	public static String MENSAJE_REENVIO_ERROR ;
	public static String MENSAJE_LISTACIUDADES_ERROR;
	public static String MENSAJE_LISTAOBJETOS_ERROR;
	public static String MENSAJE_LISTAMEDIOS_ERROR;
	public static String MENSAJE_RESPUESTAS_ERROR;
	public static String MENSAJE_MOTIVOS_ERROR;
	public static String MENSAJE_VALIDATE_ADJUNTOS;
	public static String MENSAJE_VALIDATE_FECHAINCIDENCIA;
	public static String MENSAJE_VALIDATE_CIUDAD_LOCALIDAD ;
	public static String MENSAJE_VALIDATE_NOMBRE_RECLAMANTE ;
	public static String MENSAJE_VALIDATE_NOMBRE_TITULAR ;
	public static String MENSAJE_VALIDATE_TELEFONO_REFERENCIA ;
	public static String MENSAJE_VALIDATE_TELEFONO_RECLAMO;
	public static String MENSAJE_VALIDATE_DIRECCION_RECLAMANTE ;
	public static String MENSAJE_VALIDATE_DETALLE_MOTIVOS ;
	public static String MENSAJE_VALIDATE_FAX;
	public static String MENSAJE_VALIDATE_DIRECCION_NOTIFICACION ;
	
	public static String MENSAJE_VALIDATE_OBJETOS;
	public static String MENSAJE_VALIDATE_MOTIVOS ;
	public static String MENSAJE_VALIDATE_CIUDAD_RECLAMACION ;
	public static String MENSAJE_VALIDATE_MEDIO_NOTIFICACION ;
	public static String MENSAJE_VALIDATE_ESTADORESPUESTA ;
	public static String MENSAJE_VALIDATE_DETALLERESPUESTA ;
	public static String MENSAJE_VALIDATE_DETALLEANULADO ;
	public static String MENSAJE_VALIDATE_OBSERVACION ;
	
	public static String MENSAJE_ADJUNTOS ;
	public static String MENSAJE_ADJUNTOS_ERROR ;
	public static String MENSAJE_VALIDATE_FECHAFERIADO ;
	
	public static String MENSAJE_COLA_COORDINADOR ;
	
	public static String activeInitialContext;
	public static String activeProviderUrl;
	public static String activeSecurityAuthentication;
	public static String activeSecurityPrincipal;
	public static String activeSecurityCredentials;
	public static String activeSecurityUser;

	public static String swActive;
	public static String login;
	public static String password;
	public static String nroOption;
	public static String timeOut;

	public static String activeSecurityDominio;
	public static String expresionRegular;
	public static String expresionRegularNumero;
	

	
	//public static String PROGRAMACION_TAREA = prop.getProperty("quartz.programacion.tarea");
	

	/** flujo de entrada relacionado con el archivo de configuracion */
	private static InputStream configFileStream;

	public static void init() {
		try { // Ubicar el archivo en el classPath actual
			ResourceBundle rb = ResourceBundle.getBundle(configFile);
			Parameter.MENSAJE_GUARDADO = rb.getString("mensaje.guardado");
			Parameter.MENSAJE_GUARDADO_ERROR_MAIL = rb.getString("mensaje.guardado.errorMail");
			Parameter.MENSAJE_GUARDADO_ERROR = rb.getString("mensaje.guardado.error");
			Parameter.MENSAJE_ELIMINAR =rb.getString("mensaje.eliminar");
			Parameter.MENSAJE_ELIMINAR_ERROR = rb.getString("mensaje.eliminar.error");
			Parameter.MENSAJE_REENVIO_ERRORNOTIFICACIONMAIL =rb.getString("mensaje.reenviar.errorMail");
			Parameter.MENSAJE_REENVIO = rb.getString("mensaje.reenviar");
			Parameter.MENSAJE_REENVIO_ERROR = rb.getString("mensaje.reenviar.error");
			Parameter.MENSAJE_LISTACIUDADES_ERROR = rb.getString("mensaje.listaciudades.error");
			Parameter.MENSAJE_LISTAOBJETOS_ERROR = rb.getString("mensaje.listaobjetos.error");
			Parameter.MENSAJE_LISTAMEDIOS_ERROR =rb.getString("mensaje.listamedionotificacion.error");
			Parameter.MENSAJE_RESPUESTAS_ERROR = rb.getString("mensaje.listarespuestas.error");
			Parameter.MENSAJE_MOTIVOS_ERROR=  rb.getString("mensaje.listamotivos.error");
			Parameter.MENSAJE_VALIDATE_ADJUNTOS= rb.getString("mensaje.validate.adjuntos");
			Parameter.MENSAJE_VALIDATE_FECHAINCIDENCIA=rb.getString("mensaje.validate.fechaincidencia");
			Parameter.MENSAJE_VALIDATE_CIUDAD_LOCALIDAD = rb.getString("mensaje.validate.ciudadlocalidad");
			Parameter.MENSAJE_VALIDATE_NOMBRE_RECLAMANTE = rb.getString("mensaje.validate.nombrereclamante");
			Parameter.MENSAJE_VALIDATE_NOMBRE_TITULAR = rb.getString("mensaje.validate.nombretitular");
			Parameter.MENSAJE_VALIDATE_TELEFONO_REFERENCIA = rb.getString("mensaje.validate.telefonoreferencia");
			Parameter.MENSAJE_VALIDATE_TELEFONO_RECLAMO=rb.getString("mensaje.validate.telefonoreclamo");
			Parameter.MENSAJE_VALIDATE_DIRECCION_RECLAMANTE = rb.getString("mensaje.validate.direccionreclamante");
			Parameter.MENSAJE_VALIDATE_DETALLE_MOTIVOS = rb.getString("mensaje.validate.detallemotivos");
			Parameter.MENSAJE_VALIDATE_FAX = rb.getString("mensaje.validate.fax");
			Parameter.MENSAJE_VALIDATE_DIRECCION_NOTIFICACION =rb.getString("mensaje.validate.direccionnotificacion");
			
			Parameter.MENSAJE_VALIDATE_OBJETOS = rb.getString("mensaje.validate.objeto");
			Parameter.MENSAJE_VALIDATE_MOTIVOS = rb.getString("mensaje.validate.motivo");
			Parameter.MENSAJE_VALIDATE_CIUDAD_RECLAMACION = rb.getString("mensaje.validate.ciudadreclamacion");
			Parameter.MENSAJE_VALIDATE_MEDIO_NOTIFICACION = rb.getString("mensaje.validate.medio");
			Parameter.MENSAJE_VALIDATE_ESTADORESPUESTA = rb.getString("mensaje.validate.estadorespuesta");
			Parameter.MENSAJE_VALIDATE_DETALLERESPUESTA = rb.getString("mensaje.validate.detallerespuesta");
			Parameter.MENSAJE_VALIDATE_DETALLEANULADO =rb.getString("mensaje.validate.detalleanulado");
			Parameter.MENSAJE_VALIDATE_OBSERVACION = rb.getString("mensaje.validate.observacion");
			
			Parameter.MENSAJE_ADJUNTOS = rb.getString("mensaje.adjunto");
			Parameter.MENSAJE_ADJUNTOS_ERROR = rb.getString("mensaje.adjunto.error");
			Parameter.MENSAJE_VALIDATE_FECHAFERIADO = rb.getString("mensaje.validate.fechaferiado");
			
			Parameter.MENSAJE_COLA_COORDINADOR = rb.getString("mensaje.cola.coordinador");
			
			Parameter.activeInitialContext = rb.getString("DIRECTORIO_ACTIVO.initial_context_factory");
			Parameter.activeProviderUrl = rb.getString("DIRECTORIO_ACTIVO.provider_url");
			Parameter.activeSecurityAuthentication = rb.getString("DIRECTORIO_ACTIVO.security_authentication");
			Parameter.activeSecurityPrincipal = rb.getString("DIRECTORIO_ACTIVO.security_principal");
			Parameter.activeSecurityCredentials = rb.getString("DIRECTORIO_ACTIVO.security_credentials");
			Parameter.activeSecurityUser = rb.getString("DIRECTORIO_ACTIVO.security_user");

			Parameter.swActive = rb.getString("USUARIO.sw_active_directory");
			Parameter.login = rb.getString("USUARIO.login");
			Parameter.password = rb.getString("USUARIO.password");
			Parameter.nroOption = rb.getString("USUARIO.nro_opciones");
			Parameter.timeOut = rb.getString("USUARIO.tiempo_fuera");
			
			Parameter.activeSecurityDominio = rb.getString("DIRECTORIO_ACTIVO.dominio");
			Parameter.expresionRegular=rb.getString("expresion.regular");
			Parameter.expresionRegularNumero=rb.getString("expresion.regular.numero");
			
		} catch (Exception e) {
			log.error("Error al obtener los parametros de la aplicacion.");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(configFileStream);
		}
	}
}
