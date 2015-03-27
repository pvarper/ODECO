package micrium.odeco.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the parametro database table.
 * 
 */
@Entity
@NamedQuery(name="Parametro.findAll", query="SELECT p FROM Parametro p")
public class Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PARAMETRO_ID_GENERATOR", sequenceName="PARAMETRO_ID_SEQ",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PARAMETRO_ID_GENERATOR")
	private Long id;

	private String descripcion;

	private Long idformulario;

	private String opcionesvalor;

	private Boolean tipo;

	private Long tipovalor;

	private String valor;

	public Parametro() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getIdformulario() {
		return this.idformulario;
	}

	public void setIdformulario(Long idformulario) {
		this.idformulario = idformulario;
	}

	public String getOpcionesvalor() {
		return this.opcionesvalor;
	}

	public void setOpcionesvalor(String opcionesvalor) {
		this.opcionesvalor = opcionesvalor;
	}

	public Boolean getTipo() {
		return this.tipo;
	}

	public void setTipo(Boolean tipo) {
		this.tipo = tipo;
	}

	public Long getTipovalor() {
		return this.tipovalor;
	}

	public void setTipovalor(Long tipovalor) {
		this.tipovalor = tipovalor;
	}

	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}