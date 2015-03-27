package micrium.odeco.model;

import java.io.Serializable;

import javax.persistence.*;

import micrium.user.model.RolCalendario;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * The persistent class for the calendario database table.
 * 
 */
@Entity
@NamedQuery(name="Calendario.findAll", query="SELECT c FROM Calendario c")
public class Calendario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CALENDARIO_CALENDARIOID_GENERATOR", sequenceName="CALENDARIO_CALENDARIO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CALENDARIO_CALENDARIOID_GENERATOR")
	@Column(name="calendario_id")
	private Integer calendarioId;

	private String ciudad;

	private Timestamp fecha;

	private String motivo;

	//bi-directional many-to-one association to RolCalendario
	@OneToMany(mappedBy="calendario")
	private List<RolCalendario> rolCalendarios;

	

	public Calendario() {
	}

	public Integer getCalendarioId() {
		return this.calendarioId;
	}

	public void setCalendarioId(Integer calendarioId) {
		this.calendarioId = calendarioId;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}
	
	public String getFechaFiltro(){
		 SimpleDateFormat formateador = new SimpleDateFormat("dd-MM");
		return formateador.format(this.getFecha());
	}
	
	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public List<RolCalendario> getRolCalendarios() {
		return this.rolCalendarios;
	}

	public void setRolCalendarios(List<RolCalendario> rolCalendarios) {
		this.rolCalendarios = rolCalendarios;
	}

	public RolCalendario addRolCalendario(RolCalendario rolCalendario) {
		getRolCalendarios().add(rolCalendario);
		rolCalendario.setCalendario(this);

		return rolCalendario;
	}

	public RolCalendario removeRolCalendario(RolCalendario rolCalendario) {
		getRolCalendarios().remove(rolCalendario);
		rolCalendario.setCalendario(null);

		return rolCalendario;
	}

	@Override
	public String toString() {
		return "Calendario [calendarioId=" + calendarioId + ", ciudad="
				+ ciudad + ", fecha=" + fecha + ", motivo=" + motivo + "]";
	}

	
}