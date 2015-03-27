package micrium.odeco.business;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.odeco.dao.FormularioOdecoDAO;
import micrium.odeco.model.FormularioOdeco;
import micrium.util.Parameter;

import org.apache.log4j.Logger;


@Named
public class FormularioOdecoBL implements Serializable {



	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(FormularioOdecoBL.class);
	/*
	 * @Inject private DeezerDao daoDeezer;
	 */
	@Inject
	private FormularioOdecoDAO dao;

	public String validate(FormularioOdeco dev) {
		if (dev.getCiudadLocalidad()==null||dev.getCiudadLocalidad().equalsIgnoreCase("-1"))
			return Parameter.MENSAJE_VALIDATE_CIUDAD_LOCALIDAD;
		if (dev.getNombreReclamante() == null || dev.getNombreReclamante().isEmpty() || !dev.getNombreReclamante().matches(Parameter.expresionRegular))
			return Parameter.MENSAJE_VALIDATE_NOMBRE_RECLAMANTE;
		if (dev.getNombreTitular() == null || dev.getNombreTitular().isEmpty()|| !dev.getNombreTitular().matches(Parameter.expresionRegular))
			return Parameter.MENSAJE_VALIDATE_NOMBRE_TITULAR;
		if (dev.getTelefonoReferencia() == null || dev.getTelefonoReferencia().isEmpty()|| !dev.getTelefonoReferencia().matches("^[0-9]\\d{5,8}$"))
			return Parameter.MENSAJE_VALIDATE_TELEFONO_REFERENCIA;
		if (dev.getLineaReclamo() == null || dev.getLineaReclamo().isEmpty()|| !dev.getLineaReclamo().matches("^[0-9]\\d{5,8}$"))
			return Parameter.MENSAJE_VALIDATE_TELEFONO_RECLAMO;	
		
		if (dev.getFechaIncidencia() == null )
			return Parameter.MENSAJE_VALIDATE_FECHAINCIDENCIA;	
		
		if (dev.getDetalleMotivoReclamacion() == null || dev.getDetalleMotivoReclamacion().isEmpty() || !dev.getDetalleMotivoReclamacion().matches(Parameter.expresionRegular))
			return Parameter.MENSAJE_VALIDATE_DETALLE_MOTIVOS;	
		
		if (!dev.getFax().isEmpty() )
			if(!dev.getFax().matches("^[0-9]\\d{5,8}$")){
				return Parameter.MENSAJE_VALIDATE_FAX;
			}
			
		
		if (dev.getDireccionDomicilio() == null || dev.getDireccionDomicilio().isEmpty()|| !dev.getDireccionDomicilio().matches(Parameter.expresionRegular))
			return Parameter.MENSAJE_VALIDATE_DIRECCION_RECLAMANTE;
		if (dev.getDireccionNotificacion() == null || dev.getDireccionNotificacion().isEmpty()|| !dev.getDireccionNotificacion().matches(Parameter.expresionRegular))
			return Parameter.MENSAJE_VALIDATE_DIRECCION_NOTIFICACION;
		if (dev.getObjetoReclamos() == null || dev.getObjetoReclamos().isEmpty())
			return Parameter.MENSAJE_VALIDATE_OBJETOS;
		if (dev.getMotivoReclamos() == null || dev.getMotivoReclamos().isEmpty())
			return Parameter.MENSAJE_VALIDATE_MOTIVOS;
		if (dev.getCiudad() == null )
			return Parameter.MENSAJE_VALIDATE_CIUDAD_RECLAMACION;
		if (dev.getMedioNotificacion() == null )
			return Parameter.MENSAJE_VALIDATE_MEDIO_NOTIFICACION;
		if (dev.getFormaRegistro() == null || dev.getFormaRegistro().isEmpty() || dev.getFormaRegistro().equals("Forma de Registro"))
			return "Debe Seleccionar una Forma de Registro";
		
		

		return "";
	}

	


	public void save(FormularioOdeco formulario)throws Exception {
		
		
		 dao.save(formulario);
	}

