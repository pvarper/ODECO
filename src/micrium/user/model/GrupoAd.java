package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the grupo_ad database table.
 * 
 */
@Entity
@Table(name="grupo_ad")
@NamedQuery(name="GrupoAd.findAll", query="SELECT g FROM GrupoAd g")
public class GrupoAd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GRUPO_AD_GRUPOID_GENERATOR", sequenceName="GRUPO_AD_GRUPO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GRUPO_AD_GRUPOID_GENERATOR")
	@Column(name="grupo_id")
	private Integer grupoId;

	private String detalle;

	private Boolean estado;

	private String nombre;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="rol_id")
	private Rol rol;

	public GrupoAd() {
	}

	public Integer getGrupoId() {
		return this.grupoId;
	}

	public void setGrupoId(Integer grupoId) {
		this.grupoId = grupoId;
	}

	public String getDetalle() {
		return this.detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public Boolean getEstado() {
		return this.estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}