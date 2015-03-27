package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the rol_calendario database table.
 * 
 */
@Embeddable
public class RolCalendarioPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="usuario_id", insertable=false, updatable=false)
	private Long usuarioId;

	@Column(name="calendario_id", insertable=false, updatable=false)
	private Long calendarioId;

	public RolCalendarioPK() {
	}
	public Long getUsuarioId() {
		return this.usuarioId;
	}
	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}
	public Long getCalendarioId() {
		return this.calendarioId;
	}
	public void setCalendarioId(Long calendarioId) {
		this.calendarioId = calendarioId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RolCalendarioPK)) {
			return false;
		}
		RolCalendarioPK castOther = (RolCalendarioPK)other;
		return 
			this.usuarioId.equals(castOther.usuarioId)
			&& this.calendarioId.equals(castOther.calendarioId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.usuarioId.hashCode();
		hash = hash * prime + this.calendarioId.hashCode();
		
		return hash;
	}
}