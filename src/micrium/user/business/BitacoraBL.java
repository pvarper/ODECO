package micrium.user.business;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.dao.BitacoraDAO;
import micrium.user.model.Bitacora;
import micrium.util.DescriptorBitacora;

import org.apache.log4j.Logger;

@Named
public class BitacoraBL implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	BitacoraDAO dao;

	private static Logger log = Logger.getLogger(BitacoraBL.class);

	public void save(Bitacora dato) {

		try {
			dao.save(dato);
		} catch (Exception e) {
			log.error("[save] " + e);
		}
	}

	public List<Bitacora> listBitacora() {
		return dao.listBitacora();
	}

	@SuppressWarnings("rawtypes")
	public void accionInsert(String user, String ip, Enum dato, String id, String name) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se adiciono:" + ele + " con Id:" + id + ", Nombre:" + name;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}

	@SuppressWarnings("rawtypes")
	public void accionDelete(String user, String ip, Enum dato, String id, String name) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se elimino:" + ele + " con Id:" + id + ", Nombre:" + name;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}

	@SuppressWarnings("rawtypes")
	public void accionExportar(String user, String ip, Enum dato, String id, String name) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se exporto:" + ele + " con Id:" + id + ", Nombre:" + name;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}
	
	@SuppressWarnings("rawtypes")
	public void accionUpdate(String user, String ip, Enum dato, String id, String name) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se modifico:" + ele + " con Id:" + id + ", Nombre:" + name;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}

	@SuppressWarnings("rawtypes")
	public void accionFind(String user, String ip, Enum dato, String id, String name) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se busco:" + ele + " con Campos:" + id + ", Otros:" + name;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}

	@SuppressWarnings("rawtypes")
	public void accionCortar(String user, String ip, Enum dato, String id) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se corto:" + ele + " con NumeroTelefono:" + id;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}

	@SuppressWarnings("rawtypes")
	public void accionReconectar(String user, String ip, Enum dato, String id) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		String accion = "Se reconecto:" + ele + " con NumeroTelefono:" + id;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}

	@SuppressWarnings("rawtypes")
	public void accion(String user, String ip, Enum dato, String accion) {

		String ele = dato.name();
		String formulario=((DescriptorBitacora)dato).getFormulario();
		accion = ele + " - " + accion;

		Bitacora bitacora = new Bitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		Date t = new Date();
		Timestamp ts = new Timestamp(t.getTime());
		bitacora.setFecha(ts);
		save(bitacora);
	}
}
