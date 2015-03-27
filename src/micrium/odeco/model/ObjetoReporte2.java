package micrium.odeco.model;

import java.io.Serializable;





public class ObjetoReporte2 implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String mes;
	
	private int reclamosAtendidos;
	
	private int resueltosPlazoEstablecidos;
	
	private int reclamosPendientes;
	
	private int reclamosAnulados;
	
	private int reclamosAdministrativos;
	
	private int antiguedadPendientes;
	
	private String ciudad;
	
	private boolean estado;

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public int getReclamosAtendidos() {
		return reclamosAtendidos;
	}

	public void setReclamosAtendidos(int reclamosAtendidos) {
		this.reclamosAtendidos = reclamosAtendidos;
	}

	public int getResueltosPlazoEstablecidos() {
		return resueltosPlazoEstablecidos;
	}

	public void setResueltosPlazoEstablecidos(int resueltosPlazoEstablecidos) {
		this.resueltosPlazoEstablecidos = resueltosPlazoEstablecidos;
	}

	public int getReclamosPendientes() {
		return reclamosPendientes;
	}

	public void setReclamosPendientes(int reclamosPendientes) {
		this.reclamosPendientes = reclamosPendientes;
	}

	public int getReclamosAnulados() {
		return reclamosAnulados;
	}

	public void setReclamosAnulados(int reclamosAnulados) {
		this.reclamosAnulados = reclamosAnulados;
	}

	public int getReclamosAdministrativos() {
		return reclamosAdministrativos;
	}

	public void setReclamosAdministrativos(int reclamosAdministrativos) {
		this.reclamosAdministrativos = reclamosAdministrativos;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public int getAntiguedadPendientes() {
		return antiguedadPendientes;
	}

	public void setAntiguedadPendientes(int antiguedadPendientes) {
		this.antiguedadPendientes = antiguedadPendientes;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	
	
}