	public void update(FormularioOdeco formulario)throws Exception  {
		System.out.println("****************** UPDATE *********************************");
		log.debug("[update] actualizando odeco");
		FormularioOdeco readFormulario = dao.find(formulario.getFormularioOdecoId());

		//readFormulario.setCodigoReclamacionInstitucion(formulario.getCodigoReclamacionInstitucion());
		//readFormulario.setCodigoReclamacionCiudad(formulario.getCodigoReclamacionCiudad());
		//readFormulario.setCodigoReclamacionCorrelativo(formulario.getCodigoReclamacionCorrelativo());
		readFormulario.setCiudadLocalidad(formulario.getCiudadLocalidad());
		readFormulario.setNombreReclamante(formulario.getNombreReclamante());
		readFormulario.setNombreTitular(formulario.getNombreTitular());
		readFormulario.setTelefonoReferencia(formulario.getTelefonoReferencia());
		readFormulario.setLineaReclamo(formulario.getLineaReclamo());
		readFormulario.setCorreo(formulario.getCorreo());
		readFormulario.setDireccionDomicilio(formulario.getDireccionDomicilio());
		readFormulario.setDireccionNotificacion(formulario.getDireccionNotificacion());
		readFormulario.setFechaReclamo(formulario.getFechaReclamo());
		readFormulario.setCiudad(formulario.getCiudad());
		readFormulario.setMedioNotificacion(formulario.getMedioNotificacion());
		readFormulario.setRespuestaOdeco(formulario.getRespuestaOdeco());
		readFormulario.setFormularioOdeco(formulario.getFormularioOdeco());
		readFormulario.setUsuario(formulario.getUsuario());
		readFormulario.setEstadoRespuesta(formulario.getEstadoRespuesta());
		readFormulario.setDevuelto(formulario.getDevuelto());
		readFormulario.setRevisar(formulario.getRevisar());
		readFormulario.setEnviado(formulario.getEnviado());
		readFormulario.setObservaciones(formulario.getObservaciones());
		readFormulario.setDetalleRespuesta(formulario.getDetalleRespuesta());
		readFormulario.setMotivoReclamacionAdministrativa(formulario.getMotivoReclamacionAdministrativa());
		readFormulario.setMotivoReclamos(formulario.getMotivoReclamos());
		readFormulario.setObjetoReclamos(formulario.getObjetoReclamos());
		readFormulario.setAdjuntos(formulario.getAdjuntos());
		readFormulario.setDerivado(formulario.getDerivado());
		readFormulario.setEnviadoMail(formulario.getEnviadoMail());
		readFormulario.setPeriodoObjetoReclamo(formulario.getPeriodoObjetoReclamo());
		readFormulario.setEstado(formulario.getEstado());
		readFormulario.setFechaResolucion(formulario.getFechaResolucion());
		readFormulario.setDetalleAnulado(formulario.getDetalleAnulado());
		readFormulario.setFechaIncidencia(formulario.getFechaIncidencia());
		readFormulario.setFax(formulario.getFax());
		readFormulario.setDetalleMotivoReclamacion(formulario.getDetalleMotivoReclamacion());
		readFormulario.setFechaCerrado(formulario.getFechaCerrado());
		readFormulario.setEstado(formulario.getEstado());
		readFormulario.setFormaRegistro(formulario.getFormaRegistro());

		 dao.update(readFormulario);
	}

	public  void remove(int ofertaId) throws Exception {
		FormularioOdeco readOferta = dao.find(ofertaId);
		 dao.eliminar(readOferta);
	}

	public List<FormularioOdeco> listarTodo() {
		return dao.getLista();
	}
	
	
	public List<FormularioOdeco> getListaResponsable(String login) {
		return dao.getListaResponsable(login);
	}
	public Long getMaxId(String institucion,String ciudad) {
		return this.dao.getMaxPosicion(institucion,ciudad);
	}
	public FormularioOdeco getEstadoTrue(int id) {
		return dao.find(id);
	}
	public FormularioOdeco getOdecoPI(String ciudad) {
		return dao.getFormularioPI(ciudad);
		
	}
	public FormularioOdeco getFormularioPIidSegunda(int ciudad) {
		return dao.getFormularioPIidSegunda(ciudad);
		
	}
	public List<Object[]> getMotivoReclamacion(String id) {
		return dao.getMotivosReclamos(id);
	}
	public List<Object[]> getObjetosReclamosDescripcion(int id) {
		return dao.getObjetosReclamosDescripcion(id);
	}
	public List<Object[]> getMotivosReclamosDescripcion(int id) {
		return dao.getMotivosReclamosDescripcion(id);
	}
	public List<Object[]> reclamosAtendidos(Timestamp fi, Timestamp ff){
		return dao.reclamosAtendidos(fi, ff);
	}
	public List<Object[]> reclamosPendientes(Timestamp fi, Timestamp ff){
		return dao.reclamosPendientes(fi, ff);
	}
	public List<Object[]> reclamosAnulados(Timestamp fi, Timestamp ff){
		return dao.reclamosAnulados(fi, ff);
	}
	public List<Object[]> reclamosAdministrativos(Timestamp fi, Timestamp ff){
		return dao.reclamosAdministrativos(fi, ff);
	}
	public List<Object[]> reclamosPlazoEstablecido(Timestamp fi, Timestamp ff){
		return dao.reclamosPlazoEstablecido(fi, ff);
	}
	public List<Object[]> antiguedadPendientes(Timestamp fi, Timestamp ff){
		return dao.antiguedadPendientes(fi, ff);
	}
	public Long cantidadFormularios(Timestamp fi, Timestamp ff,int id_ciudad){
		return dao.cantidadFormularios(fi, ff, id_ciudad);
	}
	public Long cantidadAbiertos(Timestamp fi, Timestamp ff,int id_ciudad){
		return dao.cantidadAbiertos(fi, ff, id_ciudad);
	}
	public Long cantidadCerrado(Timestamp fi, Timestamp ff,int id_ciudad){
		return dao.cantidadCerrado(fi, ff, id_ciudad);
	}
	public Long cantidadAnulado(Timestamp fi, Timestamp ff,int id_ciudad){
		return dao.cantidadAnulado(fi, ff, id_ciudad);
	}
	
	public List<Object[]> reporte2(Timestamp fi, Timestamp ff){
		return dao.reporte2(fi, ff);
	}
	public List<Object[]> reporte4(Timestamp fi, Timestamp ff, int objetoReclamoId){
		return dao.reporte4(fi, ff,objetoReclamoId);
	}
	public String toStringMediosPermitidos(boolean tigoShopping, boolean PDAPDV, boolean automaticoNominacion, boolean mensajeOculto) {
		String cad = "";
		if (tigoShopping)
			cad += 'T';
		else
			cad += 'N';
		if (PDAPDV)
			cad += 'P';
		else
			cad += 'N';
		if (automaticoNominacion)
			cad += 'A';
		else
			cad += 'N';
		if (mensajeOculto)
			cad += 'M';
		else
			cad += 'N';
		return cad;
	}
	

}
