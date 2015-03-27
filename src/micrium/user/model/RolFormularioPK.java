package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the rol_formulario database table.
 * 
 */
@Embeddable
public class RolFormularioPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="rol_id", insertable=false, updatable=false)
	private Integer rolId;

	@Column(name="formulario_id", insertable=false, updatable=false)
	private Integer formularioId;

	public RolFormularioPK() {
	}
	public Integer getRolId() {
		return this.rolId;
	}
	public void setRolId(Integer rolId) {
		this.rolId = rolId;
	}
	public Integer getFormularioId() {
		return this.formularioId;
	}
	public void setFormularioId(Integer formularioId) {
		this.formularioId = formularioId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RolFormularioPK)) {
			return false;
		}
		RolFormularioPK castOther = (RolFormularioPK)other;
		return 
			this.rolId.equals(castOther.rolId)
			&& this.formularioId.equals(castOther.formularioId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.rolId.hashCode();
		hash = hash * prime + this.formularioId.hashCode();
		
		return hash;
	}
}